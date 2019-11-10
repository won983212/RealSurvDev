package kp.utils;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class ChattingFilter
{
	public static TextComponentTranslation processFiltering(TextComponentTranslation comp)
	{
		if (!comp.getKey().equals("chat.type.text"))
		{
			return comp;
		}

		ArrayList<Object> list = new ArrayList<Object>();
		Iterator<ITextComponent> iter = comp.iterator();
		while (iter.hasNext())
		{
			ITextComponent obj = iter.next();
			/*if (!iter.hasNext() && obj instanceof TextComponentString)
			{
				String filtered = filtering(((TextComponentString) obj).getText());
				list.add(new TextComponentString(filtered));
			}
			else*/
			{
				list.add(obj);
			}
		}
		
		TextComponentTranslation str = new TextComponentTranslation("chat.type.text", list.toArray(new Object[list.size()]));
		str.setStyle(comp.getStyle().createShallowCopy());

		return str;
	}

	private static TextComponentString createCopy(TextComponentString comp, String text)
	{
		TextComponentString textcomponentstring = new TextComponentString(text);
		textcomponentstring.setStyle(comp.getStyle().createShallowCopy());

		for (ITextComponent itextcomponent : comp.getSiblings())
		{
			textcomponentstring.appendSibling(itextcomponent.createCopy());
		}

		return textcomponentstring;
	}
}
