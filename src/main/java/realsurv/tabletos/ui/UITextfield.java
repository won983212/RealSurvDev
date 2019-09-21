package realsurv.tabletos.ui;

import java.awt.Rectangle;

import net.minecraft.client.gui.Gui;
import realsurv.tabletos.DirWeights;
import realsurv.tabletos.GuiCompatibleTextfield;

public class UITextfield extends UIObject {
	private GuiCompatibleTextfield textfield = new GuiCompatibleTextfield(0);
	private String hint = null;
	private int hintTextColor = 0xff999999;
	
	private boolean hideText = false;
	private String hiddenText = "";
	
	public UITextfield() {
		setPadding(new DirWeights(3));
		setMinimumSize(10, fontrenderer.FONT_HEIGHT);
		textfield.setEnableBackgroundDrawing(false);
	}
	
	public String getText() {
		if(hideText)
			return hiddenText;
		return textfield.getText();
	}
	
	private String makeStarString(int num) {
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<num;i++)
			sb.append('*');
		return sb.toString();
	}
	
	@Override
	public void onGotFocus() {
		textfield.setFocused(true);
	}
	
	@Override
	public void onKeyTyped(int i, char c) {
		textfield.textboxKeyTyped(c, i);
		if(hideText)
			setText(textfield.getText());
	}
	
	@Override
	public void onLostFocus() {
		textfield.setFocused(false);
	}
	
	@Override
	public void onPress(int x, int y, int bt) {
		textfield.mouseClicked(x, y, bt);
	}
	
	@Override
	public void render(int mouseX, int mouseY) {
		Rectangle bounds = getActualBounds();
		Rectangle actBounds = getPadding().getContentRect(bounds);
		
		Gui.drawRect(bounds.x, bounds.y, bounds.x + bounds.width, bounds.y + bounds.height, backgroundColor);
		if(hint != null && textfield.getText().length() == 0)
			fontrenderer.drawString(hint, actBounds.x, actBounds.y, hintTextColor);
		textfield.setTextColor(foregroundColor);
		textfield.drawAtBounds(actBounds);
	}
	
	public UITextfield setHint(String hint) {
		this.hint = hint;
		return this;
	}
	
	public UITextfield setHintTextColor(int color) {
		this.hintTextColor = color;
		return this;
	}
	
	public UITextfield setText(String text) {
		if(hideText) {
			hiddenText = text;
			textfield.setText(makeStarString(hiddenText.length()));
		} else {
			textfield.setText(text);
		}
		return this;
	}
	
	public UITextfield setHiddenText(boolean hiddenText) {
		if(this.hideText != hiddenText) {
			this.hideText = hiddenText;
			if(hiddenText) {
				setText(textfield.getText());
			} else {
				setText(this.hiddenText);
			}
		}
		return this;
	}
}
