package realsurv.gui;

import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;

import javax.vecmath.Point2d;

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
	
	private Point2d getScreenSize() {
		double scale = Math.min(width, height * (double) TabletOS.WIDTH / TabletOS.HEIGHT);
		return new Point2d(TabletOS.WIDTH, TabletOS.HEIGHT);
	}
	
	private Point guiCoordsToScreen(int x, int y) {
		Point2d size = getScreenSize();
		x -= (width-size.x)/2;
		y -= (height-size.y)/2;
		x = (int) (TabletOS.WIDTH * x / size.x);
		y = (int) (TabletOS.HEIGHT * y / size.y);
		if(x < 0 || x > TabletOS.WIDTH || y < 0 || y > TabletOS.HEIGHT)
			return null;
		return new Point(x, y);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		Point p = guiCoordsToScreen(mouseX, mouseY);
		if(p != null)
			system.moveMouseTo(p.x, p.y);
		
		double scale = Math.min(width/10.0, height/10.0);
		GlStateManager.pushMatrix();
		GlStateManager.translate(width/2, height/2, 0);
		GlStateManager.scale(scale, scale, 1);
		GlStateManager.translate(-8, -8, 0);
		Minecraft.getMinecraft().getRenderItem().renderItemIntoGUI(dummy, 0, 0);
		GlStateManager.translate(-0.4, -0.55, 100);
		GlStateManager.scale(1.05, 1.05, 1);
		ClientEventHandler.instance.bindTabletScreenTexture();
		
		Point2d size = getScreenSize();
		Tessellator tes = Tessellator.getInstance();
		BufferBuilder buf = tes.getBuffer();

		GlStateManager.disableTexture2D();
		GlStateManager.color(0.08f, 0.08f, 0.08f, 1);
		buf.begin(7, DefaultVertexFormats.POSITION);
		buf.pos((width-size.x)/2, (height-size.y)/2, 0).endVertex();
		buf.pos((width-size.x)/2, (height+size.y)/2, 0).endVertex();
		buf.pos((width+size.x)/2, (height+size.y)/2, 0).endVertex();
		buf.pos((width+size.x)/2, (height-size.y)/2, 0).endVertex();
		tes.draw();
		GlStateManager.enableTexture2D();
		
		GlStateManager.color(1, 1, 1, 1);
		ClientEventHandler.instance.bindTabletScreenTexture();
		buf.begin(7, DefaultVertexFormats.POSITION_TEX);
		buf.pos((width-size.x)/2, (height-size.y)/2, 0).tex(0, 1).endVertex();
		buf.pos((width-size.x)/2, (height+size.y)/2, 0).tex(0, 0).endVertex();
		buf.pos((width+size.x)/2, (height+size.y)/2, 0).tex(1, 0).endVertex();
		buf.pos((width+size.x)/2, (height-size.y)/2, 0).tex(1, 1).endVertex();
		tes.draw();
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		system.keyTyped(keyCode, typedChar);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		Point pos = guiCoordsToScreen(mouseX, mouseY);
		if(pos != null)
			system.mouseClicked(pos.x, pos.y, mouseButton);
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);
		Point pos = guiCoordsToScreen(mouseX, mouseY);
		if(pos != null)
			system.mouseReleased(pos.x, pos.y, state);
	}

	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
		Point pos = guiCoordsToScreen(mouseX, mouseY);
		if(pos != null)
			system.mouseClickMove(pos.x, pos.y, clickedMouseButton, timeSinceLastClick);
	}
	
}
