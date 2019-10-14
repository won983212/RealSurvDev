package realsurv.font;

import java.util.HashMap;

public class FontFactory {
	private static HashMap<Key, TrueTypeFont> fonts = new HashMap<Key, TrueTypeFont>();
	private static HashMap<Key, AdaptiveTTF> scaledFonts = new HashMap<Key, AdaptiveTTF>();
	
	public static TrueTypeFont makeFont(String family, int size) {
		Key key = new Key(family, size, false);
		TrueTypeFont f = fonts.get(key);
		if(f != null) {
			return f;
		}
		f = new TrueTypeFont(family, size, true);
		fonts.put(key, f);
		return f;
	}
	
	public static AdaptiveTTF makeMinecraftFont(String family, int size) {
		return makeMinecraftFont(family, size, true);
	}
	
	public static AdaptiveTTF makeMinecraftFont(String family, int size, boolean isHalf) {
		Key key = new Key(family, size, isHalf);
		AdaptiveTTF f = scaledFonts.get(key);
		if(f != null) {
			return f;
		}
		f = new AdaptiveTTF(family, size);
		if(isHalf) 
			f.setFontHalf();
		scaledFonts.put(key, f);
		return f;
	}
	
	private static class Key {
		public String font;
		public int size;
		private boolean isNative;
		
		public Key(String family, int size, boolean isNative) {
			this.font = family;
			this.size = size;
			this.isNative = isNative;
		}

		@Override
		public int hashCode() {
			int hash = font.hashCode();
			hash = 31 * hash + Integer.hashCode(size);
			hash = 31 * hash + Boolean.hashCode(isNative);
			return hash;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Key) {
				Key k = (Key) obj;
				return k.font == font && k.size == size && k.isNative == isNative;
			}
			return false;
		}
	}
}
