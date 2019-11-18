package won983212.simpleui.element;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import won983212.simpleui.VerticalArrange;
import won983212.simpleui.animation.Animation;
import won983212.simpleui.animation.ColorAnimation;
import won983212.simpleui.events.IItemSelectedEvent;
import won983212.simpleui.font.TrueTypeFont;
import won983212.simpleui.parentelement.UIObject;

public class UICombobox extends UIObject implements IItemSelectedEvent {
	private ArrayList<String> items = new ArrayList<String>();
	private IItemSelectedEvent event;
	private UIMenu menu = new UIMenu();
	private int selected = 0;
	private int maxLength = 10;
	private ColorAnimation hoverColorAnimation = new ColorAnimation(Animation.MOUSELEAVE_DURATION);
	private boolean isEnteredMouse = false;

	public UICombobox() {
		menu.setVisible(false);
		menu.setItemSelectedEvent(this);
		setVerticalArrange(VerticalArrange.CENTER);
	}
	
	public UICombobox add(String item) {
		items.add(item);
		menu.addItem(item);
		maxLength = Math.max(maxLength, getFont().getStringWidth(item));
		setMinimumSize(maxLength + 12, getFont().getMaxHeight() + 2);
		return this;
	}

	public UICombobox addAll(Collection<? extends String> c) {
		items.addAll(c);
		menu.addAll(c);
		maxLength = 0;
		for (String str : items)
			maxLength = Math.max(maxLength, getFont().getStringWidth(str));
		setMinimumSize(maxLength + 12, 11);
		return this;
	}
	
	@Override
	public void arrange(Rectangle available) {
		super.arrange(available);
		if(parentPanel != null) {
			Rectangle rect = getRelativeBounds();
			parentPanel.addPopup(menu.setMinimumSize(rect.width, 0));
		}
	}
	
	@Override
	public boolean onPress(int x, int y, int bt) {
		Point loc = calculateActualLocation();
		Dimension size = getBoundsSize();
		menu.setVisible(!menu.isVisible());
		menu.setRelativeLocation(loc.x, loc.y + size.height);
		return true;
	}

	public int getSelectedIndex() {
		return selected;
	}

	public UICombobox setSelectedIndex(int index) {
		selected = index;
		return this;
	}

	public UICombobox setItemSelectedEvent(IItemSelectedEvent e) {
		this.event = e;
		return this;
	}
	
	public String getSelectedItem() {
		return items.size() > selected ? items.get(selected) : null;
	}

	public UICombobox setSelectedItem(String item) {
		int idx = items.indexOf(item);
		if (idx >= 0)
			selected = idx;
		return this;
	}

	@Override
	public void render(int mouseX, int mouseY) {
		Dimension size = getBoundsSize();
		TrueTypeFont font = getFont();
		String item = getSelectedItem();
		
		boolean isIn = containsRelative(mouseX, mouseY);
		hoverColorAnimation.setRange(backgroundColor, mouseOverColor);
		if(isEnteredMouse != isIn) {
			isEnteredMouse = isIn;
			if(isIn)
				hoverColorAnimation.setTime(0);
			hoverColorAnimation.play(!isIn);
		}
		
		renderArcRect(0, 0, size.width, size.height, arc, hoverColorAnimation.get(), showShadow);
		if(items.size() > selected)
			font.drawString(item, (size.width - font.getStringWidth(item)) / 2, (size.height - font.getMaxHeight()) / 2, foregroundColor);
		renderTriangle(size.width - size.height / 2, size.height / 2, 7, menu.isVisible());
	}
	
	private void renderTriangle(int x, int y, int size, boolean reverse) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(0, 0, 0, 1);
        bufferbuilder.begin(GL11.GL_POLYGON, DefaultVertexFormats.POSITION);
        if(reverse) {
        	bufferbuilder.pos(x, y - size / 2, 0.0D).endVertex();
            bufferbuilder.pos(x + size / 2, y + size / 2, 0.0D).endVertex();
            bufferbuilder.pos(x - size / 2, y + size / 2, 0.0D).endVertex();
        } else {
	        bufferbuilder.pos(x - size / 2, y - size / 2, 0.0D).endVertex();
	        bufferbuilder.pos(x + size / 2, y - size / 2, 0.0D).endVertex();
	        bufferbuilder.pos(x, y + size / 2, 0.0D).endVertex();
        }
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
	}

	@Override
	public void onSelected(UIObject obj, Object item) {
		selected = (int) item;
		if(event != null)
			event.onSelected(this, item);
	}
}
