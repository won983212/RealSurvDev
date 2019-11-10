package kp.utils;

import java.util.HashMap;

import org.lwjgl.input.Keyboard;

import kp.Config;

public class Key
{
	private static final HashMap<Integer, String> labels = new HashMap<Integer, String>();
	private String label;
	private int key;

	public Key(String label, int key)
	{
		this.label = label;
		this.key = key;
	}

	public Key(int confId, int key)
	{
		this(labels.get(confId), key);
	}

	public String getLabel()
	{
		return this.label;
	}

	public int getKeyCode()
	{
		return this.key;
	}

	public boolean isDown()
	{
		return Keyboard.isKeyDown(getKeyCode());
	}

	public void setKey(int key)
	{
		this.key = key;
	}

	public String toString()
	{
		return ((Integer) this.key).toString();
	}

	static
	{
		labels.put(Config.KEY_KRENG, "한/영 변환키");
		labels.put(Config.KEY_HANJA, "한자키");
		labels.put(Config.KEY_OPTION, "옵션키");
		labels.put(Config.KEY_COLOR, "컬러 픽커키");
		labels.put(Config.KEY_FAST_HANJA, "빠른 한자키 (Ctrl+)");
	}
}
