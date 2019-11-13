package won983212.korpatch;

import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import won983212.korpatch.engine.KoreanInputEngine;
import won983212.korpatch.wrapper.GuiTextfieldWrapper;
import won983212.korpatch.wrapper.TextfieldFinder;

public class ClientSideEventHandler {
	public static final ClientSideEventHandler instance = new ClientSideEventHandler();
	private List<GuiTextfieldWrapper> wrappers = null;
	private GuiTextfieldWrapper lastEditing = null;
	private KoreanInputEngine currentEngine = null;

	@SubscribeEvent
	public void onGuiInit(GuiScreenEvent.InitGuiEvent.Post e) {
		wrappers = TextfieldFinder.getTextfieldWrappers(Minecraft.getMinecraft().currentScreen);
	}

	@SubscribeEvent
	public void onGuiKeyboard(GuiScreenEvent.KeyboardInputEvent.Pre e) {
		int i = Keyboard.getEventKey();
		if (Keyboard.getEventKeyState()) {
			GuiTextfieldWrapper textfield = getFocusedWrapper();
			if (textfield != null) {
				if(lastEditing != textfield) {
					if(currentEngine != null)
						currentEngine.cancelAssemble();
					currentEngine = new KoreanInputEngine(textfield);
					lastEditing = textfield;
				}
				if (KoreanInputEngine.isKorMode()) {
					if (i == Keyboard.KEY_LEFT || i == Keyboard.KEY_RIGHT || i == Keyboard.KEY_RETURN)
						currentEngine.cancelAssemble();
				}
				if(currentEngine.handleKeyTyped(Keyboard.getEventCharacter(), i))
					e.setCanceled(true);
			}
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
