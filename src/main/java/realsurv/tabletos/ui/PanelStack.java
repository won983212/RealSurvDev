package realsurv.tabletos.ui;

import java.awt.Dimension;
import java.awt.Rectangle;

public class PanelStack extends UIPanel {
	private Orientation orientation;
	
	public PanelStack setOrientation(Orientation o) {
		orientation = o;
		return this;
	}
	
	@Override
	public Dimension measureSize() {
		Dimension size = new Dimension();
		for(UIObject obj : uiList) {
			Dimension dim = obj.getDesiredSize();
			if(orientation == Orientation.HORIZONTAL) {
				size.width += dim.width;
				size.height = Math.max(size.height, dim.height);
			} else {
				size.width = Math.max(size.width, dim.width);
				size.height += dim.height;
			}
		}
		return size;
	}

	@Override
	public void layout() {
		Rectangle bounds = getActualBounds();
		int x=bounds.x;
		int y=bounds.y;
		for(UIObject obj : uiList) {
			Dimension clientDim = obj.getDesiredSize();
			Rectangle rect = new Rectangle(x, y, clientDim.width, clientDim.height);
			if(orientation == Orientation.HORIZONTAL) {
				x += clientDim.width;
				rect.height = bounds.height;
			} else {
				y += clientDim.height;
				rect.width = bounds.width;
			}
			obj.setBoundsByArrange(rect);
		}
	}
	
	public static enum Orientation {
		HORIZONTAL,
		VERTICAL
	}
}
