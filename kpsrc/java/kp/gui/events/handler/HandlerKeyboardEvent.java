package kp.gui.events.handler;

import kp.gui.events.EventKeyboard;

public class HandlerKeyboardEvent extends HandlerEvent
{
	public void handleKeyboard(char c, int key)
	{
		for (kp.gui.events.Event e : this.events)
		{
			if ((e instanceof EventKeyboard))
			{
				((EventKeyboard) e).onTyped(c, key);
			}
		}
	}
}
