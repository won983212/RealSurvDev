package won983212.korpatch.ui;

import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import won983212.simpleui.DirWeights;
import won983212.simpleui.element.UIRectangle;
import won983212.simpleui.element.UITextButton;
import won983212.simpleui.font.FontFactory;
import won983212.simpleui.font.TrueTypeFont;
import won983212.simpleui.parentelement.StackPanel;
import won983212.simpleui.parentelement.StackPanel.Orientation;
import won983212.simpleui.parentelement.UIObject;
import won983212.simpleui.rootpane.MinecraftScreenPane;
import won983212.simpleui.rootpane.RootPane;

public class IMEPopupViewerPane extends MinecraftScreenPane {
	public IMEPopupViewerPane() {
		super();
		
		int factor = new ScaledResolution(mc).getScaleFactor();
		UIObject.setStaticFont(FontFactory.makeFont("맑은 고딕", 6 * factor, true));
		setVisible(false);
		initializePanel();
	}

	@Override
	protected void initGui() {
		add(new UIRectangle().setRadius(0).setShadowVisible(false));
		
		StackPanel panel = new StackPanel().setOrientation(Orientation.HORIZONTAL);
		panel.add(new UIRectangle().setLabel("한").setPadding(new DirWeights(2)).setShadowVisible(false).setBackgroundColor(0xffff0000)
				.setMargin(new DirWeights(4, 4, 4, 0)));
		panel.add(new UITextButton("색").setMargin(new DirWeights(4, 4, 4, 0)));
		panel.add(new UITextButton("한자").setMargin(new DirWeights(4, 4, 4, 0)));
		panel.add(new UITextButton("설정").setMargin(new DirWeights(4, 4, 4, 4)));
		add(panel);
	}
}
