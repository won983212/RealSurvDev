package kp.gui.tab;

import java.awt.Rectangle;

import kp.gui.events.handler.HandlerKeyboardEvent;
import kp.gui.events.handler.HandlerMouseEvent;
import kp.gui.screen.GuiConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

public abstract class Tab
{
	private GuiConfig gui;
	private HandlerMouseEvent e_mouse = new HandlerMouseEvent();
	private HandlerKeyboardEvent e_key = new HandlerKeyboardEvent();
	private static FontRenderer fr;

	public Tab(GuiConfig gui)
	{
		this.gui = gui;
	}

	public Gui getGraphic()
	{
		return this.gui;
	}

	public int getTabStartX()
	{
		return this.gui.getBounds().x + 55 + 2;
	}

	public int getTabStartY()
	{
		return this.gui.getBounds().y + 12 + 2;
	}

	public int getTabWidth()
	{
		return this.gui.getBounds().width - 55;
	}

	public int getTabHeight()
	{
		return this.gui.getBounds().height - 24;
	}

	protected static FontRenderer getFontRenderer()
	{
		if (fr == null)
		{
			fr = Minecraft.getMinecraft().fontRenderer;
		}

		return fr;
	}

	public HandlerMouseEvent getEventMouse()
	{
		return this.e_mouse;
	}

	public HandlerKeyboardEvent getEventKeyboard()
	{
		return this.e_key;
	}

	public abstract String getName();

	public abstract int getIcon();

	public abstract int getColor();

	public abstract void drawComponents(Gui paramGui, Rectangle paramRectangle);

	public boolean onClick()
	{
		return false;
	}
}
