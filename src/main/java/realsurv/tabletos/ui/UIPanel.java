package realsurv.tabletos.ui;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;

public class UIPanel extends UIObject {
	protected ArrayList<UIObject> uiList = new ArrayList<UIObject>();
	private UIObject focusd = null;
	private UIObject lastPressedObject = null;
	
	public void add(UIObject obj) {
		uiList.add(obj);
		obj.setParentPanel(this);
	}
	
	@Override
	public void setBoundsByArrange(Rectangle available) {
		super.setBoundsByArrange(available);
		layout();
	}
	
	@Override
	public Dimension measureSize() {
		Dimension dim = new Dimension();
		for(UIObject obj : uiList) {
			Dimension clientDim = obj.getDesiredSize();
			dim.width = Math.max(dim.width, clientDim.width);
			dim.height = Math.max(dim.height, clientDim.height);
		}
		return dim;
	}
	
	public void layout() {
		Rectangle available = getActualBounds();
		for(UIObject obj : uiList) {
			obj.setBoundsByArrange(available);
		}
	}
	
	@Override
	public void render() {
		for(UIObject obj : uiList)
			obj.render();
	}
	
	@Override
	public void invalidateSize() {
		for(UIObject obj : uiList)
			obj.invalidateSize();
		super.invalidateSize();
	}
	
	public void keyTyped(char typedChar, int keyCode) {
		for(UIObject obj : uiList) {
			if(obj.isEnabledKeyEvent() && focusd == obj) 
				obj.onKeyTyped(keyCode, typedChar);
		}
	}

	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		for(int i=uiList.size()-1;i>=0;i--) {
			UIObject obj = uiList.get(i);
			if(obj.contains(mouseX, mouseY)) {
				setFocus(obj);
				if(obj.isEnabledMouseEvent()) {
					obj.onPress(mouseX, mouseY, mouseButton);
					lastPressedObject = obj;
					break;
				}
			}
		}
	}

	public void mouseReleased(int mouseX, int mouseY, int state) {
		if(lastPressedObject != null) {
			lastPressedObject.onRelease(mouseX, mouseY, state);
			lastPressedObject = null;
		}
	}

	public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		for(UIObject obj : uiList) {
			if(obj.isEnabledMouseEvent()) {
				obj.onDrag(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
			}
		}
	}

	public void setFocus(UIObject obj) {
		if(focusd != null)
			focusd.onLostFocus();
		focusd = obj;
		if(focusd != null)
			focusd.onGotFocus();
	}
}
