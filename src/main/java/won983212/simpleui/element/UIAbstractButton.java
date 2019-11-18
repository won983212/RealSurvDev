package won983212.simpleui.element;

import java.awt.Dimension;

import won983212.simpleui.DirWeights;
import won983212.simpleui.animation.Animation;
import won983212.simpleui.animation.ColorAnimation;
import won983212.simpleui.events.IButtonEvent;
import won983212.simpleui.font.TrueTypeFont;
import won983212.simpleui.parentelement.UIObject;

public class UIAbstractButton extends UIObject {
	private IButtonEvent event;
	private boolean clicking = false;
	private ColorAnimation hoverColorAnimation = new ColorAnimation(Animation.MOUSELEAVE_DURATION);
	private boolean isEnteredMouse = false;
	
	public UIAbstractButton() {
		setPadding(new DirWeights(2));
		setShadowVisible(true);
	}
	
	public UIAbstractButton setClickEvent(IButtonEvent e) {
		this.event = e;
		return this;
	}

	@Override
	public void render(int mx, int my) {
		Dimension size = getBoundsSize();
		int offset = showShadow && clicking ? 1 : 0;
		
		boolean isIn = containsRelative(mx, my);
		hoverColorAnimation.setRange(backgroundColor, mouseOverColor);
		if(isEnteredMouse != isIn) {
			isEnteredMouse = isIn;
			if(isIn)
				hoverColorAnimation.setTime(0);
			hoverColorAnimation.play(!isIn);
		}
		
		renderArcRect(offset, offset, size.width, size.height, arc, hoverColorAnimation.get(), showShadow && !clicking);
	}

	@Override
	public boolean onPress(int x, int y, int bt) {
		if(event != null)
			event.onClicked(this, bt);
		clicking = true;
		return true;
	}

	@Override
	public boolean onRelease(int x, int y, int bt) {
		clicking = false;
		return true;
	}

	public boolean isClicking() {
		return clicking;
	}
}