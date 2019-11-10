package kp.gui.screen;

import java.awt.Rectangle;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import kp.Config;
import kp.KoreanPatch;
import kp.gui.components.Component;
import kp.gui.components.LootIconButton;
import kp.gui.components.LootTabViewer;
import kp.gui.events.components.EventClick;
import kp.gui.events.handler.HandlerKeyboardEvent;
import kp.gui.events.handler.HandlerMouseEvent;
import kp.gui.screen.dialog.DialogFirstRun;
import kp.gui.tab.Tab;
import kp.gui.tab.TabAdv;
import kp.gui.tab.TabBlank;
import kp.gui.tab.TabDesign;
import kp.gui.tab.TabFont;
import kp.gui.tab.TabGeneral;
import kp.gui.tab.TabKey;
import kp.gui.tab.TabLink;
import kp.gui.tab.TabOpenGui;
import kp.utils.IconLoader;
import net.minecraft.client.gui.GuiScreen;

public class GuiConfig extends GuiScreen implements EventClick
{
	public static final int BAR_SIZE = 12;
	public static final int GUI_WIDTH = 350;
	public static final int GUI_HEIGHT = 210;
	public HandlerMouseEvent e_mouse = new HandlerMouseEvent();
	public HandlerKeyboardEvent e_key = new HandlerKeyboardEvent();

	private SimpleDateFormat format = new SimpleDateFormat("a hh:mm:ss");
	private Date date = new Date();
	private LootTabViewer tabs = new LootTabViewer(this);
	private LootIconButton exit;
	private LootIconButton save;
	private LootIconButton reset;
	private int uiX;
	private int uiY;
	private int uiWidth;
	private int uiHeight;

	public void initGui()
	{
		Config.pushData();
		this.tabs.clearTabs();

		this.tabs.addTab(new TabGeneral(this));
		this.tabs.addTab(new TabFont(this));
		this.tabs.addTab(new TabKey(this));
		this.tabs.addTab(new TabDesign(this));
		this.tabs.addTab(new TabAdv(this));
		this.tabs.addTab(new TabBlank(this));
		this.tabs.addTab(new TabLink(this, "룻트 블로그", "http://blog.naver.com/won983212"));
		this.tabs.addTab(new TabOpenGui(this, "설명 보기", new DialogFirstRun(this)));

		this.exit = new LootIconButton(this, 0, this.uiX, this.uiY, 0);
		this.exit.setEffectByMasterXY(false);
		this.exit.setHintText("종료 (저장 안됨)");
		this.exit.setEvent(this);

		this.save = new LootIconButton(this, 1, this.uiX, this.uiY, 1);
		this.save.setEffectByMasterXY(false);
		this.save.setHintText("저장 후 종료");
		this.save.setEvent(this);

		this.reset = new LootIconButton(this, 2, this.uiX, this.uiY, 7);
		this.reset.setEffectByMasterXY(false);
		this.reset.setHintText("초기화");
		this.reset.setEvent(this);

		this.e_mouse.pushEvent(this.exit);
		this.e_mouse.pushEvent(this.save);
		this.e_mouse.pushEvent(this.reset);
	}

	public void drawTooltip(String s, int x, int y)
	{
		ArrayList<String> list = new ArrayList<String>();
		list.add(s);

		drawHoveringText(list, x, y);
	}

	public void handleInput() throws IOException
	{
		if (Mouse.isCreated())
		{
			while (Mouse.next())
			{
				int i = Mouse.getEventX() * this.width / this.mc.displayWidth;
				int j = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
				int k = Mouse.getEventButton();

				if (Mouse.getEventButtonState())
				{
					mouseClicked(i, j, k);
					this.e_mouse.handleMouse(i, j, 0);
				}
				else if (k != -1)
				{
					mouseReleased(i, j, k);
					this.e_mouse.handleMouse(i, j, 1);
				}
				else
				{
					this.e_mouse.handleMouseMove(i, j);
				}
			}
		}

		if (Keyboard.isCreated())
		{
			while (Keyboard.next())
			{
				if (Keyboard.getEventKeyState())
				{
					keyTyped(Keyboard.getEventCharacter(), Keyboard.getEventKey());
				}
				this.mc.dispatchKeypresses();
			}
		}
	}

	protected void keyTyped(char c, int k)
	{
		if (k == 1)
		{
			this.mc.displayGuiScreen((GuiScreen) null);
			this.mc.setIngameFocus();
			Config.saveConfig();
		}

		this.e_key.handleKeyboard(Keyboard.getEventCharacter(), Keyboard.getEventKey());
	}

	public void drawScreen(int mx, int my, float particalTicks)
	{
		drawDefaultBackground();

		int theme1 = Config.getInt(Config.COLOR_THEME1);

		setupSize(350, 210);
		drawRect(this.uiX, this.uiY, this.uiX + this.uiWidth, this.uiY + 12, theme1);
		drawRect(this.uiX, this.uiY + 12, this.uiX + this.uiWidth, this.uiY + this.uiHeight - 12, -1440603614);
		drawRect(this.uiX + 55, this.uiY + 12, this.uiX + this.uiWidth, this.uiY + this.uiHeight - 12, 2002081109);
		drawRect(this.uiX, this.uiY + this.uiHeight - 12, this.uiX + this.uiWidth, this.uiY + this.uiHeight, theme1);

		String time = this.format.format(this.date);
		this.date.setTime(System.currentTimeMillis());
		this.fontRenderer.drawString("Configuration - Loot's Patch | Version §6§l" + KoreanPatch.VERSION, this.uiX + 3, this.uiY + this.uiHeight - 10, -1);
		this.fontRenderer.drawString(time, this.uiX + this.uiWidth - this.fontRenderer.getStringWidth(time) - 3, this.uiY + this.uiHeight - 10, -1);

		Tab tab = this.tabs.getSelectedTab();
		if (tab != null)
		{
			IconLoader.drawTexture(this, this.uiX + 1, this.uiY, tab.getIcon(), 0);
			this.fontRenderer.drawString(tab.getName(), this.uiX + 14, this.uiY + 2, -1);
		}
		else
		{
			int x = this.uiX + (this.uiWidth - this.fontRenderer.getStringWidth("§l왼쪽에서 메뉴를 선택해주세요.")) / 2 + 27;
			this.fontRenderer.drawString("Configuration UI", this.uiX + 3, this.uiY + 2, -1);
			this.fontRenderer.drawString("§l왼쪽에서 메뉴를 선택해주세요.", x, this.uiY + 12 + (this.uiHeight - 12) / 2 - 4, -1);
		}

		Component.setupMasterPosition(0, 0);
		this.exit.draw();
		this.save.draw();
		this.reset.draw();
		this.tabs.render(this.fontRenderer);
	}

	public Rectangle getBounds()
	{
		return new Rectangle(this.uiX, this.uiY, this.uiWidth, this.uiHeight);
	}

	private void setupSize(int w, int h)
	{
		long check = this.uiX * this.uiY * this.uiWidth * this.uiHeight;

		this.uiX = ((this.width - w) / 2);
		this.uiY = ((this.height - h) / 2);
		this.uiWidth = w;
		this.uiHeight = h;

		if (check != this.uiX * this.uiY * this.uiWidth * this.uiHeight)
		{
			changedSize();
		}
	}

	public void changedSize()
	{
		this.exit.setBounds(this.uiX + this.uiWidth - 12, this.uiY, 12, 12);
		this.save.setBounds(this.uiX + this.uiWidth - 24, this.uiY, 12, 12);
		this.reset.setBounds(this.uiX + this.uiWidth - 36, this.uiY, 12, 12);
	}

	public void onAction(int id)
	{
		if (id == 0)
		{
			Config.popData();
		}
		else if (id == 1)
		{
			Config.saveConfig();
		}
		else if (id == 2)
		{
			Config.resetConfig();
		}

		this.mc.displayGuiScreen(null);
	}
}
