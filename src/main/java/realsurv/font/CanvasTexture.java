package realsurv.font;

import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.IntBuffer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;

public class CanvasTexture extends AbstractTexture {
	private BufferedImage image;
	private Graphics2D graphic = null;
	private boolean allocated = false;
	
	public CanvasTexture(int width, int height) {
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		graphic = (Graphics2D) image.getGraphics();
	}

	public FontRenderContext getFontCtx() {
		return graphic.getFontRenderContext();
	}
	
	public int getWidth() {
		return image.getWidth();
	}

	public int getHeight() {
		return image.getHeight();
	}

	public Graphics2D getGraphic() {
		return graphic;
	}
	
	public void bindTexture() {
		GlStateManager.bindTexture(getGlTextureId());
	}

	public void updateTexture() {
		if(!allocated) {
			allocated = true;
			TextureUtil.allocateTexture(getGlTextureId(), getWidth(), getHeight());
		}
		
		int w = getWidth();
		int h = getHeight();
		int[] data = new int[w * h];
		image.getRGB(0, 0, w, h, data, 0, w);
		TextureUtil.uploadTexture(this.getGlTextureId(), data, getWidth(), getHeight());
	}
	
	@Override
	public void loadTexture(IResourceManager resourceManager) throws IOException {
	}
}
