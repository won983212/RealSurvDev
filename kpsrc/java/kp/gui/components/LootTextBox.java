package kp.gui.components;

import org.lwjgl.input.Mouse;

import kp.gui.events.Event;
import kp.gui.events.components.EventChange;
import kp.gui.tab.Tab;
import kp.utils.Log;
import kp.wrapper.FontRendererWrapper;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiTextField;

public class LootTextBox extends Component
{
	private GuiTextField tf;
	private String hint = "";
	private String typeMap = null;
	private int icon = -1;
	protected int back_color = 0xffffffff;
	protected int back_disabled_color = 0xffaaaaaa;

	public LootTextBox(Tab tab, int id, int width)
	{
		super(tab, id);
		this.tf = new GuiTextField(0, getFontRenderer(), 0, 0, width, 12);
		this.tf.setEnableBackgroundDrawing(false);
		this.tf.setTextColor(0);

		setSize(width, 12);
	}

	public void setEnabled(boolean e)
	{
		super.setEnabled(e);
		this.tf.setEnabled(e);
	}

	public void draw()
	{
		Gui gui = getGui();
		Gui.drawRect(getX(), getY(), getX() + getWidth(), getY() + getHeight(), isEnabled() ? this.back_color : this.back_disabled_color);

		FontRenderer fr = getFontRenderer();
		this.tf.x = ((hasIcon() ? getX() + getHeight() : getX()) + 3);
		this.tf.y = (getY() + (getHeight() - fr.FONT_HEIGHT) / 2 + 1);
		((FontRendererWrapper.PrivateMethodAccessor) fr).setShadowMode(false);
		this.tf.drawTextBox();
		((FontRendererWrapper.PrivateMethodAccessor) fr).setShadowMode(true);

		if (hasIcon())
		{
			kp.utils.IconLoader.drawTexture(gui, getX() + getHeight() / 2 - 6, getY() + getHeight() / 2 - 6, this.icon, 4);
		}

		if ((this.hint.length() != 0) && (this.tf.getText().length() == 0) && (!this.tf.isFocused()))
		{
			fr.drawString(this.hint, this.tf.x, this.tf.y, -10066330);
		}

		super.draw();
	}

	public boolean hasIcon()
	{
		return this.icon != -1;
	}

	public void setHint(String s)
	{
		this.hint = s;
	}

	public void setIcon(int i)
	{
		this.icon = i;
	}

	public void setText(String s)
	{
		this.tf.setText(s);
	}

	public void setTextMap(String map)
	{
		this.typeMap = map;
	}

	public void setBackgroundColor(int color)
	{
		this.back_color = color;
	}

	public GuiTextField getTextField()
	{
		return this.tf;
	}

	public void onClick(int x, int y, int eventType)
	{
		if ((isEnabled()) && (eventType == 0))
		{
			this.tf.mouseClicked(x, y, Mouse.getEventButton());
		}
	}

	public void onTyped(char c, int key)
	{
		if ((this.typeMap != null) && (this.typeMap.indexOf(c) == -1) && (key != 14))
		{
			return;
		}

		if ((isEnabled()) && (this.tf.textboxKeyTyped(c, key)) && (hasEvent()))
		{
			Event e = getEvent();
			if ((e instanceof EventChange))
			{
				((EventChange) e).onChange(this.id, this.tf.getText());
			}
			else
			{
				Log.error("Event is not EventTextbox");
			}
		}
	}
}
