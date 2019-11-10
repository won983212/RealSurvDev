package kp.gui.screen.dialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import kp.gui.components.Component;
import kp.gui.events.EventDialog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public abstract class GuiDialog extends GuiScreen
{
	public GuiScreen parentScreen;
	protected List<Component> components = new ArrayList<Component>();
	private int layout = 30;
	private Minecraft mc;
	private EventDialog event;
	private boolean isCenterText = false;

	public GuiDialog(GuiScreen parent)
	{
		this.parentScreen = parent;
		this.mc = Minecraft.getMinecraft();
	}

	public void initGui()
	{
		if (getButtons() != null)
		{
			int i = 0;
			this.buttonList.clear();
			this.components.clear();
			for (String name : getButtons().split(","))
			{
				this.buttonList.add(new GuiButton(i, this.layout + 10 + i++ * 90, this.height - this.layout - 23, 80, 20, name));
			}
		}
	}

	public GuiDialog setLayout(int i)
	{
		this.layout = i;
		initGui();

		return this;
	}

	public GuiDialog setEvent(EventDialog event)
	{
		this.event = event;

		return this;
	}

	public GuiDialog setCenterText(boolean isCenter)
	{
		this.isCenterText = isCenter;

		return this;
	}

	public void drawScreen(int par1, int par2, float par3)
	{
		this.parentScreen.drawScreen(0, 0, par3);

		drawRect(0, 0, this.width, this.height, 1610612736);
		drawRect(this.layout, this.layout, this.width - this.layout, this.height - this.layout, -1442840576);
		drawRect(this.layout, this.layout, this.width - this.layout, this.layout + 18, -1437248171);

		if (getTitle() != null)
		{
			drawCenteredString(this.mc.fontRenderer, getTitle(), this.width / 2, this.layout + (18 - this.mc.fontRenderer.FONT_HEIGHT) / 2, -1);
		}

		String[] messages;
		if (getMessages() != null)
		{
			messages = getMessages().split("\n");
			int offsetY = (this.height - this.layout * 2 - 48 - messages.length * 15) / 2;
			for (int a = 0; a < messages.length; a++)
			{
				if (this.isCenterText)
				{
					drawCenteredString(this.mc.fontRenderer, "§7" + messages[a], this.width / 2, this.layout + 25 + a * 15 + offsetY, -1);
				}
				else
				{
					drawString(this.mc.fontRenderer, "§7" + messages[a], this.layout + 10, this.layout + 25 + a * 15, -1);
				}
			}
		}

		for (Component comp : this.components)
		{
			comp.draw();
		}

		super.drawScreen(par1, par2, par3);
	}

	public void handleInput() throws IOException
	{
		if (Mouse.isCreated())
		{
			while (Mouse.next())
			{
				int i = Mouse.getEventX() * this.width / this.mc.displayWidth;
				int j = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
				int k = Mouse.getEventButton();

				if (Mouse.getEventButtonState())
				{
					mouseClicked(i, j, k);
					for (Component comp : this.components)
					{
						comp.onClick(i, j, k);
					}
				}
				else if (k != -1)
				{
					mouseReleased(i, j, k);
					for (Component comp : this.components)
					{
						comp.onClick(i, j, k);
					}
				}
				else
				{
					for (Component comp : this.components)
					{
						comp.onMove(i, j);
					}
				}
			}
		}

		if (Keyboard.isCreated())
		{
			while (Keyboard.next())
			{
				if (Keyboard.getEventKeyState())
				{
					keyTyped(Keyboard.getEventCharacter(), Keyboard.getEventKey());
					for (Component comp : this.components)
					{
						comp.onTyped(Keyboard.getEventCharacter(), Keyboard.getEventKey());
					}
				}
				this.mc.dispatchKeypresses();
			}
		}
	}

	protected void actionPerformed(GuiButton bt)
	{
		if (this.event != null)
		{
			this.event.onDialogClosed(bt.id);
		}
	}

	public abstract String getTitle();

	public abstract String getMessages();

	public abstract String getButtons();
}
