package realsurv.tabletos;

import java.awt.Rectangle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import realsurv.font.TTFRenderer;

public class GuiCompatibleTextfield extends GuiTextField {
	public GuiCompatibleTextfield(int componentId, TTFRenderer font) {
		super(componentId, font.setDisableShadow(), 0, 0, 10, 10);
	}
	
	public void drawAtBounds(Rectangle bounds) {
		this.x = bounds.x;
		this.y = bounds.y;
		this.width = bounds.width;
		this.height = bounds.height;
		drawTextBox();
	}
}
