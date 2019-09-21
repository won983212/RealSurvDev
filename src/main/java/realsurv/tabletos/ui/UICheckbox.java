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
		setMinimumSize(fontrenderer.getStringWidth(label) + 10, fontrenderer.FONT_HEIGHT);
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
		Rectangle bounds = getActualBounds();
		Gui.drawRect(bounds.x, bounds.y, bounds.x + 9, bounds.y + 9, 0xffffffff);
		if(checked)
			Gui.drawRect(bounds.x + 1, bounds.y + 1, bounds.x + 8, bounds.y + 8, 0xff000000);
		fontrenderer.drawString(label, bounds.x + 10, bounds.y, foregroundColor);
	}
}
