package won983212.simpleui;

import net.minecraft.util.ResourceLocation;
import won983212.simpleui.element.UIImage;

public class SpriteIcon extends DrawableImage {
	private int width = 1;
	private int height = 1;
	private int texWidth = 1;
	private int texHeight = 1;
	
	public SpriteIcon(String path, int width, int height) {
		this(new ResourceLocation(path), 0, 0, width, height, width, height);
	}
	
	public SpriteIcon(SpriteIcon bounds, int u, int v, int width, int height) {
		this(bounds.rootImagePath, u, v, width, height, bounds.texWidth, bounds.texHeight);
	}
	
	public SpriteIcon(ResourceLocation image, int u, int v, int width, int height, int texWidth, int texHeight) {
		super(image);
		this.texWidth = texWidth;
		this.texHeight = texHeight;
		setImage(image);
		setUVPixel(u, v, width, height);
	}

	public void setUVPixel(int u, int v, int width, int height) {
		this.minU = u / (double) texWidth;
		this.minV = v / (double) texHeight;
		this.maxU = (u + width) / (double) texWidth;
		this.maxV = (v + height) / (double) texHeight;
		this.width = width;
		this.height = height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getTextureWidth() {
		return texWidth;
	}
	
	public int getTextureHeight() {
		return texHeight;
	}
	
	public void render(int x, int y) {
		render(x, y, width, height);
	}
}
