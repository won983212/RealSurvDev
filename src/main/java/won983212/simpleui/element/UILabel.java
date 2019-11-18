package won983212.simpleui.element;

import net.minecraft.client.gui.Gui;
import won983212.simpleui.font.TrueTypeFont;
import won983212.simpleui.parentelement.UIObject;

public class UILabel extends UIObject {
	private String label;
	
	public UILabel() {
		setShadowVisible(false);
	}

	public UILabel setLabel(String label) {
		TrueTypeFont font = getFont();
		this.label = label;
		setMinimumSize(font.getStringWidth(label), font.getStringHeight(label));
		return this;
	}
	
	@Override
	public void render(int mx, int my) {
		getFont().drawString(label, 0, 0, foregroundColor, showShadow);
	}
}
