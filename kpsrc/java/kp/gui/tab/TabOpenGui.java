package kp.gui.tab;

import java.awt.Rectangle;

import kp.gui.screen.GuiConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;

public class TabOpenGui extends Tab
{
	private String name;
	private GuiScreen screen;

	public TabOpenGui(GuiConfig gui, String name, GuiScreen screen)
	{
		super(gui);

		this.name = name;
		this.screen = screen;
	}

	public String getName()
	{
		return this.name;
	}

	public int getIcon()
	{
		return 6;
	}

	public int getColor()
	{
		return -1179393;
	}

	public void drawComponents(Gui gui, Rectangle bounds)
	{
	}

	public boolean onClick()
	{
		Minecraft.getMinecraft().displayGuiScreen(this.screen);
		return true;
	}
}
