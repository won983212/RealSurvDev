package won983212.simpleui.element;

import java.awt.Dimension;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import won983212.simpleui.UIObject;
import won983212.simpleui.UVBounds;

public class UIImage extends UIObject {
	private ResourceLocation loc;
	private UVBounds uv = new UVBounds();
	private int texWidth = 0;
	private int texHeight = 0;
	
	public UIImage(String path) {
		setImage(path);
	}
	
	public UIImage setImage(String path) {
		loc = new ResourceLocation(path);
		return this;
	}

	public UIImage setUV(UVBounds bounds) {
		uv = new UVBounds(bounds);
		return this;
	}
	
	public UIImage setUVArea(double minU, double minV, double maxU, double maxV) {
		uv.setUV(minU, minV, maxU, maxV);
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
			uv.setUV(0, 0, 1, 1);
		} else {
			uv.setUVPixel(u, v, width, height, texWidth, texHeight);
		}
		return this;
	}
	
	@Override
	public void render(int mx, int my) {
		Dimension size = getBoundsSize();
		Minecraft.getMinecraft().getTextureManager().bindTexture(loc);
		Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableTexture2D();
        GlStateManager.color(1, 1, 1, 1);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(0, 0, 0).tex(uv.minU, uv.minV).endVertex();
        bufferbuilder.pos(0, size.height, 0).tex(uv.minU, uv.maxV).endVertex();
        bufferbuilder.pos(size.width, size.height, 0).tex(uv.maxU, uv.maxV).endVertex();
        bufferbuilder.pos(size.width, 0, 0).tex(uv.maxU, uv.minV).endVertex();
        tessellator.draw();
	}
}
