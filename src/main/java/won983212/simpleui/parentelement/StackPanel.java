package won983212.simpleui.parentelement;

import java.awt.Dimension;
import java.awt.Rectangle;

public class StackPanel extends UIPanel {
	private Orientation orientation;
	
	public StackPanel setOrientation(Orientation o) {
		orientation = o;
		return this;
	}
	
	@Override
	public Dimension measureMinSize() {
		Dimension size = new Dimension();
		for(UIObject obj : uiList) {
			Dimension dim = obj.getLayoutMinSize();
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
		Rectangle bounds = getRelativeBounds();
		int x=0, y=0;
		for(UIObject obj : uiList) {
			Dimension clientDim = obj.getLayoutMinSize();
			Rectangle rect = new Rectangle(x, y, clientDim.width, clientDim.height);
			if(orientation == Orientation.HORIZONTAL) {
				x += clientDim.width;
				rect.height = bounds.height;
			} else {
				y += clientDim.height;
				rect.width = bounds.width;
			}
			obj.arrange(rect);
		}
	}
	
	public static enum Orientation {
		HORIZONTAL,
		VERTICAL
	}
}
