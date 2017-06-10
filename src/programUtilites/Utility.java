package programUtilites;

import java.awt.Container;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.ImageIcon;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

public class Utility {

	private static Container p = new Container();

	public static Image loadImage(String strBild) {
		MediaTracker tracker = new MediaTracker(p);
		Image img = (new ImageIcon(strBild)).getImage();
		tracker.addImage(img, 0);
		try {
			tracker.waitForID(0);
		} catch (Exception ex) {
			System.out.println("Can not load image: " + strBild);
		}
		return img;
	}

	public static Image loadResourceImage(String strBild, int width, int height) {
		MediaTracker tracker = new MediaTracker(p);
		Image img = (new ImageIcon(Utility.class.getClassLoader().getResource("resources" + "/" + strBild))).getImage();
		img = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		tracker.addImage(img, 0);
		try {
			tracker.waitForID(0);
		} catch (Exception ex) {
			System.out.println("Can not load image: " + strBild);
		}
		return img;
	}

	public static Cursor getInvisibleCursor() {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		return toolkit.createCustomCursor(image, new Point(0, 0), "img");
	}

	public static Cursor getDefaultCursor() {
		return Cursor.getDefaultCursor();
	}

	public static Image loadResourceImage(String strBild) {
		MediaTracker tracker = new MediaTracker(p);
		URL url = Utility.class.getClassLoader().getResource("userinterface/resources" + "/" + strBild);
		Image img = (new ImageIcon(url)).getImage();
		tracker.addImage(img, 0);
		try {
			tracker.waitForID(0);
		} catch (Exception ex) {
			System.out.println("Can not load image: " + strBild);
		}
		return img;
	}

	public static String loadResourceText(String datei) {
		// Looks like resources within jar want to be accessed with "/" and not
		// with File.separator!
		String res = "resources" + "/" + datei;
		URL url = Utility.class.getClassLoader().getResource(res);
		String txt = "";
		try {
			String inputLine;
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			while ((inputLine = in.readLine()) != null) {
				txt += inputLine + "\n";
			}
			in.close();
		} catch (Exception e) {
			System.out.println("Can not load File: " + datei);
		}
		return txt;
	}
	
	public static double[][] loadResourceCSVData(String datei) {
		double[][] data = null;
		int nLines = 0;
		int nColumns = 0;

		String res = "resources" + "/" + datei;
		URL url = Utility.class.getClassLoader().getResource(res);
		
		try {
			// Anzahl Zeilen und Anzahl Kolonnen festlegen:
			BufferedReader eingabeDatei = new BufferedReader(new InputStreamReader(url.openStream()));
			String[] s = eingabeDatei.readLine().split("[,\t ]+");
			nColumns = s.length;
			String line;
			while ((line = eingabeDatei.readLine()) != null) {
				if (!line.isEmpty())
					nLines++;
			}
			eingabeDatei.close();

			// Gezählte Anzahl Zeilen und Kolonnen lesen:
			eingabeDatei = new BufferedReader(new InputStreamReader(url.openStream()));
			data = new double[nLines][nColumns];
			for (int i = 0; i < data.length; i++) {
				line = eingabeDatei.readLine();
				if (!line.isEmpty()) {
					s = line.split("[,\t ]+");
					for (int k = 0; k < s.length; k++) {
						data[i][k] = Double.parseDouble(s[k]);
					}
				}
			}
			eingabeDatei.close();
		} catch (IOException exc) {
			System.err.println("Dateifehler: " + exc.toString());
		}

		return data;
	}


	public static BufferedImage[] loadResourcePDF(String datei) {
		BufferedImage[] bim = null;

		// Looks like resources within jar want to be accessed with "/" and not
		// with File.separator!
		String res = "userinterface/resources" + "/" + datei;
		URL url = Utility.class.getClassLoader().getResource(res);

		try {
			PDDocument document = PDDocument.load(url.openStream());
			PDFRenderer pdfRenderer = new PDFRenderer(document);
			int pageCounter = 0;

			bim = new BufferedImage[document.getNumberOfPages()];

			for (PDPage page : document.getPages()) {
				// note that the page number parameter is zero based
				bim[pageCounter] = pdfRenderer.renderImageWithDPI(pageCounter++, 96, ImageType.RGB);

				// suffix in filename will be used as the file format
				// ImageIOUtil.writeImage(bim, "TestImg" + "-" + (pageCounter++)
				// + ".png", 300);
			}
			document.close();

		} catch (InvalidPasswordException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		return bim;
	}
}
