package userinterface;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.Observable;

import javax.swing.JLabel;
import javax.swing.JPanel;
import model.Model;

public class VariablePanel extends JPanel {

	//================================================================================
	// Properties
	//================================================================================

	private JLabel lbK = new JLabel("K:");
	private JLabel lbSigma = new JLabel("\u03C3:");
	private JLabel lbError = new JLabel("Error:");
	private JLabel[] lbwp = new JLabel[5];
	private JLabel[] lbqp = new JLabel[5];

	public JLabel lbKValues = new JLabel();
	public JLabel lbSigmaValues = new JLabel();
	public JLabel lbErrorValues = new JLabel();
	public JLabel[] lbwpValues = new JLabel[5];
	public JLabel[] lbqpValues = new JLabel[5];

	private int wpPlacement = 1;
	private int qpPlacement = 2;

	DecimalFormat f = new DecimalFormat("##0.##E0");

	//================================================================================
	// Constructor
	//================================================================================

	public VariablePanel() {
		super(new GridBagLayout());
		setBorder(MyBorderFactory.createMyBorder("Variables"));

		add(lbK, new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.VERTICAL,
				new Insets(0, 0, 0, 0), 0, 0));

		for (int i = 0; i < lbwp.length; i++) {

			lbwp[i] = new JLabel("\u03C9p" + (i + 1) + ":");
			lbqp[i] = new JLabel("qp" + (i + 1) + ":");
			lbwpValues[i] = new JLabel();
			lbqpValues[i] = new JLabel();

			add(lbwp[i], new GridBagConstraints(0, (wpPlacement), 1, 1, 0.0, 1.0, GridBagConstraints.FIRST_LINE_START,
					GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));
			add(lbqp[i], new GridBagConstraints(0, (qpPlacement), 1, 1, 0.0, 1.0, GridBagConstraints.FIRST_LINE_START,
					GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));

			add(lbwpValues[i], new GridBagConstraints(1, (wpPlacement), 1, 1, 1.0, 1.0, GridBagConstraints.FIRST_LINE_START,
					GridBagConstraints.VERTICAL, new Insets(0, 50, 0, 0), 0, 0));
			add(lbqpValues[i], new GridBagConstraints(1, (qpPlacement), 1, 1, 1.0, 1.0, GridBagConstraints.FIRST_LINE_START,
					GridBagConstraints.VERTICAL, new Insets(0, 50, 0, 0), 0, 0));

			wpPlacement = wpPlacement + 2;
			qpPlacement = qpPlacement + 2;
		}

		add(lbSigma, new GridBagConstraints(0, 11, 1, 1, 0.0, 1.0, GridBagConstraints.FIRST_LINE_START,
				GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));
		add(lbError, new GridBagConstraints(0, 12, 1, 1, 0.0, 1.0, GridBagConstraints.FIRST_LINE_START,
				GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));

		add(lbKValues, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.FIRST_LINE_START,
				GridBagConstraints.VERTICAL, new Insets(10, 50, 0, 0), 0, 0));

		add(lbSigmaValues, new GridBagConstraints(1, 11, 1, 1, 1.0, 1.0, GridBagConstraints.FIRST_LINE_START,
				GridBagConstraints.VERTICAL, new Insets(0, 50, 0, 0), 0, 0));
		add(lbErrorValues, new GridBagConstraints(1, 12, 1, 1, 1.0, 1.0, GridBagConstraints.FIRST_LINE_START,
				GridBagConstraints.VERTICAL, new Insets(0, 50, 10, 0), 0, 0));

	}

	//================================================================================
	// Public Methods
	//================================================================================

	/**
	 * Falls die Schrittantwort approximiert wurde, werden die Variabeln aus dem
	 * Model geolt und aktualisiert. Andernfalls werden die Variabeln zurückgesetzt.
	 * @param obs the observable object.
	 * @param obj an argument passed to the notifyObservers method.
	 */
	public void update(Observable obs, Object obj) {
		Model model = (Model) obs;
		int order = model.getOrder();

		if (model.approximated()) {
			lbKValues.setText(f.format(model.getK()).toLowerCase());

			for (int i = 0; i < lbwp.length; i++) {
				if (i < model.getWqp()[0].length) {
					lbwpValues[i].setText(f.format(model.getWqp()[0][i]).toLowerCase());
					lbqpValues[i].setText(f.format(model.getWqp()[1][i]).toLowerCase());
				} else {
					lbwpValues[i].setText("-");
					lbqpValues[i].setText("-");
				}
			}

			if (order % 2 == 1) {
				lbSigmaValues.setText(f.format(model.getSigma()).toLowerCase());
			} else {
				lbSigmaValues.setText("-");
			}

			lbErrorValues.setText(f.format(model.getMeanError()).toLowerCase());
		} else {
			lbKValues.setText("-");

			for (int i = 0; i < lbwp.length; i++) {
				lbwpValues[i].setText("-");
				lbqpValues[i].setText("-");
			}
			lbSigmaValues.setText("-");
			lbErrorValues.setText("-");
		}

	}
}
