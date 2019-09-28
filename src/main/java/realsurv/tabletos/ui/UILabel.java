package realsurv.tabletos.ui;

import java.awt.Rectangle;

public class UILabel extends UIObject {
	private String label;
	
	public UILabel(String label) {
		setLabel(label);
	}

	public UILabel setLabel(String label) {
		this.label = label;
		setMinimumSize(fontrenderer.getStringWidth(label), fontrenderer.FONT_HEIGHT);
		return this;
	}
	
	@Override
	public void render(int mx, int my) {
		fontrenderer.drawString(label, 0, 0, foregroundColor);
	}
}
