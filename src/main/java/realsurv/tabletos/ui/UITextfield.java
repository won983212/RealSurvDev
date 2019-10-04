package realsurv.tabletos.ui;

import java.awt.Dimension;
import java.awt.Rectangle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import realsurv.tabletos.DirWeights;
import realsurv.tabletos.GuiCompatibleTextfield;

public class UITextfield extends UIObject {
	private GuiCompatibleTextfield textfield = new GuiCompatibleTextfield(0, getFont().makeCompatibleFont());
	private String hint = null;
	private int hintTextColor = 0xff999999;
	
	public UITextfield() {
		setPadding(new DirWeights(3));
		setMinimumSize(10, getFont().getMaxHeight());
		textfield.setEnableBackgroundDrawing(false);
	}
	
	@Override
	public UIObject setFont(String family, int size) {
		super.setFont(family, size);
		textfield = new GuiCompatibleTextfield(0, getFont().makeCompatibleFont());
		return this;
	}
	
	public String getText() {
		return textfield.getText();
	}

	@Override
	public void onGotFocus() {
		super.onGotFocus();
		textfield.setFocused(true);
	}
	
	@Override
	public void onKeyTyped(int i, char c) {
		textfield.textboxKeyTyped(c, i);
	}
	
	@Override
	public void onLostFocus() {
		super.onLostFocus();
		textfield.setFocused(false);
		Minecraft.getMinecraft().currentScreen.setFocused(true);
	}
	
	@Override
	public void onPress(int x, int y, int bt) {
		textfield.mouseClicked(x, y, bt);
	}
	
	@Override
	public void render(int mouseX, int mouseY) {
		Rectangle bounds = getInnerBounds();
		Rectangle actBounds = getPadding().getContentRect(bounds);
		int color = backgroundColor;
		
		if(containsRelative(mouseX, mouseY))
			color = offsetColor(color, 20);
		
		if(isFocusd()) {
			int offset = 0;
			if(showShadow) {
				renderArcRect(1, 1, bounds.width, bounds.height, arc, 0xff999999, false);
				offset++;
			}
			renderArcRect(0, 0, bounds.width - offset, bounds.height - offset, arc, 0xff99ccff, false);
			renderArcRect(1, 1, bounds.width - offset - 1, bounds.height - offset - 1, arc, color, false);
		} else {
			renderArcRect(0, 0, bounds.width, bounds.height, arc, color, showShadow);
		}
		if(hint != null && textfield.getText().length() == 0)
			getFont().drawString(hint, actBounds.x, actBounds.y, hintTextColor);
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
		textfield.setText(text);
		return this;
	}
}
