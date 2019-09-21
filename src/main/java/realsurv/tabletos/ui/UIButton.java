package realsurv.tabletos.ui;

import java.awt.Dimension;
import java.awt.Rectangle;

import net.minecraft.client.gui.Gui;
import realsurv.tabletos.DirWeights;
import realsurv.tabletos.ui.events.IButtonEvent;

public class UIButton extends UIObject {
	private String label;
	private IButtonEvent event;
	
	public UIButton(String label) {
		setPadding(new DirWeights(2));
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
		Rectangle bounds = getActualBounds();
		int fontWidth = fontrenderer.getStringWidth(label);
		int color = backgroundColor;

		if(bounds.contains(mx, my))
			color = offsetColor(color, 20);
		Gui.drawRect(bounds.x, bounds.y, bounds.x + bounds.width, bounds.y + bounds.height, color);
		fontrenderer.drawString(label, bounds.x + (bounds.width - fontWidth) / 2, bounds.y + (bounds.height - fontrenderer.FONT_HEIGHT) / 2, foregroundColor);
	}

	@Override
	public void onPress(int x, int y, int bt) {
		if(event != null)
			event.onClicked(this, bt);
		setBackgroundColor(offsetColor(backgroundColor, -20));
	}

	@Override
	public void onRelease(int x, int y, int bt) {
		setBackgroundColor(offsetColor(backgroundColor, 20));
	}
}