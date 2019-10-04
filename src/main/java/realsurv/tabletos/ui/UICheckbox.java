package realsurv.tabletos.ui;

import java.awt.Font;
import java.awt.Rectangle;

import net.minecraft.client.gui.Gui;
import realsurv.font.TrueTypeFont;

public class UICheckbox extends UIObject {
	private boolean checked = false;
	private String label;
	
	public boolean isChecked() {
		return checked;
	}
	
	public UICheckbox setLabel(String label) {
		TrueTypeFont font = getFont();
		this.label = label;
		setMinimumSize(font.getStringWidth(label) + 14, Math.max(9, font.getMaxHeight()));
		return this;
	}
	
	public UICheckbox setChecked(boolean checked) {
		this.checked = checked;
		return this;
	}
	
	@Override
	public void onPress(int x, int y, int bt) {
		setChecked(!checked);
	}
	
	@Override
	public void render(int mouseX, int mouseY) {
		TrueTypeFont font = getFont();
		int h = Math.max(9, font.getMaxHeight());
		int y = (h - 9) / 2;
		int color = backgroundColor;
		if(containsRelative(mouseX, mouseY))
			color = offsetColor(color, 20);
		
		renderArcRect(0, y, 9, y + 9, arc, color, showShadow);
		if(checked)
			renderArcRect(1, y + 1, showShadow ? 7 : 8, y + (showShadow ? 7 : 8), arc, 0xff000000, false);
		getFont().drawString(label, 11, (h - font.getMaxHeight()) / 2 + 1, foregroundColor);
	}
}
