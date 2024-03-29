package won983212.simpleui.font;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.GlyphVector;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

public class GlyphTextureCache {
	public static class GlyphTexture {
		public int texture;
		public double u1;
		public double v1;
		public double u2;
		public double v2;
		public int width;
		public int height;
	}

	private static final int STRING_IMAGE_WIDTH = 256;
	private static final int STRING_IMAGE_HEIGHT = 64;
	public static final int TEXTURE_WIDTH = 256;
	public static final int TEXTURE_HEIGHT = 256;
	public static final int GLYPH_PADDING = 2;

	private int cacheX = 0;
	private int cacheY = 0;

	private HashMap<Long, GlyphTexture> glyphCache = new HashMap<Long, GlyphTexture>();
	private BufferedImage stringVectorImage;
	private Graphics2D stringGraphics;
	private Alpha8Texture glyphTexture = new Alpha8Texture(TEXTURE_WIDTH, TEXTURE_HEIGHT);
	private TrueTypeFont font;

	private int[] ascent = new int[4];
	private int[] descent = new int[4];
	private int[] leading = new int[4];

	public GlyphTextureCache(TrueTypeFont font) {
		this.font = font;
		allocateStringImage(TEXTURE_WIDTH, TEXTURE_HEIGHT);
		for (int i = 0; i < 4; i++) {
			FontMetrics metrics = stringGraphics.getFontMetrics(font.getJavaFont(i));
			ascent[i] = metrics.getAscent();
			descent[i] = metrics.getDescent();
			leading[i] = metrics.getLeading();
		}
	}

	public GlyphVector layoutGlyphVector(Font font, char[] str, int start, int limit, int layoutFlag) {
		return font.layoutGlyphVector(glyphTexture.getFontRenderContext(), str, start, limit, layoutFlag);
	}

	/*private TestFrame frame = new TestFrame("StringCache");
	private TestFrame frame2 = new TestFrame("GlyphCache");*/

	public GlyphTexture[] cacheGlyphs(Font font, char[] text, int start, int limit, int style) {
		if (limit - start == 0)
			return new GlyphTexture[0];

		ArrayList<GlyphTexture> glyphs = new ArrayList<>();
		GlyphVector vec = layoutGlyphVector(font, text, start, limit, Font.LAYOUT_LEFT_TO_RIGHT);
		Rectangle bounds = null;
		boolean isInitialized = false;
		
		long styleKey = (long) style << 32;
		int countGlyphs = vec.getNumGlyphs();
		int[] textData = vec.getGlyphCodes(0, countGlyphs, null);
		
		for (int i = 0; i < countGlyphs; i++) {
			if (glyphCache.containsKey(styleKey | textData[i])) {
				glyphs.add(glyphCache.get(styleKey | textData[i]));
				continue;
			}

			if (!isInitialized) {
				isInitialized = true;

				for (int j = 0; j < limit - start; j++) {
					Point2D p = vec.getGlyphPosition(j);
					p.setLocation(p.getX() + GLYPH_PADDING * 2 * j, p.getY());
					vec.setGlyphPosition(j, p);
				}

				bounds = vec.getPixelBounds(null, 0, 0);
				int boundsMaxW = bounds.width + GLYPH_PADDING * 4;
				int boundsMaxH = bounds.height + GLYPH_PADDING * 4;
				if (boundsMaxW > stringVectorImage.getWidth() || boundsMaxH > stringVectorImage.getHeight()) {
					int w = Math.max(boundsMaxW, stringVectorImage.getWidth());
					int h = Math.max(boundsMaxH, stringVectorImage.getHeight());
					allocateStringImage(w, h);
				}

				stringGraphics.clearRect(0, 0, boundsMaxW, boundsMaxH + ascent[style]);
				stringGraphics.drawGlyphVector(vec, GLYPH_PADDING - bounds.x, GLYPH_PADDING - bounds.y);

				/*stringGraphics.setColor(Color.cyan);
				for (int j = 0; j < vec.getNumGlyphs(); j++) {
					Rectangle r = vec.getGlyphPixelBounds(j, null, GLYPH_PADDING - bounds.x, GLYPH_PADDING - bounds.y);
					stringGraphics.drawRect(r.x, r.y, r.width - 1, r.height - 1);
				}
				stringGraphics.setColor(Color.white);
				frame.updateImageTest(stringVectorImage);*/
			}
			
			Rectangle r = vec.getGlyphPixelBounds(i, null, GLYPH_PADDING - bounds.x, GLYPH_PADDING - bounds.y);
			
			if (cacheX + r.getWidth() + GLYPH_PADDING * 2 > TEXTURE_WIDTH) {
				cacheX = 0;
				cacheY += getLineHeight(0) + GLYPH_PADDING * 2;
			}

			if (cacheY + GLYPH_PADDING * 2 + getMaxHeight() > TEXTURE_HEIGHT) {
				cacheX = cacheY = 0;
				glyphTexture.reallocate(TEXTURE_WIDTH, TEXTURE_HEIGHT);
			}

			GlyphTexture glyph = new GlyphTexture();
			glyph.width = (int) r.getWidth() + GLYPH_PADDING * 2;
			glyph.height = (int) r.getHeight() + GLYPH_PADDING * 2;
			glyph.texture = glyphTexture.getGlTextureId();
			glyph.u1 = cacheX / (double) TEXTURE_WIDTH;
			glyph.v1 = cacheY / (double) TEXTURE_HEIGHT;
			glyph.u2 = (cacheX + glyph.width) / (double) TEXTURE_WIDTH;
			glyph.v2 = (cacheY + glyph.height) / (double) TEXTURE_HEIGHT;

			final int dx = cacheX;
			final int dy = cacheY;
			final int dw = glyph.width;
			final int dh = glyph.height;
			final int rx = r.x - GLYPH_PADDING;
			final int ry = r.y - GLYPH_PADDING;
			
			glyphTexture.getGraphic().drawImage(stringVectorImage, dx, dy, dx + dw, dy + dh, rx, ry, rx + dw, ry + dh, null);
			glyphTexture.updateTexture(dx, dy, dw, dh);
			glyphCache.put(styleKey | textData[i], glyph);
			glyphs.add(glyph);
			cacheX += glyph.width;
		}
		return glyphs.toArray(new GlyphTexture[glyphs.size()]);
	}

	private void allocateStringImage(int w, int h) {
		stringVectorImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		stringGraphics = (Graphics2D) stringVectorImage.getGraphics();
		if (font.isUseAntialias()) {
			stringGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			stringGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			stringGraphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		}
		stringGraphics.setColor(Color.white);
		stringGraphics.setBackground(Alpha8Texture.TRANSCOLOR);
	}

	public int getLineHeight(int style) {
		return ascent[style] + descent[style] + leading[style];
	}

	public int getMaxHeight() {
		return ascent[Font.BOLD];
	}

	public int getAscent(int style) {
		return ascent[style];
	}

	public int getDescent(int style) {
		return descent[style];
	}

	public int getLeading(int style) {
		return leading[style];
	}

	public void clear() {
		glyphCache.clear();
	}
}
