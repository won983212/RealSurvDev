package kp.gui.components;

import java.awt.Color;

import kp.gui.events.Event;
import kp.gui.events.components.EventChange;
import kp.gui.tab.Tab;
import kp.utils.IconLoader;
import kp.utils.Log;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

public class LootColorPicker extends Component implements EventChange
{
	private float[] hsbColor = new float[3];
	private float angle;
	private boolean editing = false;
	private int mx;
	private int my;

	private LootLabeledTextBox tb;
	private LootLightScrollBar sat;
	private LootLightScrollBar bright;

	public LootColorPicker(Tab tab, int id)
	{
		this(tab, id, 0);
	}

	public LootColorPicker(Tab tab, int id, int def)
	{
		super(tab, id);

		this.tb = new LootLabeledTextBox(tab, 0, 40);
		this.tb.setEffectByMasterXY(false);
		this.tb.setEvent(this);
		this.tb.getTextField().setTextColor(0xffffaa00);

		this.sat = new LootLightScrollBar(tab, 1, 40, true);
		this.sat.setGradient(-1, 0xFF000000 | getRGBColor());
		this.sat.setEffectByMasterXY(false);
		this.sat.setEvent(this);

		this.bright = new LootLightScrollBar(tab, 2, 40, true);
		this.bright.setGradient(-16777216, -1);
		this.bright.setEffectByMasterXY(false);
		this.bright.setEvent(this);

		setColorRGB(def);
	}

	public void setColor(float h, float s, float b)
	{
		this.hsbColor[0] = h;
		this.hsbColor[1] = s;
		this.hsbColor[2] = b;
		applyScroll();
		this.tb.setText("#" + fillHexZero(Integer.toHexString(getRGBColor()).substring(2)));

		callEvent();
	}

	public void callEvent()
	{
		if (hasEvent())
		{
			Event ev = getEvent();
			if ((ev instanceof EventChange))
			{
				((EventChange) ev).onChange(this.id, Integer.valueOf(getRGBColor()));
			}
			else
			{
				Log.error("Event is not EventChange");
			}
		}
	}

	public void setColorRGB(int color)
	{
		int r = color >> 16 & 0xFF;
		int g = color >> 8 & 0xFF;
		int b = color & 0xFF;
		float[] hsb = Color.RGBtoHSB(r, g, b, null);

		setColor(hsb[0], hsb[1], hsb[2]);
	}

	public int getRGBColor()
	{
		return Color.HSBtoRGB(this.hsbColor[0], this.hsbColor[1], this.hsbColor[2]);
	}

	public void setEnabled(boolean e)
	{
		super.setEnabled(e);
		this.tb.setEnabled(e);
		this.sat.setEnabled(e);
		this.bright.setEnabled(e);

		if (e)
		{
			this.tb.getTextField().setTextColor(0xffffaa00);
		}
		else
		{
			this.tb.getTextField().setTextColor(0xffaaaaaa);
		}
	}

	public void draw()
	{
		Gui gui = getGui();

		IconLoader.drawTexture(gui, getX(), getY(), 4, 1, getRGBColor());
		if (this.editing)
		{
			setColor(-this.angle / 360.0F, 1.0F, 1.0F);
			IconLoader.drawTexture(gui, getX() - 6, getY() - 6, 2, 2, 16777215, 2, 2);
			GlStateManager.pushMatrix();
			GlStateManager.translate(getX() + 6, getY() + 6, 0);
			GlStateManager.rotate(this.angle, 0.0F, 0.0F, 1.0F);
			IconLoader.drawTexture(gui, -3, -6, 2, 1, 0);
			GlStateManager.popMatrix();
		}
		else
		{
			this.tb.setLocation(getX() + 10, getY() - 1);
			this.tb.draw();

			if ((this.mx >= getX()) && (this.mx <= getX() + 40) && (this.my >= getY() + 10) && (this.my <= getY() + 18))
			{
				this.sat.setLocation(getX(), getY() + 10);
				this.sat.draw();

				this.bright.setLocation(getX(), getY() + 13);
				this.bright.draw();
			}
		}
	}

	private String fillHexZero(String s)
	{
		StringBuilder sb = new StringBuilder();

		for (int a = 0; a < 6 - s.length(); a++)
		{
			sb.append('0');
		}
		sb.append(s);

		return sb.toString();
	}

	private void applyScroll()
	{
		this.sat.setGradient(-1, 0xFF000000 | getRGBColor());
		this.sat.setValue((int) (this.hsbColor[1] * 100.0F));
		this.bright.setValue((int) (this.hsbColor[2] * 100.0F));
	}

	public void onMove(int mx, int my)
	{
		this.mx = mx;
		this.my = my;

		if (this.editing)
		{
			int x = mx - getX();
			int y = my - getY();

			this.angle = ((float) (57.29577951308232D * Math.atan2(y, x)) + 90.0F);
		}

		super.onMove(mx, my);
	}

	public void onClick(int x, int y, int eventType)
	{
		if ((isEnabled()) && (eventType == 1))
		{
			if ((x >= getX()) && (x <= getX() + 6) && (y >= getY()) && (y <= getY() + 6))
			{
				this.editing = true;
				return;
			}

			this.editing = false;
		}
	}

	public void onChange(int id, Object value)
	{
		try
		{
			if (id == 0)
			{
				if ((value instanceof String))
				{
					String str = (String) value;
					if (str.length() == 7)
					{
						setColorRGB(Integer.parseInt(str.substring(1), 16));
						applyScroll();
					}
				}
			}
			else if ((id == 1) || (id == 2))
			{
				this.hsbColor[id] = (Integer.parseInt(String.valueOf(value)) / 100.0F);
				this.tb.setText("#" + fillHexZero(Integer.toHexString(getRGBColor()).substring(2)));
				callEvent();
			}
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
		}
	}
}
