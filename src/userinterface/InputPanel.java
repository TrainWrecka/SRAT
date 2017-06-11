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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.opencsv.CSVReader;

import dataProcessing.Model;
import programUtilites.JFormattedDoubleTextField;
import programUtilites.MyBorderFactory;

/**
 * 
 * @author Lukas Loosli
 *
 */
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
	private ButtonGroup groupAutoManual = new ButtonGroup();
	
	// JCombobox
	private String comboBoxList[] = { "" + 2, "" + 3, "" + 4, "" + 5, "" + 6, "" + 7, "" + 8, "" + 9, "" + 10 };
	private JComboBox cbOrder = new JComboBox(comboBoxList);

	DecimalFormat f = new DecimalFormat("##0.0#E0");

	DecimalFormat fd = new DecimalFormat("##00.0#E0");

	// Labels
	private JLabel[] lbWp = new JLabel[5];
	private JLabel[] lbQp = new JLabel[5];

	private JLabel lbK = new JLabel("K:");
	private JLabel lbOrder = new JLabel("Order:");
	private JLabel lbSigma = new JLabel("\u03C3:");

	// Textfields
	private JFormattedDoubleTextField[] tfWp = new JFormattedDoubleTextField[5];
	private JFormattedDoubleTextField[] tfQp = new JFormattedDoubleTextField[5];

	private JFormattedDoubleTextField tfSigma = new JFormattedDoubleTextField(fd, 0);
	private JFormattedDoubleTextField tfK = new JFormattedDoubleTextField(fd, 0);

	//file chooser
	private JFileChooser fileChooser = new JFileChooser();

	private Controller controller;

	private double order;
	
	//================================================================================
	// Public Methods (excl. Setter and Getter)
	//================================================================================

	public InputPanel() {
		super(new GridBagLayout());
		setBorder(MyBorderFactory.createMyBorder("Input"));
		
		// create Buttongroup
		rbtAutomatically.setSelected(true);
		groupAutoManual.add(rbtAutomatically);
		groupAutoManual.add(rbtManually);

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
		add(lbOrder, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START,
				GridBagConstraints.NONE, new Insets(10, 0, 0, 0), 0, 0));

		// Label und Texfield für k platzieren
		add(lbK, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE,
				new Insets(20, 0, 0, 0), 0, 0));
		add(tfK, new GridBagConstraints(1, 4, 1, 1, 1.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.BOTH,
				new Insets(20, 0, 0, 0), 0, 0));
		lbK.setEnabled(false);
		tfK.setEnabled(false);

		// Array für wp Labels und Textfelder erzeugen & platzieren
		for (int i = 0; i < 5; i++) {
			lbWp[i] = new JLabel("\u03C9p" + (i + 1) + ":");
			tfWp[i] = new JFormattedDoubleTextField(fd, 0);
			add(lbWp[i], new GridBagConstraints(0, wpPlacement, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START,
					GridBagConstraints.NONE, new Insets(20, 0, 0, 0), 0, 0));
			add(tfWp[i], new GridBagConstraints(1, wpPlacement, 1, 1, 1.0, 0.0, GridBagConstraints.FIRST_LINE_START,
					GridBagConstraints.BOTH, new Insets(20, 0, 0, 0), 0, 0));

			lbWp[i].setEnabled(false);
			tfWp[i].setEnabled(false);
			wpPlacement = wpPlacement + 2;
		}
		
		// Array für qp Labels und Textfelder erzeugen & platzieren
		for (int i = 0; i < 5; i++) {
			lbQp[i] = new JLabel("qp" + (i + 1) + ":");
			tfQp[i] = new JFormattedDoubleTextField(fd, 0);
			add(lbQp[i], new GridBagConstraints(0, qpPlacement, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START,
					GridBagConstraints.NONE, new Insets(20, 0, 0, 0), 0, 0));
			add(tfQp[i], new GridBagConstraints(1, qpPlacement, 1, 1, 1.0, 0.0, GridBagConstraints.FIRST_LINE_START,
					GridBagConstraints.BOTH, new Insets(20, 0, 0, 0), 0, 0));
			lbQp[i].setEnabled(false);
			tfQp[i].setEnabled(false);
			qpPlacement = qpPlacement + 2;
		}
		
		lbSigma.setEnabled(false);
		tfSigma.setEnabled(false);

		// Label und Textfeld Sigma platzieren
		add(lbSigma, new GridBagConstraints(0, 25, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE,
				new Insets(20, 0, 0, 0), 0, 0));
		add(tfSigma, new GridBagConstraints(1, 25, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.BOTH,
				new Insets(20, 0, 0, 0), 0, 0));

		// Combobox platzieren
		add(cbOrder, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(10, 0, 0, 0), 0, 0));

		//file chooser options
		//fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home") + "/Desktop/SignaleCSV"));
		//fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
		
		//add action listener
		btLoad.addActionListener(this);
		btRun.addActionListener(this);
		rbtAutomatically.addActionListener(this);
		rbtManually.addActionListener(this);
		btCancel.addActionListener(this);
		cbOrder.addItemListener(this);

		// Buttons und labels deaktivieren für Userführung
		lbOrder.setEnabled(false);
		btRun.setEnabled(false);
		btCancel.setEnabled(false);
		rbtAutomatically.setEnabled(false);
		rbtManually.setEnabled(false);
		cbOrder.setEnabled(false);
	}

	
	/**
	 * Falls deine Approximation durchgeführt wurde, werden die Werte aus dem Model in die 
	 * entsprechenden Textfelder geschrieben. Im anderen Fall wird der Text zurückgesetzt.
	 * @param obs the observable object.
	 * @param obj an argument passed to the notifyObservers method.
	 */
	public void update(Observable obs, Object obj) {
		Model model = (Model) obs;

		if (model.approximated()) {
			rbtManually.setEnabled(true);

			for (int i = 0; i < tfWp.length; i++) {
				if (i < model.getWqp()[0].length) {
					tfWp[i].setText(f.format(model.getWqp()[0][i]).toLowerCase());
					tfQp[i].setText(f.format(model.getWqp()[1][i]).toLowerCase());
				} else {
					tfWp[i].setText("");
					tfQp[i].setText("");
				}
			}

			tfK.setText(f.format(model.getK()).toLowerCase());

			if (model.getOrder() % 2 == 1) {
				tfSigma.setText(f.format(model.getSigma()).toLowerCase());
			} else {
				tfSigma.setText("");
			}
		} else {
			for (int i = 0; i < tfWp.length; i++) {
				tfWp[i].setText("");
				tfQp[i].setText("");
			}

			tfK.setText("");
			tfSigma.setText("");
		}
	}
	
	//================================================================================
	// Events
	//================================================================================

	/**
	 *  Wenn Load gedrückt wird Open Dialog öffnen für Signalauswahl und CSVReader starten. Buttons und Labels deaktivieren
	 *  für Userführung, bei erfolgreicher Einlesung.
	 *  Wenn Run gedrückt wird, Berechnungen starten.
	 *  Wenn Cancel gedrückt wird, Berechnungen abbrechen.
	 *  Wenn Automatically ausgewählt ist, Textfelder, Labels (K, wp, qp und Sigma) deaktivieren.
	 *  Wenn Manually ausgewählt, Textfelder, Labels(K, wp, qp und Sigma) für die gewählte Ordnung aktivieren
	 *  und die Anderen deaktivieren.
	 */
	public void actionPerformed(ActionEvent e) {
		order = Double.parseDouble((String) cbOrder.getSelectedItem());

		if (e.getSource() == btLoad) {
			if (fileChooser.showOpenDialog(getParent()) == JFileChooser.APPROVE_OPTION) {
				cbOrder.setEnabled(false);
				lbOrder.setEnabled(false);
				rbtAutomatically.setEnabled(false);
				rbtManually.setEnabled(false);
				try {
					controller.setMeasurement(readCSV());
					
					rbtAutomatically.setEnabled(true);
					cbOrder.setEnabled(true);
					lbOrder.setEnabled(true);
					
				} catch (NumberFormatException e1) {
					StatusBar.showStatus("Wrong number format");
				} catch (ArrayIndexOutOfBoundsException e2) {
					StatusBar.showStatus("Wrong data format");
				} catch (RuntimeException e3) {
					StatusBar.showStatus(e3.getMessage());
				}
			}
		}

		if (e.getSource() == btRun) {
			int order = Integer.parseInt((String) cbOrder.getSelectedItem());
			controller.setOrder(order);
			if (rbtManually.isSelected() == true) {
				double[][] wqp = new double[2][(int) Math.floor(order / 2)];
				double sigma = 0;
				try {
					double K = Double.parseDouble(tfK.getText());
					for (int i = 0; i < wqp[0].length; i++) {
						wqp[0][i] = Double.parseDouble(tfWp[i].getText());
						wqp[1][i] = Double.parseDouble(tfQp[i].getText());
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
				lbWp[i].setEnabled(false);
				lbQp[i].setEnabled(false);
				tfWp[i].setEnabled(false);
				tfQp[i].setEnabled(false);
			}
			lbSigma.setEnabled(false);
			tfSigma.setEnabled(false);
		} else if (rbtManually.isSelected()) {
			lbK.setEnabled(true);
			tfK.setEnabled(true);

			for (int i = 0; i < Math.floor(order / 2); i++) {
				lbWp[i].setEnabled(true);
				lbQp[i].setEnabled(true);
				tfWp[i].setEnabled(true);
				tfQp[i].setEnabled(true);
			}
			if (order % 2 == 1) {
				lbSigma.setEnabled(true);
				tfSigma.setEnabled(true);
			}
		}
	}

	/**
	 * Wenn Ordnung ändert und Manually ausgewählt ist, Textfelder und Labels (K, wp, qp und Sigma) 
	 * für die entsprechende Ordnung aktivieren und die restlichen Labels deaktivieren.
	 */
	public void itemStateChanged(ItemEvent e) {
		order = Double.parseDouble((String)cbOrder.getSelectedItem());

		for (int i = 0; i < 5; i++) {

			if (i < Math.floor(order / 2) & rbtManually.isSelected()) {
				lbWp[i].setEnabled(true);
				lbQp[i].setEnabled(true);
				tfWp[i].setEnabled(true);
				tfQp[i].setEnabled(true);
			} else {
				lbWp[i].setEnabled(false);
				lbQp[i].setEnabled(false);
				tfWp[i].setEnabled(false);
				tfQp[i].setEnabled(false);
			}
		}
		
		if ((order % 2) != 0 & rbtManually.isSelected()) {
			lbSigma.setEnabled(true);
			tfSigma.setEnabled(true);
		} else {
			lbSigma.setEnabled(false);
			tfSigma.setEnabled(false);
		}
	}

	//================================================================================
	// Private Methods
	//================================================================================
	
	private List<String[]> readCSV() {
		List<String[]> measurementList = null;
		CSVReader reader = null;
		char[] separator = { ',', ' ', '\t' };
		boolean leave = false;

		try {
			File file = fileChooser.getSelectedFile();
			StatusBar.clear();
			StatusBar.showStatus(fileChooser.getSelectedFile().getName() + " loading");

			for (int i = 0; i < separator.length; i++) {
				try {
					leave = true;
					reader = new CSVReader(new FileReader(file), separator[i]);
					measurementList = reader.readAll();
					Double.parseDouble(measurementList.get(0)[0]);
				} catch (NumberFormatException e) {
					leave = false;
				}

				if (leave) {
					break;
				}
			}
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
	
	//================================================================================
	// Setter and Getter
	//================================================================================

	public void setController(Controller controller) {
		this.controller = controller;
	}
}
