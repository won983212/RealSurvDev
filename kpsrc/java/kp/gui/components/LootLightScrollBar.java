package kp.gui.components;

import kp.Config;
import kp.gui.tab.Tab;
import net.minecraft.client.gui.Gui;

public class LootLightScrollBar extends LootScrollBar
{
	private int c1 = -4473925;
	private int c2 = -4473925;

	public LootLightScrollBar(Tab tab, int id, int size, boolean horizontal)
	{
		super(tab, id);

		this.horizontal = horizontal;
		setBounds(0, 0, horizontal ? size : 2, horizontal ? 2 : size);
	}

	public void draw()
	{
		int startScroll = getStartScroll();

		drawGradientRect(getX(), getY(), getX() + getWidth(), getY() + getHeight(), this.c1, this.c2);
		if (this.horizontal)
		{
			Gui.drawRect(startScroll, getY(), startScroll + getSize(), getY() + getHeight(), Config.getInt(Config.COLOR_THEME2));
		}
		else
		{
			Gui.drawRect(getX(), startScroll, getX() + getWidth(), startScroll + getSize(), Config.getInt(Config.COLOR_THEME2));
		}

		super.draw();
	}

	public void setGradient(int color1, int color2)
	{
		this.c1 = color1;
		this.c2 = color2;
	}

	protected int getCanScrollSize()
	{
		return getBiggerSize() - getSize();
	}

	protected int getStartScroll()
	{
		int start = 0;

		if (this.horizontal)
		{
			start = getX() + getScroll();
		}
		else
		{
			start = getY() + getScroll();
		}

		return start;
	}
}
