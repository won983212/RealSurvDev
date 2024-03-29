package won983212.korpatch;

import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenAddServer;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import won983212.korpatch.engine.KoreanInputEngine;
import won983212.korpatch.ui.IMEPopupViewerPane;
import won983212.korpatch.wrapper.GuiTextfieldWrapper;
import won983212.korpatch.wrapper.TextfieldFinder;

public class ClientSideEventHandler {
	public static final ClientSideEventHandler instance = new ClientSideEventHandler();
	private List<GuiTextfieldWrapper> wrappers = null;
	private GuiTextfieldWrapper lastEditing = null;
	private KoreanInputEngine currentEngine = null;
	private IMEPopupViewerPane imeStatusPopup;
	
	@SubscribeEvent
	public void onGuiInit(GuiScreenEvent.InitGuiEvent.Post e) {
		wrappers = TextfieldFinder.getTextfieldWrappers(Minecraft.getMinecraft().currentScreen);
		imeStatusPopup = new IMEPopupViewerPane();
	}

	@SubscribeEvent
	public void onGuiKeyboard(GuiScreenEvent.KeyboardInputEvent.Pre e) {
		int i = Keyboard.getEventKey();
		char c = Keyboard.getEventCharacter();
		if (Keyboard.getEventKeyState()) {
			//TODO DebugKey
			if(GuiScreen.isCtrlKeyDown() && i == Keyboard.KEY_T) {
				Minecraft.getMinecraft().displayGuiScreen(new GuiScreenAddServer(e.getGui(), new ServerData("Minecraft server", "", false)));
				return;
			}
			
			GuiTextfieldWrapper textfield = getFocusedWrapper();
			if(imeStatusPopup != null)
				imeStatusPopup.onKeyTyped(i, c);
			processFocusChanged(textfield);
			
			if (textfield != null) {
				if (KoreanInputEngine.isKorMode()) {
					if (i == Keyboard.KEY_LEFT || i == Keyboard.KEY_RIGHT || i == Keyboard.KEY_RETURN) {
						currentEngine.clearAssembleCache();
					}
				}
				if (currentEngine.handleKeyTyped(c, i)) {
					e.setCanceled(true);
				}
			}
		}
	}

	@SubscribeEvent
	public void onGuiMouseEventPre(GuiScreenEvent.MouseInputEvent.Pre e) {
		if(imeStatusPopup != null && imeStatusPopup.handleMouseInput(e.getGui()))
			e.setCanceled(true);
	}
	
	@SubscribeEvent
	public void onGuiMouseEvent(GuiScreenEvent.MouseInputEvent.Post e) {
		processFocusChanged(getFocusedWrapper());
	}
	
	@SubscribeEvent
	public void onGuiDrawPost(GuiScreenEvent.DrawScreenEvent.Post e) {
		if(imeStatusPopup != null)
			imeStatusPopup.render(e.getMouseX(), e.getMouseY());
	}
	
	private void processFocusChanged(GuiTextfieldWrapper textfield) {
		if (textfield != null) {
			imeStatusPopup.setVisible(true);
			if (lastEditing != textfield) {
				if (currentEngine != null)
					currentEngine.cancelAssemble();
				currentEngine = new KoreanInputEngine(textfield);
				textfield.setIMEStatusBarLocation(imeStatusPopup);
				lastEditing = textfield;
			}
		} else {
			imeStatusPopup.setVisible(false);
		}
	}
	
	private GuiTextfieldWrapper getFocusedWrapper() {
		if (wrappers != null) {
			for (GuiTextfieldWrapper wrapper : wrappers) {
				if (wrapper.isFocused())
					return wrapper;
			}
		}
		return null;
	}
}
