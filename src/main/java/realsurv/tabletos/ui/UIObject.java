package realsurv.tabletos.ui;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import realsurv.font.FontFactory;
import realsurv.font.TrueTypeFont;
import realsurv.tabletos.DirWeights;
import realsurv.tabletos.HorizontalArrange;
import realsurv.tabletos.VerticalArrange;

public abstract class UIObject {
	private HorizontalArrange hArrange = HorizontalArrange.STRECTCH;
	private VerticalArrange vArrange = VerticalArrange.STRECTCH;
	private Rectangle bounds = new Rectangle();
	private Dimension minSize = new Dimension(10, 10);
	private DirWeights margin = new DirWeights();
	private DirWeights padding = new DirWeights();
	private TrueTypeFont font = FontFactory.createFont("���� ���", 10);
	private boolean visible = true;
	protected int backgroundColor = 0xfff4f4f4;
	protected int foregroundColor = 0xff000000;
	protected int arc = 2;
	protected boolean showShadow = true;
	
	protected UIPanel parentPanel;
	private boolean focusd = false;
	private Dimension sizeCache = null;
	public Object metadata = null;
	
	// for grid
	public int layoutX = 0;
	public int layoutY = 0;
	public int layoutXSpan = 1;
	public int layoutYSpan = 1;
	
	public void arrange(Rectangle available) {
		setSizeByArrange(available);
		setPositionByArrange(available);
	}

	public boolean containsRelative(int x, int y) {
		return x >= 0 && y >= 0 && x < bounds.width && y < bounds.height;
	}

	public Point calculateActualLocation() {
		UIObject obj = this;
		Point p = new Point(0, 0);
		while(obj != null) {
			Rectangle r = obj.getRelativeBounds();
			p.x += r.x;
			p.y += r.y;
			obj = obj.parentPanel;
		}
		return p;
	}
	
	public Rectangle getRelativeBounds() {
		return bounds;
	}
	
	public TrueTypeFont getFont() {
		return font;
	}
	
	public Dimension getBoundsSize() {
		return bounds.getSize();
	}
	
	public Rectangle getInnerBounds() {
		return new Rectangle(0, 0, bounds.width, bounds.height);
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
		if(parentPanel != null)
			parentPanel.requestLayout();
	}

	public abstract void render(int mouseX, int mouseY);
	
	public UIObject setHorizontalArrange(HorizontalArrange arr) {
		this.hArrange = arr;
		return this;
	}

	public void setRelativeLocation(int x, int y) {
		bounds.setLocation(x, y);
	}
	
	public UIObject setLayoutPosition(int x, int y) {
		this.layoutX = x;
		this.layoutY = y;
		return this;
	}
	
	public UIObject setMetadata(Object data) {
		this.metadata = data;
		return this;
	}
	
	public UIObject setFont(String family, int size) {
		this.font = FontFactory.createFont(family, size);
		return this;
	}
	
	public UIObject setLayoutSpan(int x, int y) {
		this.layoutXSpan = x;
		this.layoutYSpan = y;
		return this;
	}
	
	public UIObject setRadius(int rad) {
		this.arc = rad;
		return this;
	}
	
	public UIObject setShadowVisible(boolean show) {
		this.showShadow = show;
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
		this.parentPanel = panel;
	}

	protected void setRelativeBounds(int x, int y, int width, int height) {
		bounds.setBounds(x, y, width, height);
	}
	
	private void setPositionByArrange(Rectangle available) {
		Dimension size = getRelativeBounds().getSize();
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
	
	private static void pushVertexArc(int x, int y, int fromDegree, int toDegree, int r) {
		final int step = (int)(r * Math.PI / 2);
		GlStateManager.glBegin(GL11.GL_POLYGON);
		GlStateManager.glVertex3f(x, y, 0);
		for(int t=0;t<=step;t++) {
			double theta = (fromDegree+((toDegree-fromDegree)/(double)step)*t)*Math.PI/180;
			float xf = (float)(x + r*Math.cos(theta));
			float yf = (float)(y + r*Math.sin(theta));
			GlStateManager.glVertex3f(xf, yf, 0);
		}
		GlStateManager.glEnd();
	}
	
	public static void renderArcRect(int x1, int y1, int x2, int y2, int arc, int color, boolean shadow) {
		if(arc == 0) {
			Gui.drawRect(x1, y1, x2, y2, color);
			return;
		}
		
		if(shadow) {
			renderArcRect(x1+1, y1+1, x2--, y2--, arc, 0xff999999, false);
		}
		
		Gui.drawRect(x1 + arc, y1, x2 - arc, y1 + arc, color);
		Gui.drawRect(x1 + arc, y2 - arc, x2 - arc, y2, color);
		Gui.drawRect(x1, y1 + arc, x1 + arc, y2 - arc, color);
		Gui.drawRect(x2 - arc, y1 + arc, x2, y2 - arc, color);
		Gui.drawRect(x1 + arc, y1 + arc, x2 - arc, y2 - arc, color);
		
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		pushVertexArc(x2 - arc, y2 - arc, 90, 0, arc);
		pushVertexArc(x1 + arc, y2 - arc, 180, 90, arc);
		pushVertexArc(x1 + arc, y1 + arc, 270, 180, arc);
		pushVertexArc(x2 - arc, y1 + arc, 360, 270, arc);
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}
}
