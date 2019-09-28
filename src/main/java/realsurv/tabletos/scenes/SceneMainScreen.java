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
	
	private void refresh() {
		popupPanel = new UIPanel();
		uiList.clear();
		UIPanel panel = new UIPanel();
		panel.setMinimumSize(120, 62);
		panel.setHorizontalArrange(HorizontalArrange.CENTER);
		panel.setVerticalArrange(VerticalArrange.CENTER);
		
		GridPanel loginForm = new GridPanel();
		loginForm.setLayoutPosition(1, 1);
		loginForm.addColumn(new LengthDefinition(LengthType.ALLOCATED, 1));
		loginForm.addColumn(new LengthDefinition(LengthType.AUTO, 1));
		loginForm.addRow(new LengthDefinition(LengthType.ALLOCATED, 1));
		loginForm.addRow(new LengthDefinition(LengthType.ALLOCATED, 1));
		loginForm.addRow(new LengthDefinition(LengthType.ALLOCATED, 1));
		loginForm.addRow(new LengthDefinition(LengthType.ALLOCATED, 1));
		loginForm.add(new UIRectangle().setBackgroundColor(0xffcccccc).setLayoutSpan(2, 4));
		loginForm.add(new UITextfield().setHint("ID").setMargin(new DirWeights(2, 0, 2, 0)).setVerticalArrange(VerticalArrange.CENTER));
		loginForm.add(new UITextfield().setHint("Password").setHiddenText(true).setMargin(new DirWeights(2, 0, 2, 0)).setLayoutPosition(0, 1).setVerticalArrange(VerticalArrange.CENTER));
		loginForm.add(new UICheckbox().setLabel("Remember").setMargin(new DirWeights(0, 0, 2, 0)).setVerticalArrange(VerticalArrange.CENTER).setLayoutPosition(0, 2));
		loginForm.add(new UIButton("Login").setMargin(new DirWeights(2)).setLayoutPosition(1, 0).setLayoutSpan(1, 3));
		loginForm.add(new UICombobox().add("Item1").add("Item2").add("Item3").setLayoutPosition(0, 3).setLayoutSpan(2, 1));
		panel.add(loginForm);
		add(panel);
		
		add(popupPanel);
		invalidateSize();
		setRelativeBounds(0, 0, screenSize.width, screenSize.height);
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
