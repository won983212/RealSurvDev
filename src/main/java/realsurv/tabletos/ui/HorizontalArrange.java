package realsurv.tabletos.ui;

import java.awt.Dimension;
import java.awt.Point;
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
		}
		return available.x;
	}
	
	public int getWidthArranged(Rectangle available, Dimension objectSize) {
		switch(this) {
		case STRECTCH:
			return available.width;
		}
		return objectSize.width;
	}
}
