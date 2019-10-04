package realsurv.font;

import java.awt.AlphaComposite;
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
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureUtil;

public class TrueTypeFont {
	private static final int BOLD = 1;
	private static final int ITALIC = 2;
	private static final int UNDERLINE = 1;
	private static final int STRIKE_THROUGH = 2;

	private static final int STRING_IMAGE_WIDTH = 256;
	private static final int STRING_IMAGE_HEIGHT = 64;
	private static final int TEXTURE_WIDTH = 256;
	private static final int TEXTURE_HEIGHT = 256;
	private static final int GLYPH_PADDING = 2;

	private HashMap<Long, Glyph> glyphCache = new HashMap<Long, Glyph>();

	private BufferedImage stringVectorImage;
	private BufferedImageTexture glyphTexture = new BufferedImageTexture(TEXTURE_WIDTH, TEXTURE_HEIGHT);
	/*private int imageData[] = new int[TEXTURE_WIDTH * TEXTURE_HEIGHT];
	private IntBuffer imageBuffer = ByteBuffer.allocateDirect(4 * TEXTURE_WIDTH * TEXTURE_HEIGHT)
			.order(ByteOrder.BIG_ENDIAN).asIntBuffer();*/

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

	public TrueTypeFont(String family, int size, boolean antialias) {
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

	public int getLineHeight(int style) {
		return ascent[style] + descent[style] + leading[style];
	}

	private void applyColor(int color) {
		float f3 = (float) (color >> 24 & 255) / 255.0F;
		float f = (float) (color >> 16 & 255) / 255.0F;
		float f1 = (float) (color >> 8 & 255) / 255.0F;
		float f2 = (float) (color & 255) / 255.0F;
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

	// TODO Need to Implementation.
	public List<String> listFormattedStringToWidth(String str, int wrapWidth) {
		ArrayList<String> ret = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		float total = 0;

		cacheGlyphs(str, 0);
		for (char c : str.toCharArray()) {
			float width = glyphCache.get(c).advance;
			if (total + width > wrapWidth) {
				ret.add(sb.toString());
				sb = new StringBuilder();
			} else {
				total += width;
			}
			sb.append(c);
		}
		if (sb.length() > 0)
			ret.add(sb.toString());
		return ret;
	}

	// TODO Need to implementation
	public int getCharWidth(char c) {
		cacheGlyphs(String.valueOf(c), 0);
		return (int) glyphCache.get(c).advance;
	}

	// TODO Need to implementation.
	public int getStringWidth(String str) {
		int width = 0;
		cacheGlyphs(str, 0);
		for (int i = 0; i < str.length(); i++) {
			int cp = str.codePointAt(i);
			if (cp == '§' && i < str.length() - 1) {
				if (applyStyle(str.charAt(i + 1), false)) {
					i++;
					continue;
				}
			}
			width += glyphCache.get((long) cp).advance;
		}
		return width;
	}

	public int getMaxHeight() {
		return ascent[BOLD] + descent[BOLD];
	}

	private void enableBlend() {
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
				GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
				GlStateManager.DestFactor.ZERO);
	}

	public int drawString(String str, float x, float y, int color, boolean shadow) {
		if (shadow) {
			drawString(str, x + 1, y + 1, (color & 16579836) >> 2 | color & -16777216);
			return drawString(str, x, y, color) + 1;
		} else {
			return drawString(str, x, y, color);
		}
	}

	public int drawString(String str, float x, float y, int color) {
		cacheGlyphs(str, 0);
		applyColor(color);
		enableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
		
		style = specialStyle = 0;
		for (int i = 0; i < str.length(); i++) {
			int cp = str.codePointAt(i);
			if (cp == '§' && i < str.length() - 1) {
				if (applyStyle(str.charAt(i + 1), true)) {
					i++;
					continue;
				}
			}

			//TODO Test. style is not considered.
			Glyph g = glyphCache.get((long)cp);
			final float minX = x + g.offsetX;
			final float minY = y + g.offsetY;
			
			if (cp != ' ') {
				GlStateManager.bindTexture(g.texture);
				//GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
				GlStateManager.glBegin(7);
				GlStateManager.glTexCoord2f(g.textureX / TEXTURE_WIDTH, g.textureY / TEXTURE_HEIGHT);
				GlStateManager.glVertex3f(minX, minY, 0);
				GlStateManager.glTexCoord2f(g.textureX / TEXTURE_WIDTH, (g.textureY + g.height) / TEXTURE_HEIGHT);
				GlStateManager.glVertex3f(minX, minY + g.height, 0);
				GlStateManager.glTexCoord2f((g.textureX + g.width) / TEXTURE_WIDTH, (g.textureY + g.height) / TEXTURE_HEIGHT);
				GlStateManager.glVertex3f(minX + g.width, minY + g.height, 0);
				GlStateManager.glTexCoord2f((g.textureX + g.width) / TEXTURE_WIDTH, g.textureY / TEXTURE_HEIGHT);
				GlStateManager.glVertex3f(minX + g.width, minY, 0);
				GlStateManager.glEnd();
			}

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
			x += g.advance;
		}
		GlStateManager.disableBlend();
		return (int) x;
	}

	private void cacheGlyphs(String text, int style) {
		char[] chars = text.toCharArray();
		GlyphVector vec = null;
		Rectangle bounds = null;
		boolean isFirst = false;
		long styleKey = (long) style << 32;
		Graphics2D g = (Graphics2D) stringVectorImage.getGraphics();
		FontRenderContext ctx = g.getFontRenderContext();
		
		for (int i = 0; i < chars.length; i++) {
			int cp = text.codePointAt(i);
			if(glyphCache.containsKey(styleKey | cp)) {
				continue;
			}
			
			if (!isFirst) {
				isFirst = true;
				vec = font[style].layoutGlyphVector(ctx, chars, 0, text.length(), Font.LAYOUT_LEFT_TO_RIGHT);

				for (int j = 0; j < chars.length; j++) {
					Point2D p = vec.getGlyphPosition(j);
					p.setLocation(p.getX() + GLYPH_PADDING * 2 * j, p.getY());
					vec.setGlyphPosition(j, p);
				}

				bounds = vec.getPixelBounds(ctx, GLYPH_PADDING, GLYPH_PADDING);
				int boundsMaxW = bounds.width + GLYPH_PADDING * 2;
				int boundsMaxH = bounds.height + GLYPH_PADDING * 2;
				if (boundsMaxW > stringVectorImage.getWidth() || boundsMaxH > stringVectorImage.getHeight()) {
					int w = Math.max(boundsMaxW, stringVectorImage.getWidth());
					int h = Math.max(boundsMaxH, stringVectorImage.getHeight());
					allocateStringImage(w, h);
				}

				g.clearRect(0, 0, boundsMaxH, boundsMaxW);
				g.drawGlyphVector(vec, GLYPH_PADDING - bounds.x, GLYPH_PADDING - bounds.y);
			}

			Rectangle r = vec.getGlyphPixelBounds(i, ctx, GLYPH_PADDING - bounds.x, GLYPH_PADDING - bounds.y);
			Point2D p = vec.getGlyphPosition(i);

			if (cacheX + r.width + GLYPH_PADDING * 2 > TEXTURE_WIDTH) {
				cacheX = 0;
				cacheY += getLineHeight(0) + GLYPH_PADDING * 2;
			}

			if (cacheY + GLYPH_PADDING * 2 > TEXTURE_HEIGHT) {
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
			final int rx = r.x;
			final int ry = r.y;
			glyphTexture.getGraphic().drawImage(stringVectorImage, dx, dy, dx + dw, dy + dh, rx, ry, rx + dw, ry + dh, null);
			glyphTexture.updateTexture(dx, dy, dw, dh);
			glyphCache.put(styleKey | cp, glyph);
			cacheX += glyph.width;
		}
		glyphTexture.saveImage();
	}

	/*
	private void updateTexture(int x, int y, int width, int height) {
		updateImageBuffer(x, y, width, height);
		GlStateManager.bindTexture(texture);
		GlStateManager.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, x, y, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
				imageBuffer);
	}

	private void allocateGlyphCacheTexture() {
		glyphGraphic.clearRect(0, 0, TEXTURE_WIDTH, TEXTURE_HEIGHT);
		texture = TextureUtil.glGenTextures();

		updateImageBuffer(0, 0, TEXTURE_WIDTH, TEXTURE_HEIGHT);
		GlStateManager.bindTexture(texture);
		GlStateManager.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, TEXTURE_WIDTH, TEXTURE_HEIGHT, 0, GL11.GL_RGBA,
				GL11.GL_UNSIGNED_BYTE, imageBuffer);
	}

	private void updateImageBuffer(int x, int y, int width, int height) {
		glyphImage.getRGB(x, y, width, height, imageData, 0, width);
		for (int i = 0; i < width * height; i++) {
			int color = imageData[i];
			imageData[i] = (color << 8) | (color >>> 24);
		}
		imageBuffer.clear();
		imageBuffer.put(imageData);
		imageBuffer.flip();
	}*/

	private Graphics2D allocateStringImage(int w, int h) {
		stringVectorImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) stringVectorImage.getGraphics();
		if (antialias) {
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		}
		g.setColor(Color.white);
		g.setBackground(BufferedImageTexture.TRANSCOLOR);
		return g;
	}

	public TTFRenderer makeCompatibleFont() {
		return new TTFRenderer(font[0].getFamily(), font[0].getSize());
	}

	// TODO 굳이 offset이 필요할까?
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
}