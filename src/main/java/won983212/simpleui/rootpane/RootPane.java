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
import won983212.simpleui.parentelement.UIObject;
import won983212.simpleui.parentelement.UIPanel;

/**
 * Rootpane에서는 이벤트처리 할 때 handleMouseInput과 onKeyTyped만 호출해주면 된다.
 */
public class RootPane extends UIPanel {
	protected final Minecraft mc = Minecraft.getMinecraft();
	protected final Dimension screenSize;
	private UIPanel popupPanel;
	private int eventButton;
	private long lastMouseEvent;
	private int touchValue = 0;

	public RootPane() {
		this(0, 0);
	}
	
	public RootPane(int width, int height) {
		screenSize = new Dimension(width, height);
	}

	public void initializePanel() {
		initializePanel(1);
	}
	
	public void initializePanel(int scaleFactor) {
		popupPanel = new UIPanel();
		uiList.clear();
		initGui();
		add(popupPanel);
		invalidateSize();
		if(screenSize.width == 0 || screenSize.height == 0) {
			Dimension dim = super.measureMinSize();
			screenSize.setSize(dim.width / (double)scaleFactor, dim.height / (double)scaleFactor);
		}
		setRelativeBounds(0, 0, screenSize.width, screenSize.height);
		layout();
	}

	protected void initGui() {
	}

	/**
	 * 만약 checkCoordsExceed가 true면, coords위치가 bounds를 넘는지 확인해서 bounds밖에 있다면 null리턴
	 */
	protected Point convertCoords(int x, int y, boolean clamp) {
		double factor = new ScaledResolution(mc).getScaleFactor();
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
	public Dimension measureMinSize() {
		return screenSize;
	}

	public boolean handleMouseInput(GuiScreen screen) {
		if (isInteractive()) {
			int i = Mouse.getEventX() * screen.width / this.mc.displayWidth;
			int j = screen.height - Mouse.getEventY() * screen.height / this.mc.displayHeight - 1;
			int k = Mouse.getEventButton();
			Point p = convertCoords(i, j, false);
			if (p != null) {
				if (Mouse.getEventButtonState()) {
					if (this.mc.gameSettings.touchscreen && this.touchValue++ > 0) {
						return false;
					}
					this.eventButton = k;
					this.lastMouseEvent = Minecraft.getSystemTime();
					return onPress(p.x, p.y, this.eventButton);
				} else if (k != -1) {
					if (this.mc.gameSettings.touchscreen && --this.touchValue > 0) {
						return false;
					}
					this.eventButton = -1;
					return onRelease(p.x, p.y, k);
				} else if (this.eventButton != -1 && this.lastMouseEvent > 0L) {
					long l = Minecraft.getSystemTime() - this.lastMouseEvent;
					return onDrag(p.x, p.y, this.eventButton, l);
				}
			}
		}
		return false;
	}
}
