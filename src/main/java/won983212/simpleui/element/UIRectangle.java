package won983212.simpleui.element;

import java.awt.Dimension;
import java.awt.Rectangle;

import net.minecraft.client.gui.Gui;
import won983212.simpleui.UIObject;

public class UIRectangle extends UIObject {
	@Override
	public void render(int mx, int my) {
		Dimension size = getBoundsSize();
		renderArcRect(0, 0, size.width, size.height, arc, backgroundColor, showShadow);
	}
}
