package realsurv.tabletos.ui;

import java.awt.Rectangle;

import net.minecraft.client.gui.Gui;

public class UICheckbox extends UIObject {
	private boolean checked = false;
	private String label;
	
	public boolean isChecked() {
		return checked;
	}
	
	public UICheckbox setLabel(String label) {
		this.label = label;
		setMinimumSize(fontrenderer.getStringWidth(label) + 14, fontrenderer.FONT_HEIGHT);
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
		renderArcRect(0, 0, 9, 9, arc, backgroundColor);
		if(checked)
			renderArcRect(1, 1, 8, 8, arc, 0xff000000);
		fontrenderer.drawString(label, 12, 1, foregroundColor);
	}
}
