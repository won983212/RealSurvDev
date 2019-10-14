package realsurv.font;

import java.awt.Font;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;

public class AdaptiveTTF extends FontRenderer {
	private TrueTypeFont font;
	private boolean forceDisableShadow = false;

	public AdaptiveTTF(String family, int size) {
		this(family, size, Minecraft.getMinecraft());
	}

	private AdaptiveTTF(String family, int size, Minecraft mc) {
		super(mc.gameSettings, new ResourceLocation("textures/font/ascii.png"), mc.getTextureManager(), mc.isUnicode());
		setFont(family, size);
	}

	public AdaptiveTTF setDisableShadow() {
		forceDisableShadow = true;
		return this;
	}

	public void setFont(String family, int size) {
		font = FontFactory.makeFont(family, size).setScaledHalf();
		FONT_HEIGHT = (int) (size * 4.0 / 6.0);
	}

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
	}

	@Override
	protected void bindTexture(ResourceLocation location) {
	}

	@Override
	public int drawString(String text, float x, float y, int color, boolean dropShadow) {
		color |= 0xff000000;
		return (int) x + font.drawString(text, x, y, color, !forceDisableShadow && dropShadow);
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
	public String trimStringToWidth(String text, int width, boolean reverse) {
		return font.trimStringToWidth(text, width, reverse);
	}

	@Override
	public List<String> listFormattedStringToWidth(String str, int wrapWidth) {
		return font.listFormattedStringToWidth(str, wrapWidth);
	}

	@Override
	public String toString() {
		return "Adaptive" + font.toString();
	}
}
