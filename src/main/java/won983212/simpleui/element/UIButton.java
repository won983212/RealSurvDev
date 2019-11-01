package won983212.simpleui.element;

import java.awt.Dimension;

import won983212.simpleui.DirWeights;
import won983212.simpleui.UIObject;
import won983212.simpleui.events.IButtonEvent;
import won983212.simpleui.font.TrueTypeFont;

public class UIButton extends UIObject {
	private String label;
	private IButtonEvent event;
	private boolean clicking = false;
	
	public UIButton(String label) {
		setPadding(new DirWeights(2));
		setShadowVisible(true);
		setLabel(label);
	}
	
	public UIButton setLabel(String label) {
		TrueTypeFont font = getFont();
		this.label = label;
		setMinimumSize(font.getStringWidth(label), font.getMaxHeight());
		return this;
	}
	
	public UIButton setClickEvent(IButtonEvent e) {
		this.event = e;
		return this;
	}

	@Override
	public void render(int mx, int my) {
		Dimension size = getBoundsSize();
		TrueTypeFont font = getFont();
		int fontWidth = font.getStringWidth(label);
		int color = backgroundColor;
		int offset = showShadow && clicking ? 1 : 0;
		
		if(containsRelative(mx, my))
			color = getMouseOverColor(color);
		renderArcRect(offset, offset, size.width, size.height, arc, color, showShadow && !clicking);
		font.drawString(label, offset + (size.width - fontWidth) / 2, offset + (size.height - font.getMaxHeight()) / 2, foregroundColor);
	}

	@Override
	public void onPress(int x, int y, int bt) {
		if(event != null)
			event.onClicked(this, bt);
		clicking = true;
	}

	@Override
	public void onRelease(int x, int y, int bt) {
		clicking = false;
	}
}