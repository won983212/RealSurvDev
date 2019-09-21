package realsurv.tabletos.ui;

import java.awt.Rectangle;

import net.minecraft.client.gui.Gui;

public class UIRectangle extends UIObject {
	@Override
	public void render(int mx, int my) {
		Rectangle bounds = getActualBounds();
		Gui.drawRect(bounds.x, bounds.y, bounds.x + bounds.width, bounds.y + bounds.height, backgroundColor);
	}
}
