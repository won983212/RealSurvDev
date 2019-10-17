package realsurv.tabletos;

import java.util.ArrayList;


import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import won983212.simpleui.UIPanel;

public class TabletOS {
	public static final int WIDTH = 640;
	public static final int HEIGHT = 430;
	private Framebuffer fbo = null;
	private MainScreen mainScreen = new MainScreen();
	private int lastMouseX = 0;
	private int lastMouseY = 0;
	
	private void initializeFramebuffer() {
		if(fbo == null) {
			fbo = new Framebuffer(WIDTH, HEIGHT, true);
		}
	}
	
	public void bindFrameBufferTexture() {
		initializeFramebuffer();
		fbo.bindFramebufferTexture();
	}
	
	public void moveMouseTo(int x, int y) {
		lastMouseX = x;
		lastMouseY = y;
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
        mainScreen.render(lastMouseX, lastMouseY);
        Minecraft.getMinecraft().getFramebuffer().bindFramebuffer(true);
	}
	
	public void keyTyped(int keyCode, char typedChar) {
		mainScreen.onKeyTyped(keyCode, typedChar);
	}

	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		mainScreen.onPress(mouseX, mouseY, mouseButton);
	}

	public void mouseReleased(int mouseX, int mouseY, int state) {
		mainScreen.onRelease(mouseX, mouseY, state);
	}

	public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		mainScreen.onDrag(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
	}

	public void setWindowSize(int w, int h) {
		mainScreen.setNativeWindowSize(w, h);
	}
}
 