package realsurv.font;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;

public class BufferedImageTexture extends AbstractTexture {
	public static final Color TRANSCOLOR = new Color(255, 255, 255, 0);
	
	private BufferedImage image;
	private Graphics2D graphic = null;
	private boolean allocated = false;
	
	public BufferedImageTexture(int width, int height) {
		reallocate(width, height);
	}

	public void saveImage() {
		try {
			ImageIO.write(image, "png", new File("C:/users/qkrru/desktop/text.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	
	public void reallocate(int width, int height) {
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		graphic = image.createGraphics();
		graphic.setBackground(TRANSCOLOR);
		graphic.setComposite(AlphaComposite.Src);
		allocated = false;
	}

	public Graphics2D getGraphic() {
		return graphic;
	}
	
	public void bindTexture() {
		GlStateManager.bindTexture(getGlTextureId());
	}
	
	public void updateTexture(int x, int y, int width, int height) {
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
