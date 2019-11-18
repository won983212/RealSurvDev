package won983212.simpleui.rootpane;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class MinecraftScreenPane extends RootPane {
	public MinecraftScreenPane() {
		super();
	}
	
	public MinecraftScreenPane(int width, int height) {
		super(width, height);
	}
	
	@Override
	public void initializePanel() {
		initializePanel(new ScaledResolution(mc).getScaleFactor());
	}

	@Override
	public void render(int mouseX, int mouseY) {
		double factor = new ScaledResolution(mc).getScaleFactor();
		GlStateManager.scale(1 / factor, 1 / factor, 1 / factor);
		super.render(mouseX, mouseY);
		GlStateManager.scale(factor, factor, factor);
	}
	
	@Override
	public void setRelativeLocation(int x, int y) {
		int factor = new ScaledResolution(mc).getScaleFactor();
		super.setRelativeLocation(x * factor, y * factor);
	}
	
	@Override
	protected void setRelativeBounds(int x, int y, int width, int height) {
		int factor = new ScaledResolution(mc).getScaleFactor();
		super.setRelativeBounds(0, 0, width * factor, height * factor);
	}
}
