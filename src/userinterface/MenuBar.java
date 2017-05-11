package userinterface;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window.Type;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

public class MenuBar extends JMenuBar implements Observer, ActionListener {
	JMenu menu, submenu,optionsMenu;
	JMenuItem menuItemOnTop, submenuItem, exampleItem, settingsItem;
	JFrame frame;
	Controller controller;
	public JFrame settingsFrame;
	private SettingsPanel settingsPanel=new SettingsPanel();
	private double xPosition;
	private int settingsFrameWidth;
	private int settingsFrameHeight;
	

	public MenuBar(Controller controller, JFrame frame) {
		this.frame = frame;
		this.controller = controller;
		menu = new JMenu("Datei");
		menu.setMnemonic(KeyEvent.VK_D);

		menu.addSeparator();
		submenu = new JMenu("A submenu");
		submenu.setMnemonic(KeyEvent.VK_S);
		submenuItem = new JMenuItem("SubmenuItem");
		submenu.add(submenuItem);
		menu.add(submenu);

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
		
		optionsMenu=new JMenu("Options");
		settingsItem=new JMenuItem("Settings");
		settingsItem.setActionCommand("Settings");
		settingsItem.addActionListener(this);
		optionsMenu.add(settingsItem);
		add(optionsMenu);
		
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
		
		
	}

	public void update(Observable o, Object obj) {}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Resizable")) {
			frame.setResizable(true);
			Dimension dim = frame.getSize();
			dim.width -= 100;
			frame.setSize(dim);
		}
		if (e.getActionCommand().equals("NotResizable")) {
			frame.setResizable(false);
			Dimension dim = frame.getSize();
			dim.width += 100;
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
			settingsFrame= new JFrame("Settings");
			settingsFrame.setType(Type.POPUP);
			settingsFrame.setVisible(true);
			settingsFrame.setResizable(false);
			settingsFrame.setLayout(new GridBagLayout());
			settingsFrame.add(settingsPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0));
			settingsFrame.setPreferredSize(settingsPanel.getPreferredSize());
			settingsFrame.setSize((int) (settingsFrame.getPreferredSize().getWidth())+settingsFrameWidth, (int) (settingsFrame.getPreferredSize().getHeight())+settingsFrameHeight);
			
			settingsFrame.setLocation((int) (frame.getLocation().getX()-xPosition), (int) frame.getLocation().getY());
			
//			System.out.println(settingsFrame.getSize());
			System.out.println(settingsPanel.getSize().getWidth());
		}
	}
}
