package won983212.korpatch;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import won983212.korpatch.wrapper.GuiTextfieldWrapper;
import won983212.korpatch.wrapper.TextfieldFinder;

public class ClientSideEventHandler {
	public static final ClientSideEventHandler instance = new ClientSideEventHandler();

	@SubscribeEvent
	public void onGuiKeyboard(GuiScreenEvent.KeyboardInputEvent.Pre e) {
		if (Keyboard.getEventKeyState()) {
			GuiScreen screen = Minecraft.getMinecraft().currentScreen;
			GuiTextfieldWrapper textfield = TextfieldFinder.getTextfieldWrapper(screen);
			if (textfield != null) {
				e.setCanceled(true);
				textfield.setText(textfield.getText() + "(" + Keyboard.getEventCharacter() + ")");
			}
		}
	}
}
