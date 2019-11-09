package won983212.simpleui.element;

import java.util.ArrayList;
import java.util.Collection;

import won983212.simpleui.HorizontalArrange;
import won983212.simpleui.StackPanel;
import won983212.simpleui.animation.IntAnimation;
import won983212.simpleui.events.IButtonEvent;
import won983212.simpleui.events.IItemSelectedEvent;

public class UIMenu extends StackPanel implements IButtonEvent {
	private ArrayList<String> items = new ArrayList<String>();
	private IItemSelectedEvent event;
	
	public UIMenu() {
		setHorizontalArrange(HorizontalArrange.LEFT);
		setShadowVisible(true);
	}
	
	public UIMenu setItemSelectedEvent(IItemSelectedEvent e) {
		this.event = e;
		return this;
	}
	
	public void addItem(String item) {
		add(new UIButton(item).setClickEvent(this).setShadowVisible(false).setRadius(0).setMetadata(items.size()));
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
