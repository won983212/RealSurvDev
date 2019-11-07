package won983212.simpleui;

import won983212.simpleui.element.UIImage;

public class UVBounds {
	public double minU = 0;
	public double minV = 0;
	public double maxU = 1;
	public double maxV = 1;
	
	public UVBounds() {
		this(0, 0, 1, 1);
	}
	
	public UVBounds(UVBounds bounds) {
		this(bounds.minU, bounds.minV, bounds.maxU, bounds.maxV);
	}
	
	public UVBounds(double minU, double minV, double maxU, double maxV) {
		setUV(minU, minV, maxU, maxV);
	}
	
	public UVBounds(int u, int v, int width, int height, int texWidth, int texHeight) {
		setUVPixel(u, v, width, height, texWidth, texHeight);
	}

	public void setUV(double minU, double minV, double maxU, double maxV) {
		this.minU = minU;
		this.minV = minV;
		this.maxU = maxU;
		this.maxV = maxV;
	}
	
	public void setUVPixel(int u, int v, int width, int height, int texWidth, int texHeight) {
		minU = u / (double) texWidth;
		minV = v / (double) texHeight;
		maxU = (u + width) / (double) texWidth;
		maxV = (v + height) / (double) texHeight;
	}
}
