package won983212.simpleui.rootpane;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class HighResolutionPane extends RootPane {
	public HighResolutionPane(int width, int height) {
		super(width, height);
		setScaledFactor(0);
	}

	@Override
	public void render(int mouseX, int mouseY) {
		double factor = scaledFactor;
		if (factor <= 0) {
			ScaledResolution sr = new ScaledResolution(mc);
			factor = sr.getScaleFactor();
		}

		GlStateManager.scale(1 / factor, 1 / factor, 1 / factor);
		super.render(mouseX, mouseY);
		GlStateManager.scale(factor, factor, factor);
	}
}
