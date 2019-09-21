package realsurv.tabletos.scenes;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiScreen;
import realsurv.tabletos.DirWeights;
import realsurv.tabletos.HorizontalArrange;
import realsurv.tabletos.TabletOS;
import realsurv.tabletos.VerticalArrange;
import realsurv.tabletos.ui.GridPanel;
import realsurv.tabletos.ui.UILabel;
import realsurv.tabletos.ui.UIObject;
import realsurv.tabletos.ui.GridPanel.LengthDefinition;
import realsurv.tabletos.ui.GridPanel.LengthType;
import realsurv.tabletos.ui.events.IButtonEvent;
import realsurv.tabletos.ui.UIButton;
import realsurv.tabletos.ui.UICheckbox;
import realsurv.tabletos.ui.UICombobox;
import realsurv.tabletos.ui.UIPanel;
import realsurv.tabletos.ui.UIRectangle;
import realsurv.tabletos.ui.UITextfield;

public class SceneMainScreen extends UIPanel {
	private static final Dimension screenSize = new Dimension(TabletOS.WIDTH, TabletOS.HEIGHT);
	private UIPanel popupPanel;
	
	public SceneMainScreen() {
		refresh();
	}
	
	@Override
	public void addPopup(UIObject obj) {
		popupPanel.add(obj);
	}

	//TODO impolement layout
	@Override
	public void setPopupLocation(UIObject obj, int x, int y) {
		popupPanel.layout();
	}
	
	private void refresh() {
		popupPanel = new UIPanel();
		uiList.clear();
		UIPanel panel = new UIPanel();
		panel.setMinimumSize(120, 50);
		panel.setHorizontalArrange(HorizontalArrange.CENTER);
		panel.setVerticalArrange(VerticalArrange.CENTER);
		panel.add(new UICombobox().add("MyItem").add("Item2").add("Item3").add("Item4"));
		add(panel);
		
		add(popupPanel);
		invalidateSize();
		setActualBounds(0, 0, screenSize.width, screenSize.height);
		layout();
	}
	
	@Override
	public void onKeyTyped(int keyCode, char typedChar) {
		//TODO Debug Key
		if(keyCode == Keyboard.KEY_T) {
			if(GuiScreen.isCtrlKeyDown()) {
				refresh();
			}
		}
		super.onKeyTyped(keyCode, typedChar);
	}
	
	@Override
	public Dimension measureMinSize() {
		return screenSize;
	}
}
