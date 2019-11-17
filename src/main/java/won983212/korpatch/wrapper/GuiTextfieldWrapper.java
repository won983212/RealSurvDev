package won983212.korpatch.wrapper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiTextField;
import won983212.korpatch.engine.KoreanInputEngine;
import won983212.simpleui.animation.Animation;
import won983212.simpleui.animation.IntAnimation;
import won983212.simpleui.font.AdaptiveTTF;
import won983212.simpleui.font.TrueTypeFont;

public class GuiTextfieldWrapper implements IInputWrapper {
	private static final int Margin = 13;
	private GuiTextField textfield;
	private boolean focusedFlag = false;
	private boolean visibleFlag = false;
	private boolean bgDrawFlag = false;
	private IntAnimation imeChangeAnimation = new IntAnimation(200);
	private int tempX = 0;
	private int tempWidth = 0;

	public GuiTextfieldWrapper(GuiTextField textfield) {
		this.textfield = textfield;
		this.focusedFlag = textfield.isFocused();
		this.visibleFlag = textfield.getVisible();
		
		this.bgDrawFlag = textfield.getEnableBackgroundDrawing();
		this.tempX = textfield.x;
		this.tempWidth = textfield.width;
		
		if (focusedFlag)
			changedFocused(true);
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

	// pre
	public void updateUISetupByFocusing() {
		boolean focused = isFocused();
		
		// 애니메이션 실제 적용
		textfield.x = tempX + imeChangeAnimation.get();
		textfield.width = tempWidth - imeChangeAnimation.get();
		
		// 새로 그려야 하므로 택스트필드가 그려지지 않도록 visible을 false
		visibleFlag = textfield.getVisible();
		textfield.setVisible(false);
		
		// focused가 변경되면 changedFocused 호출
		if (focusedFlag != focused) {
			changedFocused(focused);
			focusedFlag = focused;
		}
	}

	// pre
	private void changedFocused(boolean focused) {
		int xOffset = bgDrawFlag ? 4 : 0;
		int yOffset = bgDrawFlag ? (textfield.height - 8) / 2 : 0;
		imeChangeAnimation.setRange(0, Margin + xOffset);

		if (focused) {
			// 애니메이션 초기 위치를 설정한다.
			tempX = textfield.x;
			tempWidth = textfield.width;
			
			// 애니메이션 play
			imeChangeAnimation.playForward();
			
			// guitextfield내부적으로 background를 그릴 경우에는 y가 조정되므로 이를 수정
			textfield.y += yOffset;
		} else {
			imeChangeAnimation.playReverse();
			textfield.y -= yOffset;
		}
	}

	public boolean isFocused() {
		return textfield.isFocused();
	}

	// post
	public void drawImeTextbox() {
		textfield.setVisible(visibleFlag);
		if (!visibleFlag)
			return;

		int x = tempX;
		int y = textfield.y - (textfield.height - 8) / 2;
		final int boxWidth = 10;
		final int boxHeight = 10;
		TrueTypeFont fr = ((AdaptiveTTF) Minecraft.getMinecraft().fontRenderer).getTTFont();

		textfield.setEnableBackgroundDrawing(false);
		if (bgDrawFlag) {
			int width = textfield.width + tempWidth;
			Gui.drawRect(x - 1, y - 1, x + width + 1, y + textfield.height + 1, -6250336);
			Gui.drawRect(x, y, x + width, y + textfield.height, -16777216);
		}

		x += 4;
		y += (textfield.height - boxHeight) / 2;

		/*
		 * String value = KoreanInputEngine.isKorMode() ? "한" : "A"; int color =
		 * KoreanInputEngine.isKorMode() ? 0xffff6347 : 0xff848CB5; Gui.drawRect(x, y, x
		 * + boxWidth, y + boxHeight, color); fr.drawString(value, x + (boxWidth -
		 * fr.getStringWidth(value)) / 2.0, y + (boxHeight - fr.getStringHeight(value))
		 * / 2.0, 0xffffffff, true);
		 */
		textfield.drawTextBox();
		textfield.setEnableBackgroundDrawing(bgDrawFlag);
	}
}
