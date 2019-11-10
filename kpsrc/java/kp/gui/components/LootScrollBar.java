package kp.gui.components;

import kp.Config;
import kp.gui.events.components.EventChange;
import kp.gui.events.components.EventClick;
import kp.gui.tab.Tab;
import kp.utils.Log;
import net.minecraft.client.gui.Gui;

public class LootScrollBar extends Component implements EventClick
{
	private int maxValue = 100;
	private int value;
	private Component up;
	private Component down;
	private int scrollBorder = -1;
	protected boolean horizontal = false;

	public LootScrollBar(Tab tab, int id)
	{
		super(tab, id);
	}

	public LootScrollBar(Tab tab, int id, int size, boolean horizontal)
	{
		this(tab, id);
		this.horizontal = horizontal;
		setBounds(0, 0, horizontal ? size : 6, horizontal ? 6 : size);

		int icon = horizontal ? 5 : 2;

		this.up = new LootIconButton(tab, 0, 0, 0, icon);
		this.up.setSize(6, 6);
		this.up.setEffectByMasterXY(false);
		this.up.setEvent(this);

		this.down = new LootIconButton(tab, 1, 0, 0, icon + 1);
		this.down.setSize(6, 6);
		this.down.setEffectByMasterXY(false);
		this.down.setEvent(this);
	}

	public void setEnabled(boolean e)
	{
		super.setEnabled(e);
		if (this.up != null)
		{
			this.up.setEnabled(e);
		}

		if (this.down != null)
		{
			this.down.setEnabled(e);
		}
	}

	public void draw()
	{
		int startScroll = getStartScroll();

		Gui.drawRect(getX(), getY(), getX() + getWidth(), getY() + getHeight(), -4473925);
		if (this.horizontal)
		{
			Gui.drawRect(startScroll, getY(), startScroll + getSize(), getY() + getHeight(), Config.getInt(Config.COLOR_THEME2));
		}
		else
		{
			Gui.drawRect(getX(), startScroll, getX() + getWidth(), startScroll + getSize(), Config.getInt(Config.COLOR_THEME2));
		}

		if (this.up != null)
		{
			this.up.setLocation(getX(), getY());
			this.up.draw();
		}

		if (this.down != null)
		{
			if (this.horizontal)
			{
				this.down.setLocation(getX() + getWidth() - getHeight(), getY());
			}
			else
			{
				this.down.setLocation(getX(), getY() + getHeight() - getWidth());
			}
			this.down.draw();
		}

		super.draw();
	}

	public void setScrollSize(int size)
	{
		setSize(this.horizontal ? size : 6, this.horizontal ? 6 : size);
	}

	public void setValue(int v)
	{
		this.value = v;

		if (hasEvent())
		{
			kp.gui.events.Event ev = getEvent();
			if ((ev instanceof EventChange))
			{
				((EventChange) ev).onChange(this.id, Integer.valueOf(v));
			}
			else
			{
				Log.error("Event isn't EventChange!");
			}
		}
	}

	public int getValue()
	{
		return this.value;
	}

	public void setMaxValue(int max)
	{
		if (max <= 0)
		{
			max = 1;
		}

		this.maxValue = max;

		if (this.value > this.maxValue)
		{
			this.value = this.maxValue;
		}
	}

	protected int getBiggerSize()
	{
		return this.horizontal ? getWidth() : getHeight();
	}

	protected int getSmallerSize()
	{
		return this.horizontal ? getHeight() : getWidth();
	}

	protected int getCanScrollSize()
	{
		return getBiggerSize() - getSize() - getSmallerSize() * 2;
	}

	protected int getScroll()
	{
		return (int) (getCanScrollSize() * this.value / this.maxValue);
	}

	protected int getValueBy(int pixel)
	{
		return (int) (this.maxValue * pixel / getCanScrollSize());
	}

	protected int getSize()
	{
		return getBiggerSize() / 5;
	}

	protected boolean isInsideScroll(int x, int y)
	{
		if (this.horizontal)
		{
			if ((y >= getY()) && (y <= getY() + getSmallerSize()))
			{
				int start = getStartScroll();

				if ((x >= start) && (x <= start + getSize()))
				{
					return true;
				}
			}
		}
		else if ((x >= getX()) && (x <= getX() + getSmallerSize()))
		{
			int start = getStartScroll();

			if ((y >= start) && (y <= start + getSize()))
			{
				return true;
			}
		}

		return false;
	}

	protected int getStartScroll()
	{
		int start = 0;

		if (this.horizontal)
		{
			start = getX() + getHeight() + getScroll();
		}
		else
		{
			start = getY() + getWidth() + getScroll();
		}

		return start;
	}

	public void onAction(int id)
	{
		if (isEnabled())
		{
			if (id == 0)
			{
				if (this.value > 0)
				{
					setValue(this.value - 1);
				}
			}
			else if ((id == 1) && (this.value < this.maxValue))
			{
				setValue(this.value + 1);
			}
		}
	}

	public void onClick(int x, int y, int eventType)
	{
		if (isEnabled())
		{
			int bigger = this.horizontal ? x : y;

			if ((eventType == 0) && (isInsideScroll(x, y)))
			{
				this.scrollBorder = (bigger - getStartScroll());
			}
			else if (eventType == 1)
			{
				this.scrollBorder = -1;
			}
		}
	}

	public void onMove(int mx, int my)
	{
		int value = 0;

		if (this.horizontal)
		{
			value = mx - getX() - getHeight();
		}
		else
		{
			value = my - getY() - getWidth();
		}

		if (this.scrollBorder != -1)
		{
			int v = getValueBy(value - this.scrollBorder);
			if (v < 0)
			{
				setValue(0);
			}
			else if (v > this.maxValue)
			{
				setValue(this.maxValue);
			}
			else
			{
				setValue(v);
			}
		}

		super.onMove(mx, my);
	}
}
