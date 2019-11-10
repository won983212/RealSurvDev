package won983212.korpatch.wrapper;

import net.minecraft.client.gui.GuiTextField;

public class GuiTextfieldWrapper implements IInputWrapper {
	private GuiTextField textfield;
	
	public GuiTextfieldWrapper(GuiTextField textfield) {
		this.textfield = textfield;
	}

	@Override
	public void setText(String text) {
		textfield.setText(text);
	}
	
	@Override
	public String getText() {
		return textfield.getText();
	}
}
