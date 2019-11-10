package kp.gui.components;

import kp.Config;
import kp.gui.events.Event;
import kp.gui.events.components.EventChange;
import kp.gui.tab.Tab;
import kp.utils.IconLoader;
import kp.utils.Log;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

public class LootCheckBox extends LootHoverButton
{
	private String text = "";
	private boolean selected = false;

	public LootCheckBox(Tab tab, String text, int id)
	{
		super(tab, id);
		this.text = text;
		setBounds(0, 0, 15 + getFontRenderer().getStringWidth(text), 12);
	}

	public void draw()
	{
		Gui gui = this.getGui();
		FontRenderer fr = getFontRenderer();

		Gui.drawRect(getX() + 2, getY() + 2, getX() + 10, getY() + 10, this.selected ? Config.getInt(Config.COLOR_THEME2) : -1);
		fr.drawString(this.text, getX() + 14, getY() + 2, isEnabled() ? -1 : -10066330);
		IconLoader.drawTexture(gui, getX(), getY(), 1, 3);

		if (this.selected)
		{
			IconLoader.drawTexture(gui, getX(), getY(), 0, 3);
		}

		super.draw();
	}

	public boolean getSelected()
	{
		return this.selected;
	}

	public void setSelected(boolean sel)
	{
		this.selected = sel;
	}

	public void onClick(int x, int y, int eventType)
	{
		if ((isEnabled()) && (eventType == 0) && (isInside(x, y)))
		{
			this.selected = (!this.selected);

			if (hasEvent())
			{
				Event ev = getEvent();
				if ((ev instanceof EventChange))
				{
					((EventChange) ev).onChange(this.id, Boolean.valueOf(this.selected));
				}
				else
				{
					Log.error("Event is not EventCheckbox");
				}
			}
		}
	}
}
