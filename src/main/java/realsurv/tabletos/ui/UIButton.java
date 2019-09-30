package realsurv.tabletos.ui;

import java.awt.Dimension;
import java.awt.Rectangle;

import net.minecraft.client.gui.Gui;
import realsurv.tabletos.DirWeights;
import realsurv.tabletos.ui.events.IButtonEvent;

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
		this.label = label;
		setMinimumSize(fontrenderer.getStringWidth(label), fontrenderer.FONT_HEIGHT);
		return this;
	}
	
	public UIButton setClickEvent(IButtonEvent e) {
		this.event = e;
		return this;
	}

	@Override
	public void render(int mx, int my) {
		Dimension size = getBoundsSize();
		int fontWidth = fontrenderer.getStringWidth(label);
		int color = backgroundColor;
		int offset = showShadow && clicking ? 1 : 0;
		
		if(containsRelative(mx, my))
			color = offsetColor(color, 20);
		renderArcRect(offset, offset, size.width, size.height, arc, color, showShadow && !clicking);
		fontrenderer.drawString(label, offset + (size.width - fontWidth) / 2, offset + (size.height - fontrenderer.FONT_HEIGHT) / 2, foregroundColor);
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