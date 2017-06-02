package userinterface;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window.Type;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import StepResponseApproximationTool.StepResponseApproximationTool;


public class MenuBar extends JMenuBar implements Observer, ActionListener{
	JMenu menu, optionsmenu;
	JMenuItem menuItemOnTop, exampleItem, settingsmenuItem, helpmenuItem;
	JFrame frame;
	Controller controller;
	JDialog settingsDialog = new JDialog();
	JDialog helpDialog = new JDialog();
	public JFrame settingsFrame;
	private SettingsPanel settingsPanel;
	private double xPosition;
	private int settingsFrameWidth;
	private int settingsFrameHeight;
	
	String[] zeilen;
	JTextArea jtArea;
	
	public MenuBar(Controller controller, JFrame frame) {
		this.frame = frame;
		this.controller = controller;
		menu = new JMenu("Datei");
		menu.setMnemonic(KeyEvent.VK_D);

		menu.addSeparator();

		menuItemOnTop = new JMenuItem("Allways on Top", KeyEvent.VK_T);
		menuItemOnTop.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.ALT_MASK));
		menuItemOnTop.setActionCommand("OnTop");
		menuItemOnTop.addActionListener(this);
		menu.add(menuItemOnTop);

		JMenuItem menuItemResizable = new JMenuItem("Resizable", KeyEvent.VK_R);
		menuItemResizable.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.ALT_MASK));
		menuItemResizable.setActionCommand("Resizable");
		menuItemResizable.addActionListener(this);
		menu.add(menuItemResizable);

		JMenuItem menuItemNotResizable = new JMenuItem("Not Resizable", KeyEvent.VK_N);
		menuItemNotResizable.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.ALT_MASK));
		menuItemNotResizable.setActionCommand("NotResizable");
		menuItemNotResizable.addActionListener(this);
		menu.add(menuItemNotResizable);
		add(menu);
		
		optionsmenu=new JMenu("Options");
		optionsmenu.setMnemonic(KeyEvent.VK_O);
		
		settingsmenuItem=new JMenuItem("Settings");
		settingsmenuItem.setMnemonic(KeyEvent.VK_S);
		settingsmenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
		settingsmenuItem.setActionCommand("Settings");
		settingsmenuItem.addActionListener(this);
	
		helpmenuItem = new JMenuItem("Help");
		helpmenuItem.setMnemonic(KeyEvent.VK_H);
		helpmenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.ALT_MASK));
		helpmenuItem.setActionCommand("Help");
		helpmenuItem.addActionListener(this);
		
		optionsmenu.add(settingsmenuItem);
		optionsmenu.add(helpmenuItem);
		add(optionsmenu);
		
		if(Toolkit.getDefaultToolkit().getScreenSize().getWidth()>=3700){
			xPosition=710;
			settingsFrameWidth=50;
			settingsFrameHeight=100;
		}
		else{
			xPosition=350;
			settingsFrameWidth=0;
			settingsFrameHeight=50;			
		}
		
		settingsPanel = new SettingsPanel(controller);
	}

	public void update(Observable o, Object obj) {}

	public void actionPerformed(ActionEvent e) {
		int cnt = 0;
		if (e.getActionCommand().equals("Resizable")) {
			frame.setResizable(true);
			Dimension dim = frame.getSize();
			if(cnt==0){
			dim.width -= 100;
			cnt++;
			}
			frame.setSize(dim);
		}
		if (e.getActionCommand().equals("NotResizable")) {
			frame.setResizable(false);
			Dimension dim = frame.getSize();
			if(cnt !=0){
			dim.width += 100;
			cnt--;
			}
			frame.setSize(dim);
		}
		if (e.getActionCommand().equals("OnTop")) {
			StatusBar.showStatus(this, e, e.getActionCommand());
			if (((JFrame) this.getTopLevelAncestor()).isAlwaysOnTop()) {
				((JFrame) this.getTopLevelAncestor()).setAlwaysOnTop(false);
				menuItemOnTop.setText("Allways on Top");
			} else {
				((JFrame) this.getTopLevelAncestor()).setAlwaysOnTop(true);
				menuItemOnTop.setText("Not allways on Top");
			}
		}
		if(e.getActionCommand().equals("Settings")){			
			settingsDialog.setTitle("Settings");
			settingsDialog.setVisible(true);
			settingsDialog.setResizable(false);
			settingsDialog.setLayout(new GridBagLayout());
			settingsDialog.add(settingsPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0));
			settingsDialog.setPreferredSize(settingsPanel.getPreferredSize());
			settingsDialog.setSize((int) (settingsDialog.getPreferredSize().getWidth())+settingsFrameWidth, (int) (settingsDialog.getPreferredSize().getHeight())+settingsFrameHeight);
			settingsDialog.setLocation((int) (frame.getLocation().getX()-xPosition), (int) frame.getLocation().getY());
		}
		if(e.getActionCommand().equals("Help")){
			helpDialog.setTitle("Help");
			helpDialog.setVisible(true);	
			helpDialog.setResizable(true);
		}
	}
}

