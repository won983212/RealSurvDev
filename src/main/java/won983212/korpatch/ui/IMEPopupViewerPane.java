package won983212.korpatch.ui;

import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;

import net.minecraft.client.Minecraft;
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

public class IMEPopupViewerPane extends RootPane {
	public IMEPopupViewerPane() {
		super();
		setVisible(false);
		initializePanel();
	}

	@Override
	protected void initGui() {
		int factor = new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor();
		UIObject.setStaticFont(FontFactory.makeFont("맑은 고딕", 6 * factor, factor, true));
		
		add(new UIRectangle().setBackgroundColor(0xffdce2e6).setRadius(0).setShadowVisible(false));
		add(new UIRectangle().setBackgroundColor(0xffffffff).setMargin(new DirWeights(1)).setRadius(0)
				.setShadowVisible(false));

		final int gap = 2;
		StackPanel panel = new StackPanel().setOrientation(Orientation.HORIZONTAL);
		panel.add(new UIRectangle().setLabel("한").setPadding(new DirWeights(2)).setShadowVisible(false)
				.setBackgroundColor(0xffff0000).setMargin(new DirWeights(gap, gap, gap, 0)));
		panel.add(new UITextButton("설정").setMargin(new DirWeights(gap, gap, gap, gap)));
		add(panel);
	}
}
