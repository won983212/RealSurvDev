package won983212.korpatch.wrapper;

import net.minecraft.client.gui.GuiTextField;
import won983212.korpatch.ui.IMEPopupViewerPane;

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

	@Override
	public int getAnchorCursor() {
		return textfield.getCursorPosition();
	}

	@Override
	public int getMovingCursor() {
		return textfield.getSelectionEnd();
	}

	@Override
	public void setAnchorCursor(int cursor) {
		int selection = textfield.getSelectionEnd();
		textfield.setCursorPosition(cursor);
		textfield.setSelectionPos(selection);
	}

	@Override
	public void setMovingCursor(int cursor) {
		textfield.setSelectionPos(cursor);
	}
	
	public void setIMEStatusBarLocation(IMEPopupViewerPane popup) {
		popup.setRelativeLocation(textfield.x, textfield.y + textfield.height + 3);
	}

	public boolean isFocused() {
		return textfield.isFocused();
	}
}
