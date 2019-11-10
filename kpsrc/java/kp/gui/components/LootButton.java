package kp.gui.components;

import kp.Config;
import kp.gui.events.Event;
import kp.gui.events.components.EventClick;
import kp.gui.tab.Tab;
import kp.utils.Log;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

public class LootButton extends LootHoverButton
{
	private String text = "";
	private int color = Config.getInt(Config.COLOR_THEME2);

	public LootButton(Tab tab, String s, int id, int w, int h)
	{
		super(tab, id);
		setBounds(0, 0, w, h);
		this.text = s;
	}

	public LootButton(Gui gui, String s, int id, int w, int h)
	{
		super(gui, id);
		setBounds(0, 0, w, h);
		this.text = s;
	}

	public LootButton(Tab tab, String s, int id)
	{
		this(tab, s, id, getFontRenderer().getStringWidth(s) + 10, 12);
	}

	public void draw()
	{
		FontRenderer fr = getFontRenderer();

		int fontcolor = -1;

		if (this.color != -1)
		{
			if ((this.color & 0xFFFFFF) > 12303291)
			{
				fontcolor = 0;
			}

			Gui.drawRect(getX(), getY(), getX() + getWidth(), getY() + getHeight(), this.color);
		}

		if (!isEnabled())
		{
			fontcolor = -10066330;
		}

		fr.drawString(this.text, getX() + (getWidth() - fr.getStringWidth(this.text)) / 2, getY() + getHeight() / 2 - 4, fontcolor);

		super.draw();
	}

	public void setButtonColor(int color)
	{
		this.color = color;
	}

	public void setTextAndResize(String text)
	{
		setText(text);
		setSize(getFontRenderer().getStringWidth(text) + 10, getHeight());
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public void onClick(int x, int y, int eventType)
	{
		if ((isEnabled()) && (eventType == 0) && (isInside(x, y)) && (hasEvent()))
		{
			Event e = getEvent();
			if ((e instanceof EventClick))
			{
				((EventClick) e).onAction(this.id);
			}
			else
			{
				Log.error("IconButton Event is not EventCompButton!!");
			}
		}
	}
}
