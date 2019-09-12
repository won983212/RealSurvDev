package realsurv.tabletos;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import realsurv.tabletos.scenes.SceneMainScreen;
import realsurv.tabletos.ui.UIPanel;

public class TabletOS {
	public static final int WIDTH = 320;
	public static final int HEIGHT = 215;
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
        
        Minecraft mc = Minecraft.getMinecraft();
        final ScaledResolution scaledresolution = new ScaledResolution(mc);
        int i1 = scaledresolution.getScaledWidth();
        int j1 = scaledresolution.getScaledHeight();
        final int k1 = Mouse.getX() * i1 / mc.displayWidth;
        final int l1 = j1 - Mouse.getY() * j1 / mc.displayHeight - 1;
        
        mainScreen.render(k1, l1);
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
}
 