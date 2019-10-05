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
	
	/* private TrueTypeFont font = new TrueTypeFont("¸¼Àº °íµñ", 12, true);
	@Override
	public void render(int mouseX, int mouseY) {
		super.render(mouseX, mouseY);
		String str = "Hello, ¡×nworld!¡×r ¡×l¾È¡×r³ç¡×oÇÏ¡×r¼¼¿ä!";
		Gui.drawRect(10, 10, 10 + font.getStringWidth(str), 10 + font.getMaxHeight(), 0xff000000);
		font.drawString(str, 10, 10, 0xffffffff);
	} */
	
	private void refresh() {
		popupPanel = new UIPanel();
		uiList.clear();
		
		add(new UIImage("realsurv:ui/wallpaper.png"));
		
		GridPanel contents = new GridPanel();
		contents.addRow(new LengthDefinition(LengthType.FIXED, 20));
		contents.addRow(new LengthDefinition(LengthType.ALLOCATED, 1));
		contents.addEmptyColumn();
		
		UIPanel taskbar = new UIPanel();
		//taskbar.add(new UIRectangle().setShadowVisible(false).setRadius(0).setBackgroundColor(0xaa000000).setLayoutSpan(2, 1));
		contents.add(taskbar);

		GridPanel loginForm = new GridPanel();
		loginForm.setMinimumSize(130, 50);
		loginForm.setHorizontalArrange(HorizontalArrange.CENTER);
		loginForm.setVerticalArrange(VerticalArrange.CENTER);
		loginForm.setLayoutPosition(0, 1);
		loginForm.addColumn(new LengthDefinition(LengthType.FIXED, 120));
		loginForm.addColumn(new LengthDefinition(LengthType.AUTO, 1));
		loginForm.addRow(new LengthDefinition(LengthType.ALLOCATED, 1));
		loginForm.addRow(new LengthDefinition(LengthType.ALLOCATED, 1));
		loginForm.addRow(new LengthDefinition(LengthType.ALLOCATED, 1));
		loginForm.add(new UIRectangle().setBackgroundColor(0xffcccccc).setLayoutSpan(2, 3));
		loginForm.add(new UITextfield().setHint("ID").setMargin(new DirWeights(4, 0, 4, 0)).setVerticalArrange(VerticalArrange.CENTER));
		loginForm.add(new UITextfield().setHint("Password").setMargin(new DirWeights(4, 0, 4, 0)).setLayoutPosition(0, 1).setVerticalArrange(VerticalArrange.CENTER));
		loginForm.add(new UICheckbox().setLabel("Remember").setMargin(new DirWeights(0, 0, 4, 0)).setVerticalArrange(VerticalArrange.CENTER).setLayoutPosition(0, 2));
		loginForm.add(new UIButton("Login").setMargin(new DirWeights(4)).setLayoutPosition(1, 0).setLayoutSpan(1, 3));
		contents.add(loginForm);
		add(contents);
		
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
