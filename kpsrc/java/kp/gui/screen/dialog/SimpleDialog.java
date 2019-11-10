package kp.gui.screen.dialog;

import kp.gui.events.EventDialog;
import net.minecraft.client.gui.GuiScreen;

public class SimpleDialog extends GuiDialog
{
	private final String title;
	private final String messages;
	private final String buttons;

	public SimpleDialog(GuiScreen parent, String title, String messages, String buttons, EventDialog dialog)
	{
		super(parent);

		this.title = title;
		this.messages = messages;
		this.buttons = buttons;
		setEvent(dialog);
	}

	public String getTitle()
	{
		return this.title;
	}

	public String getMessages()
	{
		return this.messages;
	}

	public String getButtons()
	{
		return this.buttons;
	}
}
