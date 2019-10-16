package won983212.guitoolkit.font;

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import won983212.guitoolkit.font.GlyphTextureCache.GlyphTexture;

//TODO BIDI는 렌더링이 안되는듯?
//TODO 알 수 없는 폰트는 알아서 찾아서 설정하도록
//TODO 갑자기 black color되어버리는 현상
//TODO 폰트 고화질 모드도 있게 만들기
public class TrueTypeFont {
	private static class ArrangedGlyph {
		public int x;
		public int y;
		public int advance;
		public int colorIndex;
		public int index;
		public int affectedStyle;
		public GlyphTexture texture;
	}

	private static class FormattedString {
		public WeakReference<String> keyReference;
		public int advance;
		public ArrangedGlyph[] glyphs;
	}

	private static final int UNDERLINE = 4;
	private static final int STRIKE_THROUGH = 8;

	private ArrangedGlyph[][] digitCache;
	private WeakHashMap<String, FormattedString> stringCache = new WeakHashMap<>();
	private WeakHashMap<String, String> inUseMap = new WeakHashMap<>();
	private final Font[] font = new Font[4];
	private final boolean antialias;
	private int[] colorCodes = new int[32];
	private GlyphTextureCache glyphTextures;
	private int fontStyle = 0;
	private int specialStyle = 0;
	private int colorIdx = -1;
	private double scaleModifier = 1;

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

	private void applyColor(int color, float alpha) {
		float f = (color >> 16 & 255) / 255.0F;
		float f1 = (color >> 8 & 255) / 255.0F;
		float f2 = (color & 255) / 255.0F;
		GlStateManager.color(f, f1, f2, alpha);
	}

	private boolean applyStyle(char code) {
		switch (code) {
		case 'k':
			return true;
		case 'l':
			fontStyle |= Font.BOLD;
			return true;
		case 'm':
			specialStyle |= STRIKE_THROUGH;
			return true;
		case 'n':
			specialStyle |= UNDERLINE;
			return true;
		case 'o':
			fontStyle |= Font.ITALIC;
			return true;
		case 'r':
			fontStyle = specialStyle = 0;
			colorIdx = 15;
			return true;
		default:
			int idx = "0123456789abcdef".indexOf(code);
			if (idx != -1) {
				colorIdx = idx;
				return true;
			}
			return false;
		}
	}

	private FormattedString cacheString(String str) {
		String key = getGeneralizedKey(str);
		char[] strc = str.toCharArray();
		FormattedString value = stringCache.get(key);
		if (value == null) {
			ArrayList<ArrangedGlyph> glyphList = new ArrayList<>();
			int advance = 0;
			int start = 0;

			fontStyle = specialStyle = 0;
			colorIdx = -1;
			for (int i = 0; i < strc.length; i++) {
				int cp = str.codePointAt(i);
				if (cp == '§' && i < strc.length - 1) {
					if ("0123456789abcdefmnrlo".indexOf(strc[i+1]) != -1) {
						advance += cacheString(strc, glyphList, start, i, advance);
						applyStyle(strc[i+1]);
						start = ++i + 1;
					}
				}
			}
			value = new FormattedString();
			String newKeyString = new String(key);
			value.keyReference = new WeakReference<String>(newKeyString);
			value.advance = advance + cacheString(strc, glyphList, start, str.length(), advance);
			value.glyphs = glyphList.toArray(new ArrangedGlyph[glyphList.size()]);
			stringCache.put(newKeyString, value);
		}
		String keyRef = value.keyReference.get();
		if (keyRef != null) {
			inUseMap.put(str, keyRef);
		}
		return value;
	}

	private int cacheString(char[] str, ArrayList<ArrangedGlyph> glyphs, int start, int limit, float advance) {
		int adv = 0;
		while(start < limit) {
			int offset = font[fontStyle].canDisplayUpTo(str, start, limit);
			if(offset == -1) {
				adv += cacheString(str, glyphs, start, limit, advance + adv, false);
				break;
			}
			if(offset == start) {
				adv += cacheString(str, glyphs, start, start + 1, advance + adv, true);
				start++;
			} else {
				adv += cacheString(str, glyphs, start, offset, advance + adv, false);
				start = offset;
			}
		}
		return adv;
	}
	
	private int cacheString(char[] str, ArrayList<ArrangedGlyph> glyphs, int start, int limit, float advance, boolean useOtherFont) {
		if (start == limit)
			return 0;

		GlyphTextureCache cache = glyphTextures;
		Font font = this.font[fontStyle];
		if(useOtherFont) {
			TrueTypeFont ttfont = FontFactory.makeSubstitutionFont(this.font[0].getSize());
			cache = ttfont.glyphTextures;
			font = ttfont.font[fontStyle];
		}
		
		GlyphTexture[] textures = cache.cacheGlyphs(str, start, limit, fontStyle);
		GlyphVector vec = cache.layoutGlyphVector(font, str, start, limit);
		Rectangle bounds = vec.getPixelBounds(null, 0, 0);
		float[] locations = vec.getGlyphPositions(0, limit - start + 1, null);
		int offsetY = cache.getAscent(fontStyle) - cache.getDescent(fontStyle);

		for (int i = 0; i < limit - start; i++) {
			Rectangle2D pos = vec.getGlyphVisualBounds(i).getBounds2D();
			ArrangedGlyph glyph = new ArrangedGlyph();
			glyph.index = start + i;
			glyph.x = (int) Math.round(pos.getX() + advance);
			glyph.y = (int) Math.round(pos.getY() + offsetY);
			glyph.colorIndex = colorIdx;
			glyph.affectedStyle = fontStyle | specialStyle;
			glyph.texture = textures[i];
			glyph.advance = (int) (locations[(i + 1) * 2] - locations[i * 2]);
			glyphs.add(glyph);
		}

		return (int) locations[locations.length - 2];
	}

	private int renderString(String str, double x, double y, int color, boolean shadow) {
		if ((color & -67108864) == 0)
			color |= -16777216;

		float alpha = ((color >> 24) & 0xFF) / 255.0f;
		applyColor(color, alpha);
		enableBlend();
		GlStateManager.enableAlpha();

		Tessellator tes = Tessellator.getInstance();
		BufferBuilder buf = tes.getBuffer();
		FormattedString cached = cacheString(str);
		double lineY = 0;
		int lastColor = color;

		for (int i = 0; i < cached.glyphs.length; i++) {
			int offsetX = 0;
			ArrangedGlyph glyph = cached.glyphs[i];
			char charAt = str.charAt(glyph.index);
			GlyphTexture tex = glyph.texture;

			if (charAt >= '0' && charAt <= '9') {
				cacheDigits();
				ArrangedGlyph digitGlyph = digitCache[glyph.affectedStyle & (Font.BOLD | Font.ITALIC)][charAt - '0'];
				tex = digitGlyph.texture;
				offsetX = (glyph.advance - digitGlyph.advance) >> 1;
			}

			final double minX = x + (glyph.x - GlyphTextureCache.GLYPH_PADDING + offsetX) / scaleModifier;
			final double minY = y + (glyph.y - GlyphTextureCache.GLYPH_PADDING) / scaleModifier;
			final double maxX = minX + tex.width / scaleModifier;
			final double maxY = minY + tex.height / scaleModifier;

			if (glyph.colorIndex != -1 && lastColor != glyph.colorIndex)
				applyColor(colorCodes[glyph.colorIndex + (shadow ? 16 : 0)], alpha);

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
					lineY = y + (glyphTextures.getAscent(0) + 1) / scaleModifier;
					buf.begin(7, DefaultVertexFormats.POSITION);
					buf.pos(x, lineY - 1 / scaleModifier, 0).endVertex();
					buf.pos(x, lineY, 0).endVertex();
					buf.pos(x + glyph.advance / scaleModifier, lineY, 0).endVertex();
					buf.pos(x + glyph.advance / scaleModifier, lineY - 1 / scaleModifier, 0).endVertex();
					tes.draw();
				}
				if ((glyph.affectedStyle & STRIKE_THROUGH) > 0) {
					lineY = y + (glyphTextures.getAscent(0) * 3 / 4) / scaleModifier;
					buf.begin(7, DefaultVertexFormats.POSITION);
					buf.pos(x, lineY - 1 / scaleModifier, 0).endVertex();
					buf.pos(x, lineY, 0).endVertex();
					buf.pos(x + glyph.advance / scaleModifier, lineY, 0).endVertex();
					buf.pos(x + glyph.advance / scaleModifier, lineY - 1 / scaleModifier, 0).endVertex();
					tes.draw();
				}
			}
		}

		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
		return (int) (cached.advance / scaleModifier);
	}

	public int drawString(String str, double x, double y, int color) {
		return drawString(str, x, y, color, false);
	}

	public int drawString(String str, double x, double y, int color, boolean shadow) {
		if (shadow) {
			renderString(str, x + 1, y + 1, (color & 16579836) >> 2 | color & -16777216, true);
			return renderString(str, x, y, color, false) + 1;
		} else {
			return renderString(str, x, y, color, false);
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
		return (int) (glyphTextures.getLineHeight(style) / scaleModifier);
	}

	public int getMaxHeight() {
		return (int) (glyphTextures.getMaxHeight() / scaleModifier);
	}

	public int getStringWidth(String str) {
		return (int) (cacheString(str).advance / scaleModifier);
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
				total = 0;
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
		for (int i = 0; i < 32; ++i) {
			int j = (i >> 3 & 1) * 85;
			int k = (i >> 2 & 1) * 170 + j;
			int l = (i >> 1 & 1) * 170 + j;
			int i1 = (i >> 0 & 1) * 170 + j;
			if (i == 6)
				k += 85;
			if (Minecraft.getMinecraft().gameSettings.anaglyph) {
				int j1 = (k * 30 + l * 59 + i1 * 11) / 100;
				int k1 = (k * 30 + l * 70) / 100;
				int l1 = (k * 30 + i1 * 70) / 100;
				k = j1;
				l = k1;
				i1 = l1;
			}
			if (i >= 16) {
				k /= 4;
				l /= 4;
				i1 /= 4;
			}
			colorCodes[i] = 255 << 24 | (k & 255) << 16 | (l & 255) << 8 | i1 & 255;
		}
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

	public TrueTypeFont setScaleModifier(double scale) {
		this.scaleModifier = scale;
		return this;
	}
}