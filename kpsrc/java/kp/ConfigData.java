package kp;

import java.util.HashMap;

import kp.utils.Key;
import kp.utils.Log;

class ConfigData extends HashMap<Integer, Object> implements ConfigDataAccess
{
	private HashMap<Integer, Object> dataMap;

	public ConfigData(HashMap<Integer, Object> defaultMap)
	{
		this.dataMap = defaultMap;
	}

	public void set(int id, Object obj)
	{
		if (replace(id, obj) == null)
		{
			Log.error(id + " isn't registered for configData");
		}
	}

	public Object getObj(int id)
	{
		Object obj = get(id);

		if (obj == null)
		{
			if (dataMap != null)
			{
				obj = dataMap.get(id);
			}

			Log.error("Can't find option: " + id + ". Use default option => " + obj);
		}

		return obj;
	}

	public Key getKey(int id)
	{
		Object k = getObj(id);

		if (k == null)
		{
			return new Key("NULL", 0);
		}

		return (Key) k;
	}

	public int getInt(int id)
	{
		Object obj = getObj(id);

		if (obj == null)
		{
			return 0;
		}
		if (obj instanceof String)
		{
			return Integer.parseInt((String) obj);
		}

		return (Integer) getObj(id);
	}

	public boolean getBool(int id)
	{
		Object obj = getObj(id);

		if (obj == null)
		{
			return false;
		}

		if (obj instanceof String)
		{
			return obj.equals("true");
		}

		return (Boolean) obj;
	}

	public String getString(int id)
	{
		Object obj = getObj(id);

		if (obj == null)
		{
			return "";
		}

		return obj.toString();
	}
}