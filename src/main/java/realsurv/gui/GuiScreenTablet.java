package realsurv.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
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
import realsurv.font.BufferedImageTexture;
import realsurv.font.TrueTypeFont;
import realsurv.tabletos.TabletOS;

public class GuiScreenTablet extends GuiScreen {
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
		ScaledResolution r = new ScaledResolution(Minecraft.getMinecraft());
		return new Point2d(TabletOS.WIDTH / r.getScaleFactor(), TabletOS.HEIGHT / r.getScaleFactor());
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
		
		Point2d size = getScreenSize();
		Tessellator tes = Tessellator.getInstance();
		BufferBuilder buf = tes.getBuffer();

		int minX = (int) ((width-size.x)/2);
		int maxX = (int) (minX + size.x);
		int minY = (int) ((height-size.y)/2);
		int maxY = (int) (minY + size.y);
		
		GlStateManager.disableTexture2D();
		GlStateManager.color(0.3f, 0.3f, 0.3f, 1);
		buf.begin(7, DefaultVertexFormats.POSITION);
		buf.pos(minX-1, maxY+1, 0).endVertex();
		buf.pos(minX-1, minY-1, 0).endVertex();
		buf.pos(maxX+1, minY-1, 0).endVertex();
		buf.pos(maxX+1, maxY+1, 0).endVertex();
		tes.draw();
		GlStateManager.color(0.08f, 0.08f, 0.08f, 1);
		buf.begin(7, DefaultVertexFormats.POSITION);
		buf.pos(minX, maxY, 0).endVertex();
		buf.pos(minX, minY, 0).endVertex();
		buf.pos(maxX, minY, 0).endVertex();
		buf.pos(maxX, maxY, 0).endVertex();
		tes.draw();
		GlStateManager.enableTexture2D();
		
		GlStateManager.color(1, 1, 1, 1);
		ClientEventHandler.instance.bindTabletScreenTexture();
		buf.begin(7, DefaultVertexFormats.POSITION_TEX);
		buf.pos(minX, maxY, 0).tex(0, 0).endVertex();
		buf.pos(minX, minY, 0).tex(0, 1).endVertex();
		buf.pos(maxX, minY, 0).tex(1, 1).endVertex();
		buf.pos(maxX, maxY, 0).tex(1, 0).endVertex();
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
