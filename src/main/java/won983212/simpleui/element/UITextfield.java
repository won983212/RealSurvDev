package won983212.simpleui.element;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import won983212.simpleui.DirWeights;
import won983212.simpleui.GuiCompatibleTextfield;
import won983212.simpleui.UIObject;
import won983212.simpleui.font.AdaptiveTTF;
import won983212.simpleui.font.FontFactory;
import won983212.simpleui.font.TrueTypeFont;

//TODO Implement it without Guitextfield.
public class UITextfield extends UIObject {
	private String hint = null;
	private String text = "";
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
		text += c;
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

		if (text.length() > 0) 
			font.drawString(text, actBounds.x, actBounds.y, foregroundColor);
		else if (hint != null)
			font.drawString(hint, actBounds.x, actBounds.y, hintTextColor);
	}

	private void drawSelectionBox(int startX, int startY, int endX, int endY) {
		Rectangle actBounds = getPadding().getContentRect(getInnerBounds());
		
		if (startX < endX) {
			int i = startX;
			startX = endX;
			endX = i;
		}

		if (startY < endY) {
			int j = startY;
			startY = endY;
			endY = j;
		}

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
		bufferbuilder.pos((double) startX, (double) endY, 0.0D).endVertex();
		bufferbuilder.pos((double) endX, (double) endY, 0.0D).endVertex();
		bufferbuilder.pos((double) endX, (double) startY, 0.0D).endVertex();
		bufferbuilder.pos((double) startX, (double) startY, 0.0D).endVertex();
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
