package kp.gui.tab;

import kp.Config;
import kp.gui.components.LootCheckBox;
import kp.gui.components.LootTextBox;
import kp.gui.events.components.EventChange;

public class TabGeneral extends Tab implements EventChange
{
	private LootCheckBox useTranslate;
	private LootCheckBox useFastHanja;
	private LootCheckBox usePrefixSuffix;
	private LootCheckBox applyOnSlash;
	private LootCheckBox showOptionKey;
	private LootCheckBox useColorModifier;
	private LootTextBox prefix;
	private LootTextBox suffix;

	public TabGeneral(kp.gui.screen.GuiConfig gui)
	{
		super(gui);

		this.useTranslate = new LootCheckBox(this, "영타 변환 사용 §o(Shift누르면서 마우스 올리면 변환됨)", 0);
		this.useTranslate.setSelected(Config.getBool(Config.USE_TARNSLATE));
		this.useTranslate.setEvent(this);
		
		this.useFastHanja = new LootCheckBox(this, "빠른 한자입력 사용. §o(Ctrl+B하면 최근 한자 자동입력)", 1);
		this.useFastHanja.setSelected(Config.getBool(Config.USE_FAST_HANJA));
		this.useFastHanja.setLocation(0, 15);
		this.useFastHanja.setEvent(this);

		this.usePrefixSuffix = new LootCheckBox(this, "접두사 및 접미사 사용", 3);
		this.usePrefixSuffix.setSelected(Config.getBool(Config.USE_PREFIX_OR_SUFFIX));
		this.usePrefixSuffix.setLocation(0, 30);
		this.usePrefixSuffix.setEvent(this);

		this.applyOnSlash = new LootCheckBox(this, "\"/\" 입력 시, 뒤에 적용", 4);
		this.applyOnSlash.setEnabled(this.usePrefixSuffix.getSelected());
		this.applyOnSlash.setSelected(Config.getBool(Config.PREFIX_ON_SLASH));
		this.applyOnSlash.setLocation(100, 30);
		this.applyOnSlash.setEvent(this);

		this.showOptionKey = new LootCheckBox(this, "채팅창 상단 옵션키 표시", 5);
		this.showOptionKey.setSelected(Config.getBool(Config.SHOW_OPTION_KEY));
		this.showOptionKey.setLocation(0, 60);
		this.showOptionKey.setEvent(this);
		
		this.useColorModifier = new LootCheckBox(this, "색깔 코드 입력시 '&' 사용하기", 6);
		this.useColorModifier.setSelected(Config.getBool(Config.USE_COLOR_MODIFIER));
		this.useColorModifier.setLocation(0, 75);
		this.useColorModifier.setEvent(this);

		this.prefix = new LootTextBox(this, 9, 50);
		this.prefix.setEnabled(this.usePrefixSuffix.getSelected());
		this.prefix.setText(Config.getString(Config.PREFIX));
		this.prefix.setHint("접두사");
		this.prefix.setLocation(0, 45);
		this.prefix.setEvent(this);

		this.suffix = new LootTextBox(this, 10, 50);
		this.suffix.setEnabled(this.usePrefixSuffix.getSelected());
		this.suffix.setText(Config.getString(Config.SUFFIX));
		this.suffix.setHint("접미사");
		this.suffix.setLocation(60, 45);
		this.suffix.setEvent(this);
	}

	public void drawComponents(net.minecraft.client.gui.Gui gui, java.awt.Rectangle b)
	{
		this.useTranslate.draw();
		this.useFastHanja.draw();
		this.usePrefixSuffix.draw();
		this.applyOnSlash.draw();
		this.showOptionKey.draw();
		this.useColorModifier.draw();
		this.prefix.draw();
		this.suffix.draw();
	}

	public String getName()
	{
		return "기본 설정";
	}

	public int getIcon()
	{
		return 0;
	}

	public int getColor()
	{
		return -5636096;
	}

	public void onChange(int id, Object value)
	{
		boolean val = false;
		if ((value instanceof Boolean))
		{
			val = ((Boolean) value).booleanValue();
		}

		switch (id)
		{
		case 0:
			Config.set(Config.USE_TARNSLATE, val);
			break;
		case 1:
			Config.set(Config.USE_FAST_HANJA, val);
			break;
		case 3:
			Config.set(Config.USE_PREFIX_OR_SUFFIX, val);
			this.applyOnSlash.setEnabled(val);
			this.prefix.setEnabled(val);
			this.suffix.setEnabled(val);
			break;
		case 4:
			Config.set(Config.PREFIX_ON_SLASH, val);
			break;
		case 5:
			Config.set(Config.SHOW_OPTION_KEY, val);
			break;
		case 6:
			Config.set(Config.USE_COLOR_MODIFIER, val);
			break;
		case 9:
			Config.set(Config.PREFIX, value);
			break;
		case 10:
			Config.set(Config.SUFFIX, value);
		}
	}
}
