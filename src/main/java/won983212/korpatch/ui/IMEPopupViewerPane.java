package won983212.korpatch.ui;

import java.awt.Point;
import java.awt.Rectangle;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import won983212.simpleui.DirWeights;
import won983212.simpleui.element.UIRectangle;
import won983212.simpleui.element.UITextButton;
import won983212.simpleui.panel.StackPanel;
import won983212.simpleui.panel.StackPanel.Orientation;
import won983212.simpleui.rootpane.HighResolutionPane;
import won983212.simpleui.rootpane.RootPane;

public class IMEPopupViewerPane extends HighResolutionPane {
	public IMEPopupViewerPane() {
		super(100, 20);
		setVisible(false);
		initializePanel();
	}
	
	@Override
	protected void initGui() {
		add(new UIRectangle());
		
		StackPanel panel = new StackPanel().setOrientation(Orientation.HORIZONTAL);
		panel.add(new UITextButton("한").setMargin(new DirWeights(3)));
		panel.add(new UITextButton("삭제").setMargin(new DirWeights(3)));
		add(panel);
	}
}
