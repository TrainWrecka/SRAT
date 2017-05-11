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

import JFreeChart.StepResponsePlot;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.sun.glass.ui.Size;

import javax.swing.JLabel;

public class StepresponsePanel extends JPanel implements ActionListener {

	public StepResponsePlot StepResponseplot = new StepResponsePlot("Stepresponse");


	public StepresponsePanel() {
		super(new GridBagLayout());
		add(StepResponseplot, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		
//		Dimension screensize = new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/10, Toolkit.getDefaultToolkit().getScreenSize().height/7);
//		setMaximumSize(screensize);
//		StepResponseplot.setMaximumSize(screensize);
		
				
		setBorder(MyBorderFactory.createMyBorder("StepResponse"));
		
	}
	
	public void clearStepresponseData(){
		StepResponseplot.clearSeries();
	}

	public void addStepresponseData(XYSeries seriesStepresponse) {
		StepResponseplot.addSeries(seriesStepresponse);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}
