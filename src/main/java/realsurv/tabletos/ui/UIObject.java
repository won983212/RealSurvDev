package realsurv.tabletos.ui;

import java.awt.Dimension;
import java.awt.Rectangle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
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
	protected UIPanel panel;
	private boolean focusd = false;
	private Dimension actualSize = null;
	
	// for grid
	public int layoutX = 0;
	public int layoutY = 0;
	public int layoutXSpan = 1;
	public int layoutYSpan = 1;
	
	public void arrange(Rectangle available) {
		setPositionByArrange(available);
		setSizeByArrange(available);
	}

	public boolean contains(int x, int y) {
		return bounds.contains(x, y);
	}

	public Rectangle getActualBounds() {
		return bounds;
	}
	
	public Dimension getDesiredSize() {
		return margin.getExpandedSize(getMeasuredSize());
	}
	
	public Dimension getMeasuredSize() {
		if(actualSize == null)
			actualSize = measureSize();
		return actualSize;
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
		actualSize = null;
	}
	
	public boolean isEnabledKeyEvent() {
		return true;
	}
	
	public boolean isEnabledMouseEvent() {
		return true;
	}
	
	public boolean isFocusd() {
		return focusd;
	}
	
	protected Dimension measureSize() {
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

	public UIObject setMinimumSize(int minWidth, int minHeight) {
		minSize.setSize(minWidth, minHeight);
		return this;
	}

	public UIObject setPadding(DirWeights padding) {
		this.padding = padding;
		return this;
	}

	public void setParentPanel(UIPanel panel) {
		this.panel = panel;
	}

	public void setPosition(int x, int y) {
		bounds.setLocation(x, y);
	}

	private void setPositionByArrange(Rectangle available) {
		Dimension size = getDesiredSize();
		int x = hArrange.getHorizontalArrangedLocation(available, size) + margin.left;
		int y = vArrange.getVerticalArrangedLocation(available, size) + margin.up;
		setPosition(x, y);
	}

	public void setSize(int width, int height) {
		bounds.setSize(width, height);
	}

	private void setSizeByArrange(Rectangle available) {
		Dimension size = getMeasuredSize();
		Rectangle marginCalc = margin.getContentRect(available);
		int width = Math.max(minSize.width, hArrange.getWidthArranged(marginCalc, size));
		int height = Math.max(minSize.height, vArrange.getHeightArranged(marginCalc, size));
		setSize(width, height);
	}

	public UIObject setVerticalArrange(VerticalArrange arr) {
		this.vArrange = arr;
		return this;
	}
}
