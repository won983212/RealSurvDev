package kp.gui.tab;

import java.awt.Rectangle;

import kp.Config;
import kp.gui.components.LootKeyBox;
import kp.gui.events.components.EventChange;
import kp.gui.screen.GuiConfig;
import kp.utils.Key;
import net.minecraft.client.gui.Gui;

public class TabKey extends Tab implements EventChange
{
	private LootKeyBox[] keyBoxes;

	public TabKey(GuiConfig gui)
	{
		super(gui);

		Key[] keys = Config.getKeys();
		if (keys.length > 0)
		{
			this.keyBoxes = new LootKeyBox[keys.length];
			for (int a = 0; a < keys.length; a++)
			{
				this.keyBoxes[a] = new LootKeyBox(this, a, keys[a].getLabel(), keys[a].getKeyCode());
				this.keyBoxes[a].setBorder(80);
				this.keyBoxes[a].setBounds(5, 5 + a * 15, 60, 12);
				this.keyBoxes[a].setEvent(this);
			}
		}
	}

	public String getName()
	{
		return "키 설정";
	}

	public int getIcon()
	{
		return 2;
	}

	public int getColor()
	{
		return -5592576;
	}

	public void drawComponents(Gui gui, Rectangle bounds)
	{
		if (this.keyBoxes != null)
		{
			for (int a = 0; a < 5; a++)
			{
				this.keyBoxes[a].draw();
			}
		}
	}

	public void onChange(int id, Object value)
	{
		Config.getKeys()[id].setKey(((Integer) value).intValue());
		Config.saveConfig();
	}
}
