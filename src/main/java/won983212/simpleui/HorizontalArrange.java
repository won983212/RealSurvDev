package won983212.simpleui;

import java.awt.Dimension;
import java.awt.Rectangle;

public enum HorizontalArrange {
	LEFT,
	RIGHT,
	CENTER,
	STRECTCH;

	public int getHorizontalArrangedLocation(Rectangle available, Dimension objectSize) {
		switch(this) {
		case RIGHT:
			return available.x + available.width - objectSize.width;
		case CENTER:
			return available.x + (available.width - objectSize.width) / 2;
		default:
			return available.x;
		}
	}
	
	public int getWidthArranged(Rectangle available, Dimension objectSize) {
		switch(this) {
		case STRECTCH:
			return available.width;
		default:
			return objectSize.width;
		}
	}
}
