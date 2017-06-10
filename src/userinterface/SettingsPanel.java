package userinterface;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SettingsPanel extends JPanel implements ActionListener, ItemListener, ChangeListener, FocusListener {

	//================================================================================
	// Properties
	//================================================================================

	private Controller controller;

	private JLabel lbLaguerre = new JLabel("Laguerrefilter Accuracy");
	private JLabel lbSimplexOptimizerRelative = new JLabel("Simplex Optimizer relative Optimum");
	private JLabel lbSimplexOptimizerAbsolute = new JLabel("Simplex Optimizer absolute Optimum");
	private JLabel lbMaxEval = new JLabel("Max Eval length");
	private JLabel lbNelderMeadSimplexSteps = new JLabel("Nelder Mead Simplex Steps");

	//	private JLabel lbFilterLength=new JLabel("Filter length");
	//	private JLabel lbFilterErrorMax=new JLabel("Filter Error Max");
	private JLabel lbFilterSignal = new JLabel("Filter Signal");
	private JLabel lbShowFilteredSignal = new JLabel("Show filtered Signal");

	private JLabel lbFill = new JLabel("");
	private JLabel lbFilter = new JLabel();

	private JLabel lbAutoFilter = new JLabel("Autofilter");

	private JTextField tfLaguerre = new JTextField();
	private JTextField tfSimplexOptimizerRelative = new JTextField();
	private JTextField tfSimplexOptimizerAbsolute = new JTextField();
	private JTextField tfMaxEval = new JTextField();
	private JTextField tfNelderMeadSimplexSteps = new JTextField();
	//	private JTextField tfFilterLength=new JTextField();
	//	private JTextField tfFilterErrorMax=new JTextField();

	private JRadioButton rbtFilterSignalYes = new JRadioButton("Yes");
	private JRadioButton rbtFilterSignalNo = new JRadioButton("No");
	private JRadioButton rbtShowFilteredSignalYes = new JRadioButton("Yes");
	private JRadioButton rbtShowFilteredSignalNo = new JRadioButton("No");

	private ButtonGroup groupFilterSignal = new ButtonGroup();
	private ButtonGroup groupShowFilteredSignal = new ButtonGroup();

	private JButton btApply = new JButton("Apply");
	private JButton btDefaults = new JButton("Defaults");

	private JCheckBox cbAutoFilter = new JCheckBox();

	static final int SlideValueMin = 0;
	static final int SlideValueMax = 100;
	static final int SlideValueInit = 100;

	private JSlider jsFilter = new JSlider(JSlider.HORIZONTAL, SlideValueMin, SlideValueMax, SlideValueInit);

	//================================================================================
	// Constructor
	//================================================================================
	
	public SettingsPanel(Controller controller) {
		super(new GridBagLayout());
		this.controller = controller;

		jsFilter.setMajorTickSpacing(20);
		jsFilter.setMinorTickSpacing(10);
		jsFilter.setPaintTicks(true);
		jsFilter.setPaintLabels(true);
		jsFilter.setSnapToTicks(true);

		lbFilter.setText("Filter accuracy in %    " + jsFilter.getValue());

		groupFilterSignal.add(rbtFilterSignalYes);
		groupFilterSignal.add(rbtFilterSignalNo);

		groupShowFilteredSignal.add(rbtShowFilteredSignalYes);
		groupShowFilteredSignal.add(rbtShowFilteredSignalNo);

		add(lbLaguerre, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(10, 10, 0, 0), 0, 0));
		add(tfLaguerre, new GridBagConstraints(1, 0, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(10, 0, 0, 10), 0, 0));
		add(lbSimplexOptimizerRelative, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 0), 0, 0));
		add(tfSimplexOptimizerRelative, new GridBagConstraints(1, 1, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 0, 0, 10), 0, 0));
		add(lbSimplexOptimizerAbsolute, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 0), 0, 0));
		add(tfSimplexOptimizerAbsolute, new GridBagConstraints(1, 3, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 0, 0, 10), 0, 0));
		add(lbNelderMeadSimplexSteps, new GridBagConstraints(0, 4, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 0), 0, 0));
		add(tfNelderMeadSimplexSteps, new GridBagConstraints(1, 4, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 0, 0, 10), 0, 0));
		add(lbMaxEval, new GridBagConstraints(0, 5, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(10, 10, 0, 0), 0, 0));
		add(tfMaxEval, new GridBagConstraints(1, 5, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(10, 0, 0, 10), 0, 0));
		
		
		
		
//		add(lbFilterLength,new GridBagConstraints(0, 6, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
//				new Insets(10, 10, 0, 0), 0, 0));
//		add(tfFilterLength,new GridBagConstraints(1, 6, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
//				new Insets(10, 0, 0, 10), 0, 0));
//		add(lbFilterErrorMax,new GridBagConstraints(0, 7, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
//				new Insets(10, 10, 0, 0), 0, 0));	
//		add(tfFilterErrorMax,new GridBagConstraints(1, 7, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
//				new Insets(10, 0, 0, 10), 0, 0));	
		
		
		
		add(lbFilterSignal, new GridBagConstraints(0, 8, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(10, 10, 0, 0), 0, 0));
		add(rbtFilterSignalYes, new GridBagConstraints(1, 8, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(10, 0, 0, 0), 0, 0));
		add(rbtFilterSignalNo, new GridBagConstraints(2, 8, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(10, 0, 0, 10), 0, 0));
		
		add(lbShowFilteredSignal, new GridBagConstraints(0, 9, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(10, 10, 0, 0), 0, 0));
		add(rbtShowFilteredSignalYes, new GridBagConstraints(1, 9, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(10, 0, 0, 0), 0, 0));
		add(rbtShowFilteredSignalNo, new GridBagConstraints(2, 9, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(10, 0, 0, 10), 0, 0));
		


		add(lbAutoFilter, new GridBagConstraints(0, 11, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 20), 0, 0));
		add(cbAutoFilter, new GridBagConstraints(1, 11, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 20), 0, 0));


		add(lbAutoFilter,new GridBagConstraints(0, 10, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(10, 10, 0, 20), 0, 0));
		add(cbAutoFilter, new GridBagConstraints(1, 10, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(10, 10, 0, 20), 0, 0));
		
		
		
		add(lbFilter, new GridBagConstraints(0, 11, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,

				new Insets(10, 10, 0, 20), 0, 0));
		add(jsFilter, new GridBagConstraints(1, 11, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(10, 0, 0, 10), 0, 0));
		
		
		add(lbFill, new GridBagConstraints(0, 12, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(10, 0, 0, 10), 0, 0));
		add(btDefaults, new GridBagConstraints(1, 12, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(10, 0, 0, 10), 0, 0));
	
		
		

		btApply.addActionListener(this);
		btDefaults.addActionListener(this);
		cbAutoFilter.addActionListener(this);
		rbtFilterSignalYes.addActionListener(this);
		rbtFilterSignalNo.addActionListener(this);
		rbtShowFilteredSignalYes.addActionListener(this);
		rbtShowFilteredSignalNo.addActionListener(this);
		cbAutoFilter.addActionListener(this);
		
		tfLaguerre.addFocusListener(this);
		tfMaxEval.addFocusListener(this);
		tfNelderMeadSimplexSteps.addFocusListener(this);
		tfSimplexOptimizerAbsolute.addFocusListener(this);
		tfSimplexOptimizerRelative.addFocusListener(this);
		
	
		

		jsFilter.addChangeListener(this);
		jsFilter.setEnabled(false);
		initFields();
		btApply.doClick();
	}
	
	//================================================================================
	// Public Methods
	//================================================================================

	// Ausgrauen von allen Textfeldern, Labels, Ordnungsauswahl und Combobox bei entsprechender Aktion
	
	/**
	 * Wenn der Default-Button gedrückt wurde werden die Standardwerte geladen, beim Apply-Button werden
	 * die Werte aller Textfelder dem Controller übergeben.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btDefaults) {
			initFields();
			//updateValues();
		}

		if (e.getSource() == btApply) {
			//updateValues();

		}
		
		if (e.getSource() == cbAutoFilter) {
			if (cbAutoFilter.isSelected()) {
				jsFilter.setEnabled(false);
			} else {
				jsFilter.setEnabled(true);
			}

		}
		
		updateValues();
		
	}

	

	// Ausgrauen von Textfeldern und Labels bei entsprechender Aktion
	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		lbFilter.setText("Filter accuracy in %   " + jsFilter.getValue());
			updateValues();
	}
	
	//================================================================================
	// Private Methods
	//================================================================================
	
	/**
	 * Lädt Standadtwerte in die Textfelder
	 */
	private void initFields() {
		tfLaguerre.setText("1e-5");
		tfSimplexOptimizerRelative.setText("1e-24");
		tfSimplexOptimizerAbsolute.setText("1e-24");
		tfNelderMeadSimplexSteps.setText("0.1");
		tfMaxEval.setText("5000");
		cbAutoFilter.setSelected(true);
		rbtFilterSignalYes.setSelected(true);
		rbtShowFilteredSignalNo.setSelected(true);
		jsFilter.setValue(100);
	}

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void focusLost(FocusEvent e) {
		// TODO Auto-generated method stub
		updateValues();
	}
	
	private void updateValues(){
		try {
			double LaguerreAcc = Double.parseDouble(tfLaguerre.getText());
			double[] simplexOpt = { Double.parseDouble(tfSimplexOptimizerRelative.getText()),
					Double.parseDouble(tfSimplexOptimizerAbsolute.getText()) };
			double nelderSteps = Double.parseDouble(tfNelderMeadSimplexSteps.getText());
			int maxEval = Integer.parseInt(tfMaxEval.getText());
			boolean doFilter = rbtFilterSignalYes.isSelected();
			boolean showFiltered = rbtShowFilteredSignalYes.isSelected();
			boolean autoFilter = cbAutoFilter.isSelected();
			int filterPercentage = jsFilter.getValue();

			controller.setSettings(new Object[] { LaguerreAcc, simplexOpt, nelderSteps, maxEval, doFilter, showFiltered,
					autoFilter, filterPercentage });
		} catch (NumberFormatException e2) {
			// TODO: handle exception
			StatusBar.showStatus("Wrong number format");
		}
	}
}
