package kp.gui.tab;

import java.awt.Font;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.Locale;

import kp.Config;
import kp.gui.components.LootButton;
import kp.gui.components.LootCheckBox;
import kp.gui.components.LootList;
import kp.gui.components.LootTextBox;
import kp.gui.events.components.EventChange;
import kp.gui.events.components.EventClick;
import kp.gui.screen.GuiConfig;
import kp.wrapper.FontRendererWrapper;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.src.betterfonts.GlyphCache;

public class TabFont extends Tab implements EventChange, EventClick
{
	private LootList fontList;
	private LootTextBox search;
	private LootCheckBox useFont;
	private LootCheckBox useAnti;
	private LootTextBox fontSize;
	private LootButton reset;

	public TabFont(GuiConfig gui)
	{
		super(gui);

		this.useFont = new LootCheckBox(this, "폰트 사용하기", 2);
		this.useFont.setSelected(Config.getBool(Config.USE_FONT));
		this.useFont.setLocation(130, 5);
		this.useFont.setEvent(this);

		this.fontList = new LootList(this, 0, 0, 0);
		this.fontList.setEnabled(this.useFont.getSelected());
		this.fontList.setValues(getFonts(null));
		this.fontList.setSelected(-1);
		this.fontList.setEvent(this);

		this.search = new LootTextBox(this, 1, 120);
		this.search.setEnabled(this.useFont.getSelected());
		this.search.setIcon(0);
		this.search.setHint("폰트 검색");
		this.search.setEvent(this);

		this.useAnti = new LootCheckBox(this, "안티엘리어싱 사용 §8<다시 시작>", 3);
		this.useAnti.setEnabled(this.useFont.getSelected());
		this.useAnti.setSelected(Config.getBool(Config.USE_FONT_ANTIALIASING));
		this.useAnti.setLocation(130, 20);
		this.useAnti.setEvent(this);

		this.fontSize = new LootTextBox(this, 4, 50);
		this.fontSize.setEnabled(this.useFont.getSelected());
		this.fontSize.setTextMap("0123456789");
		this.fontSize.setHint("폰트 크기");
		this.fontSize.setIcon(2);
		this.fontSize.setText(Config.getString(Config.SIZE_OF_FONT));
		this.fontSize.setLocation(130, 40);
		this.fontSize.setEvent(this);

		this.reset = new LootButton(this, "폰트 초기화", 5);
		this.reset.setEnabled(this.useFont.getSelected());
		this.reset.setLocation(130, 55);
		this.reset.setEvent(this);
	}

	private String[] getFonts(String fontKeyword)
	{
		LinkedList<String> fonts = new LinkedList<String>();

		for (Font f : GlyphCache.allFonts)
		{
			String fontName = f.getFontName(Locale.KOREAN);
			if (fontKeyword == null || fontName.indexOf(fontKeyword.trim()) != -1)
			{
				fonts.add(fontName);
			}
		}

		return (String[]) fonts.toArray(new String[fonts.size()]);
	}

	public void drawComponents(Gui gui, Rectangle bounds)
	{
		int h = getTabHeight() - 12;
		FontRenderer fr = getFontRenderer();

		this.fontList.setBounds(-2, -2, 120, h);
		this.search.setLocation(-2, -2 + h);
		this.fontList.draw();
		this.search.draw();
		this.useFont.draw();
		this.useAnti.draw();
		this.fontSize.draw();
		this.reset.draw();

		fr.drawString("§c(외국 폰트가 한글을 지원하지 않을 수 있습니다.)", getTabStartX() + 125, getTabStartY() + h, -1);
	}

	public String getName()
	{
		return "폰트 설정";
	}

	public int getIcon()
	{
		return 1;
	}

	public int getColor()
	{
		return -5609984;
	}

	public void onChange(int id, Object value)
	{
		FontRenderer fr = getFontRenderer();

		if (id == 1)
		{
			this.fontList.setValues(getFonts((String) value));
		}
		try
		{
			switch (id)
			{
				case 0:
					String sel = this.fontList.getSelectedString();
					if (sel != null)
					{
						Config.set(Config.FONT, this.fontList.getSelectedString());
						setFont(fr);
					}
					break;
				case 2:
					boolean usefont = (Boolean) value;
					Config.set(Config.USE_FONT, usefont);
					this.fontList.setEnabled(usefont);
					this.search.setEnabled(usefont);
					this.useAnti.setEnabled(usefont);
					this.fontSize.setEnabled(usefont);
					break;
				case 3:
					Config.set(Config.USE_FONT_ANTIALIASING, value);
					setFont(fr);
					break;
				case 4:
					int val = Integer.parseInt((String) value);
					if ((val >= 8) && (val <= 20))
					{
						Config.set(Config.SIZE_OF_FONT, val);
						setFont(fr);

						this.fontSize.setHintText(null);
						this.fontSize.setBackgroundColor(-1);
					}
					else
					{
						this.fontSize.setHintText("크기는 8~20까지만 입력 가능합니다.");
						this.fontSize.setBackgroundColor(0xFFFFAAAA);
					}
					break;
			}
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
		}
	}

	public void onAction(int id)
	{
		Config.resetConfig(Config.FONT);
		Config.resetConfig(Config.SIZE_OF_FONT);
		Config.resetConfig(Config.USE_FONT_ANTIALIASING);
		setFont(getFontRenderer());

		fontList.setSelected(-1);
		this.fontSize.setText(Config.getString(Config.SIZE_OF_FONT));
		this.useAnti.setSelected(false);
	}

	private void setFont(FontRenderer fr)
	{
		((FontRendererWrapper.PrivateMethodAccessor) fr).getStringCache().setDefaultFont(Config.getString(Config.FONT), Config.getInt(Config.SIZE_OF_FONT), Config.getBool(Config.USE_FONT_ANTIALIASING));
	}
}