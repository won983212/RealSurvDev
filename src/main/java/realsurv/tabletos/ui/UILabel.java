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
	public void render() {
		Rectangle rect = getActualBounds();
		fontrenderer.drawString(label, rect.x, rect.y, 0xffffffff);
	}
}
