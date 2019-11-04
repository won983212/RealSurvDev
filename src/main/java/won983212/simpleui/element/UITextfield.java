package won983212.simpleui.element;

import java.awt.Rectangle;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ChatAllowedCharacters;
import won983212.simpleui.DirWeights;
import won983212.simpleui.UIObject;
import won983212.simpleui.font.TrueTypeFont;

//TODO Implement it without Guitextfield.
public class UITextfield extends UIObject {
	private static final int LineOffsetAmount = 5;
	
	private String hint = null;
	private String text = "";
	private int cursorStart = 0;
	private int cursorEnd = 0;
	private int lineOffset = 0;
	private int hintTextColor = 0xff999999;

	public UITextfield() {
		setPadding(new DirWeights(3));
		setMinimumSize(10, getFont().getMaxHeight());
	}

	@Override
	public void onKeyTyped(int i, char c) {
		if(!isFocusd()) {
			return;
		}
		if(GuiScreen.isShiftKeyDown()) {
			if(i == Keyboard.KEY_LEFT){
				if(cursorEnd > 0)
					cursorEnd--;
			} else if(i == Keyboard.KEY_RIGHT) {
				if(cursorEnd < text.length())
					cursorEnd++;
			}
		} else if(GuiScreen.isCtrlKeyDown()) {
			if(i == Keyboard.KEY_A) { //TODO bug
				cursorStart = 0;
				cursorEnd = text.length();
			}
		} else if (i == Keyboard.KEY_BACK) {
			remove(1);
		} else if (i == Keyboard.KEY_LEFT) {
			if (cursorStart != cursorEnd) {
				cursorEnd = cursorStart;
			} else if (cursorStart > 0) {
				cursorStart = cursorEnd = cursorStart - 1;
			}
		} else if (i == Keyboard.KEY_RIGHT) {
			if (cursorStart != cursorEnd) {
				cursorEnd = cursorStart;
			} else if (cursorStart < text.length()) {
				cursorStart = cursorEnd = cursorStart + 1;
			}
		} else if(ChatAllowedCharacters.isAllowedCharacter(c)){
			write(c);
		}
		
		Rectangle actBounds = getPadding().getContentRect(getInnerBounds());
		String str = getFont().trimStringToWidth(text.substring(lineOffset), actBounds.width, false);
		if(cursorEnd < lineOffset) {
			lineOffset = Math.max(0, cursorEnd - 2);
		}
		if(cursorEnd > lineOffset + str.length()) {
			lineOffset = Math.max(0, cursorEnd - str.length());
		}
	}

	@Override
	public void render(int mouseX, int mouseY) {
		Rectangle bounds = getInnerBounds();
		Rectangle actBounds = getPadding().getContentRect(bounds);
		TrueTypeFont font = getFont();
		int color = backgroundColor;

		if (containsRelative(mouseX, mouseY))
			color = getMouseOverColor(color);

		if (isFocusd()) {
			int offset = 0;
			if (showShadow) {
				renderArcRect(1, 1, bounds.width, bounds.height, arc, 0xff999999, false);
				offset++;
			}
			renderArcRect(0, 0, bounds.width - offset, bounds.height - offset, arc, 0xff99ccff, false);
			renderArcRect(1, 1, bounds.width - offset - 1, bounds.height - offset - 1, arc, color, false);
		} else {
			renderArcRect(0, 0, bounds.width, bounds.height, arc, color, showShadow);
		}

		if (text.length() > 0) {
			String str = font.trimStringToWidth(text.substring(lineOffset), actBounds.width, false);
			font.drawString(str, actBounds.x, actBounds.y, foregroundColor);
		} else if (hint != null) {
			font.drawString(hint, actBounds.x, actBounds.y, hintTextColor);
		}
		drawSelectionBox(font);
	}
	
	private void remove(int amount) {
		int newcw = Math.max(0, Math.min(cursorStart, cursorEnd) - (cursorStart != cursorEnd ? 0 : amount));
		String s1 = text.substring(0, newcw);
		String s2 = text.substring(cursorEnd);
		text = s1 + s2;
		cursorStart = cursorEnd = newcw;
	}
	
	private void write(char c) {
		int newcw = Math.min(cursorStart, cursorEnd);
		String s1 = text.substring(0, newcw);
		String s2 = text.substring(Math.max(cursorStart, cursorEnd));
		text = s1 + c + s2;
		cursorStart = cursorEnd = newcw + 1;
	}

	private void drawSelectionBox(TrueTypeFont font) {
		Rectangle actBounds = getPadding().getContentRect(getInnerBounds());
		int start = Math.min(cursorStart, cursorEnd);
		int end = Math.max(cursorStart, cursorEnd);
		
		if(lineOffset > start && lineOffset > end)
			return;
		
		int startX = actBounds.x + font.getStringWidth(text.substring(lineOffset, Math.max(lineOffset, start)));
		int startY = actBounds.y;
		int endX = actBounds.x + font.getStringWidth(text.substring(lineOffset, Math.max(lineOffset, end)));
		int endY = actBounds.y + actBounds.height;
		int w = actBounds.x + actBounds.width;

		if (text.length() == 0)
			return;
		if (endX > w)
			endX = w;
		if (startX < 0)
			startX = 0;
		if (startX == endX)
			endX += 1;
		
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		GlStateManager.color(0, 0, 0, 255);
		GlStateManager.disableTexture2D();
		GlStateManager.enableColorLogic();
		GlStateManager.colorLogicOp(GlStateManager.LogicOp.OR_REVERSE);
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
		bufferbuilder.pos(startX, endY, 0.0D).endVertex();
		bufferbuilder.pos(endX, endY, 0.0D).endVertex();
		bufferbuilder.pos(endX, startY, 0.0D).endVertex();
		bufferbuilder.pos(startX, startY, 0.0D).endVertex();
		tessellator.draw();
		GlStateManager.disableColorLogic();
		GlStateManager.enableTexture2D();
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
		this.text = text;
		return this;
	}

	public String getText() {
		return text;
	}
}
