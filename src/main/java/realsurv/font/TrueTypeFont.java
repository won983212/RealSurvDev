package realsurv.font;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.client.renderer.GlStateManager;
import realsurv.TestFrame;

public class TrueTypeFont {
	private static class Glyph {
		public int texture;
		public int textureX;
		public int textureY;
		public int width;
		public int height;
		public int offsetX;
		public int offsetY;
		public float advance;
	}
	
	private static final int BOLD = 1;
	private static final int ITALIC = 2;
	private static final int UNDERLINE = 1;

	private static final int STRIKE_THROUGH = 2;
	private static final int STRING_IMAGE_WIDTH = 256;
	private static final int STRING_IMAGE_HEIGHT = 64;
	private static final int TEXTURE_WIDTH = 256;
	private static final int TEXTURE_HEIGHT = 256;

	private static final int GLYPH_PADDING = 1;

	private HashMap<Long, Glyph> glyphCache = new HashMap<Long, Glyph>();
	private BufferedImage stringVectorImage;
	private Graphics2D stringGraphics;
	private FontImageTexture glyphTexture = new FontImageTexture(TEXTURE_WIDTH, TEXTURE_HEIGHT);
	
	private final Font[] font = new Font[4];
	private final boolean antialias;
	private int[] colorCodes = new int[16];
	
	private int cacheX = 0;
	private int cacheY = 0;
	private int style = 0;
	private int specialStyle = 0;
	
	private int[] ascent = new int[4];
	private int[] descent = new int[4];
	private int[] leading = new int[4];

	protected TrueTypeFont(String family, int size, boolean antialias) {
		for (int i = 0; i < 4; i++) {
			font[i] = new Font(family, i, size);
			FontMetrics metrics = glyphTexture.getGraphic().getFontMetrics(font[i]);
			ascent[i] = metrics.getAscent();
			descent[i] = metrics.getDescent();
			leading[i] = metrics.getLeading();
		}
		this.antialias = antialias;
		allocateStringImage(TEXTURE_WIDTH, TEXTURE_HEIGHT);
		makeColorTable();
	}

	private void allocateStringImage(int w, int h) {
		stringVectorImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		stringGraphics = (Graphics2D) stringVectorImage.getGraphics();
		if (antialias) {
			stringGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			stringGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			stringGraphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		}
		stringGraphics.setColor(Color.white);
		stringGraphics.setBackground(FontImageTexture.TRANSCOLOR);
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
			style |= BOLD;
			return true;
		case 'm':
			specialStyle |= STRIKE_THROUGH;
			return true;
		case 'n':
			specialStyle |= UNDERLINE;
			return true;
		case 'o':
			style |= ITALIC;
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

	private void cacheGlyphs(String text) {
		StringBuilder sb = new StringBuilder();
		int style = 0;
		for (int i = 0; i < text.length(); i++) {
			int cp = text.codePointAt(i);
			if (cp == '」' && i < text.length() - 1) {
				int idx = "rlo".indexOf(text.charAt(i + 1));
				if (idx != -1) {
					cacheGlyphs(sb.toString(), style);
					i++;
					if (idx == 0) {
						style = 0;
					} else {
						style |= idx;
					}
				} else {
					sb.append((char) cp);
				}
			} else {
				sb.append((char) cp);
			}
		}
		cacheGlyphs(sb.toString(), style);
	}

	private TestFrame frame;
	
	private void cacheGlyphs(String text, int style) {
		if (text.length() == 0)
			return;
		
		if(frame == null) {
			frame = new TestFrame();
			frame.initFrame();
		}

		char[] chars = text.toCharArray();
		GlyphVector vec = null;
		Rectangle bounds = null;
		boolean isFirst = false;
		long styleKey = (long) style << 32;

		for (int i = 0; i < chars.length; i++) {
			int cp = text.codePointAt(i);
			if (glyphCache.containsKey(styleKey | cp)) {
				continue;
			}

			if (!isFirst) {
				isFirst = true;
				vec = font[style].layoutGlyphVector(stringGraphics.getFontRenderContext(), chars, 0, chars.length, Font.LAYOUT_LEFT_TO_RIGHT);

				for (int j = 0; j < chars.length; j++) {
					Point2D p = vec.getGlyphPosition(j);
					p.setLocation(p.getX() + GLYPH_PADDING * 2 * j, p.getY());
					vec.setGlyphPosition(j, p);
				}

				bounds = vec.getPixelBounds(null, 0, 0);
				int boundsMaxW = bounds.width + GLYPH_PADDING * 2;
				int boundsMaxH = bounds.height + GLYPH_PADDING * 2;
				if (boundsMaxW > stringVectorImage.getWidth() || boundsMaxH > stringVectorImage.getHeight()) {
					int w = Math.max(boundsMaxW, stringVectorImage.getWidth());
					int h = Math.max(boundsMaxH, stringVectorImage.getHeight());
					allocateStringImage(w, h);
				}

				stringGraphics.clearRect(0, 0, boundsMaxW, boundsMaxH + ascent[style]);
				stringGraphics.drawGlyphVector(vec, GLYPH_PADDING - bounds.x, GLYPH_PADDING + ascent[style]);
			}

			Rectangle r = vec.getGlyphPixelBounds(i, null, GLYPH_PADDING - bounds.x, GLYPH_PADDING + ascent[style]);
			Point2D p = vec.getGlyphPosition(i);

			if (cacheX + r.width + GLYPH_PADDING * 2 > TEXTURE_WIDTH) {
				cacheX = 0;
				cacheY += getLineHeight(0) + GLYPH_PADDING * 2;
			}

			if (cacheY + GLYPH_PADDING * 2 + getMaxHeight() > TEXTURE_HEIGHT) {
				cacheX = cacheY = 0;
				glyphTexture.reallocate(TEXTURE_WIDTH, TEXTURE_HEIGHT);
			}

			Glyph glyph = new Glyph();
			glyph.offsetX = (int) (r.x - p.getX()) - GLYPH_PADDING;
			glyph.offsetY = (int) (r.y - p.getY()) - GLYPH_PADDING;
			glyph.width = r.width + GLYPH_PADDING * 2;
			glyph.height = r.height + GLYPH_PADDING * 2;
			glyph.texture = glyphTexture.getGlTextureId();
			glyph.textureX = cacheX;
			glyph.textureY = cacheY;
			glyph.advance = vec.getGlyphMetrics(i).getAdvanceX();

			final int dx = cacheX;
			final int dy = cacheY;
			final int dw = glyph.width;
			final int dh = glyph.height;
			final int rx = r.x - GLYPH_PADDING;
			final int ry = r.y - GLYPH_PADDING;
			glyphTexture.getGraphic().drawImage(stringVectorImage, dx, dy, dx + dw, dy + dh, rx, ry, rx + dw, ry + dh, null);
			glyphTexture.updateTexture(dx, dy, dw, dh);
			frame.updateImageTest(glyphTexture.getImage());
			glyphCache.put(styleKey | cp, glyph);
			cacheX += glyph.width;
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

			Glyph g = glyphCache.get((long) style << 32 | cp);
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
					GlStateManager.translate(0, ascent[0] + 1, 0);
					GlStateManager.glBegin(7);
					GlStateManager.glVertex3f(x, y - 1, 0);
					GlStateManager.glVertex3f(x, y, 0);
					GlStateManager.glVertex3f(x + g.advance, y, 0);
					GlStateManager.glVertex3f(x + g.advance, y - 1, 0);
					GlStateManager.glEnd();
					GlStateManager.translate(0, -ascent[0] - 1, 0);
				}

				if ((specialStyle & STRIKE_THROUGH) > 0) {
					GlStateManager.translate(0, ascent[0] * 3 / 4, 0);
					GlStateManager.glBegin(7);
					GlStateManager.glVertex3f(x, y - 1, 0);
					GlStateManager.glVertex3f(x, y, 0);
					GlStateManager.glVertex3f(x + g.advance, y, 0);
					GlStateManager.glVertex3f(x + g.advance, y - 1, 0);
					GlStateManager.glEnd();
					GlStateManager.translate(0, -ascent[0] * 3 / 4, 0);
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
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
	}

	public int getCharWidth(char c) {
		cacheGlyphs(String.valueOf(c));
		return (int) glyphCache.get((long) c).advance;
	}

	public int getLineHeight(int style) {
		return ascent[style] + descent[style] + leading[style];
	}

	public int getMaxHeight() {
		return ascent[BOLD] + descent[BOLD];
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
}