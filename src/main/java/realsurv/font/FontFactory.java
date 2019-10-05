package realsurv.font;

import java.util.HashMap;

public class FontFactory {
	private static HashMap<Key, TrueTypeFont> fonts = new HashMap<Key, TrueTypeFont>();
	
	public static TrueTypeFont makeFont(String family, int size) {
		return getFont(new Key(family, size));
	}
	
	private static TrueTypeFont getFont(Key key) {
		TrueTypeFont f = fonts.get(key);
		if(f != null) {
			return f;
		}
		
		f = key.makeFont();
		fonts.put(key, f);
		return f;
	}
	
	private static class Key {
		public String font;
		public int size;
		
		public Key(String family, int size) {
			this.font = family;
			this.size = size;
		}

		@Override
		public int hashCode() {
			int hash = font.hashCode();
			hash = 31 * hash + size;
			return hash;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Key) {
				Key k = (Key) obj;
				return k.font == font && k.size == size;
			}
			return false;
		}
		
		public TrueTypeFont makeFont() {
			return new TrueTypeFont(font, size, true);
		}
	}
}
