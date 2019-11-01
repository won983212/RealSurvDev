package won983212.simpleui.font;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public class AdaptiveTTF extends FontRenderer {
	private TrueTypeFont font;
	private boolean forceDisableShadow = false;

	protected AdaptiveTTF(String family, int size) {
		this(family, size, Minecraft.getMinecraft());
	}

	private AdaptiveTTF(String family, int size, Minecraft mc) {
		super(mc.gameSettings, new ResourceLocation("textures/font/ascii.png"), mc.getTextureManager(), mc.isUnicode());
		font = new TrueTypeFont(family, size, true);
		FONT_HEIGHT = (int) (size * 4.0 / 6.0);
	}

	public AdaptiveTTF setDisableShadow() {
		forceDisableShadow = true;
		return this;
	}

	protected void setFontScale(int scale) {
		font.setScaleModifier(scale);
		FONT_HEIGHT = (int) (font.getJavaFont(0).getSize() * 4.0 / 3.0 / font.getScaleModifier());
	}

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
	}

	@Override
	protected void bindTexture(ResourceLocation location) {
	}

	@Override
	public int drawString(String text, float x, float y, int color, boolean dropShadow) {
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
