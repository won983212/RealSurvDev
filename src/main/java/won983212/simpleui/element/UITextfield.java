package won983212.simpleui.element;

import java.awt.Rectangle;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import won983212.simpleui.DirWeights;
import won983212.simpleui.UIObject;
import won983212.simpleui.font.TrueTypeFont;

//TODO Implement it without Guitextfield.
public class UITextfield extends UIObject {
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
		if(i == Keyboard.KEY_BACK) {
			remove(1);
		} else {
			write(c);
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
			font.drawString(getRenderingText(font, actBounds.width), actBounds.x, actBounds.y, foregroundColor);
		} else if (hint != null)
			font.drawString(hint, actBounds.x, actBounds.y, hintTextColor);
		drawSelectionBox(font);
	}
	
	private String getRenderingText(TrueTypeFont font, int width) {
		return font.trimStringToWidth(text.substring(lineOffset), width, false);
	}
	
	private void remove(int amount) {
		amount = cursorStart != cursorEnd ? 0 : amount;
		String s1 = text.substring(0, Math.max(0, cursorStart - amount));
		String s2 = text.substring(cursorEnd);
		text = s1 + s2;
	}
	
	private void write(char c) {
		String s1 = text.substring(0, cursorStart);
		String s2 = text.substring(cursorEnd);
		text = s1 + c + s2;
		cursorStart = cursorEnd = cursorStart + 1;
	}

	private void drawSelectionBox(TrueTypeFont font) {
		Rectangle actBounds = getPadding().getContentRect(getInnerBounds());
		int startX = actBounds.x + font.getStringWidth(text.substring(lineOffset, cursorStart));
		int startY = actBounds.y;
		int endX = actBounds.x + font.getStringWidth(text.substring(lineOffset, cursorEnd));
		int endY = actBounds.y + actBounds.height;

		if (endX > actBounds.x + actBounds.width) {
			endX = actBounds.x + actBounds.width;
		}

		if (startX > actBounds.x + actBounds.width) {
			startX = actBounds.x + actBounds.width;
		}

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		GlStateManager.color(0.0F, 0.0F, 255.0F, 255.0F);
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
