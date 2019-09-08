package realsurv.tabletos.ui;

import java.awt.Dimension;
import java.awt.Rectangle;

public enum VerticalArrange {
	TOP,
	BOTTOM,
	CENTER,
	STRECTCH;

	public int getVerticalArrangedLocation(Rectangle available, Dimension objectSize) {
		switch(this) {
		case BOTTOM:
			return available.y + available.height - objectSize.height;
		case CENTER:
			return available.y + (available.height - objectSize.height) / 2;
		}
		return available.y;
	}
	
	public int getHeightArranged(Rectangle available, Dimension objectSize) {
		switch(this) {
		case STRECTCH:
			return available.height;
		}
		return objectSize.height;
	}
}