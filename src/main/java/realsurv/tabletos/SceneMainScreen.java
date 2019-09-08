package realsurv.tabletos;

import java.awt.Dimension;
import java.util.ArrayList;

import realsurv.tabletos.ui.UIPanel;
import realsurv.tabletos.ui.DirWeights;
import realsurv.tabletos.ui.HorizontalArrange;
import realsurv.tabletos.ui.PanelStack;
import realsurv.tabletos.ui.PanelStack.Orientation;
import realsurv.tabletos.ui.UILabel;
import realsurv.tabletos.ui.UIRectangle;
import realsurv.tabletos.ui.VerticalArrange;

public class SceneMainScreen extends UIPanel {
	private ArrayList<UIPanel> tasks = new ArrayList<UIPanel>();
	private static final Dimension screenSize = new Dimension(TabletOS.WIDTH, TabletOS.HEIGHT);
	
	public SceneMainScreen() {
//		add(new UIRectangle(0xffff0000).setVerticalArrange(VerticalArrange.TOP));
//		add(new UIRectangle(0xff00ff00).setVerticalArrange(VerticalArrange.CENTER));
//		add(new UIRectangle(0xff0000ff).setVerticalArrange(VerticalArrange.BOTTOM));
//		add(new UIRectangle(0xffffff00).setHorizontalArrange(HorizontalArrange.LEFT));
//		add(new UIRectangle(0xffff00ff).setHorizontalArrange(HorizontalArrange.CENTER));
//		add(new UIRectangle(0xffffffff).setHorizontalArrange(HorizontalArrange.RIGHT));
//		add(new UILabel("LeftTop").setHorizontalArrange(HorizontalArrange.LEFT).setVerticalArrange(VerticalArrange.TOP));
//		add(new UILabel("RightTop").setHorizontalArrange(HorizontalArrange.RIGHT).setVerticalArrange(VerticalArrange.TOP));
//		add(new UILabel("CenterTop").setHorizontalArrange(HorizontalArrange.CENTER).setVerticalArrange(VerticalArrange.TOP));
//		add(new UILabel("LeftCenter").setHorizontalArrange(HorizontalArrange.LEFT).setVerticalArrange(VerticalArrange.CENTER));
//		add(new UILabel("RightCenter").setHorizontalArrange(HorizontalArrange.RIGHT).setVerticalArrange(VerticalArrange.CENTER));
//		add(new UILabel("CenterCenter").setHorizontalArrange(HorizontalArrange.CENTER).setVerticalArrange(VerticalArrange.CENTER));
//		add(new UILabel("LeftBottom").setHorizontalArrange(HorizontalArrange.LEFT).setVerticalArrange(VerticalArrange.BOTTOM));
//		add(new UILabel("RightBottom").setHorizontalArrange(HorizontalArrange.RIGHT).setVerticalArrange(VerticalArrange.BOTTOM));
//		add(new UILabel("CenterBottom").setHorizontalArrange(HorizontalArrange.CENTER).setVerticalArrange(VerticalArrange.BOTTOM));
		PanelStack rootpanel = new PanelStack();
		rootpanel.setOrientation(Orientation.HORIZONTAL);
		
		PanelStack panel = new PanelStack();
		panel.setOrientation(Orientation.VERTICAL);
		panel.add(new UILabel("List1"));
		panel.add(new UILabel("List2"));
		panel.add(new UILabel("List3").setMargin(new DirWeights(5)));
		panel.add(new UILabel("List4"));
		panel.add(new UILabel("List5").setHorizontalArrange(HorizontalArrange.CENTER));
		panel.add(new UILabel("List6"));
		panel.add(new UILabel("List7"));
		rootpanel.add(panel);
		
		PanelStack panel2 = new PanelStack();
		panel2.setOrientation(Orientation.VERTICAL);
		panel2.add(new UILabel("List1").setHorizontalArrange(HorizontalArrange.CENTER));
		panel2.add(new UILabel("List3").setMargin(new DirWeights(5)));
		panel2.add(new UILabel("List6"));
		panel2.add(new UILabel("List7"));
		rootpanel.add(panel2);
		
		add(rootpanel);
		
		setPosition(0, 0);
		invalidateSize();
		setSize(screenSize.width, screenSize.height);
		layout();
	}
	
	@Override
	public Dimension measureSize() {
		return screenSize;
	}
}
