package kp.gui.events.handler;

import java.util.LinkedList;

import kp.gui.events.Event;

public class HandlerEvent
{
	protected LinkedList<Event> events = new LinkedList<Event>();

	public void pushEvent(Event e)
	{
		if (!this.events.contains(e))
		{
			this.events.add(e);
		}
	}

	public void removeEvent(Event e)
	{
		if (this.events.contains(e))
		{
			this.events.remove(e);
		}
	}

	public int size()
	{
		return this.events.size();
	}
}
