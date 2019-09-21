package realsurv.tabletos;

import java.awt.Rectangle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;

public class GuiCompatibleTextfield extends GuiTextField {
	private static final NoShadowFontRenderer fr = new NoShadowFontRenderer(Minecraft.getMinecraft());
	
	public GuiCompatibleTextfield(int componentId) {
		super(componentId, fr, 0, 0, 10, 10);
	}

	public void drawAtBounds(Rectangle bounds) {
		this.x = bounds.x;
		this.y = bounds.y;
		this.width = bounds.width;
		this.height = bounds.height;
		drawTextBox();
	}
	
	private static class NoShadowFontRenderer extends FontRenderer {
		public NoShadowFontRenderer(Minecraft mc) {
			super(mc.gameSettings, new ResourceLocation("textures/font/ascii.png"), mc.getTextureManager(), mc.isUnicode());
			super.onResourceManagerReload(null);
		}
		
		@Override
		public int drawStringWithShadow(String text, float x, float y, int color) {
			return super.drawString(text, x, y, color, false);
		}
	}
}
