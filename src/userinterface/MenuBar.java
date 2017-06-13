package userinterface;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import com.opencsv.CSVReader;

import programUtilities.ImagePanel;
import programUtilities.Utility;

/**
 * 
 * @author Lukas Loosli
 *
 */
public class MenuBar extends JMenuBar implements ActionListener {

	//================================================================================
	// Properties
	//================================================================================

	JMenu menu, optionsmenu;
	JMenuItem menuItemOnTop, exampleItem, settingsmenuItem, helpmenuItem, ExamplemenuItem;
	JFrame frame;
	Controller controller;
	JDialog settingsDialog = new JDialog();
	JDialog helpDialog = new JDialog();
	public JFrame settingsFrame;
	private SettingsPanel settingsPanel;
	private int settingsFrameWidth;
	private int settingsFrameHeight;

	private Image icon = Utility.loadResourceImage("SRAT_LOGO.png");

	int cnt = 0;
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	String[] zeilen;
	JTextArea jtArea;

	//================================================================================
	// Constructor
	//================================================================================

	public MenuBar(Controller controller, JFrame frame) {
		this.frame = frame;
		this.controller = controller;
		menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);

		menu.addSeparator();

		menuItemOnTop = new JMenuItem("Allways on Top", KeyEvent.VK_T);
		menuItemOnTop.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.ALT_MASK));
		menuItemOnTop.setActionCommand("OnTop");
		menuItemOnTop.addActionListener(this);
		menu.add(menuItemOnTop);

		JMenuItem menuItemResizable = new JMenuItem("Resizable", KeyEvent.VK_Z);
		menuItemResizable.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.ALT_MASK));
		menuItemResizable.setActionCommand("Resizable");
		menuItemResizable.addActionListener(this);
		menu.add(menuItemResizable);

		JMenuItem menuItemNotResizable = new JMenuItem("Not Resizable", KeyEvent.VK_N);
		menuItemNotResizable.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.ALT_MASK));
		menuItemNotResizable.setActionCommand("NotResizable");
		menuItemNotResizable.addActionListener(this);
		menu.add(menuItemNotResizable);
		add(menu);

		optionsmenu = new JMenu("Options");
		optionsmenu.setMnemonic(KeyEvent.VK_O);

		settingsmenuItem = new JMenuItem("Settings");
		settingsmenuItem.setMnemonic(KeyEvent.VK_S);
		settingsmenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
		settingsmenuItem.setActionCommand("Settings");
		settingsmenuItem.addActionListener(this);

		helpmenuItem = new JMenuItem("Help");
		helpmenuItem.setMnemonic(KeyEvent.VK_H);
		helpmenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.ALT_MASK));
		helpmenuItem.setActionCommand("Help");
		helpmenuItem.addActionListener(this);

		exampleItem = new JMenuItem("Load Example");
		exampleItem.setMnemonic(KeyEvent.VK_E);
		exampleItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.ALT_MASK));
		exampleItem.setActionCommand("Load Example");
		exampleItem.addActionListener(this);

		optionsmenu.add(settingsmenuItem);
		optionsmenu.add(helpmenuItem);
		optionsmenu.add(exampleItem);
		add(optionsmenu);

		if (Toolkit.getDefaultToolkit().getScreenSize().getWidth() >= 3700) {
			settingsFrameWidth = 50;
			settingsFrameHeight = 100;
		} else {
			settingsFrameWidth = 0;
			settingsFrameHeight = 50;
		}

		settingsPanel = new SettingsPanel(controller);
	}

	//================================================================================
	// Events
	//================================================================================

	/**
	 * Wenn Resizable gedr�ckt wird, aktuelle Gr�sse des Frames um 100 verkleinern (nur wenn Frame vorher nicht resizable war) 
	 * und Frame resizable setzen.
	 * Wenn NotResizable gedr�ckt wird, aktuelle Gr�sse des Frames wieder auf Startgr�sse und Position setzen
	 * und Frame nicht resizable setzen.
	 * Wenn Allways on top gedr�ckt wird, Frame allways on top true setzen und Text in Not allways on top �ndern.
	 * Wenn Not allways on top gedr�ckt wird, Frame allways on top false setzen und Text in Allways on top �ndern.
	 * Wenn Settings gedr�ckt wird, Settings-Dialog �ffnen.
	 * Wenn Help gedr�ckt wird, Help-Dialog �ffnen.
	 * Wenn Load example gedr�ckt wird, Beispiel laden.
	 */
	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand().equals("Resizable")) {
			frame.setResizable(true);
			Dimension dim = frame.getSize();
			if (cnt == 0) {
				dim.width -= 100;
				cnt++;
			}
			frame.setSize(dim);
		}
		if (e.getActionCommand().equals("NotResizable")) {
			frame.setSize((screenSize.width * 2 / 4), (screenSize.height * 10 / 11));
			frame.setLocation((screenSize.width - frame.getSize().width) / 2, (screenSize.height - frame.getSize().height) / 2);
			frame.setResizable(false);
			if (cnt != 0) {
				cnt--;
			}
		}
		if (e.getActionCommand().equals("OnTop")) {
			if (((JFrame) this.getTopLevelAncestor()).isAlwaysOnTop()) {
				((JFrame) this.getTopLevelAncestor()).setAlwaysOnTop(false);
				settingsDialog.setAlwaysOnTop(false);
				menuItemOnTop.setText("Allways on Top");
			} else {
				((JFrame) this.getTopLevelAncestor()).setAlwaysOnTop(true);
				settingsDialog.setAlwaysOnTop(true);
				menuItemOnTop.setText("Not allways on Top");
			}
		}
		if (e.getActionCommand().equals("Settings")) {
			settingsDialog.setTitle("Settings");
			settingsDialog.setVisible(true);
			settingsDialog.setResizable(false);
			settingsDialog.setLayout(new GridBagLayout());
			settingsDialog.setIconImage(icon);
			settingsDialog.add(settingsPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			settingsDialog.setPreferredSize(settingsPanel.getPreferredSize());
			settingsDialog.setSize((int) (settingsDialog.getPreferredSize().getWidth()) + settingsFrameWidth,
					(int) (settingsDialog.getPreferredSize().getHeight()) + settingsFrameHeight);

			settingsDialog.setLocation((screenSize.width - settingsDialog.getSize().width) / 2,
					(screenSize.height - settingsDialog.getSize().height) / 3);
		}

		if (e.getActionCommand().equals("Help")) {
			helpDialog.setTitle("Help");
			helpDialog.setVisible(true);
			helpDialog.setResizable(true);
			helpDialog.setLayout(new GridBagLayout());
			helpDialog.setSize((int) (screenSize.height / Math.sqrt(2)), screenSize.height);
			helpDialog.setLocation((screenSize.width - helpDialog.getSize().width) / 2,
					(screenSize.height - helpDialog.getSize().height) / 2);
			helpDialog.setIconImage(icon);
			BufferedImage[] bim = Utility.loadResourcePDF("UserGuide.pdf");
			ImagePanel bildPanel = new ImagePanel(bim[0]);
			helpDialog.add(bildPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			helpDialog.setLocation((screenSize.width - helpDialog.getSize().width) / 2,
					(screenSize.height - helpDialog.getSize().height) / 2);
		}

		if (e.getActionCommand().equals("Load Example")) {

			URL url = Utility.class.getResource("resources/Signal7.csv");
			CSVReader reader = null;

			try {
				reader = new CSVReader(new InputStreamReader(url.openStream()));
				controller.setMeasurement(reader.readAll());
				reader.close();
			} catch (FileNotFoundException e1) {
				throw new RuntimeException("File does not exist");
			} catch (IOException e2) {
				throw new RuntimeException("IO Problem when reading file");
			}
		}
	}
}
