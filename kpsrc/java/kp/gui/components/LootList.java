package kp.gui.components;

import kp.Config;
import kp.gui.events.Event;
import kp.gui.events.components.EventChange;
import kp.gui.tab.Tab;
import kp.utils.Log;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

public class LootList extends Component
{
	private String[] list;
	private LootScrollBar scroll;
	private int selected = -1;

	public LootList(Tab tab, int id, int w, int h)
	{
		super(tab, id);

		this.scroll = new LootScrollBar(tab, 0, h, false);
		this.scroll.setEffectByMasterXY(false);

		setBounds(0, 0, w, h);
	}

	public void setValues(String[] list)
	{
		this.list = list;
		this.scroll.setMaxValue(list.length - getSight());
	}

	public void draw()
	{
		FontRenderer fr = getFontRenderer();
		int border = getX() + getWidth() - this.scroll.getWidth();

		Gui.drawRect(getX(), getY(), border, getY() + getHeight(), -1118482);

		this.scroll.setLocation(border, getY());
		this.scroll.draw();

		int start = this.scroll.getValue();
		for (int a = 0; a < getSight(); a++)
		{
			if (start + a >= this.list.length)
			{
				break;
			}

			int drawX = getX() + 2;
			int drawY = getItemY(a);

			if (this.selected == start + a)
			{
				int color = Config.getInt(Config.COLOR_THEME2) & 0x88FFFFFF;
				Gui.drawRect(getX(), drawY - 1, border, drawY + fr.FONT_HEIGHT + 1, color);
			}
			
			fr.drawString(this.list[start + a], drawX, drawY, isEnabled() ? 0 : -10066330);
		}

		super.draw();
	}

	public void setEnabled(boolean e)
	{
		super.setEnabled(e);
		this.scroll.setEnabled(e);
	}

	public void setSize(int w, int h)
	{
		super.setSize(w, h);
		this.scroll.setScrollSize(h);
		if (this.list != null)
		{
			this.scroll.setMaxValue(this.list.length - getSight());
		}
	}

	public void onClick(int x, int y, int eventType)
	{
		if ((isEnabled()) && (eventType == 0))
		{
			int sight = getSight();
			int maxY = getItemY(sight);

			if ((x >= getX()) && (x <= getX() + getWidth() - this.scroll.getWidth()) && (y >= getY() + 2) && (y <= maxY))
			{
				int select = (y - (getY() + 2)) / getItemHeight() + this.scroll.getValue();

				if ((select >= 0) && (select < sight + this.scroll.getValue()))
				{
					setSelected(select);
				}
			}
		}
	}

	private boolean verifySelected(int sel)
	{
		if ((sel >= 0) && (sel < this.list.length))
		{
			return true;
		}

		return false;
	}

	public void setSelected(int sel)
	{
		this.selected = sel;

		if (hasEvent())
		{
			Event ev = getEvent();
			if ((ev instanceof EventChange))
			{
				((EventChange) ev).onChange(this.id, Integer.valueOf(this.selected));
			}
			else
			{
				Log.error("Event is not EventChange");
			}
		}
	}

	private int getItemHeight()
	{
		return getFontRenderer().FONT_HEIGHT + 1;
	}

	private int getSight()
	{
		return (getHeight() - 4) / getItemHeight();
	}

	private int getItemY(int index)
	{
		return getY() + 2 + getItemHeight() * index;
	}

	public String getSelectedString()
	{
		if (verifySelected(this.selected))
		{
			return this.list[this.selected];
		}
		return null;
	}
}
