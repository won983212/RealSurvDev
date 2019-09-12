package realsurv.tabletos.ui;

import java.awt.Dimension;
import java.awt.Rectangle;

import net.minecraft.client.gui.Gui;
import realsurv.tabletos.DirWeights;

public class UIButton extends UIObject {
	private String label;
	private int test = 0;
	
	public UIButton(String label) {
		setMargin(new DirWeights(2));
		setPadding(new DirWeights(2));
		setLabel(label);
	}
	
	public void setLabel(String label) {
		this.label = label;
		DirWeights padding = getPadding();
		setMinimumSize(fontrenderer.getStringWidth(label) + padding.left + padding.right, fontrenderer.FONT_HEIGHT + padding.up + padding.down);
		requestLayout();
	}
	
	@Override
	public void render(int mx, int my) {
		Rectangle bounds = getActualBounds();
		DirWeights padding = getPadding();
		int fontWidth = fontrenderer.getStringWidth(label);
		Gui.drawRect(bounds.x, bounds.y, bounds.x + bounds.width, bounds.y + bounds.height, 0xff999999);
		fontrenderer.drawString(label, bounds.x + padding.left + (bounds.width - fontWidth) / 2, bounds.y + padding.up + (bounds.height - fontrenderer.FONT_HEIGHT) / 2, 0xffffffff);
	}

	@Override
	public void onPress(int x, int y, int bt) {
		setLabel("Clicked " + ++test);
	}

	@Override
	public void onRelease(int x, int y, int bt) {
		// TODO Auto-generated method stub
		super.onRelease(x, y, bt);
	}	
}