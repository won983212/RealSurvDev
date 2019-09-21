package realsurv.tabletos.ui;

import java.awt.Dimension;
import java.awt.Rectangle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import realsurv.tabletos.DirWeights;
import realsurv.tabletos.HorizontalArrange;
import realsurv.tabletos.VerticalArrange;

public abstract class UIObject {
	protected static final FontRenderer fontrenderer = Minecraft.getMinecraft().fontRenderer;
	private HorizontalArrange hArrange = HorizontalArrange.STRECTCH;
	private VerticalArrange vArrange = VerticalArrange.STRECTCH;
	private Rectangle bounds = new Rectangle();
	private Dimension minSize = new Dimension(10, 10);
	private DirWeights margin = new DirWeights();
	private DirWeights padding = new DirWeights();
	private boolean visible = true;
	protected int backgroundColor = 0xfff0f0f0;
	protected int foregroundColor = 0xff000000;
	
	protected UIPanel panel;
	private boolean focusd = false;
	private Dimension sizeCache = null;
	
	// for grid
	public int layoutX = 0;
	public int layoutY = 0;
	public int layoutXSpan = 1;
	public int layoutYSpan = 1;
	
	public void arrange(Rectangle available) {
		setSizeByArrange(available);
		setPositionByArrange(available);
	}

	public boolean contains(int x, int y) {
		return bounds.contains(x, y);
	}

	public Rectangle getActualBounds() {
		return bounds;
	}
	
	public Dimension getLayoutMinSize() {
		return margin.getExpandedSize(getMeasuredMinSize());
	}
	
	public Dimension getMeasuredMinSize() {
		if(sizeCache == null)
			sizeCache = measureMinSize();
		return sizeCache;
	}

	public HorizontalArrange getHorizontalArrange() {
		return hArrange;
	}

	public DirWeights getMargin() {
		return margin;
	}
	
	public Dimension getMinimumSize() {
		return minSize;
	}
	
	public DirWeights getPadding() {
		return padding;
	}
	
	public VerticalArrange getVerticalArrange() {
		return vArrange;
	}
	
	public void invalidateSize() {
		sizeCache = null;
	}
	
	public boolean isInteractive() {
		return visible;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public boolean isFocusd() {
		return focusd;
	}
	
	protected Dimension measureMinSize() {
		return padding.getExpandedSize(minSize);
	}
	
	public void onDrag(int x, int y, int bt, long time) {
	}
	
	public void onGotFocus() {
		focusd = true;
	}
	
	public void onKeyTyped(int i, char c) {
	}
	
	public void onLostFocus() {
		focusd = false;
	}
	
	public void onPress(int x, int y, int bt) {
	}

	public void onRelease(int x, int y, int bt) {
	}
	
	public void requestLayout() {
		if(panel != null)
			panel.requestLayout();
	}

	public abstract void render(int mouseX, int mouseY);
	
	public UIObject setHorizontalArrange(HorizontalArrange arr) {
		this.hArrange = arr;
		return this;
	}

	public UIObject setLayoutPosition(int x, int y) {
		this.layoutX = x;
		this.layoutY = y;
		return this;
	}
	
	public UIObject setLayoutSpan(int x, int y) {
		this.layoutXSpan = x;
		this.layoutYSpan = y;
		return this;
	}
	
	public UIObject setMargin(DirWeights margin) {
		this.margin = margin;
		return this;
	}
	
	public UIObject setBackgroundColor(int color) {
		this.backgroundColor = color;
		return this;
	}
	
	public UIObject setForegroundColor(int color) {
		this.foregroundColor = color;
		return this;
	}

	public UIObject setMinimumSize(int minWidth, int minHeight) {
		minSize.setSize(minWidth, minHeight);
		return this;
	}

	public UIObject setPadding(DirWeights padding) {
		this.padding = padding;
		return this;
	}
	
	public UIObject setVisible(boolean visible) {
		this.visible = visible;
		return this;
	}

	public void setParentPanel(UIPanel panel) {
		this.panel = panel;
	}

	protected void setActualBounds(int x, int y, int width, int height) {
		bounds.setBounds(x, y, width, height);
	}
	
	private void setPositionByArrange(Rectangle available) {
		Dimension size = getActualBounds().getSize();
		int x = hArrange.getHorizontalArrangedLocation(available, size) + margin.left;
		int y = vArrange.getVerticalArrangedLocation(available, size) + margin.top;
		bounds.setLocation(x, y);
	}

	private void setSizeByArrange(Rectangle available) {
		Dimension size = getMeasuredMinSize();
		Rectangle marginCalc = margin.getContentRect(available);
		int width = Math.max(minSize.width, hArrange.getWidthArranged(marginCalc, size));
		int height = Math.max(minSize.height, vArrange.getHeightArranged(marginCalc, size));
		bounds.setSize(width, height);
	}

	public UIObject setVerticalArrange(VerticalArrange arr) {
		this.vArrange = arr;
		return this;
	}
	
	public static int offsetColor(int color, int offset) {
		int a = (color >> 24) & 0xff;
		int r = Math.min(((color >> 16) & 0xff) + offset, 0xff);
		int g = Math.min(((color >> 8) & 0xff) + offset, 0xff);
		int b = Math.min((color & 0xff) + offset, 0xff);
		return (a << 24) + (r << 16) + (g << 8) + b;
	}
	
	public static void renderBorder(int x1, int y1, int x2, int y2, int color, int thickness) {
		Gui.drawRect(x1, y1, x2, y1 + thickness, color);
		Gui.drawRect(x1, y2 - thickness, x2, y2, color);
		Gui.drawRect(x1, y1, x1 + thickness, y2, color);
		Gui.drawRect(x2 - thickness, y1, x2, y2, color);
	}
}
