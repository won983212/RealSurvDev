package kp.gui.tab;

import java.awt.Rectangle;

import kp.Config;
import kp.gui.components.LootCheckBox;
import kp.gui.components.LootComboBox;
import kp.inputs.keyboard.KeyLoader;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

public class TabAdv extends Tab implements kp.gui.events.components.EventChange
{
	private LootCheckBox jasoFirst;
	private LootComboBox keyboards;

	public TabAdv(kp.gui.screen.GuiConfig gui)
	{
		super(gui);

		this.jasoFirst = new LootCheckBox(this, "한글 지울 때 자소 단위 우선", 0);
		this.jasoFirst.setSelected(Config.getBool(Config.DELETE_JASO_UNIT));
		this.jasoFirst.setEvent(this);

		this.keyboards = new LootComboBox(this, 1, 80);
		this.keyboards.setList(KeyLoader.getKeyLoader().getKeyboards());
		this.keyboards.setSelected(Config.getInt(Config.TYPE_KEYARRAY));
		this.keyboards.setLocation(45, 15);
		this.keyboards.setEvent(this);
	}

	public String getName()
	{
		return "고급 설정";
	}

	public int getIcon()
	{
		return 4;
	}

	public int getColor()
	{
		return -16777046;
	}

	public void drawComponents(Gui gui, Rectangle bounds)
	{
		FontRenderer fr = getFontRenderer();

		this.jasoFirst.draw();
		this.keyboards.draw();

		fr.drawString("키보드 배열: ", getTabStartX(), getTabStartY() + 16, -1);
	}

	public void onChange(int id, Object value)
	{
		if (id == 0)
		{
			Config.set(Config.DELETE_JASO_UNIT, value);
		}
		else if (id == 1)
		{
			Config.set(Config.TYPE_KEYARRAY, value);
			KeyLoader.getKeyLoader().setKeyboard(Config.getInt(Config.TYPE_KEYARRAY));
		}
	}
}
