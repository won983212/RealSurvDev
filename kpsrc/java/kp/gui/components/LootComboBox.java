package kp.gui.components;

import kp.Config;
import kp.gui.events.Event;
import kp.gui.events.components.EventChange;
import kp.gui.tab.Tab;
import kp.utils.Log;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

public class LootComboBox extends Component implements kp.gui.events.components.EventClick
{
	private String[] list;
	private int selected;
	private LootButton left;
	private LootButton right;

	public LootComboBox(Tab tab, int id, int width)
	{
		super(tab, id);

		this.left = new LootButton(tab, "<", 0, 11, 11);
		this.left.setEvent(this);

		this.right = new LootButton(tab, ">", 1, 11, 11);
		this.right.setEvent(this);

		setBounds(0, 0, width, 11);
	}

	public void setLocation(int x, int y)
	{
		this.left.setLocation(x, y);
		this.right.setLocation(x + getWidth() - 11, y);

		super.setLocation(x, y);
	}

	public void setList(String[] list)
	{
		this.list = list;
	}

	public void setSelected(int sel)
	{
		this.selected = sel;
	}

	public void setSelected(String s)
	{
		for (int a = 0; a < this.list.length; a++)
		{
			if (this.list[a].equals(s))
			{
				this.selected = a;
			}
		}
	}

	public int getSelected()
	{
		return this.selected;
	}

	public String getSelectedLabel()
	{
		if ((this.selected >= 0) && (this.selected < this.list.length))
		{
			return this.list[this.selected];
		}
		return "Over Index";
	}

	public void setEnabled(boolean e)
	{
		super.setEnabled(e);
		this.left.setEnabled(e);
		this.right.setEnabled(e);
	}

	public void draw()
	{
		FontRenderer fr = getFontRenderer();

		this.left.draw();
		this.right.draw();

		int color = isEnabled() ? -1 : -10066330;

		Gui.drawRect(getX() + 11, getY(), getX() + getWidth() - 11, getY() + 11, Config.getInt(Config.COLOR_THEME2));
		fr.drawString(getSelectedLabel(), getX() + (getWidth() - fr.getStringWidth(getSelectedLabel())) / 2, getY() + 1, color);

		super.draw();
	}

	public void onAction(int id)
	{
		if (isEnabled())
		{
			if (id == 0)
			{
				if (this.selected > 0)
				{
					this.selected -= 1;
				}
				else
				{
					this.selected = (this.list.length - 1);
				}
			}
			else if (id == 1)
			{
				if (this.selected < this.list.length - 1)
				{
					this.selected += 1;
				}
				else
				{
					this.selected = 0;
				}
			}

			if (hasEvent())
			{
				Event e = getEvent();
				if ((e instanceof EventChange))
				{
					((EventChange) e).onChange(this.id, Integer.valueOf(this.selected));
				}
				else
				{
					Log.error("Event is not EventComboBox!!");
				}
			}
		}
	}
}
