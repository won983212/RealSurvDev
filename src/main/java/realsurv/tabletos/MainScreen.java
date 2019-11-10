package realsurv.tabletos;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import com.sun.jna.platform.unix.X11.Drawable;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import won983212.simpleui.DirWeights;
import won983212.simpleui.DrawableImage;
import won983212.simpleui.GridPanel;
import won983212.simpleui.GridPanel.LengthDefinition;
import won983212.simpleui.GridPanel.LengthType;
import won983212.simpleui.HorizontalArrange;
import won983212.simpleui.RootPane;
import won983212.simpleui.StackPanel;
import won983212.simpleui.StackPanel.Orientation;
import won983212.simpleui.UIPanel;
import won983212.simpleui.SpriteIcon;
import won983212.simpleui.VerticalArrange;
import won983212.simpleui.element.UITextButton;
import won983212.simpleui.element.UICheckbox;
import won983212.simpleui.element.UICombobox;
import won983212.simpleui.element.UIIconButton;
import won983212.simpleui.element.UIImage;
import won983212.simpleui.element.UILabel;
import won983212.simpleui.element.UIRectangle;
import won983212.simpleui.element.UITextfield;

public class MainScreen extends RootPane {
	private ArrayList<DesktopAppInfo> desktopApps = new ArrayList<>();
	private UIImage timeTexture;
	private UILabel timeLabel;

	public MainScreen() {
		super(TabletOS.WIDTH, TabletOS.HEIGHT);
		setScaledFactor(0);
		registerDesktopApps();
		initializePanel();
	}

	private void registerDesktopApps() {
		addDesktopApp("Banking", 0, 0);
		addDesktopApp("인터넷뱅킹", 0, 0);
		addDesktopApp("은행", 0, 0);
		addDesktopApp("BANK", 0, 0);
	}

	private void addDesktopApp(String name, int iconX, int iconY) {
		SpriteIcon icon = new SpriteIcon(DrawableImage.APPICONS, iconX, iconY, 48, 48);
		desktopApps.add(new DesktopAppInfo(name, icon));
	}

	@Override
	protected void initGui() {
		add(new UIImage(DrawableImage.WALLPAPER));

		GridPanel contents = new GridPanel();
		contents.addRow(new LengthDefinition(LengthType.FIXED, 26));
		contents.addRow(new LengthDefinition(LengthType.ALLOCATED, 1));
		contents.addEmptyColumn();

		// taskbar
		UIPanel taskbar = new UIPanel();
		taskbar.add(new UIRectangle().setShadowVisible(false).setRadius(0).setBackgroundColor(ColorPalette.Black)
				.setLayoutSpan(2, 1));
		taskbar.add(new UILabel().setLabel("§lApp Internet").setVerticalArrange(VerticalArrange.CENTER)
				.setMargin(new DirWeights(0, 0, 12, 0)).setHorizontalArrange(HorizontalArrange.LEFT)
				.setForegroundColor(0xffffffff));

		// Time view
		StackPanel timePanel = new StackPanel();
		timePanel.setOrientation(Orientation.HORIZONTAL);
		timeTexture = new UIImage("minecraft:textures/items/clock_00.png");
		timePanel.add(timeTexture.setMinimumSize(16, 16));
		timeLabel = new UILabel().setLabel("00:00");
		timePanel.add(timeLabel.setVerticalArrange(VerticalArrange.CENTER).setMargin(new DirWeights(0, 0, 2, 5))
				.setForegroundColor(0xffffffff));
		taskbar.add(timePanel.setVerticalArrange(VerticalArrange.CENTER).setHorizontalArrange(HorizontalArrange.RIGHT));
		contents.add(taskbar);

		// desktop apps
		if (desktopApps.size() > 0) {
			GridPanel apps = new GridPanel();
			apps.setLayoutPosition(0, 1);
			apps.setHorizontalArrange(HorizontalArrange.CENTER);
			apps.setVerticalArrange(VerticalArrange.BOTTOM);
			apps.addRow(new LengthDefinition(LengthType.AUTO, 0));
			apps.addRow(new LengthDefinition(LengthType.AUTO, 0));
			for (int i = 0; i < desktopApps.size(); i++) {
				apps.addColumn(new LengthDefinition(LengthType.ALLOCATED, 1));
			}
			for (int i = 0; i < desktopApps.size(); i++) {
				DesktopAppInfo app = desktopApps.get(i);
				apps.add(new UIIconButton(app.appIcon).setHorizontalArrange(HorizontalArrange.CENTER)
						.setVerticalArrange(VerticalArrange.CENTER).setLayoutPosition(i, 0));
				apps.add(new UILabel().setLabel(app.appName).setShadowVisible(true).setForegroundColor(0xffffffff)
						.setMargin(new DirWeights(5)).setHorizontalArrange(HorizontalArrange.CENTER)
						.setVerticalArrange(VerticalArrange.CENTER).setLayoutPosition(i, 1));
			}
			contents.add(apps);
		}

		GridPanel loginForm = new GridPanel();
		loginForm.setMinimumSize(130, 50);
		loginForm.setHorizontalArrange(HorizontalArrange.CENTER);
		loginForm.setVerticalArrange(VerticalArrange.CENTER);
		loginForm.setLayoutPosition(0, 1);
		loginForm.addColumn(new LengthDefinition(LengthType.FIXED, 120));
		loginForm.addColumn(new LengthDefinition(LengthType.AUTO, 1));
		loginForm.addRow(new LengthDefinition(LengthType.ALLOCATED, 1));
		loginForm.addRow(new LengthDefinition(LengthType.ALLOCATED, 1));
		loginForm.addRow(new LengthDefinition(LengthType.ALLOCATED, 1));
		loginForm.addRow(new LengthDefinition(LengthType.ALLOCATED, 1));
		loginForm.add(new UIRectangle().setBackgroundColor(0xffcccccc).setLayoutSpan(2, 4));
		loginForm.add(new UITextfield().setHint("ID").setMargin(new DirWeights(4, 0, 4, 0))
				.setVerticalArrange(VerticalArrange.CENTER));
		loginForm.add(new UITextfield().setHint("Password").setMargin(new DirWeights(4, 0, 4, 0))
				.setLayoutPosition(0, 1).setVerticalArrange(VerticalArrange.CENTER));
		loginForm.add(new UICheckbox().setLabel("Remember").setMargin(new DirWeights(0, 0, 4, 0))
				.setVerticalArrange(VerticalArrange.CENTER).setLayoutPosition(0, 2));
		loginForm.add(
				new UITextButton("Login").setMargin(new DirWeights(4)).setLayoutPosition(1, 0).setLayoutSpan(1, 3));
		loginForm.add(new UICombobox().add("Item1").add("Item2").add("Item3").setMargin(new DirWeights(0, 0, 4, 4))
				.setLayoutSpan(2, 1).setLayoutPosition(0, 3));
		contents.add(loginForm);
		add(contents);
	}

	@Override
	public void render(int mouseX, int mouseY) {
		timeLabel.setLabel(TimeUtil.getCurrentTime());
		timeTexture.setImage(TimeUtil.getCurrentTimeTexture());
		super.render(mouseX, mouseY);
	}

	@Override
	public void onKeyTyped(int keyCode, char typedChar) {
		// TODO Debug Key
		if (GuiScreen.isCtrlKeyDown()) {
			if (keyCode == Keyboard.KEY_T) {
				initializePanel();
			}
		}
		super.onKeyTyped(keyCode, typedChar);
	}

	public static class DesktopAppInfo {
		public final String appName;
		public final SpriteIcon appIcon;

		public DesktopAppInfo(String name, SpriteIcon icon) {
			this.appName = name;
			this.appIcon = icon;
		}
	}
}