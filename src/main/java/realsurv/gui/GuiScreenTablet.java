package realsurv.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import realsurv.ClientEventHandler;
import realsurv.CommonProxy;
import realsurv.tabletos.TabletOS;

public class GuiScreenTablet extends GuiScreen {
	private static ItemStack dummy = new ItemStack(CommonProxy.tablet);
	private static TabletOS system = ClientEventHandler.instance.getTabletContext();
	
	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		
		double scale = Math.min(width/10.0, height/10.0);
		GlStateManager.pushMatrix();
		GlStateManager.translate(width/2, height/2, 0);
		GlStateManager.scale(scale, scale, 1);
		GlStateManager.translate(-8, -8, 0);
		Minecraft.getMinecraft().getRenderItem().renderItemIntoGUI(dummy, 0, 0);
		GlStateManager.translate(-0.4, -0.55, 100);
		GlStateManager.scale(1.05, 1.05, 1);
		ClientEventHandler.instance.bindTabletScreenTexture();
		
		Tessellator tes = Tessellator.getInstance();
		BufferBuilder buf = tes.getBuffer();
		buf.begin(7, DefaultVertexFormats.POSITION_TEX);
		buf.pos(2, 5, 0).tex(0, 1).endVertex();
		buf.pos(2, 11, 0).tex(0, 0).endVertex();
		buf.pos(14, 11, 0).tex(1, 0).endVertex();
		buf.pos(14, 5, 0).tex(1, 1).endVertex();
		tes.draw();
		GlStateManager.popMatrix();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		system.keyTyped(typedChar, keyCode);
	}

	//TODO Mousex and mousey must be converted to tablet position.
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		system.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);
		system.mouseReleased(mouseX, mouseY, state);
	}

	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
		system.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
	}
	
}
