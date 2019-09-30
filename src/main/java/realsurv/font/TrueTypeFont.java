package realsurv.font;

import java.awt.Font;

public class TrueTypeFont {
	private Font font;
	
	public TrueTypeFont(String family, int style, int size) {
		font = new Font(family, style, size);
	}
	
	public Font getFont() {
		return font;
	}
}
