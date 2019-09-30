package realsurv.tabletos.ui;

import java.awt.Dimension;
import java.awt.Rectangle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class UIImage extends UIObject {
	private ResourceLocation loc;
	private double minU = 0;
	private double minV = 0;
	private double maxU = 1;
	private double maxV = 1;
	private int texWidth = 0;
	private int texHeight = 0;
	
	public UIImage(String path) {
		setImage(path);
	}
	
	public UIImage setImage(String path) {
		loc = new ResourceLocation(path);
		return this;
	}

	public UIImage setUVArea(double minU, double minV, double maxU, double maxV) {
		this.minU = minU;
		this.minV = minV;
		this.maxU = maxU;
		this.maxV = maxV;
		return this;
	}

	public UIImage setTextureSize(int w, int h) {
		this.texWidth = w;
		this.texHeight = h;
		return this;
	}
	
	/**
	 * Must be called after set texture size.
	 */
	public UIImage setUVAreaPixel(int u, int v, int width, int height) {
		if(texWidth < 1 || texHeight < 1) {
			minU = minV = 0;
			maxU = maxV = 1;
		} else {
			minU = u / (double) texWidth;
			minV = v / (double) texHeight;
			maxU = (u + width) / (double) texWidth;
			maxV = (v + height) / (double) texHeight;
		}
		return this;
	}
	
	@Override
	public void render(int mx, int my) {
		Dimension size = getBoundsSize();
		Minecraft.getMinecraft().getTextureManager().bindTexture(loc);
		Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(0, 0, 0).tex(minU, minV).endVertex();
        bufferbuilder.pos(0, size.height, 0).tex(minU, maxV).endVertex();
        bufferbuilder.pos(size.width, size.height, 0).tex(maxU, maxV).endVertex();
        bufferbuilder.pos(size.width, 0, 0).tex(maxU, minV).endVertex();
        tessellator.draw();
	}
}
