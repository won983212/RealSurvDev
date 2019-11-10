package kp.gui.events;

public abstract interface EventKeyboard extends Event
{
	public abstract void onTyped(char paramChar, int paramInt);
}
