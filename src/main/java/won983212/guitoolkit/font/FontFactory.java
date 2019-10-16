package won983212.guitoolkit.font;

import java.awt.Font;
import java.util.HashMap;
import java.util.HashSet;

//TODO 안티엘리어싱 기능 정의부가 미흡하다.
public class FontFactory {
	private static HashMap<Key, TrueTypeFont> fonts = new HashMap<Key, TrueTypeFont>();
	private static HashMap<Key, AdaptiveTTF> scaledFonts = new HashMap<Key, AdaptiveTTF>();
	private static HashSet<String> noAntialiasFonts = new HashSet<>();
	private static final String SUBSTITUTION_FONT = Font.SANS_SERIF;
	
	static {
		noAntialiasFonts.add(Font.SANS_SERIF);
	}
	
	public static TrueTypeFont makeSubstitutionFont(int size) {
		return makeFont(SUBSTITUTION_FONT, size);
	}
	
	public static TrueTypeFont makeFont(String family, int size) {
		Key key = new Key(family, size, 1);
		TrueTypeFont f = fonts.get(key);
		if(f != null) {
			return f;
		}
		f = new TrueTypeFont(family, size, true);
		fonts.put(key, f);
		return f;
	}
	
	public static AdaptiveTTF makeMinecraftFont(String family, int size) {
		return makeMinecraftFont(family, size, 2);
	}
	
	public static AdaptiveTTF makeMinecraftFont(String family, int size, int scale) {
		Key key = new Key(family, size, scale);
		AdaptiveTTF f = scaledFonts.get(key);
		if(f != null) {
			return f;
		}
		f = new AdaptiveTTF(family, size);
		f.setFontScale(scale);
		scaledFonts.put(key, f);
		return f;
	}
	
	private static class Key {
		public String font;
		public int size;
		private int scale;
		
		public Key(String family, int size, int scale) {
			this.font = family;
			this.size = size;
			this.scale = scale;
		}

		@Override
		public int hashCode() {
			int hash = font.hashCode();
			hash = 31 * hash + Integer.hashCode(size);
			hash = 31 * hash + Integer.hashCode(scale);
			return hash;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Key) {
				Key k = (Key) obj;
				return k.font == font && k.size == size && k.scale == scale;
			}
			return false;
		}
	}
}
