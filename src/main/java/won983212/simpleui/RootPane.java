package won983212.simpleui;

import java.awt.Dimension;
import java.awt.Point;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.math.MathHelper;

public class RootPane extends UIPanel {
	private final Minecraft mc = Minecraft.getMinecraft();
	private final Dimension screenSize;
	private UIPanel popupPanel;
	private double scaledFactor = 0;
	private boolean autoConvert = false;
	private int nativeWndWidth = 0;
	private int nativeWndHeight = 0;

	public RootPane(int width, int height) {
		screenSize = new Dimension(width, height);
	}

	private Point getCenteredGuiCoords(int x, int y, boolean clamp) {
		double factor = scaledFactor;
		if (factor <= 0) {
			ScaledResolution sr = new ScaledResolution(mc);
			factor = sr.getScaleFactor();
		}

		x -= (nativeWndWidth - screenSize.width / factor) / 2;
		y -= (nativeWndHeight - screenSize.height / factor) / 2;
		x *= factor;
		y *= factor;
		if (x < 0 || x > screenSize.width || y < 0 || y > screenSize.height) {
			if (clamp) {
				x = MathHelper.clamp(x, 0, screenSize.width);
				y = MathHelper.clamp(y, 0, screenSize.height);
			} else {
				return null;
			}
		}
		return new Point(x, y);
	}

	public void enableMouseCoordsAutoConvert() {
		this.autoConvert = true;
	}

	/**
	 * Factor is used for calculate click position. if factor is less than 1, factor
	 * will be set properly value automatically.
	 **/
	public void setScaledFactor(double factor) {
		this.scaledFactor = factor;
		if (factor <= 0)
			scaledFactor = 0;
	}

	public void setNativeWindowSize(int width, int height) {
		this.nativeWndWidth = width;
		this.nativeWndHeight = height;
	}

	public void initializePanel() {
		popupPanel = new UIPanel();
		uiList.clear();
		initGui();
		add(popupPanel);
		invalidateSize();
		setRelativeBounds(0, 0, screenSize.width, screenSize.height);
		layout();
	}

	protected void initGui() {
	}

	@Override
	public void addPopup(UIObject obj) {
		popupPanel.add(obj);
	}

	@Override
	public Dimension measureMinSize() {
		return screenSize;
	}

	@Override
	public void render(int mouseX, int mouseY) {
		Point p = getCenteredGuiCoords(mouseX, mouseY, true);
		super.render(p.x, p.y);
	}

	@Override
	public void onPress(int mouseX, int mouseY, int mouseButton) {
		Point p = getCenteredGuiCoords(mouseX, mouseY, false);
		if (p != null)
			super.onPress(p.x, p.y, mouseButton);
	}

	@Override
	public void onRelease(int mouseX, int mouseY, int state) {
		Point p = getCenteredGuiCoords(mouseX, mouseY, false);
		if (p != null)
			super.onRelease(p.x, p.y, state);
	}

	@Override
	public void onDrag(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		Point p = getCenteredGuiCoords(mouseX, mouseY, false);
		if (p != null)
			super.onDrag(p.x, p.y, clickedMouseButton, timeSinceLastClick);
	}
}
