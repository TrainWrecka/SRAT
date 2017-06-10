package userinterface;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Observable;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.opencsv.CSVReader;

import model.Model;

public class InputPanel extends JPanel implements ActionListener, ItemListener {

	//================================================================================
	// Properties
	//================================================================================
	
	private int wpPlacement = 5;
	private int qpPlacement = 6;
	// Buttons
	private JButton btLoad = new JButton("Load");
	public JButton btRun = new JButton("Run");
	public JButton btCancel = new JButton("Cancel");

	private JRadioButton rbtAutomatically = new JRadioButton("Automatically");
	private JRadioButton rbtManually = new JRadioButton("Manually");
	// Buttongroup
	private ButtonGroup gruppeAuto_Manual = new ButtonGroup();
	// JCombobox
	private String comboBoxListe[] = { "" + 2, "" + 3, "" + 4, "" + 5, "" + 6, "" + 7, "" + 8, "" + 9, "" + 10 };
	private JComboBox cbOrdnungsauswahl = new JComboBox(comboBoxListe);

	DecimalFormat f = new DecimalFormat("##0.0#E0");
	
//	DecimalFormat fd = new DecimalFormat("##00.0#E0");


	// Labels

	private JLabel[] lbwp = new JLabel[5];
	private JLabel[] lbqp = new JLabel[5];

	private JLabel lbK = new JLabel("K:");
	private JLabel lbOrdnung = new JLabel("Order:");
	private JLabel lbSigma = new JLabel("\u03C3:");

	// Textfields
	private JEngineerField[] tfwp = new JEngineerField[5];
	private JEngineerField[] tfqp = new JEngineerField[5];

	private JEngineerField tfSigma = new JEngineerField(3, 0);
	private JEngineerField tfK = new JEngineerField(3, 0);

	//file chooser
	private JFileChooser fileChooser = new JFileChooser();

	private Controller controller;

	private String Ordnung;
	private double Ordnung1;

	//================================================================================
	// Constructor
	//================================================================================
	
	public InputPanel() {
		super(new GridBagLayout());
		setBorder(MyBorderFactory.createMyBorder("Input"));
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

		add(btRun, new GridBagConstraints(0, 26, 2, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(10, 5, 0, 10), 0, 0));
		add(btCancel, new GridBagConstraints(0, 27, 2, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(10, 5, 0, 10), 0, 0));

		// add Labels to Panel	
		add(lbOrdnung, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START,
				GridBagConstraints.NONE, new Insets(10, 0, 0, 0), 0, 0));

		//add action listener
		btLoad.addActionListener(this);
		btRun.addActionListener(this);

		// Label und Texfield für k platzieren
		add(lbK, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE,
				new Insets(20, 0, 0, 0), 0, 0));
		add(tfK, new GridBagConstraints(1, 4, 1, 1, 1.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.BOTH,
				new Insets(20, 0, 0, 0), 0, 0));
		lbK.setEnabled(false);
		tfK.setEnabled(false);

		// Array für wp Labels und Textfelder erzeugen & platzieren
		for (int i = 0; i < 5; i++) {
			lbwp[i] = new JLabel("\u03C9p" + (i + 1) + ":");
			tfwp[i] = new JEngineerField(3, 0);
			add(lbwp[i], new GridBagConstraints(0, wpPlacement, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START,
					GridBagConstraints.NONE, new Insets(20, 0, 0, 0), 0, 0));
			add(tfwp[i], new GridBagConstraints(1, wpPlacement, 1, 1, 1.0, 0.0, GridBagConstraints.FIRST_LINE_START,
					GridBagConstraints.BOTH, new Insets(20, 0, 0, 0), 0, 0));

			lbwp[i].setEnabled(false);
			tfwp[i].setEnabled(false);
			wpPlacement = wpPlacement + 2;
		}
		// Array für qp Labels und Textfelder erzeugen & platzieren
		for (int i = 0; i < 5; i++) {
			lbqp[i] = new JLabel("qp" + (i + 1) + ":");
			tfqp[i] = new JEngineerField(3, 0);
			add(lbqp[i], new GridBagConstraints(0, qpPlacement, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START,
					GridBagConstraints.NONE, new Insets(20, 0, 0, 0), 0, 0));
			add(tfqp[i], new GridBagConstraints(1, qpPlacement, 1, 1, 1.0, 0.0, GridBagConstraints.FIRST_LINE_START,
					GridBagConstraints.BOTH, new Insets(20, 0, 0, 0), 0, 0));
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
				new Insets(20, 0, 0, 0), 0, 0));
		add(tfSigma, new GridBagConstraints(1, 25, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.BOTH,
				new Insets(20, 0, 0, 0), 0, 0));

		// Combobox platzieren
		add(cbOrdnungsauswahl, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(10, 0, 0, 0), 0, 0));

		//file chooser options
		//fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home") + "/Desktop/SignaleCSV"));
		//	fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));

		cbOrdnungsauswahl.addItemListener(this);
		rbtAutomatically.addActionListener(this);
		rbtManually.addActionListener(this);
		btCancel.addActionListener(this);
		btRun.setEnabled(false);
		btCancel.setEnabled(false);
		rbtManually.setEnabled(false);

	}



	//================================================================================
	// Public Methods
	//================================================================================
	
	/**
	 * setzt controller
	 * @param controller
	 */
	public void setController(Controller controller) {
		this.controller = controller;
	}

	/**
	 *  Wenn Load gedrückt wird Open Dialog öffnen für Signalauswahl.
	 *  
	 *  Wenn Run gedrückt wird, Berechnungen starten.
	 *  
	 *  Wenn Cancel gedrückt wird, Berechnungen abbrechen.
	 *  
	 *  Wenn Automatically ausgewählt, Textfelder, Labels (K, wp, qp und Sigma) deaktivieren.
	 *  
	 *  Wenn Manually ausgewählt, Textfelder, Labels(K, wp, qp und Sigma) für die gewählte Ordnung aktivieren
	 *  und die Anderen deaktivieren.
	 *  
	 *  
	 */
	public void actionPerformed(ActionEvent e) {
		Ordnung = (String) cbOrdnungsauswahl.getSelectedItem();
		Ordnung1 = Double.parseDouble(Ordnung);

		if (e.getSource() == btLoad) {
			if (fileChooser.showOpenDialog(getParent()) == JFileChooser.APPROVE_OPTION) {
				try {
					controller.setMeasurement(readCSV());
					rbtAutomatically.setSelected(true);
					rbtManually.setEnabled(false);

				} catch (RuntimeException e1) {
					StatusBar.showStatus(e1.getMessage());
				}
			}
		}

		if (e.getSource() == btRun) {
			int order = Integer.parseInt((String) cbOrdnungsauswahl.getSelectedItem());
			controller.setOrder(order);
			if (rbtManually.isSelected() == true) {
				double[][] wqp = new double[2][(int) Math.floor(order / 2)];
				double sigma = 0;
				try {
					double K = Double.parseDouble(tfK.getText());
					for (int i = 0; i < wqp[0].length; i++) {
						wqp[0][i] = Double.parseDouble(tfwp[i].getText());
						wqp[1][i] = Double.parseDouble(tfqp[i].getText());
					}

					if (order % 2 == 0) {
						sigma = 0;
					} else {
						sigma = Double.parseDouble(tfSigma.getText());
					}
					
					controller.setK(K);
					controller.setSigma(sigma);
					controller.setWqp(wqp);
					controller.approximateManual();
				} catch (NumberFormatException e2) {
					// TODO: handle exception
					StatusBar.showStatus("Wrong number format");
				}

			} else {
				controller.approximateAuto();
			}
		}
		if (e.getSource() == btCancel) {
			controller.stopApproximation();
		}

		if (rbtAutomatically.isSelected()) {
			lbK.setEnabled(false);
			tfK.setEnabled(false);

			for (int i = 0; i < 5; i++) {
				lbwp[i].setEnabled(false);
				lbqp[i].setEnabled(false);
				tfwp[i].setEnabled(false);
				tfqp[i].setEnabled(false);
			}
			lbSigma.setEnabled(false);
			tfSigma.setEnabled(false);
		} else if (rbtManually.isSelected()) {
			lbK.setEnabled(true);
			tfK.setEnabled(true);

			for (int i = 0; i < Math.floor(Ordnung1 / 2); i++) {
				lbwp[i].setEnabled(true);
				lbqp[i].setEnabled(true);
				tfwp[i].setEnabled(true);
				tfqp[i].setEnabled(true);
			}
			if (Ordnung1 % 2 == 1) {
				lbSigma.setEnabled(true);
				tfSigma.setEnabled(true);
			}
		}
	}


	/**
	 * Wenn Ordnung ändert und Manually ausgewählt ist, Textfelder und Labels (K, wp, qp und Sigma) 
	 * für die entsprechende Ordnung aktivieren und die Anderen deaktivieren.
	 */
	public void itemStateChanged(ItemEvent e) {
		Ordnung = "0";
		Ordnung = (String) cbOrdnungsauswahl.getSelectedItem();
		Ordnung1 = Double.parseDouble(Ordnung);

		for (int i = 0; i < 5; i++) {

			if (i < Math.floor(Ordnung1 / 2) & rbtManually.isSelected()) {
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
		if ((Ordnung1 % 2) != 0 & rbtManually.isSelected() == false) {
			lbSigma.setEnabled(true);
			tfSigma.setEnabled(true);
		} else {
			lbSigma.setEnabled(false);
			tfSigma.setEnabled(false);
		}

	}

	/**
	 * 
	 * @param obs
	 * @param obj
	 */
	public void update(Observable obs, Object obj) {
		Model model = (Model) obs;

		if (model.approximated()) {
			//btRun.setEnabled(true);
			rbtManually.setEnabled(true);

			for (int i = 0; i < tfwp.length; i++) {
				if (i < model.getWqp()[0].length) {
					tfwp[i].setText(f.format(model.getWqp()[0][i]).toLowerCase());
					tfqp[i].setText(f.format(model.getWqp()[1][i]).toLowerCase());
				} else {
					tfwp[i].setText("");
					tfqp[i].setText("");
				}
			}

			tfK.setText(f.format(model.getK()).toLowerCase());

			if (model.getOrder() % 2 == 1) {
				tfSigma.setText(f.format(model.getSigma()).toLowerCase());
			} else {
				tfSigma.setText("");
			}
		} else {
			for (int i = 0; i < tfwp.length; i++) {
				tfwp[i].setText("");
				tfqp[i].setText("");
			}

			tfK.setText("");
			tfSigma.setText("");
		}
		
	}
	
	//================================================================================
	// Private Methods
	//================================================================================
	
	private List<String[]> readCSV() {
		List<String[]> measurementList = null;

		try {
			File file = fileChooser.getSelectedFile();
			StatusBar.clear();
			StatusBar.showStatus(fileChooser.getSelectedFile().getName() + " loading");
			CSVReader reader = new CSVReader(new FileReader(file));
			
			measurementList = reader.readAll();
			reader.close();
			StatusBar.clear();
			StatusBar.showStatus(fileChooser.getSelectedFile().getName() + " loaded");
		} catch (FileNotFoundException e1) {
			throw new RuntimeException("File does not exist");
		} catch (IOException e2) {
			throw new RuntimeException("IO Problem when reading file");
		}

		return measurementList;
	}
	
	private double[] stringToCoeff(String s) {
		String[] tokens = s.split("[, ]+");
		double[] z = new double[tokens.length];
		for (int i = 0; i < z.length; i++) {
			z[i] = Double.parseDouble(tokens[i]);
		}
		return z;
	}
}
