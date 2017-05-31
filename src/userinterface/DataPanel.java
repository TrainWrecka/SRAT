package userinterface;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import org.jfree.data.xy.XYSeries;

import JFreeChart.Plots;

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
