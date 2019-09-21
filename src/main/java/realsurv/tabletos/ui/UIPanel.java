package realsurv.tabletos.ui;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;

import realsurv.tabletos.DirWeights;

public class UIPanel extends UIObject {
	protected ArrayList<UIObject> uiList = new ArrayList<UIObject>();
	
	private UIObject focusd = null;
	private UIObject lastPressedObject = null;
	
	public void add(UIObject obj) {
		uiList.add(obj);
		obj.setParentPanel(this);
	}
	
	public void addPopup(UIObject obj) {
		if(panel != null)
			panel.addPopup(obj);
	}
	
	public void setPopupLocation(UIObject obj, int x, int y) {
		if(panel != null)
			panel.setPopupLocation(obj, x, y);
	}
	
	public void requestLayout() {
		if(panel == null) {
			invalidateSize();
			layout();
		} else
			panel.requestLayout();
	}
	
	@Override
	public void arrange(Rectangle available) {
		super.arrange(available);
		layout();
	}
	
	@Override
	public Dimension measureMinSize() {
		Dimension dim = new Dimension();
		for(UIObject obj : uiList) {
			Dimension clientDim = obj.getLayoutMinSize();
			dim.width = Math.max(dim.width, clientDim.width);
			dim.height = Math.max(dim.height, clientDim.height);
		}
		return dim;
	}
	
	public void layout() {
		Rectangle available = getActualBounds();
		for(UIObject obj : uiList) {
			obj.arrange(available);
		}
	}
	
	@Override
	public void render(int mouseX, int mouseY) {
		for(UIObject obj : uiList)
			if(obj.isVisible())
				obj.render(mouseX, mouseY);
	}
	
	@Override
	public void invalidateSize() {
		for(UIObject obj : uiList)
			obj.invalidateSize();
		super.invalidateSize();
	}
	
	@Override
	public boolean contains(int x, int y) {
		for(UIObject obj : uiList) {
			if(obj.isInteractive() && obj.contains(x, y)) 
				return true;
		}
		return false;
	}
	
	@Override
	public void onKeyTyped(int keyCode, char typedChar) {
		for(UIObject obj : uiList) {
			if(obj instanceof UIPanel && focusd == obj) 
				obj.onKeyTyped(keyCode, typedChar);
		}
	}

	@Override
	public void onPress(int mouseX, int mouseY, int mouseButton) {
		boolean any = false;
		for(int i=uiList.size()-1;i>=0;i--) {
			UIObject obj = uiList.get(i);
			if(obj.contains(mouseX, mouseY)) {
				if(obj.isInteractive()) {
					setFocus(obj);
					any = true;
					obj.onPress(mouseX, mouseY, mouseButton);
					lastPressedObject = obj;
					break;
				}
			}
		}

		if(!any && focusd != null)
			setFocus(null);
	}

	@Override
	public void onRelease(int mouseX, int mouseY, int state) {
		if(lastPressedObject != null) {
			lastPressedObject.onRelease(mouseX, mouseY, state);
			lastPressedObject = null;
		}
	}

	@Override
	public void onDrag(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		for(UIObject obj : uiList) {
			if(obj.isInteractive()) {
				obj.onDrag(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
			}
		}
	}

	public void setFocus(UIObject obj) {
		if(focusd == obj)
			return;
		if(focusd != null)
			focusd.onLostFocus();
		focusd = obj;
		if(focusd != null)
			focusd.onGotFocus();
	}
}
