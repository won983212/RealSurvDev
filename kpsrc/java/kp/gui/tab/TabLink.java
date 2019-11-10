package kp.gui.tab;

import java.awt.Desktop;
import java.awt.Rectangle;
import java.io.IOException;
import java.net.URI;

import kp.gui.screen.GuiConfig;
import net.minecraft.client.gui.Gui;

public class TabLink extends Tab
{
	private String name;
	private String url;

	public TabLink(GuiConfig gui, String name, String url)
	{
		super(gui);

		this.name = name;
		this.url = url;
	}

	public String getName()
	{
		return this.name;
	}

	public int getIcon()
	{
		return 5;
	}

	public int getColor()
	{
		return -9369174;
	}

	public void drawComponents(Gui gui, Rectangle bounds)
	{
	}

	public boolean onClick()
	{
		try
		{
			Desktop.getDesktop().browse(URI.create(this.url));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return true;
	}
}
