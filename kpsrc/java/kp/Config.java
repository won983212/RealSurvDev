package kp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

import org.lwjgl.input.Keyboard;

import kp.inputs.keyboard.KeyLoader;
import kp.utils.Key;
import kp.utils.Log;
import kp.wrapper.FontRendererWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class Config
{
	private static ConfigData defaultData = new ConfigData(null);
	private static ConfigData storedData = new ConfigData(null);
	private static ConfigData configData;
	public static File configfile = new File(Minecraft.getMinecraft().mcDataDir, "/KoreanPatch.cfg");

	// Integer Type
	public static final int SIZE_OF_FONT = 2;
	public static final int TYPE_KEYARRAY = 3;
	public static final int COLOR_CHAT_BACK = 4;
	public static final int COLOR_CHAT_CURSOR = 5;
	public static final int COLOR_THEME1 = 6;
	public static final int COLOR_THEME2 = 7;

	// Boolean Type
	public static final int USE_TARNSLATE = 8;
	public static final int SHOW_OPTION_KEY = 10;
	public static final int USE_COLOR_MODIFIER = 11;
	public static final int USE_FONT_ANTIALIASING = 12;
	public static final int SHOW_CHAT_LENGTH = 13;
	public static final int USE_PREFIX_OR_SUFFIX = 14;
	public static final int USE_FOLLOWING_INFO_PANEL = 15;
	public static final int USE_INFO_PANEL = 16;
	public static final int SHOW_EDITING_CURSOR = 17;
	public static final int PREFIX_ON_SLASH = 18;
	public static final int USE_FAST_HANJA = 19;
	public static final int DELETE_JASO_UNIT = 20;
	public static final int USE_FONT = 21;

	// String Type
	public static final int FONT = 23;
	public static final int PREFIX = 24;
	public static final int SUFFIX = 25;

	// Key Type
	public static final int KEY_KRENG = 26;
	public static final int KEY_HANJA = 27;
	public static final int KEY_OPTION = 28;
	public static final int KEY_COLOR = 29;
	public static final int KEY_FAST_HANJA = 30;

	public static void resetConfig()
	{
		if (isConfigLoaded())
		{
			configData.clear();
			configData.putAll(defaultData);
			setupAfterLoad();

			saveConfig();
		}
		else
		{
			Log.error("Config was not loaded.");
		}
	}

	public static void resetConfig(int id)
	{
		if (isConfigLoaded())
		{
			set(id, defaultData.get(id));
		}
	}

	public static boolean isConfigLoaded()
	{
		return configData != null;
	}

	private static void setupAfterLoad()
	{
		FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
		if (fr != null)
		{
			((FontRendererWrapper.PrivateMethodAccessor) fr).getStringCache().setDefaultFont(getString(FONT), getInt(SIZE_OF_FONT), getBool(USE_FONT_ANTIALIASING));
		}

		KeyLoader.getKeyLoader().setKeyboard(getInt(TYPE_KEYARRAY));
	}

	public static boolean loadConfig()
	{
		BufferedReader br = null;
		boolean ret = true;

		try
		{
			configData = new ConfigData(defaultData);
			br = new BufferedReader(new FileReader(configfile));

			String line = null;
			while ((line = br.readLine()) != null)
			{
				if (line.startsWith("#") || line.trim().isEmpty()) continue;

				String[] split1 = line.split("%", 2);
				int id = Integer.parseInt(split1[0]);
				Object value = split1[1].substring(2, split1[1].length());

				if (split1[1].startsWith("I:"))
				{
					value = Integer.parseInt((String) value);
				}
				else if (split1[1].startsWith("B:"))
				{
					value = value.equals("true");
				}
				else if (split1[1].startsWith("K:"))
				{
					value = new Key(id, Integer.parseInt((String) value));
				}

				configData.put(id, value);
			}

			if (configData.size() != defaultData.size())
			{
				Log.error("Configuration file is invaild. This file will be reset.");

				ret = false;
				resetConfig();
			}
			else
			{
				setupAfterLoad();
				Log.i("Configuration is loaded.");
			}
		}
		catch (FileNotFoundException e)
		{
			ret = false;
			Log.error("Can't find configuration file.");
		}
		catch (Exception e)
		{
			ret = false;
			e.printStackTrace();
			Log.error("Configuration file is invaild. This file will be reset.");
		}
		finally
		{
			try
			{
				if (br != null) br.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

			if (!ret)
			{
				resetConfig();
			}
		}

		return ret;
	}

	public static boolean saveConfig()
	{
		PrintWriter pw = null;
		boolean ret = true;

		try
		{
			pw = new PrintWriter(new FileWriter(configfile));
			Iterator<Entry<Integer, Object>> iter = configData.entrySet().iterator();

			while (iter.hasNext())
			{
				Entry<Integer, Object> obj = iter.next();
				int id = obj.getKey();
				Object value = obj.getValue();

				pw.print(id);
				pw.print('%');
				if (value instanceof Integer) pw.print("I:");
				else if (value instanceof Boolean) pw.print("B:");
				else if (value instanceof String) pw.print("S:");
				else if (value instanceof Key) pw.print("K:");

				if (iter.hasNext())
				{
					pw.println(value);
				}
				else
				{
					pw.print(value);
				}
			}

			Log.i("Configuration is saved.");
		}
		catch (IOException e)
		{
			e.printStackTrace();
			ret = false;
		}
		finally
		{
			if (pw != null) pw.close();
		}

		return ret;
	}

	/**
	 * This method isn't use structure of stack.
	 */
	public static void pushData()
	{
		if (isConfigLoaded())
		{
			storedData.putAll(configData);
		}
	}

	public static void popData()
	{
		if (isConfigLoaded() && storedData.size() == configData.size())
		{
			configData.clear();
			configData.putAll(storedData);
			setupAfterLoad();
		}
		else
		{
			Log.error("There is not stored data.");
		}

		storedData.clear();
	}

	public static ConfigDataAccess getDefaultData()
	{
		return defaultData;
	}

	public static void set(int id, Object obj)
	{
		if (isConfigLoaded())
		{
			configData.set(id, obj);
		}
	}

	public static Object get(int id)
	{
		if (isConfigLoaded())
		{
			return configData.getObj(id);
		}

		return null;
	}

	public static int getInt(int id)
	{
		if (isConfigLoaded())
		{
			return configData.getInt(id);
		}

		return 0;
	}

	public static boolean getBool(int id)
	{
		if (isConfigLoaded())
		{
			return configData.getBool(id);
		}

		return false;
	}

	public static String getString(int id)
	{
		if (isConfigLoaded())
		{
			return configData.getString(id);
		}

		return "";
	}

	public static Key getKey(int id)
	{
		if (isConfigLoaded())
		{
			return configData.getKey(id);
		}

		return new Key("NULL", 0);
	}

	public static Key[] getKeys()
	{
		LinkedList<Key> keys = new LinkedList<Key>();
		Iterator<Object> iter = configData.values().iterator();

		while (iter.hasNext())
		{
			Object obj = iter.next();
			if (obj instanceof Key)
			{
				keys.add((Key) obj);
			}
		}

		return keys.toArray(new Key[keys.size()]);
	}

	static
	{
		defaultData.put(SIZE_OF_FONT, 15);
		defaultData.put(TYPE_KEYARRAY, 0);
		defaultData.put(COLOR_CHAT_BACK, Integer.MIN_VALUE);
		defaultData.put(COLOR_CHAT_CURSOR, 0x40FFFFFF);
		defaultData.put(COLOR_THEME1, -14052157);
		defaultData.put(COLOR_THEME2, -12303292);

		defaultData.put(USE_TARNSLATE, true);
		defaultData.put(SHOW_OPTION_KEY, true);
		defaultData.put(SHOW_CHAT_LENGTH, true);
		defaultData.put(SHOW_EDITING_CURSOR, true);
		defaultData.put(USE_FONT_ANTIALIASING, false);
		defaultData.put(USE_PREFIX_OR_SUFFIX, false);
		defaultData.put(USE_FOLLOWING_INFO_PANEL, true);
		defaultData.put(USE_INFO_PANEL, true);
		defaultData.put(USE_FAST_HANJA, true);
		defaultData.put(PREFIX_ON_SLASH, true);
		defaultData.put(DELETE_JASO_UNIT, true);
		defaultData.put(USE_FONT, true);
		defaultData.put(USE_COLOR_MODIFIER, true);

		defaultData.put(FONT, "SansSerif");
		defaultData.put(PREFIX, "");
		defaultData.put(SUFFIX, "");

		defaultData.put(KEY_KRENG, new Key(KEY_KRENG, Keyboard.KEY_LCONTROL));
		defaultData.put(KEY_HANJA, new Key(KEY_HANJA, Keyboard.KEY_KANJI));
		defaultData.put(KEY_OPTION, new Key(KEY_OPTION, Keyboard.KEY_F5));
		defaultData.put(KEY_COLOR, new Key(KEY_COLOR, Keyboard.KEY_INSERT));
		defaultData.put(KEY_FAST_HANJA, new Key(KEY_FAST_HANJA, Keyboard.KEY_B));
	}
}
