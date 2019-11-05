package won983212.simpleui.element;

import java.awt.Rectangle;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.math.MathHelper;
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
		if(i == Keyboard.KEY_LEFT){
			if(GuiScreen.isShiftKeyDown()) {
				if(cursorEnd > 0)
					cursorEnd--;
			} else if (cursorStart != cursorEnd) {
				cursorEnd = cursorStart;
			} else if (cursorStart > 0) {
				cursorStart = cursorEnd = cursorStart - 1;
			}
		} else if(i == Keyboard.KEY_RIGHT) {
			if(GuiScreen.isShiftKeyDown()) {
				if(cursorEnd < text.length())
					cursorEnd++;
			} else if (cursorStart != cursorEnd) {
				cursorEnd = cursorStart;
			} else if (cursorStart < text.length()) {
				cursorStart = cursorEnd = cursorStart + 1;
			}
		} else if(GuiScreen.isKeyComboCtrlA(i)) {
			cursorStart = 0;
			cursorEnd = text.length();
		} else if(GuiScreen.isKeyComboCtrlC(i) || GuiScreen.isKeyComboCtrlX(i)) {
			int start = Math.min(cursorStart, cursorEnd);
			int end = Math.max(cursorStart, cursorEnd);
			if(start != end) {
				GuiScreen.setClipboardString(text.substring(start, end));
				if(GuiScreen.isKeyComboCtrlX(i))
					remove(0);
			}
		} else if(GuiScreen.isKeyComboCtrlV(i)) {
			write(GuiScreen.getClipboardString());
		} else if (i == Keyboard.KEY_BACK) {
			remove(1);
		} else if (i == Keyboard.KEY_DELETE) {
			remove(-1);
		} else if(ChatAllowedCharacters.isAllowedCharacter(c)){
			write(String.valueOf(c));
		}
		
		if(cursorEnd < lineOffset) {
			lineOffset = Math.max(0, cursorEnd - 2);
		} else {
			Rectangle actBounds = getPadding().getContentRect(getInnerBounds());
			String txt = text.substring(lineOffset);
			String str = getFont().trimStringToWidth(txt, actBounds.width, false);
			if(cursorEnd > lineOffset + str.length()) {
				lineOffset = Math.max(0, cursorEnd - str.length());
			}
		}
	}
	
	@Override
	public void onPress(int x, int y, int bt) {
		Rectangle actBounds = getPadding().getContentRect(getInnerBounds());
		if(x > actBounds.x && x < actBounds.x + actBounds.width) {
			String str = getFont().trimStringToWidth(text.substring(lineOffset), x - actBounds.x, false);
			cursorStart = cursorEnd = str.length() + lineOffset;
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
		
		if(isFocusd()) {
			int start = Math.min(cursorStart, cursorEnd);
			int end = Math.max(cursorStart, cursorEnd);
			if(lineOffset <= start || lineOffset <= end) {
				int sx = actBounds.x + getViewStringWidth(font, start);
				int ex = actBounds.x + getViewStringWidth(font, end);
				drawSelectionBox(sx, ex);
			}
		}
	}
	
	private void remove(int amount) {
		int sub1, sub2;
		if(cursorStart != cursorEnd) {
			sub1 = Math.max(0, Math.min(cursorStart, cursorEnd));
			sub2 = Math.max(cursorStart, cursorEnd);
		} else if(amount < 0){
			sub1 = Math.min(cursorStart, cursorEnd);
			sub2 = Math.min(text.length(), Math.max(cursorStart, cursorEnd) - amount);
		} else {
			sub1 = Math.max(0, Math.min(cursorStart, cursorEnd) - amount);
			sub2 = Math.max(cursorStart, cursorEnd);
		}
		text = text.substring(0, sub1) + text.substring(sub2);
		cursorStart = cursorEnd = sub1;
	}
	
	private void write(String str) {
		int newcw = Math.min(cursorStart, cursorEnd);
		String s1 = text.substring(0, newcw);
		String s2 = text.substring(Math.max(cursorStart, cursorEnd));
		text = s1 + str + s2;
		cursorStart = cursorEnd = newcw + str.length();
	}

	private int getViewStringWidth(TrueTypeFont font, int index) {
		return font.getStringWidth(text.substring(lineOffset, Math.max(lineOffset, index)));
	}
	
	private void drawSelectionBox(int startX, int endX) {
		Rectangle actBounds = getPadding().getContentRect(getInnerBounds());
		int startY = actBounds.y;
		int endY = actBounds.y + actBounds.height;
		int w = actBounds.x + actBounds.width;

		if (text.length() == 0)
			return;
		
		endX = MathHelper.clamp(endX, 0, w);
		startX = MathHelper.clamp(startX, 0, w);
		if (startX >= endX)
			endX = startX + 1;
		
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
