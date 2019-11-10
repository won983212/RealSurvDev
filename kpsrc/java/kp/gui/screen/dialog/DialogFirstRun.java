package kp.gui.screen.dialog;

import kp.KoreanPatch;
import kp.gui.events.EventDialog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class DialogFirstRun extends GuiDialog implements EventDialog
{
	public DialogFirstRun(GuiScreen parent)
	{
		super(parent);
		setEvent(this);
		setLayout(20);
	}

	public String getTitle()
	{
		return "§r한글패치 설치 완료! §6§l(" + KoreanPatch.VERSION + ")";
	}

	public String getMessages()
	{
		return "성공적으로 설치되었습니다!\n\n한/영으로 전환하고 싶으실 때는 §e\"Ctrl\"§7키를 눌러주세요. §c§l채팅 옵션(F5)-키 설정§7에서 바꿀 수 있습니다.\n채팅 옵션은 채팅창에서 §e\"F5\"§7를 누르시면 옵션을 바꾸실 수 있습니다.\n채팅창에서 §e\"Insert\"§7키를 누르면 색을 지정할 수 있지만 &색 코드가 지원하는 서버에서만 사용 가능합니다.\n또 채팅창에서 자음(ㄱ~ㅎ)을 입력하고 §e\"한자키\"§7를 누르면 한자 및 이모티콘을 사용하실 수 있습니다.\n마지막으로, §e\"Shift\"§7를 누르면서 영타로 입력된 채팅에 마우스를 올리면 한타로 변환됩니다.\n\n버그나 아이디어가 있거나 더 많은 설명을 듣고 싶으시면 제작자 블로그로 방문해주세요.\n";
	}

	public String getButtons()
	{
		return "확인,룻트 블로그로..";
	}

	public void onDialogClosed(int buttonIndex)
	{
		if (buttonIndex == 0)
		{
			Minecraft.getMinecraft().displayGuiScreen(new SimpleDialog(this.parentScreen, "알림", "좀 더 편리한 한글패치 이용을 위해서, 사용자에게 맞도록 한글패치를 재설정하고자 합니다.\n동의하시겠습니까?", "예, 아니오", new EventDialog()
			{
				public void onDialogClosed(int buttonIndex)
				{
					if (buttonIndex == 0)
					{
						Minecraft.getMinecraft().displayGuiScreen(new ConfigAskDialog(DialogFirstRun.this.parentScreen));
					}
					else
					{
						Minecraft.getMinecraft().displayGuiScreen(null);
					}
				}
			}).setCenterText(true));
		}
		else if (buttonIndex == 1)
		{
			kp.KoreanPatch.openURL("http://blog.naver.com/won983212");
		}
	}
}
