package realsurv;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class TestFrame {
	private JFrame frame = null;
	private BufferedImage image = null;

	public void updateImageTest(BufferedImage image) {
		this.image = image;
		frame.repaint();
	}

	public void initFrame() {
		frame = new JFrame();
		frame.setSize(300, 300);
		frame.setLocationByPlatform(true);
		frame.add(new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				if (image != null) {
					g.setColor(Color.black);
					g.fillRect(0, 0, image.getWidth(), image.getHeight());
					g.drawImage(image, 0, 0, null);
				}
			}
		});
		frame.setVisible(true);
	}
}
