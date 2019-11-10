package kp.gui.components;

import kp.gui.events.Event;
import kp.gui.events.components.EventHovering;
import kp.gui.tab.Tab;
import net.minecraft.client.gui.Gui;

public class LootHoverButton extends Component
{
	private boolean first = true;

	public LootHoverButton(Tab tab, int id)
	{
		super(tab, id);
	}

	public LootHoverButton(Gui gui, int id)
	{
		super(gui, id);
	}

	public void draw()
	{
		if ((isEnabled()) && (isHovered()))
		{
			Gui.drawRect(getX(), getY(), getX() + getWidth(), getY() + getHeight(), 1157627903);
		}

		super.draw();
	}

	public void onMove(int mx, int my)
	{
		if (isEnabled())
		{
			if ((isHovered()) && (hasEvent()))
			{
				Event e = getEvent();
				if ((e instanceof EventHovering))
				{
					((EventHovering) e).onHovering(this.id, mx, my);
					this.first = true;
				}
			}
			else if ((!isHovered()) && (this.first) && (hasEvent()))
			{
				Event e = getEvent();
				if ((e instanceof EventHovering))
				{
					((EventHovering) e).onDisHovering(this.id);
					this.first = false;
				}
			}
		}

		super.onMove(mx, my);
	}
}
