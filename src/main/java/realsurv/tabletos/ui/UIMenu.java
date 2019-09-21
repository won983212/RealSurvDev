package realsurv.tabletos.ui;

import java.util.ArrayList;
import java.util.Collection;

public class UIMenu extends UIPanel {
	private ArrayList<String> items = new ArrayList<String>();
	private StackPanel panel = new StackPanel();

	public UIMenu() {
		add(panel);
	}
	
	public void addItem(String item) {
		items.add(item);
		panel.add(new UIButton(item));
	}
	
	public void addAll(Collection<? extends String> col) {
		items.addAll(col);
		for(String str : col)
			panel.add(new UIButton(str));
	}
}
