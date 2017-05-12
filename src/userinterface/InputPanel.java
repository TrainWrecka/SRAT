package userinterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;

import com.opencsv.CSVReader;

import model.Model;

import java.awt.*;
import java.awt.Window.Type;
import java.util.List;
import java.util.Observable;

public class InputPanel extends JPanel implements ActionListener, ItemListener {

	private int wpPlacement = 5;
	private int qpPlacement = 6;
	// Buttons
	public JButton btLoad = new JButton("Load");
	private JButton btRun = new JButton("Run");
	private JRadioButton rbtAutomatically = new JRadioButton("Automatically");
	private JRadioButton rbtManually = new JRadioButton("Manually");
	// Buttongroup
	private ButtonGroup gruppeAuto_Manual = new ButtonGroup();
	// JCombobox
	private String comboBoxListe[] = { "" + 2, "" + 3, "" + 4, "" + 5, "" + 6, "" + 7, "" + 8, "" + 9, "" + 10 };
	private JComboBox cbOrdnungsauswahl = new JComboBox(comboBoxListe);
	// Labels

	private JLabel[] lbwp = new JLabel[10];
	private JLabel[] lbqp = new JLabel[10];

	private JLabel lbK = new JLabel("K:");
	private JLabel lbOrdnung = new JLabel("Ordnung:");
	private JLabel lbSigma = new JLabel("\u03C3:");

	private JLabel Output = new JLabel("");
	// Textfields
	private JTextField[] tfwp = new JTextField[10];
	private JTextField[] tfqp = new JTextField[10];
	
	private JTextField tfSigma = new JTextField();
	private JTextField tfK = new JTextField();

	//file chooser
	private JFileChooser fileChooser = new JFileChooser();

	private Controller controller;

	private String Ordnung;
	private double Ordnung1;

	private JFrame settingsFrame;

	//	private StatusBar statusBar = new StatusBar();

	public InputPanel() {
		super(new GridBagLayout());
		// create Buttongroup
		rbtAutomatically.setSelected(true);
		gruppeAuto_Manual.add(rbtAutomatically);
		gruppeAuto_Manual.add(rbtManually);

		// add Buttons to Panel

		add(btLoad, new GridBagConstraints(0, 0, 2, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(10, 5, 0, 10), 0, 0));

		add(rbtAutomatically, new GridBagConstraints(0, 1, 2, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, new Insets(10, 5, 0, 10), 0, 0));
		add(rbtManually, new GridBagConstraints(0, 2, 2, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 10), 0, 0));

		add(btRun, new GridBagConstraints(0, 26, 2, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(10, 5, 0, 10), 0, 0));

		// add Labels to Panel	
		add(lbOrdnung, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START,
				GridBagConstraints.NONE, new Insets(10, 0, 0, 0), 0, 0));

		//add action listener
		btLoad.addActionListener(this);

		// Label und Texfield für k platzieren
		add(lbK, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		add(tfK, new GridBagConstraints(1, 4, 1, 1, 1.0, 0.0, GridBagConstraints.FIRST_LINE_START,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		lbK.setEnabled(false);
		tfK.setEnabled(false);
		
		// Array für wp Labels und Textfelder erzeugen & platzieren
		for (int i = 0; i < 10; i++) {
			lbwp[i] = new JLabel("\u03C9p" + (i + 1) + ":");
			tfwp[i] = new JTextField();
			add(lbwp[i], new GridBagConstraints(0, wpPlacement, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START,
					GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			add(tfwp[i], new GridBagConstraints(1, wpPlacement, 1, 1, 1.0, 0.0, GridBagConstraints.FIRST_LINE_START,
					GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

			lbwp[i].setEnabled(false);
			tfwp[i].setEnabled(false);
			wpPlacement = wpPlacement + 2;
		}
		// Array für qp Labels und Textfelder erzeugen & platzieren
		for (int i = 0; i < 10; i++) {
			lbqp[i] = new JLabel("qp" + (i + 1) + ":");
			tfqp[i] = new JTextField();
			add(lbqp[i], new GridBagConstraints(0, qpPlacement, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START,
					GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			add(tfqp[i], new GridBagConstraints(1, qpPlacement, 1, 1, 1.0, 0.0, GridBagConstraints.FIRST_LINE_START,
					GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			lbqp[i].setEnabled(false);
			tfqp[i].setEnabled(false);
			qpPlacement = qpPlacement + 2;
		}

		lbSigma.setEnabled(false);
		tfSigma.setEnabled(false);
		cbOrdnungsauswahl.setEnabled(true);
		lbOrdnung.setEnabled(true);

		// Label und Textfeld Sigma platzieren
		add(lbSigma, new GridBagConstraints(0, 25, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		add(tfSigma, new GridBagConstraints(1, 25, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		// Combobox platzieren
		//		cbOrdnungsauswahl.setPreferredSize(new Dimension(50, 20));
		add(cbOrdnungsauswahl, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(10, 0, 0, 0), 0, 0));

		//file chooser options
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));

		cbOrdnungsauswahl.addItemListener(this);
		rbtAutomatically.addActionListener(this);
		rbtManually.addActionListener(this);

	}

	private double[] stringToCoeff(String s) {
		String[] tokens = s.split("[, ]+");
		double[] z = new double[tokens.length];
		for (int i = 0; i < z.length; i++) {
			z[i] = Double.parseDouble(tokens[i]);
		}
		return z;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	// Ausgrauen von allen Textfeldern, Labels, Ordnungsauswahl und Combobox bei entsprechender Aktion
	@Override
	public void actionPerformed(ActionEvent e) {
		Ordnung = (String) cbOrdnungsauswahl.getSelectedItem();
		Ordnung1 = Double.parseDouble(Ordnung);
		if (e.getSource() == rbtAutomatically) {
			lbK.setEnabled(false);
			tfK.setEnabled(false);
			
			for (int i = 0; i < 10; i++) {
				lbwp[i].setEnabled(false);
				lbqp[i].setEnabled(false);
				tfwp[i].setEnabled(false);
				tfqp[i].setEnabled(false);
			}
			lbSigma.setEnabled(false);
			tfSigma.setEnabled(false);
		} else if (e.getSource() == rbtManually) {
			lbK.setEnabled(true);
			tfK.setEnabled(true);
			
			for (int i = 0; i < Ordnung1; i++) {
			lbwp[i].setEnabled(true);
			lbqp[i].setEnabled(true);
			tfwp[i].setEnabled(true);
			tfqp[i].setEnabled(true);
			
			}
			if(Ordnung1%2==1){
				lbSigma.setEnabled(true);
				tfSigma.setEnabled(true);
			}
		}
		if (e.getSource() == btLoad) {
			if (fileChooser.showOpenDialog(getParent()) == JFileChooser.APPROVE_OPTION) {
				controller.setOrder(Integer.parseInt((String) cbOrdnungsauswahl.getSelectedItem()));
				controller.setMeasurement(readCSV());
			}

			
		}

	}

	// Ausgrauen von Textfeldern und Labels bei entsprechender Aktion
	@Override
	public void itemStateChanged(ItemEvent e) {
		Ordnung = "0";
		Ordnung = (String) cbOrdnungsauswahl.getSelectedItem();
		Ordnung1 = Double.parseDouble(Ordnung);

		for (int i = 0; i < 10; i++) {

			if (i < Ordnung1 & rbtAutomatically.isSelected() == false) {
				lbwp[i].setEnabled(true);
				lbqp[i].setEnabled(true);
				tfwp[i].setEnabled(true);
				tfqp[i].setEnabled(true);
			} else {
				lbwp[i].setEnabled(false);
				lbqp[i].setEnabled(false);
				tfwp[i].setEnabled(false);
				tfqp[i].setEnabled(false);
			}
		}
		if ((Ordnung1 % 2) != 0 & rbtAutomatically.isSelected() == false) {
			lbSigma.setEnabled(true);
			tfSigma.setEnabled(true);
		} else {
			lbSigma.setEnabled(false);
			tfSigma.setEnabled(false);
		}
		//		switch (Ordnung) {
		//			case "1":
		//				for (int i = 0; i < 10; i++) {
		//					if (i < 1) {
		//						lbwp[i].setEnabled(true);
		//						lbqp[i].setEnabled(true);
		//						tfwp[i].setEnabled(true);
		//						tfqp[i].setEnabled(true);
		//					} else {
		//						lbwp[i].setEnabled(false);
		//						lbqp[i].setEnabled(false);
		//						tfwp[i].setEnabled(false);
		//						tfqp[i].setEnabled(false);
		//					}
		//				}
		//				lbSigma.setEnabled(true);
		//				tfSigma.setEditable(true);
		//				break;
		//
		//			case "2":
		//				for (int i = 0; i < 10; i++) {
		//					if (i < 2) {
		//						lbwp[i].setEnabled(true);
		//						lbqp[i].setEnabled(true);
		//						tfwp[i].setEnabled(true);
		//						tfqp[i].setEnabled(true);
		//					} else {
		//						lbwp[i].setEnabled(false);
		//						lbqp[i].setEnabled(false);
		//						tfwp[i].setEnabled(false);
		//						tfqp[i].setEnabled(false);
		//					}
		//				}
		//				lbSigma.setEnabled(false);
		//				tfSigma.setEditable(false);
		//				break;
		//
		//			case "3":
		//				for (int i = 0; i < 10; i++) {
		//					if (i < 3) {
		//						lbwp[i].setEnabled(true);
		//						lbqp[i].setEnabled(true);
		//						tfwp[i].setEnabled(true);
		//						tfqp[i].setEnabled(true);
		//					} else {
		//						lbwp[i].setEnabled(false);
		//						lbqp[i].setEnabled(false);
		//						tfwp[i].setEnabled(false);
		//						tfqp[i].setEnabled(false);
		//					}
		//				}
		//				lbSigma.setEnabled(true);
		//				tfSigma.setEditable(true);
		//				break;
		//
		//			case "4":
		//				for (int i = 0; i < 10; i++) {
		//					if (i < 4) {
		//						lbwp[i].setEnabled(true);
		//						lbqp[i].setEnabled(true);
		//						tfwp[i].setEnabled(true);
		//						tfqp[i].setEnabled(true);
		//					} else {
		//						lbwp[i].setEnabled(false);
		//						lbqp[i].setEnabled(false);
		//						tfwp[i].setEnabled(false);
		//						tfqp[i].setEnabled(false);
		//					}
		//				}
		//				lbSigma.setEnabled(false);
		//				tfSigma.setEditable(false);
		//				break;
		//
		//			case "5":
		//				for (int i = 0; i < 10; i++) {
		//					if (i < 5) {
		//						lbwp[i].setEnabled(true);
		//						lbqp[i].setEnabled(true);
		//						tfwp[i].setEnabled(true);
		//						tfqp[i].setEnabled(true);
		//					} else {
		//						lbwp[i].setEnabled(false);
		//						lbqp[i].setEnabled(false);
		//						tfwp[i].setEnabled(false);
		//						tfqp[i].setEnabled(false);
		//					}
		//				}
		//				lbSigma.setEnabled(true);
		//				tfSigma.setEditable(true);
		//				break;
		//
		//			case "6":
		//				for (int i = 0; i < 10; i++) {
		//					if (i < 6) {
		//						lbwp[i].setEnabled(true);
		//						lbqp[i].setEnabled(true);
		//						tfwp[i].setEnabled(true);
		//						tfqp[i].setEnabled(true);
		//					} else {
		//						lbwp[i].setEnabled(false);
		//						lbqp[i].setEnabled(false);
		//						tfwp[i].setEnabled(false);
		//						tfqp[i].setEnabled(false);
		//					}
		//				}
		//				lbSigma.setEnabled(false);
		//				tfSigma.setEditable(false);
		//				break;
		//
		//			case "7":
		//				for (int i = 0; i < 10; i++) {
		//					if (i < 7) {
		//						lbwp[i].setEnabled(true);
		//						lbqp[i].setEnabled(true);
		//						tfwp[i].setEnabled(true);
		//						tfqp[i].setEnabled(true);
		//					} else {
		//						lbwp[i].setEnabled(false);
		//						lbqp[i].setEnabled(false);
		//						tfwp[i].setEnabled(false);
		//						tfqp[i].setEnabled(false);
		//					}
		//				}
		//				lbSigma.setEnabled(true);
		//				tfSigma.setEditable(true);
		//				break;
		//
		//			case "8":
		//				for (int i = 0; i < 10; i++) {
		//					if (i < 8) {
		//						lbwp[i].setEnabled(true);
		//						lbqp[i].setEnabled(true);
		//						tfwp[i].setEnabled(true);
		//						tfqp[i].setEnabled(true);
		//					} else {
		//						lbwp[i].setEnabled(false);
		//						lbqp[i].setEnabled(false);
		//						tfwp[i].setEnabled(false);
		//						tfqp[i].setEnabled(false);
		//					}
		//				}
		//				lbSigma.setEnabled(false);
		//				tfSigma.setEditable(false);
		//				break;
		//
		//			case "9":
		//				for (int i = 0; i < 10; i++) {
		//					if (i < 9) {
		//						lbwp[i].setEnabled(true);
		//						lbqp[i].setEnabled(true);
		//						tfwp[i].setEnabled(true);
		//						tfqp[i].setEnabled(true);
		//					} else {
		//						lbwp[i].setEnabled(false);
		//						lbqp[i].setEnabled(false);
		//						tfwp[i].setEnabled(false);
		//						tfqp[i].setEnabled(false);
		//					}
		//				}
		//				lbSigma.setEnabled(true);
		//				tfSigma.setEditable(true);
		//				break;
		//
		//			case "10":
		//				for (int i = 0; i < 10; i++) {
		//					lbwp[i].setEnabled(true);
		//					lbqp[i].setEnabled(true);
		//					tfwp[i].setEnabled(true);
		//					tfqp[i].setEnabled(true);
		//				}
		//				lbSigma.setEnabled(false);
		//				tfSigma.setEditable(false);
		//				break;
		//		}
	}

	/*
	 * reads a csv file
	 */
	private List<String[]> readCSV() {
		CSVReader reader = null;
		List<String[]> measurementList = null;

		try {
			reader = new CSVReader(new FileReader(fileChooser.getSelectedFile()));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			StatusBar.showStatus("File does not exist");
		}

		try {
			measurementList = reader.readAll();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			StatusBar.showStatus("File does not exist");
		}

		StatusBar.showStatus(fileChooser.getSelectedFile().getName() + " loading...");

		return measurementList;
	}
}
