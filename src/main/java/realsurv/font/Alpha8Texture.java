package realsurv.font;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;

public class Alpha8Texture extends AbstractTexture {
	public static final Color TRANSCOLOR = new Color(255, 255, 255, 0);
	private int imageData[];
	private IntBuffer imageBuffer;

	private BufferedImage image;
	private Graphics2D graphic = null;
	private boolean allocated = false;

	public Alpha8Texture(int width, int height) {
		reallocate(width, height);
	}

	public int getWidth() {
		return image.getWidth();
	}

	public int getHeight() {
		return image.getHeight();
	}

	public void reallocate(int width, int height) {
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		imageData = new int[width * height];
		imageBuffer = ByteBuffer.allocateDirect(4 * width * height).order(ByteOrder.BIG_ENDIAN).asIntBuffer();

		graphic = image.createGraphics();
		graphic.setBackground(TRANSCOLOR);
		allocated = false;
		glTextureId = -1;
	}

	public Graphics2D getGraphic() {
		return graphic;
	}

	public void bindTexture() {
		GlStateManager.bindTexture(getGlTextureId());
	}

	public void updateTexture(int x, int y, int width, int height) {
		if (!allocated) {
			allocated = true;
			updateImageBuffer(0, 0, image.getWidth(), image.getHeight());
	        GL11.glBindTexture(GL11.GL_TEXTURE_2D, getGlTextureId());
	        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_ALPHA8, image.getWidth(), image.getHeight(), 0,
	            GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, imageBuffer);
	        setBlurMipmap(false, false);
		}

		updateImageBuffer(x, y, width, height);
		GlStateManager.bindTexture(getGlTextureId());
		GlStateManager.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, x, y, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
				imageBuffer);
	}

	private void updateImageBuffer(int x, int y, int width, int height) {
		image.getRGB(x, y, width, height, imageData, 0, width);
		for (int i = 0; i < width * height; i++) {
			int color = imageData[i];
			imageData[i] = (color << 8) | (color >>> 24);
		}
		imageBuffer.clear();
		imageBuffer.put(imageData);
		imageBuffer.flip();
	}

	@Override
	public void loadTexture(IResourceManager resourceManager) throws IOException {
	}

	public BufferedImage getImage() {
		return image;
	}
}
