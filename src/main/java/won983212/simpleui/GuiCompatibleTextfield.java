package won983212.simpleui;

import java.awt.Rectangle;

import net.minecraft.client.gui.GuiTextField;
import won983212.simpleui.font.AdaptiveTTF;

public class GuiCompatibleTextfield extends GuiTextField {
	public GuiCompatibleTextfield(int componentId, AdaptiveTTF font) {
		super(componentId, font.setDisableShadow(), 0, 0, 10, 10);
	}
	
	public void drawAtBounds(Rectangle bounds) {
		this.x = bounds.x;
		this.y = bounds.y;
		this.width = bounds.width;
		this.height = bounds.height;
		drawTextBox();
	}
}
