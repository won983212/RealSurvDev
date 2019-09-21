package realsurv.tabletos.ui;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;

import net.minecraft.client.gui.Gui;
import realsurv.tabletos.VerticalArrange;

public class UICombobox extends UIObject {
	private ArrayList<String> items = new ArrayList<String>();
	private UIMenu menu = new UIMenu();
	private int selected = 0;
	private int maxLength = 10;

	public UICombobox() {
		menu.setVisible(false);
		setVerticalArrange(VerticalArrange.CENTER);
	}
	
	public UICombobox add(String item) {
		items.add(item);
		menu.addItem(item);
		maxLength = Math.max(maxLength, fontrenderer.getStringWidth(item));
		setMinimumSize(maxLength + 12, 11);
		return this;
	}

	public UICombobox addAll(Collection<? extends String> c) {
		items.addAll(c);
		menu.addAll(c);
		maxLength = 0;
		for (String str : items)
			maxLength = Math.max(maxLength, fontrenderer.getStringWidth(str));
		setMinimumSize(maxLength + 12, 11);
		return this;
	}
	
	@Override
	public void arrange(Rectangle available) {
		super.arrange(available);
		if(panel != null) {
			Rectangle rect = getActualBounds();
			panel.addPopup(menu);
			panel.setPopupLocation(menu, rect.x, rect.y + rect.height);
		}
	}
	
	@Override
	public void onPress(int x, int y, int bt) {
		menu.setVisible(!menu.isVisible());
	}

	public int getSelectedIndex() {
		return selected;
	}

	public UICombobox setSelectedIndex(int index) {
		selected = index;
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
		Rectangle bounds = getActualBounds();
		Gui.drawRect(bounds.x, bounds.y, bounds.x + bounds.width, bounds.y + bounds.height, backgroundColor);
		Gui.drawRect(bounds.x + bounds.width - 11, bounds.y, bounds.x + bounds.width, bounds.y + bounds.height, 0xffcccccc);
		if(items.size() > selected)
			fontrenderer.drawString(getSelectedItem(), bounds.x + 2, bounds.y + 2, foregroundColor);
	}
}
