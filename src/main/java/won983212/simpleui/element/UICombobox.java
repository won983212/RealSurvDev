package won983212.simpleui.element;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;

import won983212.simpleui.UIObject;
import won983212.simpleui.VerticalArrange;
import won983212.simpleui.events.IItemSelectedEvent;
import won983212.simpleui.font.TrueTypeFont;

public class UICombobox extends UIObject implements IItemSelectedEvent {
	private ArrayList<String> items = new ArrayList<String>();
	private IItemSelectedEvent event;
	private UIMenu menu = new UIMenu();
	private int selected = 0;
	private int maxLength = 10;

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
	public void onPress(int x, int y, int bt) {
		Point loc = calculateActualLocation();
		Dimension size = getBoundsSize();
		menu.setVisible(!menu.isVisible());
		menu.setRelativeLocation(loc.x, loc.y + size.height);
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
		
		int color = backgroundColor;
		if(containsRelative(mouseX, mouseY))
			color = getMouseOverColor(color);
		
		renderArcRect(0, 0, size.width, size.height, arc, color, showShadow);
		if(items.size() > selected)
			font.drawString(item, (size.width - font.getStringWidth(item)) / 2, (size.height - font.getMaxHeight()) / 2, foregroundColor);
	}

	@Override
	public void onSelected(UIObject obj, Object item) {
		selected = (int) item;
		if(event != null)
			event.onSelected(this, item);
	}
}