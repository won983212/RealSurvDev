package won983212.simpleui;

import java.awt.Dimension;
import java.awt.Rectangle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class DirWeights {
	public int top;
	public int bottom;
	public int left;
	public int right;

	public DirWeights() {
		this(0, 0, 0, 0);
	}
	
	public DirWeights(int r) {
		this(r, r, r, r);
	}
	
	public DirWeights(int horizontal, int vertical) {
		this(vertical, vertical, horizontal, horizontal);
	}
	
	public DirWeights(int top, int bottom, int left, int right) {
		this.top = top;
		this.bottom = bottom;
		this.left = left;
		this.right = right;
	}

	public Dimension getExpandedSize(Dimension size) {
		return new Dimension(size.width + left + right, size.height + top + bottom);
	}
	
	public Rectangle getContentRect(Rectangle rect) {
		return new Rectangle(rect.x + left, rect.y + top, rect.width - left - right, rect.height - top - bottom);
	}
}