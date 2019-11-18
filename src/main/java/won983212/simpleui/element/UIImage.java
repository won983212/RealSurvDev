package won983212.simpleui.element;

import java.awt.Dimension;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import won983212.simpleui.DrawableImage;
import won983212.simpleui.FullImage;
import won983212.simpleui.SpriteIcon;
import won983212.simpleui.parentelement.UIObject;

public class UIImage extends UIObject {
	private DrawableImage icon = null;
	
	public UIImage(String path) {
		setImage(path);
	}
	
	public UIImage(DrawableImage icon) {
		setImage(icon);
	}
	
	public UIImage setImage(String path) {
		icon = new FullImage(path);
		return this;
	}

	public UIImage setImage(DrawableImage icon) {
		this.icon = icon;
		if(icon instanceof SpriteIcon) {
			SpriteIcon sprite = (SpriteIcon) icon;
			setMinimumSize(sprite.getWidth(), sprite.getHeight());
		}
		return this;
	}
	
	public DrawableImage getIcon() {
		return icon;
	}
	
	@Override
	public void render(int mx, int my) {
		Dimension size = getBoundsSize();
		icon.render(0, 0, size.width, size.height);
	}
}
