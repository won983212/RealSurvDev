package realsurv.tabletos.ui;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.client.renderer.GlStateManager;
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
		if(parentPanel != null)
			parentPanel.addPopup(obj);
	}
	
	public void requestLayout() {
		if(parentPanel == null) {
			invalidateSize();
			layout();
		} else
			parentPanel.requestLayout();
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
		Rectangle available = getInnerBounds();
		for(UIObject obj : uiList) {
			obj.arrange(available);
		}
	}
	
	@Override
	public void render(int mouseX, int mouseY) {
		for(UIObject obj : uiList) {
			if(obj.isVisible()) {
				Rectangle bounds = obj.getRelativeBounds();
				GlStateManager.translate(bounds.x, bounds.y, 0);
				obj.render(mouseX - bounds.x, mouseY - bounds.y);
				GlStateManager.translate(-bounds.x, -bounds.y, 0);
			}
		}
	}
	
	@Override
	public void invalidateSize() {
		for(UIObject obj : uiList)
			obj.invalidateSize();
		super.invalidateSize();
	}
	
	@Override
	public boolean containsRelative(int x, int y) {
		for(UIObject obj : uiList) {
			Rectangle bounds = obj.getRelativeBounds();
			if(obj.isInteractive() && obj.containsRelative(x - bounds.x, y - bounds.y)) 
				return true;
		}
		return false;
	}
	
	@Override
	public void onKeyTyped(int keyCode, char typedChar) {
		for(UIObject obj : uiList) {
			if(obj instanceof UIPanel || focusd == obj) 
				obj.onKeyTyped(keyCode, typedChar);
		}
	}

	@Override
	public void onPress(int mouseX, int mouseY, int mouseButton) {
		boolean any = false;
		for(int i=uiList.size()-1;i>=0;i--) {
			UIObject obj = uiList.get(i);
			Rectangle rect = obj.getRelativeBounds();
			if(obj.containsRelative(mouseX - rect.x, mouseY - rect.y)) {
				if(obj.isInteractive()) {
					setFocus(obj);
					any = true;
					obj.onPress(mouseX - rect.x, mouseY - rect.y, mouseButton);
					lastPressedObject = obj;
					break;
				}
			}
		}

		if(!any)
			setFocus(null);
	}

	@Override
	public void onRelease(int mouseX, int mouseY, int state) {
		if(lastPressedObject != null) {
			Rectangle rect = lastPressedObject.getRelativeBounds();
			lastPressedObject.onRelease(mouseX - rect.x, mouseY - rect.y, state);
			lastPressedObject = null;
		}
	}

	@Override
	public void onDrag(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		for(UIObject obj : uiList) {
			if(obj.isInteractive()) {
				Rectangle rect = obj.getRelativeBounds();
				obj.onDrag(mouseX - rect.x, mouseY - rect.y, clickedMouseButton, timeSinceLastClick);
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
	
	@Override
	public void onLostFocus() {
		super.onLostFocus();
		setFocus(null);
	}
}
