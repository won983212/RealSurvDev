package realsurv.tabletos.ui;

import java.awt.Dimension;
import java.awt.Rectangle;

import net.minecraft.client.gui.Gui;

public class UIRectangle extends UIObject {
	@Override
	public void render(int mx, int my) {
		Dimension size = getBoundsSize();
		Gui.drawRect(0, 0, size.width, size.height, backgroundColor);
	}
}
