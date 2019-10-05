package realsurv.font;

import java.awt.Font;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;

public class TTFRenderer extends FontRenderer {
	private TrueTypeFont font;
	private boolean forceDisableShadow = false;
	
	public TTFRenderer(String family, int size) {
		this(family, size, Minecraft.getMinecraft());
	}
	
	private TTFRenderer(String family, int size, Minecraft mc) {
		super(mc.gameSettings, new ResourceLocation("textures/font/ascii.png"), mc.getTextureManager(), mc.isUnicode());
		setFont(family, size);
	}
	
	public TTFRenderer setDisableShadow() {
		forceDisableShadow = true;
		return this;
	}
	
	public void setFont(String family, int size) {
		font = FontFactory.makeFont(family, size);
		FONT_HEIGHT = (int)(size * 4.0 / 3);
	}

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
	}

	@Override
	public int drawString(String text, float x, float y, int color, boolean dropShadow) {
		return font.drawString(text, x, y, color, !forceDisableShadow && dropShadow);
	}

	@Override
	public int getStringWidth(String text) {
		return font.getStringWidth(text);
	}

	@Override
	public int getCharWidth(char character) {
		return font.getCharWidth(character);
	}

	@Override
	public int getWordWrappedHeight(String str, int maxLength) {
		return font.getLineHeight(0) * listFormattedStringToWidth(str, maxLength).size();
	}

	@Override
	public List<String> listFormattedStringToWidth(String str, int wrapWidth) {
		return font.listFormattedStringToWidth(str, wrapWidth);
	}
	
}
