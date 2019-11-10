package kp.gui.components;

import org.lwjgl.input.Keyboard;

import kp.gui.events.Event;
import kp.gui.events.components.EventChange;
import kp.gui.events.components.EventClick;
import kp.gui.tab.Tab;
import kp.utils.Key;
import net.minecraft.client.gui.Gui;

public class LootKeyBox extends Component implements EventClick
{
	private Key key;
	private LootButton bt;
	private boolean editing = false;
	private int border = 0;

	public LootKeyBox(Tab tab, int id, String text, int def)
	{
		super(tab, id);

		this.key = new Key(text, def);
		this.bt = new LootButton(tab, Keyboard.getKeyName(this.key.getKeyCode()), 0, 40, 12);
		this.bt.setEffectByMasterXY(false);
		this.bt.setEvent(this);

		setBounds(0, 0, 40, 12);
	}

	public LootKeyBox(Gui gui, int id, String text, int def)
	{
		super(gui, id);

		this.key = new Key(text, def);
		this.bt = new LootButton(gui, Keyboard.getKeyName(this.key.getKeyCode()), 0, 40, 12);
		this.bt.setEffectByMasterXY(false);
		this.bt.setEvent(this);

		setBounds(0, 0, 40, 12);
	}

	public void draw()
	{
		int sx = getX() + 6;

		getFontRenderer().drawString(this.key.getLabel(), getX() + 3, getY() + 1, isEnabled() ? -1 : -10066330);
		if (this.border > sx - getX())
		{
			sx += this.border - sx + getX();
		}

		this.bt.setLocation(sx, getY());
		this.bt.draw();

		super.draw();
	}

	public void setSize(int w, int h)
	{
		this.bt.setSize(w, h);
	}

	public void setEnabled(boolean e)
	{
		super.setEnabled(e);
		this.bt.setEnabled(e);
	}

	public void onClick(int x, int y, int eventType)
	{
		super.onClick(x, y, eventType);
		this.bt.onClick(x, y, eventType);
	}

	public void onMove(int mx, int my)
	{
		super.onMove(mx, my);
		this.bt.onMove(mx, my);
	}

	public void onTyped(char c, int key)
	{
		if (this.editing)
		{
			this.key.setKey(key);
			this.bt.setText(Keyboard.getKeyName(key));
			this.editing = false;

			if (hasEvent())
			{
				Event e = getEvent();
				if ((e instanceof EventChange))
				{
					((EventChange) e).onChange(this.id, Integer.valueOf(key));
				}
			}
		}
	}

	public void onAction(int id)
	{
		if ((isEnabled()) && (!this.editing))
		{
			this.bt.setText("입력중..");
			this.editing = true;
		}
	}

	public void setBorder(int border)
	{
		this.border = border;
	}

	public Key getKey()
	{
		return this.key;
	}
}
