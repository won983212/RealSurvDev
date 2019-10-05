package realsurv.tabletos;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import realsurv.font.TrueTypeFont;
import realsurv.tabletos.ui.GridPanel;
import realsurv.tabletos.ui.UILabel;
import realsurv.tabletos.ui.UIObject;
import realsurv.tabletos.ui.GridPanel.LengthDefinition;
import realsurv.tabletos.ui.GridPanel.LengthType;
import realsurv.tabletos.ui.events.IButtonEvent;
import realsurv.tabletos.ui.UIButton;
import realsurv.tabletos.ui.UICheckbox;
import realsurv.tabletos.ui.UICombobox;
import realsurv.tabletos.ui.UIImage;
import realsurv.tabletos.ui.UIPanel;
import realsurv.tabletos.ui.UIRectangle;
import realsurv.tabletos.ui.UITextfield;

public class MainScreen extends UIPanel {
	private static final Dimension screenSize = new Dimension(TabletOS.WIDTH, TabletOS.HEIGHT);
	private UIPanel popupPanel;
	
	public MainScreen() {
		refresh();
	}
	
	@Override
	public void addPopup(UIObject obj) {
		popupPanel.add(obj);
	}
	
	private void refresh() {
		popupPanel = new UIPanel();
		uiList.clear();
		add(new UIImage("realsurv:ui/wallpaper.png"));
		
		GridPanel contents = new GridPanel();
		contents.addRow(new LengthDefinition(LengthType.FIXED, 30));
		contents.addRow(new LengthDefinition(LengthType.ALLOCATED, 1));
		contents.addEmptyColumn();
		add(contents);
		
		UIPanel taskbar = new UIPanel();
		taskbar.add(new UIRectangle().setShadowVisible(false).setRadius(0).setBackgroundColor(0xaa000000));
		taskbar.add(new UILabel().setLabel("12:31").setMargin(new DirWeights(0, 0, 0, 5)).setHorizontalArrange(HorizontalArrange.RIGHT).setVerticalArrange(VerticalArrange.CENTER).setForegroundColor(0xffffffff));
		taskbar.add(new UILabel().setLabel("¡×lInternet App").setMargin(new DirWeights(0, 0, 10, 0)).setHorizontalArrange(HorizontalArrange.LEFT).setVerticalArrange(VerticalArrange.CENTER).setForegroundColor(0xffffffff));
		contents.add(taskbar);		
		
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
