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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;

import com.opencsv.CSVReader;

import javafx.scene.control.RadioButton;
import model.Model;

import java.awt.*;
import java.util.List;
import java.util.Observable;

public class SettingsPanel extends JPanel implements ActionListener, ItemListener, ChangeListener {

	private Controller controller;
	
	private JLabel lbLaguerre=new JLabel("Laguerrefilter Accuracy");
	private JLabel lbSimplexOptimizerRelative=new JLabel("Simplex Optimizer relative Optimum");
	private JLabel lbSimplexOptimizerAbsolute=new JLabel("Simplex Optimizer absolute Optimum"); 
	private JLabel lbMaxEval=new JLabel("Max Eval length");
	private JLabel lbNelderMeadSimplexSteps=new JLabel("Nelder Mead Simplex Steps");
	
//	private JLabel lbFilterLength=new JLabel("Filter length");
//	private JLabel lbFilterErrorMax=new JLabel("Filter Error Max");
	private JLabel lbFilterSignal=new JLabel("Filter Signal");
	private JLabel lbShowFilteredSignal=new JLabel("Show filtered Signal");
	
	private JLabel lbFill=new JLabel("");
	private JLabel lbFilter=new JLabel();
	
	private JLabel lbAutoFilter= new JLabel("Autofilter");
	
	private JTextField tfLaguerre=new JTextField();
	private JTextField tfSimplexOptimizerRelative=new JTextField();
	private JTextField tfSimplexOptimizerAbsolute=new JTextField();
	private JTextField tfMaxEval=new JTextField();
	private JTextField tfNelderMeadSimplexSteps=new JTextField();
//	private JTextField tfFilterLength=new JTextField();
//	private JTextField tfFilterErrorMax=new JTextField();
	
	private JRadioButton rbtFilterSignalYes=new JRadioButton("Yes");
	private JRadioButton rbtFilterSignalNo=new JRadioButton("No");
	private JRadioButton rbtShowFilteredSignalYes=new JRadioButton("Yes");
	private JRadioButton rbtShowFilteredSignalNo=new JRadioButton("No");
	
	private ButtonGroup groupFilterSignal = new ButtonGroup();
	private ButtonGroup groupShowFilteredSignal=new ButtonGroup();
	
	private JButton btApply=new JButton("Apply");
	private JButton btDefaults=new JButton("Defaults");
	
	private JCheckBox cbAutoFilter = new JCheckBox();
	
	
	
	
	static final int SlideValueMin = 0;
	static final int SlideValueMax = 100;
	static final int SlideValueInit = 100;
	
	private JSlider Filter=new JSlider(JSlider.HORIZONTAL,SlideValueMin, SlideValueMax, SlideValueInit);
	
	public SettingsPanel(Controller controller) {
		super(new GridBagLayout());
		this.controller = controller;
		
		Filter.setMajorTickSpacing(20);
		Filter.setMinorTickSpacing(10);
		Filter.setPaintTicks(true);
		Filter.setPaintLabels(true);
		Filter.setSnapToTicks(true);
	
		lbFilter.setText("Filter accuracy in %    "+ Filter.getValue());
		
		groupFilterSignal.add(rbtFilterSignalYes);
		groupFilterSignal.add(rbtFilterSignalNo);
		
		groupShowFilteredSignal.add(rbtShowFilteredSignalYes);
		groupShowFilteredSignal.add(rbtShowFilteredSignalNo);
		
		add(lbLaguerre, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(10, 10, 0, 0), 0, 0));
		add(tfLaguerre, new GridBagConstraints(1, 0, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(10, 0, 0, 10), 0, 0));
		add(lbSimplexOptimizerRelative, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(10, 10, 0, 0), 0, 0));
		add(tfSimplexOptimizerRelative, new GridBagConstraints(1, 1, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(10, 0, 0, 10), 0, 0));
		add(lbSimplexOptimizerAbsolute ,new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(10, 10, 0, 0), 0, 0));
		add(tfSimplexOptimizerAbsolute, new GridBagConstraints(1, 3, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(10, 0, 0, 10), 0, 0));
		add(lbNelderMeadSimplexSteps, new GridBagConstraints(0, 4, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(10, 10, 0, 0), 0, 0));
		add(tfNelderMeadSimplexSteps, new GridBagConstraints(1, 4, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(10, 0, 0, 10), 0, 0));
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
		
		
		add(lbFill, new GridBagConstraints(0, 10, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(10, 0, 0, 10), 0, 0));
		add(btDefaults, new GridBagConstraints(1, 10, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(10, 0, 0, 10), 0, 0));
		btApply.setPreferredSize(btDefaults.getMinimumSize());
		
		add(btApply, new GridBagConstraints(2, 10, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(10, 0, 0, 10), 0, 0));
		

		
//		add(btOk, new GridBagConstraints(1, 9, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
//				new Insets(10, 0, 0, 10), 0, 0));
//		add(btCancel, new GridBagConstraints(2, 9, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
//				new Insets(10, 0, 0, 10), 0, 0));
		
//		setBorder(MyBorderFactory.createMyBorder("Settings"));

		add(lbAutoFilter,new GridBagConstraints(0, 11, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(10, 10, 0, 20), 0, 0));
		add(cbAutoFilter, new GridBagConstraints(1, 11, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(10, 10, 0, 20), 0, 0));
		
		
		
		add(lbFilter, new GridBagConstraints(0, 12, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(10, 10, 0, 20), 0, 0));
		add(Filter, new GridBagConstraints(1, 12, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(10, 0, 0, 10), 0, 0));
		
		btApply.addActionListener(this);
//		btOk.addActionListener(this);
//		btCancel.addActionListener(this);
		btDefaults.addActionListener(this);
		cbAutoFilter.addActionListener(this);
		
		Filter.addChangeListener(this);

		
		initFields();
		}
	
	
	
	private double[] stringToCoeff(String s) {
		String[] tokens = s.split("[, ]+");
		double[] z = new double[tokens.length];
		for (int i = 0; i < z.length; i++) {
			z[i] = Double.parseDouble(tokens[i]);
		}
		return z;
	}

	
//	public void setController(Controller controller) {
//		this.controller = controller;
//	}

	// Ausgrauen von allen Textfeldern, Labels, Ordnungsauswahl und Combobox bei entsprechender Aktion
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btDefaults){
			initFields();
		}
		
		if(e.getSource() == btApply){
			double LaguerreAcc = Double.parseDouble(tfLaguerre.getText());
			double[] simplexOpt = {Double.parseDouble(tfSimplexOptimizerRelative.getText()), Double.parseDouble(tfSimplexOptimizerAbsolute.getText())};
			double nelderSteps = Double.parseDouble(tfNelderMeadSimplexSteps.getText());
			int maxEval = Integer.parseInt(tfMaxEval.getText());
			boolean filter = rbtFilterSignalYes.isSelected();
			boolean showConditioned = rbtShowFilteredSignalYes.isSelected();
			
			controller.setSettings(new Object[]{LaguerreAcc, simplexOpt, nelderSteps, maxEval, filter, showConditioned});
		}
		if(e.getSource() == cbAutoFilter){
			if(cbAutoFilter.isSelected()){
				Filter.setEnabled(false);
			}else{
				Filter.setEnabled(true);
			}
			
		}
	}

	private void initFields() {
		tfLaguerre.setText("1e-6");
		tfSimplexOptimizerRelative.setText("1e-24");
		tfSimplexOptimizerAbsolute.setText("1e-24");
		tfNelderMeadSimplexSteps.setText("0.01");
		tfMaxEval.setText("5000");
		rbtFilterSignalYes.setSelected(true);
		rbtShowFilteredSignalYes.setSelected(true);
		Filter.setValue(100);
	}

	// Ausgrauen von Textfeldern und Labels bei entsprechender Aktion
	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
	}
		

	@Override
	public void stateChanged(ChangeEvent arg0) {
		lbFilter.setText("Filter accuracy in %   "+Filter.getValue());
		
	}
		
	}

	

