package won983212.simpleui.element;

import won983212.simpleui.animation.Animation;
import won983212.simpleui.animation.ColorAnimation;
import won983212.simpleui.font.TrueTypeFont;
import won983212.simpleui.parentelement.UIObject;

public class UICheckbox extends UIObject {
	private boolean checked = false;
	private String label;
	private ColorAnimation hoverColorAnimation = new ColorAnimation(Animation.MOUSELEAVE_DURATION);
	private ColorAnimation checkAnimation = new ColorAnimation(Animation.STATECHANGE_DURATION);
	private boolean isEnteredMouse = false;
	private boolean lastChecked = false;
	
	public boolean isChecked() {
		return checked;
	}
	
	public UICheckbox setLabel(String label) {
		TrueTypeFont font = getFont();
		this.label = label;
		setMinimumSize(font.getStringWidth(label) + 14, Math.max(9, font.getMaxHeight()));
		return this;
	}
	
	public UICheckbox setChecked(boolean checked) {
		this.checked = checked;
		return this;
	}
	
	@Override
	public boolean onPress(int x, int y, int bt) {
		setChecked(!checked);
		return true;
	}
	
	@Override
	public void render(int mouseX, int mouseY) {
		TrueTypeFont font = getFont();
		int h = Math.max(9, font.getMaxHeight());
		int y = (h - 9) / 2;

		boolean isIn = containsRelative(mouseX, mouseY);
		hoverColorAnimation.setRange(backgroundColor, mouseOverColor);
		if(isEnteredMouse != isIn) {
			isEnteredMouse = isIn;
			if(isIn)
				hoverColorAnimation.setTime(0);
			hoverColorAnimation.play(!isIn);
		}
		
		renderArcRect(0, y, 9, y + 9, arc, hoverColorAnimation.get(), showShadow);
		if(lastChecked != checked) {
			lastChecked = checked;
			checkAnimation.setRange(backgroundColor, 0xff000000);
			checkAnimation.play(!checked);
		}
		
		renderArcRect(1, y + 1, showShadow ? 7 : 8, y + (showShadow ? 7 : 8), arc, checkAnimation.get(), false);
		getFont().drawString(label, 11, (h - font.getMaxHeight()) / 2 + 1, foregroundColor);
	}
}
