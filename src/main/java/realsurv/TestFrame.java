package realsurv;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class TestFrame {
	private JFrame frame = null;
	private BufferedImage image = null;
	private String title = "";
	
	public TestFrame() {
	}

	public TestFrame(String title) {
		this.title = title;
	}
	
	public void updateImageTest(BufferedImage image) {
		this.image = image;
		if(frame == null)
			initFrame();
		frame.repaint();
	}

	private void initFrame() {
		frame = new JFrame();
		frame.setLocationByPlatform(true);
		frame.setTitle(title);
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
}
