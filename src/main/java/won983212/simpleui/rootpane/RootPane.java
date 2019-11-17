package won983212.simpleui.rootpane;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import won983212.simpleui.UIObject;
import won983212.simpleui.panel.UIPanel;

/**
 * Rootpane에서는 이벤트처리 할 때 handleMouseInput과 onKeyTyped만 호출해주면 된다.
 */
public class RootPane extends UIPanel {
	protected final Minecraft mc = Minecraft.getMinecraft();
	protected final Dimension screenSize;
	protected double scaledFactor = 0;
	private UIPanel popupPanel;
	private int eventButton;
	private long lastMouseEvent;
	private int touchValue = 0;

	public RootPane(int width, int height) {
		screenSize = new Dimension(width, height);
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

	/**
	 * 만약 checkCoordsExceed가 true면, coords위치가 bounds를 넘는지 확인해서 bounds밖에 있다면 null리턴
	 */
	protected Point convertCoords(int x, int y, boolean clamp) {
		double factor = scaledFactor;
		if (factor <= 0) {
			ScaledResolution sr = new ScaledResolution(mc);
			factor = sr.getScaleFactor();
		}
		
		Rectangle r = getRelativeBounds();
		x -= r.x / factor;
		y -= r.y / factor;
		x *= factor;
		y *= factor;
		
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

	@Override
	public void addPopup(UIObject obj) {
		popupPanel.add(obj);
	}

	@Override
	public void render(int mouseX, int mouseY) {
		if (isVisible()) {
			Point p = convertCoords(mouseX, mouseY, true);
			Rectangle bounds = getRelativeBounds();
			GlStateManager.translate(bounds.x, bounds.y, 0);
			super.render(p.x, p.y);
			GlStateManager.translate(-bounds.x, -bounds.y, 0);
		}
	}

	@Override
	public void onKeyTyped(int keyCode, char typedChar) {
		if (isInteractive()) {
			super.onKeyTyped(keyCode, typedChar);
		}
	}

	@Override
	public void onPress(int mouseX, int mouseY, int mouseButton) {
		if (isInteractive()) {
			Point p = convertCoords(mouseX, mouseY, false);
			if (p != null)
				super.onPress(p.x, p.y, mouseButton);
		}
	}

	@Override
	public void onRelease(int mouseX, int mouseY, int state) {
		if (isInteractive()) {
			Point p = convertCoords(mouseX, mouseY, false);
			if (p != null)
				super.onRelease(p.x, p.y, state);
		}
	}

	@Override
	public void onDrag(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		if (isInteractive()) {
			Point p = convertCoords(mouseX, mouseY, false);
			if (p != null)
				super.onDrag(p.x, p.y, clickedMouseButton, timeSinceLastClick);
		}
	}

	@Override
	public Dimension measureMinSize() {
		return screenSize;
	}

	public void handleMouseInput(GuiScreen screen) {
		if (isInteractive()) {
			int i = Mouse.getEventX() * screen.width / this.mc.displayWidth;
			int j = screen.height - Mouse.getEventY() * screen.height / this.mc.displayHeight - 1;
			int k = Mouse.getEventButton();

			if (Mouse.getEventButtonState()) {
				if (this.mc.gameSettings.touchscreen && this.touchValue++ > 0) {
					return;
				}
				this.eventButton = k;
				this.lastMouseEvent = Minecraft.getSystemTime();
				onPress(i, j, this.eventButton);
			} else if (k != -1) {
				if (this.mc.gameSettings.touchscreen && --this.touchValue > 0) {
					return;
				}
				this.eventButton = -1;
				onRelease(i, j, k);
			} else if (this.eventButton != -1 && this.lastMouseEvent > 0L) {
				long l = Minecraft.getSystemTime() - this.lastMouseEvent;
				onDrag(i, j, this.eventButton, l);
			}
		}
	}
}
