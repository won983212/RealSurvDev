package kp.gui.screen.dialog;

import kp.Config;
import kp.gui.events.EventDialog;
import kp.inputs.keyboard.KeyLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class ConfigAskDialog extends GuiDialog implements EventDialog
{
	private String message = "현재 사용하고 계신 키보드 배열을 선택해주세요.\n잘 모른다면 §c두벌식§7을 선택하세요.";
	private String buttons = "모르겠습니다.," + KeyLoader.getKeyLoader().getKeyboardsToString();
	private int channel = 0;

	public ConfigAskDialog(GuiScreen parent)
	{
		super(parent);
		setCenterText(true);
		setEvent(this);
	}

	public String getTitle()
	{
		return "사용자 맞춤 설정";
	}

	public String getMessages()
	{
		return this.message;
	}

	public String getButtons()
	{
		return this.buttons;
	}

	public void onDialogClosed(int buttonIndex)
	{
		if (this.channel == 0)
		{
			if (buttonIndex == 0)
			{
				buttonIndex = 1;
			}
			Config.set(Config.TYPE_KEYARRAY, buttonIndex - 1);
			KeyLoader.getKeyLoader().setKeyboard(buttonIndex - 1);

			this.message = "룻트의 한글패치에서는 폰트 기능을 제공하고있습니다.\n§c폰트 기능을 사용§7하시겠습니까?\n(채팅창 - f5 - 폰트설정에서 언제든지 바꿀 수 있습니다.)";
			this.buttons = "네.,아니오.";
		}
		else if (this.channel == 1)
		{
			Config.set(Config.USE_FONT, buttonIndex == 0);
			this.message = "한/영키는 §c§l왼쪽 컨트롤§7입니다.\n바꾸고 싶으시면 §c채팅창 - F5§7를 누르시고 §c키 설정§7에서 바꿔주세요.";
			this.buttons = "확인";
		}
		else if (this.channel == 2)
		{
			Minecraft.getMinecraft().displayGuiScreen(null);
			Config.saveConfig();
		}

		initGui();
		this.channel += 1;
	}
}
