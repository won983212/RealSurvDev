package realsurv.tabletos.ui;

import java.util.ArrayList;
import java.util.Collection;

import realsurv.tabletos.HorizontalArrange;
import realsurv.tabletos.ui.events.IButtonEvent;
import realsurv.tabletos.ui.events.IItemSelectedEvent;

public class UIMenu extends StackPanel implements IButtonEvent {
	private ArrayList<String> items = new ArrayList<String>();
	private IItemSelectedEvent event;
	
	public UIMenu() {
		setHorizontalArrange(HorizontalArrange.LEFT);
	}
	
	public UIMenu setItemSelectedEvent(IItemSelectedEvent e) {
		this.event = e;
		return this;
	}
	
	public void addItem(String item) {
		add(new UIButton(item).setClickEvent(this).setMetadata(items.size()));
		items.add(item);
	}
	
	public void addAll(Collection<? extends String> col) {
		for(String str : col)
			add(new UIButton(str).setMetadata(uiList.size()));
		items.addAll(col);
	}
	
	public void clearItem() {
		items.clear();
		uiList.clear();
	}

	@Override
	public void onClicked(UIButton button, int buttontype) {
		setVisible(false);
		if(event != null) {
			event.onSelected(this, button.metadata);
		}
	}
}
