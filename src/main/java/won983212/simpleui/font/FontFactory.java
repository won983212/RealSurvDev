package won983212.simpleui.font;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class FontFactory {
	private static final HashMap<Key, TrueTypeFont> fonts = new HashMap<Key, TrueTypeFont>();
	private static final HashMap<Key, AdaptiveTTF> scaledFonts = new HashMap<Key, AdaptiveTTF>();
	
	private static final List<Font> allFonts = Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts());
	private static final Font sans_serif_font = new Font(Font.SANS_SERIF, 0, 1);
	
	public static Font findSubstitutionJavaFont(char c, int size, int style) {
		if(sans_serif_font.canDisplay(c))
			return sans_serif_font.deriveFont(style, size);
		
		Iterator<Font> iter = allFonts.iterator();
		while(iter.hasNext()) {
			Font f = iter.next();
			if(f.canDisplay(c))
				return f.deriveFont(style, size);
		}
		
		return sans_serif_font.deriveFont(style, size);
	}
	
	// it is used for Simple UI Font rendering. 
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
	
	// it is used for minecraft font rendering.
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
