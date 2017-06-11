package userinterface;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;


public class ImagePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private Image bild;
	private Image img;
	private JPanel panel = this;
	private int randLinks, randOben;

	public ImagePanel(Image image) {
		super(null);
		this.bild = image;

		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				double min = Math.min((double) (panel.getWidth()) / bild.getWidth(panel),
						(double) (panel.getHeight()) / bild.getHeight(panel));

				int bildBreite = (int) (bild.getWidth(panel) * min);
				int bildHoehe = (int) (bild.getHeight(panel) * min);
				randLinks = (panel.getWidth() - bildBreite) / 2;
				randOben = (panel.getHeight() - bildHoehe) / 2;

				System.out.println(min);

				System.out.println(bildBreite);

				img = bild.getScaledInstance(bildBreite, -1, Image.SCALE_SMOOTH);
				img.getWidth(panel);
			}
		});
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(img, randLinks, randOben, this);
	}

}
