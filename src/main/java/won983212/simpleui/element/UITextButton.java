package won983212.simpleui.element;

import java.awt.Dimension;

import won983212.simpleui.DirWeights;
import won983212.simpleui.animation.Animation;
import won983212.simpleui.animation.ColorAnimation;
import won983212.simpleui.events.IButtonEvent;
import won983212.simpleui.font.TrueTypeFont;
import won983212.simpleui.parentelement.UIObject;

public class UITextButton extends UIAbstractButton {
	private String label;
	
	public UITextButton(String label) {
		super();
		setLabel(label);
	}
	
	public UITextButton setLabel(String label) {
		TrueTypeFont font = getFont();
		this.label = label;
		setMinimumSize(font.getStringWidth(label), font.getStringHeight(label));
		return this;
	}

	@Override
	public void render(int mx, int my) {
		super.render(mx, my);
		
		Dimension size = getBoundsSize();
		TrueTypeFont font = getFont();
		int fontWidth = font.getStringWidth(label);
		int offset = showShadow && isClicking() ? 1 : 0;
		
		font.drawString(label, offset + (size.width - fontWidth) / 2, offset + (size.height - font.getStringHeight(label)) / 2, foregroundColor);
	}
}