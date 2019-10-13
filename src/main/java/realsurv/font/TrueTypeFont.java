package realsurv.font;

import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.font.GlyphVector;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import realsurv.font.GlyphTextureCache.GlyphTexture;

public class TrueTypeFont {
	private static class ArrangedGlyph {
		public int x;
		public int y;
		public int advance;
		public int color;
		public int index;
		public int affectedStyle;
		public GlyphTexture texture;
	}

	private static class FormattedString {
		public int advance;
		public ArrangedGlyph[] glyphs;
	}

	private static final int UNDERLINE = 4;
	private static final int STRIKE_THROUGH = 8;

	private ArrangedGlyph[][] digitCache;
	private HashMap<String, FormattedString> stringCache = new HashMap<>();
	private final Font[] font = new Font[4];
	private final boolean antialias;
	private int[] colorCodes = new int[16];
	private GlyphTextureCache glyphTextures;
	private int style = 0;
	private int specialStyle = 0;
	private int lastColor = 0;

	protected TrueTypeFont(String family, int size, boolean antialias) {
		for (int i = 0; i < 4; i++)
			font[i] = new Font(family, i, size);
		this.antialias = antialias;
		this.glyphTextures = new GlyphTextureCache(this);
		makeColorTable();
	}

	private void cacheDigits() {
		if (digitCache != null)
			return;
		digitCache = new ArrangedGlyph[4][];
		digitCache[Font.PLAIN] = cacheString("0123456789").glyphs;
		digitCache[Font.BOLD] = cacheString("§l0123456789").glyphs;
		digitCache[Font.ITALIC] = cacheString("§o0123456789").glyphs;
		digitCache[Font.BOLD | Font.ITALIC] = cacheString("§l§o0123456789").glyphs;
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
		lastColor = color;
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
		if (value != null)
			return value;

		ArrayList<ArrangedGlyph> glyphList = new ArrayList<>();
		StringBuilder cacheList = new StringBuilder();
		int advance = 0;
		int lastIndex = 0;

		style = specialStyle = 0;
		lastColor = 0xffffffff;
		for (int i = 0; i < str.length(); i++) {
			int cp = str.codePointAt(i);
			if (cp == '§' && i < str.length() - 1) {
				if ("0123456789abcdefmnrlo".indexOf(cp) != -1) {
					advance += cacheString(cacheList.toString(), glyphList, lastIndex, advance, style, specialStyle, lastColor);
					applyStyle(str.charAt(i + 1), false);
					lastIndex = ++i + 1;
				} else {
					cacheList.append((char) cp);
				}
			} else {
				cacheList.append((char) cp);
			}
		}
		value = new FormattedString();
		value.advance = advance + cacheString(cacheList.toString(), glyphList, lastIndex, advance, style, specialStyle, lastColor);
		value.glyphs = glyphList.toArray(new ArrangedGlyph[glyphList.size()]);
		stringCache.put(key, value);
		return value;
	}

	private int cacheString(String str, ArrayList<ArrangedGlyph> glyphs, int start, float advance, int fontStyle, int specialStyle, int color) {
		if (str.length() == 0)
			return 0;

		GlyphTexture[] textures = glyphTextures.cacheGlyphs(str, fontStyle);
		GlyphVector vec = glyphTextures.layoutGlyphVector(font[fontStyle], str);
		Rectangle bounds = vec.getPixelBounds(null, 0, 0);
		float[] locations = vec.getGlyphPositions(0, str.length() + 1, null);
		int offsetY = glyphTextures.getAscent(fontStyle) - glyphTextures.getDescent(fontStyle) - glyphTextures.getLeading(fontStyle);
		double adv = 0;
		
		//TODO 어떻게 폰트를 더 잘 그릴 수 있을까?
		for (int i = 0; i < str.length(); i++) {
			Rectangle2D pos = vec.getGlyphVisualBounds(i).getBounds2D();
			ArrangedGlyph glyph = new ArrangedGlyph();
			glyph.index = start + i;
			glyph.x = (int)(pos.getX() - locations[(i + 1) * 2] + adv + advance);
			glyph.y = (int)(pos.getY() + offsetY);
			glyph.color = color;
			glyph.affectedStyle = fontStyle | specialStyle;
			glyph.texture = textures[i];
			glyph.advance = (int) (locations[(i + 1) * 2] - locations[i * 2]);
			glyphs.add(glyph);
			adv += locations[(i + 1) * 2] - locations[i * 2];
		}

		return (int) locations[locations.length - 2];
	}

	public int drawString(String str, double x, double y, int color) {
		applyColor(color);
		enableBlend();
		GlStateManager.enableAlpha();

		Tessellator tes = Tessellator.getInstance();
		BufferBuilder buf = tes.getBuffer();
		FormattedString cached = cacheString(str);
		double lineY = 0;

		for (int i = 0; i < str.length(); i++) {
			int offsetX = 0;
			ArrangedGlyph glyph = cached.glyphs[i];
			char charAt = str.charAt(glyph.index);
			GlyphTexture tex = glyph.texture;

			if (charAt >= '0' && charAt <= '9') {
				cacheDigits();
				ArrangedGlyph digitGlyph = digitCache[glyph.affectedStyle][charAt - '0'];
				tex = digitGlyph.texture;
				offsetX = (glyph.advance - digitGlyph.advance) >> 1;
			}

			double minX = x + glyph.x - GlyphTextureCache.GLYPH_PADDING + offsetX;
			double minY = y + glyph.y - GlyphTextureCache.GLYPH_PADDING;
			double maxX = minX + tex.width;
			double maxY = minY + tex.height;

			if (charAt != ' ') {
				GlStateManager.enableTexture2D();
				GlStateManager.bindTexture(tex.texture);
				buf.begin(7, DefaultVertexFormats.POSITION_TEX);
				buf.pos(minX, minY, 0).tex(tex.u1, tex.v1).endVertex();
				buf.pos(minX, maxY, 0).tex(tex.u1, tex.v2).endVertex();
				buf.pos(maxX, maxY, 0).tex(tex.u2, tex.v2).endVertex();
				buf.pos(maxX, minY, 0).tex(tex.u2, tex.v1).endVertex();
				tes.draw();
			}

			if ((glyph.affectedStyle & (UNDERLINE | STRIKE_THROUGH)) > 0) {
				GlStateManager.disableTexture2D();
				if ((glyph.affectedStyle & UNDERLINE) > 0) {
					lineY = y + glyphTextures.getAscent(0) + 1;
					buf.begin(7, DefaultVertexFormats.POSITION);
					buf.pos(x, lineY - 1, 0).endVertex();
					buf.pos(x, lineY, 0).endVertex();
					buf.pos(x + glyph.advance, lineY, 0).endVertex();
					buf.pos(x + glyph.advance, lineY - 1, 0).endVertex();
					tes.draw();
				}
				if ((glyph.affectedStyle & STRIKE_THROUGH) > 0) {
					lineY = y + glyphTextures.getAscent(0) * 3 / 4;
					buf.begin(7, DefaultVertexFormats.POSITION);
					buf.pos(x, lineY - 1, 0).endVertex();
					buf.pos(x, lineY, 0).endVertex();
					buf.pos(x + glyph.advance, lineY, 0).endVertex();
					buf.pos(x + glyph.advance, lineY - 1, 0).endVertex();
					tes.draw();
				}
			}
		}

		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
		return (int) cached.advance;
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
		return getStringWidth(String.valueOf(c));
	}

	public int getLineHeight(int style) {
		return glyphTextures.getLineHeight(style);
	}

	public int getMaxHeight() {
		return glyphTextures.getMaxHeight();
	}

	public int getStringWidth(String str) {
		return (int) cacheString(str).advance;
	}

	public String trimStringToWidth(String str, int wrapWidth, boolean reverse) {
		StringBuilder sb = new StringBuilder();
		int total = 0;
		FormattedString format = cacheString(str);
		int i = reverse ? format.glyphs.length - 1 : 0;
		while (reverse ? (i >= 0) : (i < format.glyphs.length)) {
			ArrangedGlyph glyph = format.glyphs[i];
			if (total + glyph.advance > wrapWidth) {
				return sb.toString();
			} else {
				total += glyph.advance;
			}
			sb.append((char) str.charAt(glyph.index));
			i += reverse ? -1 : 1;
		}
		return sb.toString();
	}

	public List<String> listFormattedStringToWidth(String str, int wrapWidth) {
		ArrayList<String> ret = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		int total = 0;

		FormattedString format = cacheString(str);
		for (ArrangedGlyph glyph : format.glyphs) {
			if (total + glyph.advance > wrapWidth) {
				ret.add(sb.toString());
				sb = new StringBuilder();
			} else {
				total += glyph.advance;
			}
			sb.append((char) str.charAt(glyph.index));
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

	public AdaptiveTTF makeCompatibleFont() {
		return new AdaptiveTTF(font[0].getFamily(), font[0].getSize());
	}

	public Font getJavaFont(int style) {
		return font[style];
	}

	public boolean isUseAntialias() {
		return antialias;
	}

	@Override
	public String toString() {
		return "TTFont[" + font[0].getFamily() + ", " + font[0].getSize() + "]";
	}
}