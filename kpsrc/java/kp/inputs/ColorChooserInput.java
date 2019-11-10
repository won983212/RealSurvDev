package kp.inputs;

import kp.Config;
import kp.utils.InputUtils;
import kp.wrapper.FontRendererWrapper;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

public class ColorChooserInput implements InputHelper
{
	private static final String COLOR_MAP = "0123456789abcdeflmnor";
	private char colorstart = '&';
	private boolean ismenu;
	private boolean useFollowing = true;
	private int xp = 2;
	private int yp = 2;
	private int width = -1;

	public boolean typed(Inputer in, char c, int n)
	{
		if (this.ismenu)
		{
			if (COLOR_MAP.indexOf(c) > 0)
			{
				InputUtils.editText(in, 0, this.colorstart + "" + c);
				in.setCursorPos(in.getCursorPos());
				return true;
			}
		}

		if (n == Config.getKey(Config.KEY_COLOR).getKeyCode())
		{
			this.ismenu = true;
		}

		return false;
	}

	private boolean isColorCode(int c)
	{
		return ((c >= 48) && (c <= 57)) || ((c >= 97) && (c <= 102));
	}

	public void draw(Inputer in, Gui gui, FontRenderer fontRenderer)
	{
		if (!Config.getKey(Config.KEY_COLOR).isDown())
		{
			this.ismenu = false;
		}

		if (this.ismenu)
		{
			int sx = this.xp + ((in.getCursorPos() == 0) || (!this.useFollowing) ? 0 : fontRenderer.getStringWidth(in.getTargetText().substring(0, in.getCursorPos())));
			int sy = this.yp < 39 ? this.yp : this.yp - 43;

			if ((this.width > 0) && (sx + 51 > this.width - 2))
			{
				sx = this.width - 2 - 51;
			}

			Gui.drawRect(sx, sy, sx + 51, sy + 41, -1442840576);

			int i = 0;
			for (int b = 0; b < 4; b++)
			{
				for (int a = 0; a < 5; a++)
				{
					char ch = COLOR_MAP.charAt(i);
					int code = ((FontRendererWrapper.PrivateMethodAccessor) fontRenderer).getStringCache().getColorCodes()[Math.min(i, 15)] | 0xff000000;

					int len = fontRenderer.getCharWidth(ch);
					int mulx = sx + 10 * a + 1;
					int muly = sy + 10 * b + 1;

					Gui.drawRect(mulx, muly, mulx + 9, muly + 9, code);
					fontRenderer.drawString((isColorCode(ch) ? "" : "§" + ch) + ch, mulx + (9 - len) / 2, muly, code < -16733696 ? -1 : -16777216);

					i++;
				}
			}
		}
	}

	public void setXY(int x, int y)
	{
		this.xp = x;
		this.yp = y;
	}

	public void setUseFollowing(boolean b)
	{
		this.useFollowing = b;
	}

	public void setWidth(int w)
	{
		this.width = w;
	}

	public void setPrefix(char c)
	{
		this.colorstart = c;
	}
}
