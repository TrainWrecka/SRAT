package StepResponseApproximationTool;


import java.awt.BorderLayout;


import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;

import model.Model;
import userinterface.Controller;
import userinterface.MenuBar;
import userinterface.StatusBar;
import userinterface.View;

public class StepResponseApproximationTool extends JFrame {

	private enum Mode {
		FIXED, PACKED, FIXEDRESIZABLE, PACKEDRESIZABLE, CHRIS
	};

	private Mode mode = Mode.FIXED;
//	private int width = 1200, height = 800;
	private Model model = new Model();
	private Controller controller = new Controller(model, this);
	private View view = new View(controller);
	private MenuBar menuBar = new MenuBar(controller, this);
	private StatusBar statusBar = new StatusBar();
	Dimension screenSize= Toolkit.getDefaultToolkit().getScreenSize();	// ScreenSize Problem
	int screenRes= Toolkit.getDefaultToolkit().getScreenResolution();	// ScreenSize Problem

	int height =screenSize.height*10/11 ;			// ScreenSize Problem
	int width = screenSize.width *2/4;			// ScreenSize Problem

	

	private static enum LAF {
		METAL, OCEAN, SYSTEM, NIMROD, NAPKIN
	}

	private static LAF laf = LAF.SYSTEM;

	public void init() {
		setPreferredSize(new Dimension(width,height));

		
		
		model.addObserver(view);
		model.addObserver(menuBar);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(view, BorderLayout.CENTER);
		getContentPane().add(statusBar, BorderLayout.SOUTH);
		setJMenuBar(menuBar);
		
		System.out.println(width);
//		D

		
		pack();
		
		synchronized (getTreeLock()) {
//			setAllFonts(getComponents(), getFont().deriveFont(12.0f));
			setAllFonts(getComponents(), getFont().deriveFont((float)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/80));
		}
		

		// Center the window
		switch (mode) {
			case FIXED:
				setMinimumSize(getPreferredSize());
				setSize(width, height);
				setResizable(false);
				validate();
				break;
			case FIXEDRESIZABLE:
				setMinimumSize(getPreferredSize());
				setSize(width, height);
				setResizable(true);
				validate();
				break;
			case PACKED:
				setMinimumSize(getPreferredSize());
				setResizable(false);
				break;
			case PACKEDRESIZABLE:
				setMinimumSize(getPreferredSize());
				setResizable(true);
				break;
			case CHRIS:
				setPreferredSize(new Dimension((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2,(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()-300));
				setMinimumSize(getPreferredSize());
				setSize(getPreferredSize());
				setMaximumSize(getPreferredSize());
				validate();
				break;
		}
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
		setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
	}

	public void setAllFonts(Component[] comps, Font font) {
		for (int i = 0; i < comps.length; i++) {
			comps[i].setFont(font);
			if (comps[i] instanceof Container) {
				setAllFonts(((Container) comps[i]).getComponents(), font);
			}
		}
	}

	public static void main(String args[]) {


		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				try {
					switch (laf) {
						case METAL:
							UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
							break;
						case OCEAN:
							UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
							MetalLookAndFeel.setCurrentTheme(new OceanTheme());
							break;
						case SYSTEM:
							UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
							break;
						case NIMROD:
							UIManager.setLookAndFeel(new MyNimRODLookAndFeel("DarkGray.theme"));
							break;
						case NAPKIN:
							UIManager.setLookAndFeel(new net.sourceforge.napkinlaf.NapkinLookAndFeel());
							break;
					}
				} catch (Exception exception) {
					exception.printStackTrace();
				}
				StepResponseApproximationTool frame = new StepResponseApproximationTool();
				if (laf != LAF.SYSTEM) {
					frame.setUndecorated(true);
					frame.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
				}
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setTitle("MVCFramework");
				frame.init();
				frame.setVisible(true);
			}
		});
	}
}
