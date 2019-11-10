package kp.gui.components;

import kp.gui.events.Event;
import kp.gui.events.EventMouse;
import kp.gui.events.EventMouseMove;
import kp.gui.events.components.EventClick;
import kp.gui.tab.Tab;
import kp.utils.IconLoader;
import kp.utils.Log;
import net.minecraft.client.gui.Gui;

public class LootIconButton extends LootHoverButton implements EventMouse, EventMouseMove
{
	private int icon;

	public LootIconButton(Tab tab, int id, int x, int y, int icon)
	{
		super(tab, id);
		this.icon = icon;

		setBounds(x, y, 12, 12);
	}

	public LootIconButton(Gui gui, int id, int x, int y, int icon)
	{
		super(gui, id);
		this.icon = icon;

		setBounds(x, y, 12, 12);
	}

	public void draw()
	{
		IconLoader.drawTexture(getGui(), getX(), getY(), this.icon, 1);
		super.draw();
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

/*
 * Location: C:\Users\cd\Desktop\b77_1710f
 * (1).jar!\kp\gui\components\LootIconButton.class Java compiler version: 6
 * (50.0) JD-Core Version: 0.7.1
 */