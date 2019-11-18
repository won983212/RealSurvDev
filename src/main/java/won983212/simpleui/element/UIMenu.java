package won983212.simpleui.element;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;

import won983212.simpleui.HorizontalArrange;
import won983212.simpleui.animation.Animation;
import won983212.simpleui.animation.IntAnimation;
import won983212.simpleui.events.IButtonEvent;
import won983212.simpleui.events.IItemSelectedEvent;
import won983212.simpleui.parentelement.StackPanel;
import won983212.simpleui.parentelement.UIObject;

public class UIMenu extends StackPanel implements IButtonEvent {
	private ArrayList<String> items = new ArrayList<String>();
	private IItemSelectedEvent event;
	private IntAnimation menuOpenAnimation = new IntAnimation(Animation.MENUOPEN_DURATION);
	private boolean hideAfterEnding = false;

	public UIMenu() {
		setHorizontalArrange(HorizontalArrange.LEFT);
		setShadowVisible(true);
	}

	public UIMenu setItemSelectedEvent(IItemSelectedEvent e) {
		this.event = e;
		return this;
	}

	public void addItem(String item) {
		add(new UITextButton(item).setClickEvent(this).setShadowVisible(false).setRadius(0).setMetadata(items.size()));
		items.add(item);
	}

	public void addAll(Collection<? extends String> col) {
		for (String str : col)
			add(new UITextButton(str).setMetadata(uiList.size()));
		items.addAll(col);
	}

	public void clearItem() {
		items.clear();
		uiList.clear();
	}

	@Override
	public void onClicked(UIAbstractButton button, int buttontype) {
		setVisible(false);
		if (event != null) {
			event.onSelected(this, button.metadata);
		}
	}

	@Override
	public void render(int mouseX, int mouseY) {
		if (menuOpenAnimation.isAnimating()) {
			menuOpenAnimation.setRange(0, getMeasuredMinSize().height);
			renderArcRect(0, 0, getBoundsSize().width, menuOpenAnimation.get(), 0, backgroundColor, false);
		} else {
			if(hideAfterEnding) {
				hideAfterEnding = false;
				super.setVisible(false);
				return;
			}
			super.render(mouseX, mouseY);
		}
	}

	@Override
	public UIObject setVisible(boolean visible) {
		if(visible) {
			super.setVisible(true);
		} else {
			hideAfterEnding = true;
		}
		menuOpenAnimation.play(!visible);
		return this;
	}
}
