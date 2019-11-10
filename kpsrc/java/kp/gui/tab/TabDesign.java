package kp.gui.tab;

import kp.Config;
import kp.gui.components.LootCheckBox;
import kp.gui.components.LootColorPicker;
import kp.gui.events.components.EventChange;
import kp.gui.screen.GuiConfig;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

public class TabDesign extends Tab implements EventChange
{
	private LootCheckBox usePanel;
	private LootCheckBox followPanel;
	private LootCheckBox useCursor;
	private LootCheckBox showLength;
	private LootColorPicker chatBackColor;
	private LootColorPicker cursorColor;
	private LootColorPicker theme1Color;
	private LootColorPicker theme2Color;

	public TabDesign(GuiConfig gui)
	{
		super(gui);

		this.usePanel = new LootCheckBox(this, "한/영 패널 사용", 0);
		this.usePanel.setSelected(Config.getBool(Config.USE_INFO_PANEL));
		this.usePanel.setEvent(this);

		this.followPanel = new LootCheckBox(this, "패널 따라오게 하기", 1);
		this.followPanel.setSelected(Config.getBool(Config.USE_FOLLOWING_INFO_PANEL));
		this.followPanel.setEnabled(Config.getBool(Config.USE_INFO_PANEL));
		this.followPanel.setLocation(0, 15);
		this.followPanel.setEvent(this);

		this.chatBackColor = new LootColorPicker(this, 2, Config.getInt(Config.COLOR_CHAT_BACK));
		this.chatBackColor.setEnabled(Config.getBool(Config.USE_INFO_PANEL));
		this.chatBackColor.setLocation(100, 16);
		this.chatBackColor.setEvent(this);

		this.useCursor = new LootCheckBox(this, "한글 입력 시 커서 표시", 3);
		this.useCursor.setSelected(Config.getBool(Config.SHOW_EDITING_CURSOR));
		this.useCursor.setLocation(0, 30);
		this.useCursor.setEvent(this);

		this.showLength = new LootCheckBox(this, "채팅 길이 표시하기", 7);
		this.showLength.setSelected(Config.getBool(Config.SHOW_CHAT_LENGTH));
		this.showLength.setEnabled(Config.getBool(Config.USE_INFO_PANEL));
		this.showLength.setLocation(0, 45);
		this.showLength.setEvent(this);

		this.cursorColor = new LootColorPicker(this, 4, Config.getInt(Config.COLOR_CHAT_CURSOR));
		this.cursorColor.setEnabled(Config.getBool(Config.SHOW_EDITING_CURSOR));
		this.cursorColor.setLocation(100, 30);
		this.cursorColor.setEvent(this);

		this.theme1Color = new LootColorPicker(this, 5, Config.getInt(Config.COLOR_THEME1));
		this.theme1Color.setLocation(50, 60);
		this.theme1Color.setEvent(this);

		this.theme2Color = new LootColorPicker(this, 6, Config.getInt(Config.COLOR_THEME2));
		this.theme2Color.setLocation(50, 75);
		this.theme2Color.setEvent(this);
	}

	public String getName()
	{
		return "디자인 설정";
	}

	public int getIcon()
	{
		return 3;
	}

	public int getColor()
	{
		return -16733696;
	}

	public void drawComponents(Gui gui, java.awt.Rectangle bounds)
	{
		FontRenderer fr = getFontRenderer();

		this.usePanel.draw();
		this.followPanel.draw();
		this.chatBackColor.draw();
		this.cursorColor.draw();
		this.useCursor.draw();
		this.showLength.draw();
		this.theme1Color.draw();
		this.theme2Color.draw();

		fr.drawString("창 테마", getTabStartX(), getTabStartY() + 60, -1);
		fr.drawString("컴포넌트 테마", getTabStartX(), getTabStartY() + 75, -1);
	}

	public void onChange(int id, Object value)
	{
		switch (id)
		{
		case 0:
			Config.set(Config.USE_INFO_PANEL, value);
			this.followPanel.setEnabled((Boolean) value);
			this.chatBackColor.setEnabled((Boolean) value);
			break;
		case 1:
			Config.set(Config.USE_FOLLOWING_INFO_PANEL, value);
			break;
		case 2:
			Config.set(Config.COLOR_CHAT_BACK, ((Integer) value & 0xFFFFFF) + Integer.MIN_VALUE);
			break;
		case 3:
			Config.set(Config.SHOW_EDITING_CURSOR, value);
			this.cursorColor.setEnabled((Boolean) value);
			break;
		case 4:
			Config.set(Config.COLOR_CHAT_CURSOR, ((Integer) value & 0xFFFFFF) + 0x40000000);
			break;
		case 5:
			Config.set(Config.COLOR_THEME1, value);
			break;
		case 6:
			Config.set(Config.COLOR_THEME2, value);
		case 7:
			Config.set(Config.SHOW_CHAT_LENGTH, value);
		}
	}
}
