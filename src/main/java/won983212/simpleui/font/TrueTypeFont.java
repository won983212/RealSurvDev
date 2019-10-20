package won983212.simpleui.font;

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.font.GlyphVector;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import org.lwjgl.opengl.GL11;

import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.ArabicShapingException;
import com.ibm.icu.text.Bidi;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import won983212.simpleui.font.GlyphTextureCache.GlyphTexture;

public class TrueTypeFont {
	private static class ArrangedGlyph {
		public int x;
		public int y;
		public int advance;
		public int colorIndex;
		public int fontStyle;
		public int specialStyle;
		public GlyphTexture texture;
		public int index;
	}

	private static class FormattedString {
		public WeakReference<String> keyReference;
		public int advance;
		public ArrangedGlyph[] glyphs;
	}

	private static final int UNDERLINE = 1;
	private static final int STRIKE_THROUGH = 2;

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

	public void clearCache() {
		stringCache.clear();
		inUseMap.clear();
		glyphTextures.clear();
		digitCache = null;
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
		FormattedString value = stringCache.get(key);
		if (value == null) {
			ArrayList<ArrangedGlyph> glyphList = new ArrayList<>();
			String newKeyString = new String(key);

			int bidiIndexMap[] = null;
			try {
				int prevLen = str.length();
				Bidi bidi = new Bidi((new ArabicShaping(8)).shape(str), Bidi.DIRECTION_DEFAULT_RIGHT_TO_LEFT);
				bidi.setReorderingMode(Bidi.OPTION_DEFAULT);
				str = bidi.writeReordered(Bidi.DO_MIRRORING);
				bidiIndexMap = bidi.getVisualMap();
				
				prevLen -= bidi.getProcessedLength();
				for(int i=0;i<bidiIndexMap.length;i++)
					bidiIndexMap[i] += prevLen;
			} catch (ArabicShapingException e) {
				e.printStackTrace();
			}
			if(bidiIndexMap == null) {
				bidiIndexMap = new int[str.length()];
				for(int i=0;i<bidiIndexMap.length;i++)
					bidiIndexMap[i] = i;
			}
			
			value = new FormattedString();
			value.keyReference = new WeakReference<String>(newKeyString);
			value.advance = layoutStyle(str.toCharArray(), glyphList, 0, str.length(), 0, bidiIndexMap);
			value.glyphs = glyphList.toArray(new ArrangedGlyph[glyphList.size()]);
			stringCache.put(newKeyString, value);
		}
		String keyRef = value.keyReference.get();
		if (keyRef != null) {
			inUseMap.put(str, keyRef);
		}
		return value;
	}

	private int layoutStyle(char[] str, ArrayList<ArrangedGlyph> glyphs, int start, int limit, int advance, int[] bidiIdxMap) {
		fontStyle = specialStyle = 0;
		colorIdx = -1;
		for (int i = start; i < limit; i++) {
			if (str[i] == '§' && i < str.length - 1) {
				if ("0123456789abcdefmnrlo".indexOf(str[i + 1]) != -1) {
					advance = layoutMissingGlyphs(str, glyphs, start, i, advance, bidiIdxMap);
					applyStyle(str[i + 1]);
					start = ++i + 1;
				}
			}
		}
		return layoutMissingGlyphs(str, glyphs, start, limit, advance, bidiIdxMap);
	}

	private int layoutMissingGlyphs(char[] str, ArrayList<ArrangedGlyph> glyphs, int start, int limit, int advance, int[] bidiIdxMap) {
		if(start == limit)
			return advance;
		
		int end = start;
		int lastSize = glyphs.size();
		while (start < limit) {
			int offset = font[fontStyle].canDisplayUpTo(str, start, limit);
			if (offset == -1) {
				advance = layoutGlyphs(str, glyphs, start, limit, advance, bidiIdxMap, font[fontStyle]);
				break;
			}
			if (offset == start) {
				end = start + 1;
			} else {
				end = offset;
			}
			Font f = font[fontStyle];
			if(offset == start)
				f = FontFactory.findSubstitutionJavaFont(str[start], font[0].getSize(), fontStyle);
			advance = layoutGlyphs(str, glyphs, start, end, advance, bidiIdxMap, f);
			start = end;
		}
		
		if((specialStyle & (UNDERLINE | STRIKE_THROUGH)) > 0) {
			ArrangedGlyph startGlyph = glyphs.get(lastSize);
			ArrangedGlyph endGlyph = glyphs.get(glyphs.size() - 1);
			endGlyph.specialStyle = (startGlyph.x & 0x3FFFFFFF) << 2;
			endGlyph.specialStyle |= specialStyle & (UNDERLINE | STRIKE_THROUGH);
		}
		
		return advance;
	}

	private int layoutGlyphs(char[] str, ArrayList<ArrangedGlyph> glyphs, int start, int limit, int advance, int[] bidiIdxMap, Font font) {
		if (start == limit)
			return advance;

		GlyphTexture[] textures = glyphTextures.cacheGlyphs(font, str, start, limit, fontStyle);
		GlyphVector vec = glyphTextures.layoutGlyphVector(font, str, start, limit, Font.LAYOUT_LEFT_TO_RIGHT);
		Rectangle bounds = vec.getPixelBounds(null, 0, 0);
		float[] locations = vec.getGlyphPositions(0, limit - start + 1, null);
		int offsetY = glyphTextures.getAscent(fontStyle) - glyphTextures.getDescent(fontStyle);

		for (int i = 0; i < limit - start; i++) {
			Point2D actLoc = vec.getGlyphPosition(i);
			Rectangle2D pos = vec.getGlyphVisualBounds(i).getBounds2D();
			ArrangedGlyph glyph = new ArrangedGlyph();
			glyph.index = bidiIdxMap[start + i];
			glyph.x = (int) Math.round(pos.getX() - actLoc.getX() + advance);
			glyph.y = (int) Math.round(pos.getY() - actLoc.getY() + offsetY);
			glyph.colorIndex = colorIdx;
			glyph.fontStyle = fontStyle;
			glyph.texture = textures[i];
			glyph.advance = (int) vec.getGlyphMetrics(i).getAdvanceX();
			advance += glyph.advance;
			glyphs.add(glyph);
		}

		return (int) advance;
	}

	private int renderString(String str, double x, double y, int color, boolean shadow) {
		if ((color & -67108864) == 0)
			color |= -16777216;

		float alpha = ((color >> 24) & 0xFF) / 255.0f;
		applyColor(color, alpha);
		enableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
		
		Tessellator tes = Tessellator.getInstance();
		BufferBuilder buf = tes.getBuffer();
		FormattedString cached = cacheString(str);
		double lineY = 0;
		int lastColor = color;

		for (int i = 0; i < cached.glyphs.length; i++) {
			ArrangedGlyph glyph = cached.glyphs[i];
			char charAt = str.charAt(glyph.index);
			
			if (glyph.colorIndex != -1 && lastColor != glyph.colorIndex)
				applyColor(colorCodes[glyph.colorIndex + (shadow ? 16 : 0)], alpha);

			if (charAt != ' ') {
				double offsetX = 0;
				GlyphTexture tex = glyph.texture;
				if (charAt >= '0' && charAt <= '9') {
					cacheDigits();
					ArrangedGlyph digitGlyph = digitCache[glyph.fontStyle][charAt - '0'];
					tex = digitGlyph.texture;
					offsetX = (glyph.advance - digitGlyph.advance) / 2.0;
				}
				
				final double minX = x + (glyph.x - GlyphTextureCache.GLYPH_PADDING + offsetX) / scaleModifier;
				final double minY = y + (glyph.y - GlyphTextureCache.GLYPH_PADDING) / scaleModifier;
				final double maxX = minX + tex.width / scaleModifier;
				final double maxY = minY + tex.height / scaleModifier;

				GlStateManager.enableTexture2D();
				GlStateManager.bindTexture(tex.texture);
				buf.begin(7, DefaultVertexFormats.POSITION_TEX);
				buf.pos(minX, minY, 0).tex(tex.u1, tex.v1).endVertex();
				buf.pos(minX, maxY, 0).tex(tex.u1, tex.v2).endVertex();
				buf.pos(maxX, maxY, 0).tex(tex.u2, tex.v2).endVertex();
				buf.pos(maxX, minY, 0).tex(tex.u2, tex.v1).endVertex();
				tes.draw();
			}
			
			if (glyph.specialStyle > 0) {
				final double startX = x + (glyph.specialStyle >> 2);
				GlStateManager.disableTexture2D();
				if ((glyph.specialStyle & UNDERLINE) > 0) {
					lineY = y + (glyphTextures.getAscent(0) - glyphTextures.getDescent(0) + 1) / scaleModifier;
					buf.begin(7, DefaultVertexFormats.POSITION);
					buf.pos(startX, lineY - 1 / scaleModifier, 0).endVertex();
					buf.pos(startX, lineY, 0).endVertex();
					buf.pos(x + (glyph.x + glyph.advance) / scaleModifier, lineY, 0).endVertex();
					buf.pos(x + (glyph.x + glyph.advance) / scaleModifier, lineY - 1 / scaleModifier, 0).endVertex();
					tes.draw();
				}
				if ((glyph.specialStyle & STRIKE_THROUGH) > 0) {
					lineY = y + ((glyphTextures.getAscent(0) - glyphTextures.getDescent(0)) / 1.5) / scaleModifier;
					buf.begin(7, DefaultVertexFormats.POSITION);
					buf.pos(startX, lineY - 1 / scaleModifier, 0).endVertex();
					buf.pos(startX, lineY, 0).endVertex();
					buf.pos(x + (glyph.x + glyph.advance) / scaleModifier, lineY, 0).endVertex();
					buf.pos(x + (glyph.x + glyph.advance) / scaleModifier, lineY - 1 / scaleModifier, 0).endVertex();
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
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
				GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
				GlStateManager.DestFactor.ZERO);
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

	public double getScaleModifier() {
		return scaleModifier;
	}
}