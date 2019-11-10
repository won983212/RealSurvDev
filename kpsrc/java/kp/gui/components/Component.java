package kp.gui.components;

import java.awt.Rectangle;

import kp.gui.events.Event;
import kp.gui.events.EventKeyboard;
import kp.gui.events.EventMouse;
import kp.gui.events.EventMouseMove;
import kp.gui.events.components.EventComponents;
import kp.gui.screen.GuiConfig;
import kp.gui.tab.Tab;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

public class Component extends Gui implements EventMouse, EventMouseMove, EventKeyboard
{
	private final Rectangle bounds = new Rectangle();

	public final int id;
	private EventComponents event = null;
	private static FontRenderer fr;
	private Tab tab;
	private Gui gui;
	private String hint = null;

	private static int masterX = 0;
	private static int masterY = 0;
	private boolean effectMasterBorder = true;

	private boolean isHover = false;
	private boolean enabled = true;

	public Component(Tab tab, int id)
	{
		this.id = id;
		this.tab = tab;

		if (tab != null)
		{
			tab.getEventMouse().pushEvent(this);
			tab.getEventKeyboard().pushEvent(this);
		}
	}

	public Component(Gui gui, int id)
	{
		this.id = id;
		this.gui = gui;
	}

	protected static FontRenderer getFontRenderer()
	{
		if (fr == null)
		{
			fr = Minecraft.getMinecraft().fontRenderer;
		}

		return fr;
	}

	public void setBounds(int x, int y, int w, int h)
	{
		setLocation(x, y);
		setSize(w, h);
	}

	public void setLocation(int x, int y)
	{
		this.bounds.setLocation(x, y);
	}

	public void setSize(int w, int h)
	{
		this.bounds.setSize(w, h);
	}

	public void setEnabled(boolean e)
	{
		this.enabled = e;
	}

	public void setHintText(String hint)
	{
		this.hint = hint;
	}

	public void setEffectByMasterXY(boolean effect)
	{
		this.effectMasterBorder = effect;
	}

	public static void setupMasterPosition(int x, int y)
	{
		masterX = x;
		masterY = y;
	}

	public int getX()
	{
		return (this.effectMasterBorder ? masterX : 0) + this.bounds.x;
	}

	public int getY()
	{
		return (this.effectMasterBorder ? masterY : 0) + this.bounds.y;
	}

	public int getWidth()
	{
		return this.bounds.width;
	}

	public int getHeight()
	{
		return this.bounds.height;
	}

	public boolean isEnabled()
	{
		return this.enabled;
	}

	public boolean isHovered()
	{
		return this.isHover;
	}

	public void setEvent(EventComponents e)
	{
		this.event = e;
	}

	public boolean hasEvent()
	{
		return this.event != null;
	}

	protected Event getEvent()
	{
		return this.event;
	}

	public void onClick(int x, int y, int eventType)
	{
	}

	public void onMove(int mx, int my)
	{
		this.isHover = isInside(mx, my);
	}

	public void onTyped(char c, int key)
	{
	}

	public void draw()
	{
		if ((this.enabled) && (this.hint != null) && (getGui() != null) && (this.isHover))
		{
			((GuiConfig) getGui()).drawTooltip(this.hint, getX(), getY());
			GlStateManager.disableLighting();
		}
	}

	protected Gui getGui()
	{
		if (this.gui != null)
		{
			return this.gui;
		}
		if (this.tab != null)
		{
			this.gui = this.tab.getGraphic();
		}

		return this.gui;
	}

	public boolean isInside(int x, int y)
	{
		if ((x >= getX()) && (x <= getX() + getWidth()) && (y >= getY()) && (y <= getY() + getHeight()))
		{
			return true;
		}

		return false;
	}
}
