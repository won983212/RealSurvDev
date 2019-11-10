package kp;

import kp.utils.Key;

public interface ConfigDataAccess
{
	public void set(int id, Object obj);

	public Object getObj(int id);

	public Key getKey(int id);

	public int getInt(int id);

	public boolean getBool(int id);

	public String getString(int id);
}
