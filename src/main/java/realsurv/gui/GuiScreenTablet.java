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
import realsurv.tabletos.TabletOS;
import won983212.guitoolkit.font.Alpha8Texture;
import won983212.guitoolkit.font.TrueTypeFont;

public class GuiScreenTablet extends GuiScreen {
	private static TabletOS system = ClientEventHandler.instance.getTabletContext();

	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		system.setWindowSize(width, height);
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	@Override
	public void onResize(Minecraft mcIn, int w, int h) {
		super.onResize(mcIn, w, h);
		system.setWindowSize(w, h);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		system.moveMouseTo(mouseX, mouseY);

		Tessellator tes = Tessellator.getInstance();
		BufferBuilder buf = tes.getBuffer();
		int minX = (int) ((width - TabletOS.WIDTH / 2) / 2);
		int maxX = (int) (minX + TabletOS.WIDTH / 2);
		int minY = (int) ((height - TabletOS.HEIGHT / 2) / 2);
		int maxY = (int) (minY + TabletOS.HEIGHT / 2);

		GlStateManager.disableTexture2D();
		GlStateManager.color(0.3f, 0.3f, 0.3f, 1);
		buf.begin(7, DefaultVertexFormats.POSITION);
		buf.pos(maxX + 1, maxY + 1, 0).endVertex();
		buf.pos(maxX + 1, minY - 1, 0).endVertex();
		buf.pos(minX - 1, minY - 1, 0).endVertex();
		buf.pos(minX - 1, maxY + 1, 0).endVertex();
		tes.draw();
		GlStateManager.color(0.08f, 0.08f, 0.08f, 1);
		buf.begin(7, DefaultVertexFormats.POSITION);
		buf.pos(maxX, maxY, 0).endVertex();
		buf.pos(maxX, minY, 0).endVertex();
		buf.pos(minX, minY, 0).endVertex();
		buf.pos(minX, maxY, 0).endVertex();
		tes.draw();
		GlStateManager.enableTexture2D();

		GlStateManager.color(1, 1, 1, 1);
		ClientEventHandler.instance.bindTabletScreenTexture();
		buf.begin(7, DefaultVertexFormats.POSITION_TEX);
		buf.pos(maxX, maxY, 0).tex(1, 0).endVertex();
		buf.pos(maxX, minY, 0).tex(1, 1).endVertex();
		buf.pos(minX, minY, 0).tex(0, 1).endVertex();
		buf.pos(minX, maxY, 0).tex(0, 0).endVertex();
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
