package kp.inputs.keyboard;

public abstract class KeyboardArray
{
	public static final String MAP_EN = "`1234567890-=\\qwertyuiop[]asdfghjkl;'zxcvbnm/~!@#$%^&*()_+|QWERTYUIOP{}ASDFGHJKL:\"ZXCVBNM<>?";

	/**
	 * @param c
	 *            is must be a~z (english)
	 */
	public boolean hasCapital(char c)
	{
		String keyboard = getKeyboardArray();
		int center = keyboard.length() / 2 - 1;

		c = Character.toLowerCase(c);
		if ((c >= 'a') && (c <= 'z'))
		{
			int idx = MAP_EN.indexOf(c);
			if (keyboard.charAt(idx) != keyboard.charAt(idx + center))
			{
				return true;
			}
		}

		return false;
	}

	public Character getLetter(String s)
	{
		int index = -1;

		if (s != null)
		{
			index = getLetter().indexOf(s);
		}

		if (index != -1)
		{
			return Character.valueOf(getLetter().charAt(index - 1));
		}

		return null;
	}

	public String getLetter(char c)
	{
		int index = getLetter().indexOf(c);

		if (index != -1)
		{
			return getLetter().substring(index + 1, index + 3);
		}

		return null;
	}

	public abstract String getKeyboardArray();

	public abstract String getChosung();

	public abstract String[] getJungsung();

	public abstract String[] getJongsung();

	public abstract String getLetter();

	public abstract String keyboardName();
}
