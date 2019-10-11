package realsurv;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.GlyphVector;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class TestFrame {
	private JFrame frame = null;
	private BufferedImage image = null;
	private String title = "";
	private boolean useDefExit = false;

	public TestFrame() {
	}

	public TestFrame(String title) {
		this.title = title;
	}

	public void updateImageTest(BufferedImage image) {
		this.image = image;
		if (frame == null)
			initFrame();
		frame.repaint();
	}
	
	public void setDefaultExit() {
		useDefExit = true;
	}

	private void initFrame() {
		frame = new JFrame();
		frame.setLocationByPlatform(true);
		frame.setTitle(title);
		if(useDefExit)
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				if (image != null) {
					g.setColor(Color.black);
					g.fillRect(0, 0, image.getWidth(), image.getHeight());
					g.drawImage(image, 0, 0, null);
				}
			}

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(image.getWidth(), image.getHeight());
			}
		});
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		BufferedImage image = new BufferedImage(300, 300, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = image.createGraphics();
		String text = "Remember";
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		
		GlyphVector vec = new Font("¸¼Àº °íµñ", 0, 14).layoutGlyphVector(g2.getFontRenderContext(), text.toCharArray(), 0, text.length(), Font.LAYOUT_LEFT_TO_RIGHT);
		/*for (int j = 0; j < text.length(); j++) {
			Point2D p = vec.getGlyphPosition(j);
			p.setLocation(p.getX() + 2 * j, p.getY());
			vec.setGlyphPosition(j, p);
		}*/
		
		g2.setColor(Color.black);
		g2.fillRect(0, 0, 300, 300);
		g2.setColor(Color.white);
		g2.drawGlyphVector(vec, 10, 20);
		g2.setColor(Color.green);
		for(int i=0;i<vec.getNumGlyphs();i++) {
			Rectangle r = vec.getGlyphPixelBounds(i, null, 10, 20);
			g2.drawRect(r.x, r.y, r.width, r.height);
		}
		
		TestFrame t = new TestFrame();
		t.setDefaultExit();
		t.updateImageTest(image);
	}
}
