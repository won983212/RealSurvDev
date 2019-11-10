package won983212.simpleui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class DrawableImage {
	public static final FullImage WALLPAPER = new FullImage("realsurv:ui/wallpaper.png");
	public static final SpriteIcon APPICONS = new SpriteIcon("realsurv:ui/appIcons.png", 512, 512);
	
	protected ResourceLocation rootImagePath;
	protected double minU = 0;
	protected double minV = 0;
	protected double maxU = 1;
	protected double maxV = 1;

	protected DrawableImage(ResourceLocation image) {
		this.rootImagePath = image;
	}
	
	protected DrawableImage(ResourceLocation image, double minU, double minV, double maxU, double maxV) {
		this.rootImagePath = image;
		this.minU = minU;
		this.minV = minV;
		this.maxU = maxU;
		this.maxV = maxV;
	}
	
	public void setUV(double minU, double minV, double maxU, double maxV) {
		this.minU = minU;
		this.minV = minV;
		this.maxU = maxU;
		this.maxV = maxV;
	}

	public double getMinU() {
		return minU;
	}

	public double getMinV() {
		return minV;
	}

	public double getMaxU() {
		return maxU;
	}

	public double getMaxV() {
		return maxV;
	}
	
	public ResourceLocation getImage() {
		return rootImagePath;
	}
	
	public void setImage(String image) {
		this.rootImagePath = new ResourceLocation(image);
	}
	
	public void setImage(ResourceLocation image) {
		this.rootImagePath = image;
	}
	
	public void render(int x, int y, int width, int height) {
		Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        Minecraft.getMinecraft().getTextureManager().bindTexture(rootImagePath);
        GlStateManager.enableTexture2D();
        GlStateManager.color(1, 1, 1, 1);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(x, y, 0).tex(minU, minV).endVertex();
        bufferbuilder.pos(x, y + height, 0).tex(minU, maxV).endVertex();
        bufferbuilder.pos(x + width, y + height, 0).tex(maxU, maxV).endVertex();
        bufferbuilder.pos(x + width, y, 0).tex(maxU, minV).endVertex();
        tessellator.draw();
	}
}
