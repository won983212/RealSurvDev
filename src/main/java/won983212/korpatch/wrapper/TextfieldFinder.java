package won983212.korpatch.wrapper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class TextfieldFinder {
	private static HashMap<Class, Field[]> fieldsCache = new HashMap<>();

	public static List<GuiTextfieldWrapper> getTextfieldWrappers(GuiScreen screen) {
		try {
			Class screenCls = screen.getClass();
			Field[] textfields = fieldsCache.get(screenCls);
			if (textfields == null) {
				ArrayList<Field> fieldList = new ArrayList<>();
				for (Field field : screenCls.getDeclaredFields()) {
					if (field.getType() == GuiTextField.class) {
						field.setAccessible(true);
						fieldList.add(field);
					}
				}
				if (fieldList.size() > 0) {
					textfields = fieldList.toArray(new Field[fieldList.size()]);
					fieldsCache.put(screenCls, textfields);
				}
			}
			if (textfields != null) {
				ArrayList<GuiTextfieldWrapper> wrappers = new ArrayList<>();
				for (Field field : textfields) {
					Object obj = field.get(screen);
					if (obj != null && obj instanceof GuiTextField) {
						wrappers.add(new GuiTextfieldWrapper((GuiTextField) obj));
					}
				}
				return wrappers;
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
}
