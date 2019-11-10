package kp.utils;

import kp.inputs.Inputer;

public class InputUtils
{
	public static boolean checkEnglish(String s)
	{
		for (char c : s.toCharArray())
		{
			if (((c >= 44032) && (c <= 55203)) || ((c >= 'ㄱ') && (c <= 'ㅎ')))
			{
				return false;
			}
		}
		return true;
	}

	public static void editText(Inputer in, int del, String add)
	{
		int prevLen = in.getTargetText().length();
		int cur = in.getCursorPos();
		String s1 = in.getTargetText().substring(0, cur - del);
		String s2 = in.getTargetText().substring(cur);

		in.setTargetText(s1 + add + s2);
		in.setCursorPos(in.getTargetText().length() - prevLen + cur);
	}
}
