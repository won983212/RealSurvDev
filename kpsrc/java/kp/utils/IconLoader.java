package kp.utils;

import org.lwjgl.opengl.GL11;

import kp.KoreanPatch;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class IconLoader
{
	public static final ResourceLocation TEXTURE_GUI = new ResourceLocation(KoreanPatch.ICON_URL);

	public static void drawTexture(Gui source, int x, int y, int indexX, int indexY)
	{
		drawTexture(source, x, y, indexX, indexY, 16777215, 1, 1, 2);
	}

	public static void drawTexture(Gui source, int x, int y, int indexX, int indexY, int color)
	{
		drawTexture(source, x, y, indexX, indexY, color, 1, 1, 2);
	}

	public static void drawTexture(Gui source, int x, int y, int indexX, int indexY, int color, int width, int height)
	{
		drawTexture(source, x, y, indexX, indexY, color, width, height, 2);
	}

	public static void drawTexture(Gui source, int x, int y, int indexX, int indexY, int color, int width, int height, int renderRatio)
	{
		double ratio = 1.0D / renderRatio;
		double ratioF = 1 / ratio;
		Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE_GUI);

		float r = (color >> 16 & 0xFF) / 255.0F;
		float g = (color >> 8 & 0xFF) / 255.0F;
		float b = (color & 0xFF) / 255.0F;
		int w = 24 * width;
		int h = 24 * height;

		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.color(r, g, b);
		GlStateManager.scale(ratio, ratio, ratio);
		source.drawTexturedModalRect(x * 2, y * 2, indexX * 24, indexY * 24, w, h);
		GlStateManager.scale(ratioF, ratioF, ratioF);
		GlStateManager.disableBlend();
	}
}
