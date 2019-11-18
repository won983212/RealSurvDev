package won983212.simpleui.element;

import java.awt.Dimension;
import java.awt.Rectangle;

import net.minecraft.client.gui.Gui;
import won983212.simpleui.font.TrueTypeFont;
import won983212.simpleui.parentelement.UIObject;

public class UIRectangle extends UIObject {
	private String label = "";
	
	public UIRectangle setLabel(String label) {
		TrueTypeFont font = getFont();
		this.label = label;
		setMinimumSize(font.getStringWidth(label), font.getStringHeight(label));
		return this;
	}
	
	@Override
	public void render(int mx, int my) {
		Dimension size = getBoundsSize();
		renderArcRect(0, 0, size.width, size.height, arc, backgroundColor, showShadow);
		
		Rectangle r = getPadding().getContentRect(getInnerBounds());
		TrueTypeFont font = getFont();
		int x = r.x + (r.width - font.getStringWidth(label)) / 2;
		int y = r.y + (r.height - font.getStringHeight(label)) / 2;
		getFont().drawString(label, x, y, foregroundColor, showShadow);
	}
}
