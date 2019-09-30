package realsurv.font;

import java.awt.Font;
import java.awt.font.GlyphVector;

public class GlyphCache {
	private static final int MAX_UNICODE = 0x10ffff;
	private static final int GLYPHS_PER_PAGE = 512;
	private static final int PAGES = (int)(MAX_UNICODE / (double) GLYPHS_PER_PAGE + 0.99);
	
	private TrueTypeFont font;
	private CanvasTexture[] pages = new CanvasTexture[PAGES];
	private Glyph[][] glyphs = new Glyph[PAGES][];
	
	public GlyphCache(TrueTypeFont font) {
		this.font = font;
	}
	
	public Glyph getGlyph(int codepoint) {
		int page = codepoint / PAGES;
		int glyphIndex = codepoint % PAGES;
		if(glyphs[page] == null) {
			glyphs[page] = new Glyph[GLYPHS_PER_PAGE];
			cacheGlyphs(page);
		}
		return glyphs[page][glyphIndex];
	}

	//TODO Caching
	private void cacheGlyphs(int page) {
		int x = 0;
		int y = 0;
		Font f = font.getFont();
		int square = (int)(f.getSize2D() * 4 / 3.0 + 5);
		
		pages[page] = new CanvasTexture(square, square);
		GlyphVector vec = f.layoutGlyphVector(pages[page].getFontCtx(), makeChars(page), 0, GLYPHS_PER_PAGE, Font.LAYOUT_LEFT_TO_RIGHT);
		for(int code = page * GLYPHS_PER_PAGE; code <= (page + 1) * GLYPHS_PER_PAGE; code++) {
			
			
		}
	}
	
	private char[] makeChars(int page) {
		char[] ret = new char[GLYPHS_PER_PAGE];
		for(int code = 0; code <= GLYPHS_PER_PAGE; code++) {
			ret[code] = (char)(code + page * GLYPHS_PER_PAGE);
		}
		return ret;
	}
}
