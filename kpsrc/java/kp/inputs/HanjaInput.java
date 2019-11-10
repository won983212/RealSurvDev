package kp.inputs;

import org.lwjgl.input.Keyboard;

import kp.Config;
import kp.utils.HanjaUtils;
import kp.utils.HanjaUtils.Hanja;
import kp.utils.InputUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

public class HanjaInput implements InputHelper
{
	private boolean hanja = false;
	private int npage = 0;
	private int mpage = 0;
	private Hanja[] nchar = null;

	private int width = -1;
	private int height = -1;
	private int x;
	private int y;
	private static char lastUse = '\000';

	public boolean typed(Inputer in, char c, int n)
	{
		int cur = in.getCursorPos();

		if ((Config.getBool(Config.USE_FAST_HANJA)) && (isCtrlDown()) && (n == Config.getKey(Config.KEY_FAST_HANJA).getKeyCode()) && (lastUse != 0))
		{
			InputUtils.editText(in, 0, String.valueOf(lastUse));
			return true;
		}

		if ((n == Config.getKey(Config.KEY_HANJA).getKeyCode()) && !isHanja() && (in.getCursorPos() > 0))
		{
			this.hanja = true;

			if ((cur > 0) && (cur <= in.getTargetText().length()))
			{
				this.nchar = HanjaUtils.getList(in.getTargetText().charAt(in.getCursorPos() - 1));
			}

			if (this.nchar != null)
			{
				this.mpage = (this.nchar.length / 9);
			}
			this.npage = 0;
			return true;
		}

		if ((c >= '1') && (c <= '9') && (this.nchar != null) && isHanja())
		{
			int index = this.npage * 9 + (c - '0') - 1;
			if (index >= this.nchar.length)
			{
				return true;
			}
			lastUse = this.nchar[index].getHanja();
			InputUtils.editText(in, 1, String.valueOf(lastUse));
			this.hanja = false;
			return true;
		}

		if ((n == 203) && isHanja())
		{
			if (this.npage > 0) this.npage -= 1;
			return true;
		}

		if ((n == 205) && isHanja())
		{
			if (this.npage < this.mpage) this.npage += 1;
			return true;
		}

		this.hanja = false;
		return false;
	}

	private boolean isHanja()
	{
		return hanja && nchar != null && nchar.length > 0;
	}

	private boolean isCtrlDown()
	{
		return Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157);
	}

	public void draw(Inputer in, Gui gui, FontRenderer fr)
	{
		if ((this.hanja) && (this.nchar != null))
		{
			int sx = 2;
			int sy = 2;
			if ((this.width != -1) && (this.height != -1))
			{
				sx = this.x + fr.getStringWidth(in.getTargetText().substring(0, in.getCursorPos()));
				sy = this.y - 30 - 96;

				if (sx + 25 > this.width)
				{
					sx = this.width - 25;
				}
			}

			Gui.drawRect(sx, sy + 13, sx + 8, sy + 96 + 15, -576017750);
			Gui.drawRect(sx + 8, sy + 13, sx + 60, sy + 96 + 15, -570425345);

			for (int a = 0; a < 9; a++)
			{
				int idx = this.npage * 9 + a;
				if (idx >= this.nchar.length)
				{
					break;
				}

				String s = a + 1 + "   " + this.nchar[idx].getHanja() + " " + this.nchar[idx].getMean();
				fr.drawString(s, sx + 1, sy + 14 + 11 * a, -16777216);
			}

			gui.drawString(fr, this.npage + 1 + " / " + (this.mpage + 1), sx + 3, sy + 2, -1);
		}
	}

	public void setSize(int w, int h)
	{
		this.width = w;
		this.height = h;
	}

	public void setXY(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
}
