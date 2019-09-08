package realsurv.tabletos;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import realsurv.tabletos.ui.UIPanel;

public class TabletOS {
	public static final int WIDTH = 160*2;
	public static final int HEIGHT = 90*2;
	private Framebuffer fbo = null;
	private SceneMainScreen mainScreen = new SceneMainScreen();
	
	private void initializeFramebuffer() {
		if(fbo == null) {
			fbo = new Framebuffer(WIDTH, HEIGHT, true);
		}
	}
	
	public void bindFrameBufferTexture() {
		initializeFramebuffer();
		fbo.bindFramebufferTexture();
	}
	
	public void updateScreen() {
		initializeFramebuffer();
		fbo.bindFramebuffer(true);
		GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0D, WIDTH, HEIGHT, 0.0D, 1000.0D, 3000.0D);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        GlStateManager.clearColor(0, 0, 0, 0);
        GlStateManager.clear(GL11.GL_COLOR_BUFFER_BIT);
        GlStateManager.translate(0.0F, 0.0F, -2000.0F);
        GlStateManager.enableTexture2D();
        mainScreen.render();
        Minecraft.getMinecraft().getFramebuffer().bindFramebuffer(true);
	}
	
	public void keyTyped(char typedChar, int keyCode) {
		mainScreen.keyTyped(typedChar, keyCode);
	}

	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		mainScreen.mouseClicked(mouseX, mouseY, mouseButton);
	}

	public void mouseReleased(int mouseX, int mouseY, int state) {
		mainScreen.mouseReleased(mouseX, mouseY, state);
	}

	public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		mainScreen.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
	}
}
 