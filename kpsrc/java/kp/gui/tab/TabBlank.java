package kp.gui.tab;

import java.awt.Rectangle;

import kp.gui.screen.GuiConfig;
import net.minecraft.client.gui.Gui;

public class TabBlank extends Tab
{
	public TabBlank(GuiConfig gui)
	{
		super(gui);
	}

	public String getName()
	{
		return null;
	}

	public int getIcon()
	{
		return 0;
	}

	public int getColor()
	{
		return 0;
	}

	public void drawComponents(Gui gui, Rectangle bounds)
	{
	}
}