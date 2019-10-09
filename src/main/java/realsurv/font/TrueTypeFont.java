package realsurv.font;

import java.awt.Font;
import java.awt.Point;
import java.awt.font.GlyphVector;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.client.renderer.GlStateManager;
import realsurv.font.GlyphTextureCache.GlyphTexture;

public class TrueTypeFont {
	private static class ArrangedGlyph {
		public int x;
		public int y;
		public float advance;
		public int codepoint;
		public GlyphTexture texture;
	}
	
	private static class FormattedString {
		public float advance;
		public ArrangedGlyph[] glyphs;
	}

	private static final int UNDERLINE = 1;
	private static final int STRIKE_THROUGH = 2;

	private HashMap<String, FormattedString> stringCache = new HashMap<>();
	private final Font[] font = new Font[4];
	private final boolean antialias;
	private int[] colorCodes = new int[16];

	private GlyphTextureCache glyphTextures;
	private int style = 0;
	private int specialStyle = 0;

	protected TrueTypeFont(String family, int size, boolean antialias) {
		for (int i = 0; i < 4; i++)
			font[i] = new Font(family, i, size);
		this.glyphTextures = new GlyphTextureCache(this);
		this.antialias = antialias;
		makeColorTable();
	}

	// All digits will be zero.
	private String getGeneralizedKey(String text) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < text.length(); i++) {
			int cp = text.codePointAt(i);
			if (cp >= '0' && cp <= '9') {
				sb.append('0');
			} else {
				sb.append((char) cp);
			}
		}
		return sb.toString();
	}

	private void applyColor(int color) {
		float f3 = (color >> 24 & 255) / 255.0F;
		float f = (color >> 16 & 255) / 255.0F;
		float f1 = (color >> 8 & 255) / 255.0F;
		float f2 = (color & 255) / 255.0F;
		GlStateManager.color(f, f1, f2, f3);
	}

	private boolean applyStyle(char code, boolean applyColor) {
		switch (code) {
		case 'k':
			return true;
		case 'l':
			style |= Font.BOLD;
			return true;
		case 'm':
			specialStyle |= STRIKE_THROUGH;
			return true;
		case 'n':
			specialStyle |= UNDERLINE;
			return true;
		case 'o':
			style |= Font.ITALIC;
			return true;
		case 'r':
			style = specialStyle = 0;
			if (applyColor)
				applyColor(0xffffffff);
			return true;
		default:
			int idx = "0123456789abcdef".indexOf(code);
			if (idx != -1) {
				if (applyColor)
					applyColor(colorCodes[idx]);
				return true;
			}
			return false;
		}
	}
	
	private FormattedString cacheString(String str) {
		String key = getGeneralizedKey(str);
		FormattedString value = stringCache.get(key);
		if(value != null)
			return value;
		
		ArrayList<ArrangedGlyph> glyphList = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		int style = 0;
		int advance = 0;
		for (int i = 0; i < str.length(); i++) {
			int cp = str.codePointAt(i);
			if (cp == '」' && i < str.length() - 1) {
				int idx = "rlo".indexOf(str.charAt(i + 1));
				if (idx != -1) {
					advance += cacheString(sb.toString(), glyphList, advance, style);
					if (idx == 0) {
						style = 0;
					} else {
						style |= idx;
					}
					i++;
				} else {
					sb.append((char) cp);
				}
			} else {
				sb.append((char) cp);
			}
		}
		value = new FormattedString();
		value.advance = advance + cacheString(sb.toString(), glyphList, 0, style);
		value.glyphs = glyphList.toArray(new ArrangedGlyph[glyphList.size()]);
		stringCache.put(key, value);
		return value;
	}
	
	private int cacheString(String str, ArrayList<ArrangedGlyph> glyphs, float advance, int style) {
		if(str.length() == 0)
			return 0;
		
		GlyphTexture[] textures = glyphTextures.cacheGlyphs(str, style);
		GlyphVector vec = glyphTextures.layoutGlyphVector(font[style], str);
		float[] locations = new float[str.length()];
		vec.getGlyphPositions(0, str.length(), locations);
		
		for(int i=0;i<str.length();i++) {
			Point pos = vec.getGlyphPixelBounds(i, null, advance, 0).getLocation();
			ArrangedGlyph glyph = new ArrangedGlyph();
			glyph.codepoint = str.codePointAt(i);
			glyph.x = pos.x;
			glyph.y = pos.y;
			glyph.advance = locations[i];
			//TODO Cache STring implement.
		}
	}

	public int drawString(String str, float x, float y, int color) {
		cacheGlyphs(str);
		applyColor(color);
		enableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();

		style = specialStyle = 0;
		for (int i = 0; i < str.length(); i++) {
			int cp = str.codePointAt(i);
			if (cp == '」' && i < str.length() - 1) {
				if (applyStyle(str.charAt(i + 1), true)) {
					i++;
					continue;
				}
			}

			GlyphTexture g = glyphCache.get((long) style << 32 | cp);
			final float minX = x + g.offsetX;
			final float minY = y + g.offsetY;
			final float texW = TEXTURE_WIDTH;
			final float texH = TEXTURE_HEIGHT;

			if (cp != ' ') {
				GlStateManager.bindTexture(g.texture);
				GlStateManager.glBegin(7);
				GlStateManager.glTexCoord2f(g.textureX / texW, g.textureY / texH);
				GlStateManager.glVertex3f(minX, minY, 0);
				GlStateManager.glTexCoord2f(g.textureX / texW, (g.textureY + g.height) / texH);
				GlStateManager.glVertex3f(minX, minY + g.height, 0);
				GlStateManager.glTexCoord2f((g.textureX + g.width) / texW, (g.textureY + g.height) / texH);
				GlStateManager.glVertex3f(minX + g.width, minY + g.height, 0);
				GlStateManager.glTexCoord2f((g.textureX + g.width) / texW, g.textureY / texH);
				GlStateManager.glVertex3f(minX + g.width, minY, 0);
				GlStateManager.glEnd();
			}

			if (specialStyle != 0) {
				GlStateManager.disableTexture2D();
				if ((specialStyle & UNDERLINE) > 0) {
					GlStateManager.translate(0, glyphTextures.getAscent(0) + 1, 0);
					GlStateManager.glBegin(7);
					GlStateManager.glVertex3f(x, y - 1, 0);
					GlStateManager.glVertex3f(x, y, 0);
					GlStateManager.glVertex3f(x + g.advance, y, 0);
					GlStateManager.glVertex3f(x + g.advance, y - 1, 0);
					GlStateManager.glEnd();
					GlStateManager.translate(0, -glyphTextures.getAscent(0) - 1, 0);
				}

				if ((specialStyle & STRIKE_THROUGH) > 0) {
					GlStateManager.translate(0, glyphTextures.getAscent(0) * 3 / 4, 0);
					GlStateManager.glBegin(7);
					GlStateManager.glVertex3f(x, y - 1, 0);
					GlStateManager.glVertex3f(x, y, 0);
					GlStateManager.glVertex3f(x + g.advance, y, 0);
					GlStateManager.glVertex3f(x + g.advance, y - 1, 0);
					GlStateManager.glEnd();
					GlStateManager.translate(0, -glyphTextures.getAscent(0) * 3 / 4, 0);
				}

				GlStateManager.enableTexture2D();
			}
			x += g.advance;
		}
		GlStateManager.disableBlend();
		return (int) x;
	}

	public int drawString(String str, float x, float y, int color, boolean shadow) {
		if (shadow) {
			drawString(str, x + 1, y + 1, (color & 16579836) >> 2 | color & -16777216);
			return drawString(str, x, y, color) + 1;
		} else {
			return drawString(str, x, y, color);
		}
	}

	private void enableBlend() {
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
				GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
				GlStateManager.DestFactor.ZERO);
	}

	public int getCharWidth(char c) {
		cacheGlyphs(String.valueOf(c));
		return (int) glyphCache.get((long) c).advance;
	}

	public int getLineHeight(int style) {
		return glyphTextures.getLineHeight(style);
	}

	public int getMaxHeight() {
		return glyphTextures.getMaxHeight();
	}

	public int getStringWidth(String str) {
		int width = 0;
		cacheGlyphs(str);
		style = specialStyle = 0;
		for (int i = 0; i < str.length(); i++) {
			int cp = str.codePointAt(i);
			if (cp == '」' && i < str.length() - 1) {
				if (applyStyle(str.charAt(i + 1), false)) {
					i++;
					continue;
				}
			}
			width += glyphCache.get((long) style << 32 | cp).advance;
		}
		return width;
	}

	public List<String> listFormattedStringToWidth(String str, int wrapWidth) {
		ArrayList<String> ret = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		float total = 0;

		cacheGlyphs(str);
		style = specialStyle = 0;
		for (int i = 0; i < str.length(); i++) {
			int cp = str.codePointAt(i);
			if (cp == '」' && i < str.length() - 1) {
				if (applyStyle(str.charAt(i + 1), false)) {
					i++;
					continue;
				}
			}

			float width = glyphCache.get((long) style << 32 | cp).advance;
			if (total + width > wrapWidth) {
				ret.add(sb.toString());
				sb = new StringBuilder();
			} else {
				total += width;
			}
			sb.append((char) cp);
		}
		if (sb.length() > 0)
			ret.add(sb.toString());
		return ret;
	}

	private void makeColorTable() {
		for (int i = 0; i < 16; ++i) {
			int j = (i >> 3 & 1) * 85;
			int k = (i >> 2 & 1) * 170 + j;
			int l = (i >> 1 & 1) * 170 + j;
			int i1 = (i >> 0 & 1) * 170 + j;
			if (i == 6)
				k += 85;
			colorCodes[i] = 255 << 24 | (k & 255) << 16 | (l & 255) << 8 | i1 & 255;
		}
	}

	public TTFRenderer makeCompatibleFont() {
		return new TTFRenderer(font[0].getFamily(), font[0].getSize());
	}

	public Font getJavaFont(int style) {
		return font[style];
	}

	public boolean isUseAntialias() {
		return antialias;
	}
}