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
		return makeFont(family, size, true);
	}
	
	public static TrueTypeFont makeFont(String family, int size, boolean antialias) {
		Key key = new Key(family, size, 1, antialias);
		TrueTypeFont f = fonts.get(key);
		if(f != null) {
			return f;
		}
		f = new TrueTypeFont(family, size, antialias);
		fonts.put(key, f);
		return f;
	}

	// it is used for minecraft font rendering.
	public static AdaptiveTTF makeMinecraftFont(String family, int size, int scale) {
		return makeMinecraftFont(family, size, scale, true);
	}
	
	public static AdaptiveTTF makeMinecraftFont(String family, int size, int scale, boolean antialias) {
		Key key = new Key(family, size, scale, antialias);
		AdaptiveTTF f = scaledFonts.get(key);
		if(f != null) {
			return f;
		}
		f = new AdaptiveTTF(family, size, antialias);
		f.setFontScale(scale);
		scaledFonts.put(key, f);
		return f;
	}
	
	private static class Key {
		public String font;
		public int size;
		public boolean antialias;
		private int scale;
		
		public Key(String family, int size, int scale, boolean antialias) {
			this.font = family;
			this.size = size;
			this.scale = scale;
			this.antialias = antialias;
		}

		@Override
		public int hashCode() {
			int hash = font.hashCode();
			hash = 31 * hash + Integer.hashCode(size);
			hash = 31 * hash + Integer.hashCode(scale);
			hash = 31 * hash + Boolean.hashCode(antialias);
			return hash;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Key) {
				Key k = (Key) obj;
				return k.font == font && k.size == size && k.scale == scale && k.antialias == antialias;
			}
			return false;
		}
	}
}
