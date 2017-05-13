package userinterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

import java.awt.GridBagLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayoutInfo;
import java.awt.Insets;
import java.awt.Toolkit;

import JFreeChart.Plots;
import JFreeChart.StepResponsePlot;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.sun.glass.ui.Size;

import javax.swing.JLabel;

public class DataPanel extends JPanel implements ActionListener {

//	public StepResponsePlot StepResponseplot = new StepResponsePlot("Stepresponse");


	public DataPanel() {
		super(new GridBagLayout());
		setBorder(MyBorderFactory.createMyBorder(""));
	}
	
	public void clearData(Plots plot){
		plot.clearSeries();
	}

	public void addData(XYSeries seriesStepresponse, Plots plot) {
		plot.addSeries(seriesStepresponse);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}
