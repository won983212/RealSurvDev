package realsurv.tabletos.ui;

import java.awt.Dimension;
import java.awt.Rectangle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

// LAYOUT Makanism
// 1. 자식 요소들의 사이즈를 전부 측정하여 bounds에 크기정보만 저장한다.
// 2. 부모 요소들에 의해서 자식의 위치, 크기정보가 갱신된다.

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
	
	public void setParentPanel(UIPanel panel) {
		this.panel = panel;
	}

	public void setPosition(int x, int y) {
		bounds.setLocation(x, y);
	}

	public void setSize(int width, int height) {
		bounds.setSize(width, height);
	}
	
	private void setPositionByArrange(Rectangle available) {
		Dimension size = getDesiredSize();
		int x = hArrange.getHorizontalArrangedLocation(available, size) + margin.left;
		int y = vArrange.getVerticalArrangedLocation(available, size) + margin.up;
		setPosition(x, y);
	}

	private void setSizeByArrange(Rectangle available) {
		Dimension size = getDesiredSize();
		int width = Math.max(minSize.width, hArrange.getWidthArranged(available, size));
		int height = Math.max(minSize.height, vArrange.getHeightArranged(available, size));
		setSize(width, height);
	}

	public void setBoundsByArrange(Rectangle available) {
		setPositionByArrange(available);
		setSizeByArrange(available);
	}
	
	public UIObject setMinimumSize(int minWidth, int minHeight) {
		minSize.setSize(minWidth, minHeight);
		return this;
	}
	
	public UIObject setMargin(DirWeights margin) {
		this.margin = margin;
		return this;
	}
	
	public UIObject setPadding(DirWeights padding) {
		this.padding = padding;
		return this;
	}
	
	public Dimension getDesiredSize() {
		if(actualSize == null)
			actualSize = measureSize();
		return actualSize;
	}
	
	public Dimension measureSize() {
		return margin.outExpandedSize(minSize);
	}
	
	public UIObject setHorizontalArrange(HorizontalArrange arr) {
		this.hArrange = arr;
		return this;
	}
	
	public UIObject setVerticalArrange(VerticalArrange arr) {
		this.vArrange = arr;
		return this;
	}
	
	public HorizontalArrange getHorizontalArrange() {
		return hArrange;
	}
	
	public VerticalArrange getVerticalArrange() {
		return vArrange;
	}
	
	public Dimension getMinimumSize() {
		return minSize;
	}
	
	public Rectangle getActualBounds() {
		return bounds;
	}
	
	public DirWeights getMargin() {
		return margin;
	}
	
	public DirWeights getPadding() {
		return padding;
	}

	public boolean isFocusd() {
		return focusd;
	}

	public boolean contains(int x, int y) {
		return bounds.contains(x, y);
	}
	
	public void invalidateSize() {
		actualSize = null;
	}

	public abstract void render();

	public boolean isEnabledMouseEvent() {
		return true;
	}

	public boolean isEnabledKeyEvent() {
		return true;
	}

	public void onKeyTyped(int i, char c) {
	}

	public void onPress(int x, int y, int bt) {
	}

	public void onRelease(int x, int y, int bt) {
	}

	public void onDrag(int x, int y, int bt, long time) {
	}

	public void onGotFocus() {
		focusd = true;
	}

	public void onLostFocus() {
		focusd = false;
	}
}
