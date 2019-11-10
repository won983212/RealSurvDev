package kp.gui.components;

import java.awt.Rectangle;
import java.util.LinkedList;

import kp.gui.events.EventKeyboard;
import kp.gui.events.EventMouseMove;
import kp.gui.screen.GuiConfig;
import kp.gui.tab.Tab;
import kp.utils.IconLoader;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;

public class LootTabViewer implements kp.gui.events.EventMouse, EventMouseMove, EventKeyboard
{
	public static final int TAB_SIZE = 55;
	private LinkedList<Tab> tabs = new LinkedList<Tab>();
	private GuiConfig parent;
	private int selected = 0;
	private int hoverd = 0;

	public LootTabViewer(GuiConfig parent)
	{
		this.parent = parent;

		parent.e_mouse.pushEvent(this);
		parent.e_key.pushEvent(this);
	}

	public void clearTabs()
	{
		this.tabs.clear();
	}

	public void addTab(Tab tab)
	{
		this.tabs.add(tab);
	}

	public void render(FontRenderer fr)
	{
		Rectangle bon = this.parent.getBounds();

		int i = 0;
		GuiConfig.drawRect(bon.x, bon.y + 12, bon.x + 1, bon.y + bon.height - 12, -10066330);
		for (Tab tab : this.tabs)
		{
			int fontColor = -10066330;
			int tabColor = -10066330;
			int startY = bon.y + 12 + i * 15;

			if (tab.getName() == null)
			{
				GuiConfig.drawRect(bon.x, startY, bon.x + 2, startY + 15, tabColor);
				i++;
			}
			else
			{
				if (i == this.selected)
				{
					GuiConfig.drawRect(bon.x, startY, bon.x + 55, startY + 15, 0x33ffffff);
				}
				else if (i == this.hoverd)
				{
					GuiConfig.drawRect(bon.x, startY, bon.x + 55, startY + 15, 0x22ffffff);
				}

				if (i == this.selected)
				{
					fontColor = -1;
					tabColor = tab.getColor();

					Component.setupMasterPosition(tab.getTabStartX(), tab.getTabStartY());
					tab.drawComponents(this.parent, bon);
				}
				else if (i == this.hoverd)
				{
					fontColor = -5592406;
					tabColor = tab.getColor();
				}

				GuiConfig.drawRect(bon.x, startY, bon.x + 2, startY + 15, tabColor);
				if (i > 2)
				{
					GlStateManager.enableBlend();
				}

				fr.drawString(tab.getName(), bon.x + 17, startY + (15 - fr.FONT_HEIGHT) / 2, fontColor);
				IconLoader.drawTexture(this.parent, bon.x + 4, startY + 2, tab.getIcon(), 0, -1);

				i++;
			}
		}
	}

	public Tab getSelectedTab()
	{
		if (verifySelected(this.selected))
		{
			return (Tab) this.tabs.get(this.selected);
		}
		return null;
	}

	private int getSelectMenu(int x, int y)
	{
		Rectangle bon = this.parent.getBounds();

		if ((x >= bon.x) && (x <= bon.x + 55) && (y <= bon.y + 12 + 15 * this.tabs.size()) && (y >= bon.y + 12))
		{
			return (y - bon.y - 12) / 15;
		}

		return -1;
	}

	private boolean verifySelected(int sel)
	{
		return (sel >= 0) && (sel < this.tabs.size()) && (((Tab) this.tabs.get(sel)).getName() != null);
	}

	public void onClick(int x, int y, int eventType)
	{
		if (verifySelected(this.selected))
		{
			((Tab) this.tabs.get(this.selected)).getEventMouse().handleMouse(x, y, eventType);
		}

		if (eventType == 0)
		{
			int sel = getSelectMenu(x, y);
			if ((verifySelected(sel)) && (!((Tab) this.tabs.get(sel)).onClick()))
			{
				this.selected = sel;
			}
		}
	}

	public void onMove(int mx, int my)
	{
		this.hoverd = getSelectMenu(mx, my);

		if (verifySelected(this.selected))
		{
			((Tab) this.tabs.get(this.selected)).getEventMouse().handleMouseMove(mx, my);
		}
	}

	public void onTyped(char c, int key)
	{
		if (verifySelected(this.selected))
		{
			((Tab) this.tabs.get(this.selected)).getEventKeyboard().handleKeyboard(c, key);
		}
	}
}
