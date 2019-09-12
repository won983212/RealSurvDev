package realsurv.tabletos;

import java.awt.Dimension;
import java.awt.Rectangle;

public class DirWeights {
	public int up;
	public int down;
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
	
	public DirWeights(int n, int s, int e, int w) {
		up = n;
		down = s;
		left = e;
		right = w;
	}

	public Dimension getExpandedSize(Dimension size) {
		return new Dimension(size.width + left + right, size.height + up + down);
	}
	
	public Rectangle getContentRect(Rectangle rect) {
		return new Rectangle(rect.x + left, rect.y + up, rect.width - left - right, rect.height - up - down);
	}
}