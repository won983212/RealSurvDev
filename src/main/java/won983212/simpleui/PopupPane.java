package won983212.simpleui;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.math.MathHelper;

/**
 *	PopupPane은 GuiScreen의 아무 위치에 둘 수 있는 작은 규모의 창을 만들 때 유용하다.
 */
public class PopupPane extends UIPanel {
	private static final Minecraft mc = Minecraft.getMinecraft();
	private UIPanel popupPanel;
	private double scaledFactor = 0;

	public void initializePanel(int x, int y, int width, int height) {
		popupPanel = new UIPanel();
		uiList.clear();
		initGui();
		add(popupPanel);
		invalidateSize();
		setRelativeBounds(x, y, width, height);
		layout();
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
	
	private Point convertCoords(int x, int y, boolean clamp) {
		double factor = scaledFactor;
		if (factor <= 0) {
			ScaledResolution sr = new ScaledResolution(mc);
			factor = sr.getScaleFactor();
		}
		
		Rectangle r = getRelativeBounds();
		x -= r.x;
		y -= r.y;
		x /= scaledFactor;
		y /= scaledFactor;
		
		if (x < 0 || x > r.width || y < 0 || y > r.height) {
			if (clamp) {
				x = MathHelper.clamp(x, 0, r.width);
				y = MathHelper.clamp(y, 0, r.height);
			} else {
				return null;
			}
		}
		return new Point(x, y);
	}

	protected void initGui() {
	}

	@Override
	public void addPopup(UIObject obj) {
		popupPanel.add(obj);
	}

	@Override
	public void render(int mouseX, int mouseY) {
		Point p = convertCoords(mouseX, mouseY, true);
		super.render(p.x, p.y);
	}

	@Override
	public void onPress(int mouseX, int mouseY, int mouseButton) {
		Point p = convertCoords(mouseX, mouseY, false);
		if (p != null)
			super.onPress(p.x, p.y, mouseButton);
	}

	@Override
	public void onRelease(int mouseX, int mouseY, int state) {
		Point p = convertCoords(mouseX, mouseY, false);
		if (p != null)
			super.onRelease(p.x, p.y, state);
	}

	@Override
	public void onDrag(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		Point p = convertCoords(mouseX, mouseY, false);
		if (p != null)
			super.onDrag(p.x, p.y, clickedMouseButton, timeSinceLastClick);
	}
}