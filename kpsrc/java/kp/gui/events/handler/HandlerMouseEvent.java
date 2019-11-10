package kp.gui.events.handler;

import kp.gui.events.Event;
import kp.gui.events.EventMouse;
import kp.gui.events.EventMouseMove;

public class HandlerMouseEvent extends HandlerEvent
{
	public void handleMouse(int mx, int my, int ev)
	{
		for (Event e : this.events)
		{
			if ((e instanceof EventMouse))
			{
				((EventMouse) e).onClick(mx, my, ev);
			}
		}
	}

	public void handleMouseMove(int mx, int my)
	{
		for (kp.gui.events.Event e : this.events)
		{
			if ((e instanceof EventMouseMove))
			{
				((EventMouseMove) e).onMove(mx, my);
			}
		}
	}
}
