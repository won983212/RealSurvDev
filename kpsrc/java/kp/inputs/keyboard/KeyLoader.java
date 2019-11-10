package kp.inputs.keyboard;

import java.util.ArrayList;

public class KeyLoader
{
	private static KeyLoader instance = null;
	private final ArrayList<KeyboardArray> keyboards = new ArrayList<KeyboardArray>();
	private int currentKeyboard = 0;

	public static KeyLoader getKeyLoader()
	{
		if (instance == null)
		{
			instance = new KeyLoader();
		}
		return instance;
	}

	public KeyLoader()
	{
		loadKeyboards();
	}

	public void loadKeyboards()
	{
		this.keyboards.clear();
		this.keyboards.add(new KeyboardQwerty());
		this.keyboards.add(new KeyboardThird390());
		this.keyboards.add(new KeyboardThird391());
	}

	public KeyboardArray getKeyboard()
	{
		return (KeyboardArray) this.keyboards.get(this.currentKeyboard);
	}

	public String[] getKeyboards()
	{
		String[] names = new String[this.keyboards.size()];

		for (int a = 0; a < names.length; a++)
		{
			names[a] = ((KeyboardArray) this.keyboards.get(a)).keyboardName();
		}

		return names;
	}

	public String getKeyboardsToString()
	{
		StringBuilder sb = new StringBuilder();

		int i = 0;
		String[] keyboards = getKeyboards();
		for (String str : keyboards)
		{
			sb.append(str);
			if (i++ < keyboards.length)
			{
				sb.append(",");
			}
		}

		return sb.toString();
	}

	public void setKeyboard(int keyboardIndex)
	{
		if (this.currentKeyboard != keyboardIndex)
		{
			if (keyboardIndex >= 0 && keyboardIndex < this.keyboards.size())
			{
				this.currentKeyboard = keyboardIndex;
				kp.utils.Log.i("Set keyboard to " + ((KeyboardArray) this.keyboards.get(keyboardIndex)).keyboardName());
			}
			else
			{
				kp.utils.Log.error("Keyboard index " + keyboardIndex + " is out of array!");
			}
		}
	}
}
