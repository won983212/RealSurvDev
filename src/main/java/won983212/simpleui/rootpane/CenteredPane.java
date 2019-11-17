package won983212.simpleui.rootpane;

import java.awt.Dimension;
import java.awt.Point;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.math.MathHelper;
import won983212.simpleui.UIObject;
import won983212.simpleui.panel.UIPanel;

/**
 *	CenteredPane은 GuiScreen의 중앙에 하나의 창을 만들 때 사용한다.
 */
public class CenteredPane extends RootPane {
	private final Minecraft mc = Minecraft.getMinecraft();
	private int nativeWndWidth = 0;
	private int nativeWndHeight = 0;

	public CenteredPane(int width, int height) {
		super(width, height);
	}

	@Override
	protected Point convertCoords(int x, int y, boolean checkCoordsExceed) {
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
			if (checkCoordsExceed) {
				x = MathHelper.clamp(x, 0, screenSize.width);
				y = MathHelper.clamp(y, 0, screenSize.height);
			} else {
				return null;
			}
		}
		return new Point(x, y);
	}

	public void setNativeWindowSize(int width, int height) {
		this.nativeWndWidth = width;
		this.nativeWndHeight = height;
	}
}
