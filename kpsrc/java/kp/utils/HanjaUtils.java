package kp.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;

public class HanjaUtils
{
	private static HashMap<Character, Hanja[]> hanmap = new HashMap<Character, Hanja[]>();

	public static class Hanja
	{
		private char hanja;
		private String mean;

		public Hanja(char c, String m)
		{
			this.hanja = c;
			this.mean = m;
		}

		public char getHanja()
		{
			return this.hanja;
		}

		public String getMean()
		{
			return this.mean;
		}
	}

	public static Hanja[] cacheHanja(char c)
	{
		Hanja[] hanjas = (Hanja[]) hanmap.get(Character.valueOf(c));
		
		if (hanjas != null)
		{
			return hanjas;
		}
		
		try
		{
			InputStream is = HanjaUtils.class.getResourceAsStream("/hanja");
			if (is == null)
			{
				Log.error("Error in loading Hanja");
				return hanjas;
			}

			InputStreamReader isr = new InputStreamReader(is, "UTF-8");
			BufferedReader br = new BufferedReader(isr);

			boolean found = false;
			LinkedList<Hanja> hanlist = new LinkedList<Hanja>();
			String l;
			while ((l = br.readLine()) != null)
			{
				if (l.startsWith("#"))
				{
					if (found)
					{
						hanjas = (Hanja[]) hanlist.toArray(new Hanja[hanlist.size()]);
						hanmap.put(Character.valueOf(c), hanjas);
						break;
					}

					found = l.charAt(1) == c;
				}
				else if (found)
				{
					hanlist.add(new Hanja(l.charAt(0), l.substring(1)));
				}
			}

			is.close();
			isr.close();
			br.close();
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return hanjas;
	}

	public static Hanja[] getList(char key)
	{
		long l = System.nanoTime();
		Hanja[] ret = cacheHanja(key);
		long l2 = System.nanoTime();
		Log.i("GetHanjaList # " + (l2 - l) / 1000000.0D + "ms");

		return ret;
	}
}
