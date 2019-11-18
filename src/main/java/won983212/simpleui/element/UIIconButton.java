package won983212.simpleui.element;

import java.awt.Dimension;

import won983212.simpleui.DirWeights;
import won983212.simpleui.DrawableImage;
import won983212.simpleui.SpriteIcon;
import won983212.simpleui.animation.Animation;
import won983212.simpleui.animation.ColorAnimation;
import won983212.simpleui.events.IButtonEvent;
import won983212.simpleui.font.TrueTypeFont;
import won983212.simpleui.parentelement.UIObject;

public class UIIconButton extends UIAbstractButton {
	private SpriteIcon icon;
	
	public UIIconButton(SpriteIcon icon) {
		setShadowVisible(false);
		setBackgroundColor(0);
		setMouseOverColor(0x33ffffff);
		setIcon(icon);
	}
	
	public UIIconButton setIcon(SpriteIcon icon) {
		this.icon = icon;
		setMinimumSize(icon.getWidth(), icon.getHeight());
		return this;
	}

	@Override
	public void render(int mx, int my) {
		icon.render(2, 2);
		super.render(mx, my);
	}
}
