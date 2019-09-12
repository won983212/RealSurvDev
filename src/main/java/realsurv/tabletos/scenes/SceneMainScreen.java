package realsurv.tabletos.scenes;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;

import realsurv.tabletos.HorizontalArrange;
import realsurv.tabletos.TabletOS;
import realsurv.tabletos.VerticalArrange;
import realsurv.tabletos.ui.GridPanel;
import realsurv.tabletos.ui.UILabel;
import realsurv.tabletos.ui.GridPanel.LengthDefinition;
import realsurv.tabletos.ui.GridPanel.LengthType;
import realsurv.tabletos.ui.UIButton;
import realsurv.tabletos.ui.UIPanel;
import realsurv.tabletos.ui.UIRectangle;

public class SceneMainScreen extends UIPanel {
	private ArrayList<UIPanel> tasks = new ArrayList<UIPanel>();
	private static final Dimension screenSize = new Dimension(TabletOS.WIDTH, TabletOS.HEIGHT);
	
	public SceneMainScreen() {
		GridPanel grid = new GridPanel();
		grid.addColumn(new LengthDefinition(LengthType.ALLOCATED, 1));
		grid.addColumn(new LengthDefinition(LengthType.ALLOCATED, 1));
		grid.addColumn(new LengthDefinition(LengthType.ALLOCATED, 1));
		grid.addRow(new LengthDefinition(LengthType.ALLOCATED, 1));
		grid.addRow(new LengthDefinition(LengthType.ALLOCATED, 1));
		grid.addRow(new LengthDefinition(LengthType.ALLOCATED, 1));
		grid.add(new UIRectangle(0xffff0000).setLayoutPosition(1, 1));
		grid.add(new UIButton("Please click me.").setHorizontalArrange(HorizontalArrange.CENTER).setVerticalArrange(VerticalArrange.CENTER).setLayoutPosition(1, 1));
		add(grid);
		
		invalidateSize();
		setPosition(0, 0);
		setSize(screenSize.width, screenSize.height);
		layout();
	}
	
	@Override
	public Dimension measureSize() {
		return screenSize;
	}
}
