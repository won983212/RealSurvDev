package realsurv.tabletos;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import won983212.simpleui.DirWeights;
import won983212.simpleui.GridPanel;
import won983212.simpleui.HorizontalArrange;
import won983212.simpleui.RootPane;
import won983212.simpleui.UIObject;
import won983212.simpleui.UIPanel;
import won983212.simpleui.VerticalArrange;
import won983212.simpleui.GridPanel.LengthDefinition;
import won983212.simpleui.GridPanel.LengthType;
import won983212.simpleui.element.UIButton;
import won983212.simpleui.element.UICheckbox;
import won983212.simpleui.element.UICombobox;
import won983212.simpleui.element.UIImage;
import won983212.simpleui.element.UILabel;
import won983212.simpleui.element.UIRectangle;
import won983212.simpleui.element.UITextfield;
import won983212.simpleui.events.IButtonEvent;
import won983212.simpleui.font.FontFactory;
import won983212.simpleui.font.StringCache;
import won983212.simpleui.font.TrueTypeFont;

public class MainScreen extends RootPane {
	public MainScreen() {
		super(TabletOS.WIDTH, TabletOS.HEIGHT);
		setScaledFactor(0);
	}

	private TrueTypeFont font = FontFactory.makeFont(Font.SANS_SERIF, 14);
	@Override
	public void render(int mouseX, int mouseY) {
		super.render(mouseX, mouseY);
		String str[] = {
			"§l@동해물§r과 §l§m§o백두산§r이 §a마르고 §c닳도록 §l하느§o님이§r 보우하사 우리 나라만세. 012312312311",
			"دّث بلغة يونيكود. تسجّل الآن لحضور المؤتمر الدولي العاشر ليونيكود (Unicode Conference)، الذ",
			"Item1",
			"Item2",
			"Item3",
			"<Player175436> Nice to meet you?"
		};
		for(int i=0;i<str.length;i++) {
			String ent = str[i];
			Gui.drawRect(10, 10 + i * 15, 10 + font.getStringWidth(ent), 10 + (i + 1) * 15, 0xff000000);
			font.drawString(ent, 10, 10 + i * 15, 0xffffffff);
		}
	}

	@Override
	protected void initGui() {
		add(new UIImage("realsurv:ui/wallpaper.png"));

		GridPanel contents = new GridPanel();
		contents.addRow(new LengthDefinition(LengthType.FIXED, 20));
		contents.addRow(new LengthDefinition(LengthType.ALLOCATED, 1));
		contents.addEmptyColumn();

		UIPanel taskbar = new UIPanel();
		// taskbar.add(new
		// UILabel().setLabel("Remember").setForegroundColor(0xffffffff));
		// taskbar.add(new
		// UIRectangle().setShadowVisible(false).setRadius(0).setBackgroundColor(0xaa000000).setLayoutSpan(2,
		// 1));
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
		loginForm.addRow(new LengthDefinition(LengthType.ALLOCATED, 1));
		loginForm.add(new UIRectangle().setBackgroundColor(0xffcccccc).setLayoutSpan(2, 4));
		loginForm.add(new UITextfield().setHint("ID").setMargin(new DirWeights(4, 0, 4, 0)).setVerticalArrange(VerticalArrange.CENTER));
		loginForm.add(new UITextfield().setHint("Password").setMargin(new DirWeights(4, 0, 4, 0)).setLayoutPosition(0, 1).setVerticalArrange(VerticalArrange.CENTER));
		loginForm.add(new UICheckbox().setLabel("Remember").setMargin(new DirWeights(0, 0, 4, 0)).setVerticalArrange(VerticalArrange.CENTER).setLayoutPosition(0, 2));
		loginForm.add(new UIButton("Login").setMargin(new DirWeights(4)).setLayoutPosition(1, 0).setLayoutSpan(1, 3));
		loginForm.add(new UICombobox().add("Item1").add("Item2").add("Item3").setMargin(new DirWeights(0, 0, 4, 4)).setLayoutSpan(2, 1).setLayoutPosition(0, 3));
		contents.add(loginForm);
		add(contents);
	}

	@Override
	public void onKeyTyped(int keyCode, char typedChar) {
		// TODO Debug Key
		if (GuiScreen.isCtrlKeyDown()) {
			if (keyCode == Keyboard.KEY_T) {
				initializePanel();
			} else if (keyCode == Keyboard.KEY_R) {
				font.clearCache();
			}
		}
		super.onKeyTyped(keyCode, typedChar);
	}
}