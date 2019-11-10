package kp.inputs;

import org.lwjgl.input.Keyboard;

import kp.Config;
import kp.inputs.keyboard.KeyLoader;
import kp.inputs.keyboard.KeyboardArray;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ChatAllowedCharacters;

public class KoreanInput implements InputHelper
{
	private static boolean kor = false;
	private static KoreanInput instance = null;

	private int h1 = -1;
	private int h2 = -1;
	private int h3 = 0;
	private boolean korEdit = true;
	private int maxLen = -1;

	private static enum Code
	{
		CHO, JUNG, JONG;
	}

	public static KoreanInput getSharedInstance()
	{
		if (instance == null)
		{
			instance = new KoreanInput();
		}
		return instance;
	}

	private KeyboardArray getKeyboard()
	{
		return KeyLoader.getKeyLoader().getKeyboard();
	}

	private char getT(char c)
	{
		if (("`1234567890-=\\qwertyuiop[]asdfghjkl;'zxcvbnm/~!@#$%^&*()_+|QWERTYUIOP{}ASDFGHJKL:\"ZXCVBNM<>?".indexOf(c) == -1) && (c >= 'A') && (c <= 'Z')) return getT((char) (c + ' '));
		return (c + getKeyboard().getKeyboardArray()).charAt("`1234567890-=\\qwertyuiop[]asdfghjkl;'zxcvbnm/~!@#$%^&*()_+|QWERTYUIOP{}ASDFGHJKL:\"ZXCVBNM<>?".indexOf(c) + 1);
	}

	private int getI(String c, Code m)
	{
		if (c == null)
		{
			return m == Code.JONG ? 0 : -1;
		}

		String[] map;
		switch (m)
		{
		case CHO:
			return getKeyboard().getChosung().indexOf(c);
		case JUNG:
			map = getKeyboard().getJungsung();
			break;
		case JONG:
			map = getKeyboard().getJongsung();
			break;
		default:
			return -1;
		}

		for (int a = 0; a < map.length; a++)
		{
			String str = map[a];
			if (str.equals(c)) return a;
			if (str.indexOf(' ') != -1)
			{
				for (String s : str.split(" "))
				{
					if (s.equals(c))
					{
						return a;
					}
				}
			}
		}

		return m == Code.JONG ? 0 : -1;
	}

	private String subWithlen(String s, int maxlen, int sx, int ex)
	{
		if (ex > maxlen)
		{
			return null;
		}
		return s.substring(sx, ex);
	}

	private void clearBuffer(StringBuilder buf)
	{
		if (this.h1 == -1)
		{
			return;
		}

		if (this.h2 == -1)
		{
			char t = getKeyboard().getChosung().charAt(this.h1);
			if (t == '*')
			{
				buf.append("ㄱㄲㄴㄷㄸㄹㅁㅂㅃㅅㅆㅇㅈㅉㅊㅋㅌㅍㅎ".charAt(this.h1));
			}
			else
			{
				buf.append(getT(t));
			}
			this.h1 = -1;
			return;
		}

		buf.append((char) (44032 + this.h1 * 21 * 28 + this.h2 * 28 + (this.h3 == -1 ? 0 : this.h3)));

		this.h1 = -1;
		this.h2 = -1;
		this.h3 = 0;
	}

	private boolean canInput(String text, char c)
	{
		if ((this.maxLen == -1) || (text.length() < this.maxLen))
		{
			return true;
		}

		if ((kor) && (text.length() == make(reassmEng(text) + c).length()))
		{
			return true;
		}

		return false;
	}

	public char applyKor(char c)
	{
		return kor ? getT(c) : c;
	}

	public boolean typed(Inputer in, char c, int i)
	{
		boolean isShifted = (Keyboard.isKeyDown(42)) || (Keyboard.isKeyDown(54));

		if (i == Config.getKey(Config.KEY_KRENG).getKeyCode())
		{
			kor = !kor;
			this.korEdit = false;
			return false;
		}

		if ((ChatAllowedCharacters.isAllowedCharacter(c) && (getJamoTier(c) == 0)) || (i == 14))
		{
			String text = in.getTargetText();
			String k = "";
			int cur = in.getCursorPos();
			int mc = text.length();

			if (kor)
			{
				if ((isShifted) && (getKeyboard().hasCapital(c)))
				{
					c = Character.toUpperCase(c);
				}
				else
				{
					c = Character.toLowerCase(c);
				}
			}

			if (cur != 0)
			{
				k = String.valueOf(text.charAt(cur - 1));
			}

			String a;
			if (i == 14)
			{
				if (cur > 0)
				{
					if ((Config.getBool(Config.DELETE_JASO_UNIT)) && (this.korEdit) && (isKorean(k.charAt(0))))
					{
						String d = reassmEng(k);
						a = make(d.substring(0, d.length() - 1));
					}
					else
					{
						a = "";
					}
				}
				else
				{
					return false;
				}
			}
			else
			{
				if (canInput(text, c))
				{
					if (kor)
					{
						if (this.korEdit)
						{
							if ((k.length() > 0) && ((isKorean(k.charAt(0))) || (getJamoTier(k.charAt(0)) > 0)))
							{
								a = make(reassmEng(k) + c);
							}
							else a = k + getT(c);
						}
						else a = k + getT(c);
					}
					else a = k + c;
				}
				else a = k;
			}

			k = cur == 0 ? "" : text.substring(0, cur - 1);
			in.setTargetText(k + a + text.substring(cur));
			in.setCursorPos(cur + in.getTargetText().length() - mc);

			if (a.length() > 0)
			{
				c = a.charAt(a.length() - 1);
			}
			else
			{
				c = '*';
			}

			this.korEdit = ((in.getTargetText().length() < mc) || ((getJamoTier(c) == 0) && (!isKorean(c))) ? false : kor);
			return true;
		}

		return false;
	}

	private String reassmEng(String s)
	{
		StringBuilder sb = new StringBuilder();

		for (char c : s.toCharArray())
		{
			if (isKorean(c))
			{
				int cd = c - 44032;

				appendLetter(sb, cd / 588);
				appendJunsung(sb, cd / 28 % 21);
				sb.append(getKeyboard().getJongsung()[(cd % 28)]);
			}
			else if (getJamoTier(c) == 1)
			{
				appendLetter(sb, getIndexK(c, Code.CHO));
			}
			else if (getJamoTier(c) == 2)
			{
				appendJunsung(sb, c - 'ㅏ');
			}
			else
			{
				sb.append(c);
			}
		}

		return sb.toString();
	}

	private void appendJunsung(StringBuilder sb, int idx)
	{
		String append = getKeyboard().getJungsung()[idx];

		if (append.indexOf(' ') >= 0)
		{
			sb.append(append.split(" ")[0]);
		}
		else
		{
			sb.append(append);
		}
	}

	private void appendLetter(StringBuilder sb, int idx)
	{
		String letter = getKeyboard().getLetter("ㄱㄲㄴㄷㄸㄹㅁㅂㅃㅅㅆㅇㅈㅉㅊㅋㅌㅍㅎ".charAt(idx));

		if (letter != null)
		{
			sb.append(letter);
		}
		else
		{
			sb.append(getKeyboard().getChosung().charAt(idx));
		}
	}

	private int getIndexK(char c, Code code)
	{
		char[] delMap = null;
		int idx = c - 'ㄱ';

		switch (code)
		{
		case CHO:
			delMap = "ㄳㄵㄶㄺㄻㄼㄽㄾㄿㅀㅄ".toCharArray();
			break;
		case JUNG:
			return c - 'ㅏ';
		case JONG:
			delMap = "ㄸㅃㅉ".toCharArray();
		}

		for (int a = 0; (a < delMap.length) && (delMap[a] < c); a++)
		{
			idx--;
		}
		return idx;
	}

	public void draw(Inputer in, FontRenderer fr, Gui gui, int x, int y, int offsetLine)
	{
		String text = in.getTargetText();
		int cur = in.getCursorPos();

		if ((this.korEdit) && (cur > 0) && (Config.getBool(Config.SHOW_EDITING_CURSOR)))
		{
			char ec = text.charAt(cur - 1);
			if ((isKorean(ec)) || (getJamoTier(ec) > 0))
			{
				int size = fr.getStringWidth(String.valueOf(text.charAt(cur - 1)));
				int offsetX = fr.getStringWidth(text.substring(offsetLine, cur - 1));

				Gui.drawRect(x + offsetX, y, x + offsetX + size, y + fr.FONT_HEIGHT, Config.getInt(Config.COLOR_CHAT_CURSOR));
			}
		}
	}

	public String make(String text)
	{
		StringBuilder buf = new StringBuilder();
		int len = text.length();

		for (int idx = 0; idx < len; idx++)
		{
			char c = text.charAt(idx);
			String is = String.valueOf(c);

			if (getT(c) == c)
			{
				clearBuffer(buf);
				buf.append(c);
			}
			else if (this.h1 == -1)
			{
				Character temp_letter = getKeyboard().getLetter(subWithlen(text, len, idx, idx + 2));
				if (temp_letter != null)
				{
					this.h1 = getIndexK(temp_letter.charValue(), Code.CHO);
					idx++;
				}
				else
				{
					this.h1 = getI(is, Code.CHO);
				}

				if (this.h1 == -1)
				{
					buf.append(getT(c));
				}
			}
			else if (this.h2 == -1)
			{
				this.h2 = getI(subWithlen(text, len, idx, idx + 2), Code.JUNG);
				if (this.h2 != -1)
				{
					idx++;
				}
				else
				{
					this.h2 = getI(is, Code.JUNG);
					if (this.h2 == -1)
					{

						clearBuffer(buf);
						idx--;
					}
				}
			}
			else
			{
				if (this.h3 == 0)
				{
					Character temp_letter = getKeyboard().getLetter(subWithlen(text, len, idx, idx + 2));
					if (temp_letter != null)
					{
						this.h3 = (getIndexK(temp_letter.charValue(), Code.JONG) + 1);
						idx++;
						clearBuffer(buf);
						continue;
					}
					this.h3 = getI(subWithlen(text, len, idx, idx + 2), Code.JONG);
					if (this.h3 != 0)
					{
						if (getI(subWithlen(text, len, idx + 2, idx + 3), Code.JUNG) != -1)
						{
							this.h3 = getI(is, Code.JONG);
							clearBuffer(buf);
							continue;
						}
						idx++;
						clearBuffer(buf);
						continue;
					}
					this.h3 = getI(is, Code.JONG);
					if (getI(subWithlen(text, len, idx + 1, idx + 2), Code.JUNG) != -1)
					{
						this.h3 = 0;
						idx--;
					}
					else if (this.h3 == 0)
					{
						this.h3 = 0;
						clearBuffer(buf);
						buf.append(getT(c));
						continue;
					}
				}

				clearBuffer(buf);
			}
		}
		clearBuffer(buf);
		return buf.toString();
	}

	public void cancelEdit()
	{
		this.korEdit = false;
	}

	public void startEdit()
	{
		this.korEdit = true;
	}

	public void setMaxlen(int max)
	{
		this.maxLen = max;
	}

	public static boolean isKorean(char c)
	{
		return (c >= 44032) && (c <= 55203);
	}

	public static boolean isKor()
	{
		return kor;
	}

	public static int getJamoTier(char c)
	{
		if ((c >= 'ㄱ') && (c <= 'ㅎ'))
		{
			return 1;
		}
		if ((c >= 'ㅏ') && (c <= 'ㅣ'))
		{
			return 2;
		}
		return 0;
	}
}
