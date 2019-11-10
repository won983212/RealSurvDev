package won983212.simpleui;

import net.minecraft.util.ResourceLocation;

public class FullImage extends DrawableImage {
	public FullImage(String image) {
		super(new ResourceLocation(image), 0, 0, 1, 1);
	}
	
	public FullImage(ResourceLocation image) {
		super(image, 0, 0, 1, 1);
	}
}
