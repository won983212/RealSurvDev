package won983212.guitoolkit.element;

import java.awt.Rectangle;

import won983212.guitoolkit.UIObject;
import won983212.guitoolkit.font.TrueTypeFont;

public class UILabel extends UIObject {
	private String label;
	
	public UILabel() {
		setShadowVisible(false);
	}

	public UILabel setLabel(String label) {
		TrueTypeFont font = getFont();
		this.label = label;
		setMinimumSize(font.getStringWidth(label), font.getMaxHeight());
		return this;
	}
	
	@Override
	public void render(int mx, int my) {
		getFont().drawString(label, 0, 0, foregroundColor, showShadow);
	}
}
