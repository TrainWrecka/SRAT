package JFreeChart;

import org.jfree.chart.ChartPanel;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import userinterface.StatusBar;

import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class StepResponsePlot extends JPanel{

	// create a dataset...
	XYSeriesCollection dataset = new XYSeriesCollection();
	Dimension screensize = new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/10, Toolkit.getDefaultToolkit().getScreenSize().height/7);

	public static ChartPanel StepresponseChartPanel;
	private static int count=0;

	public StepResponsePlot(String type) {
		this.setLayout(new GridBagLayout());
		XYSeries series1 = new XYSeries("Planned");
		for (double i = 0.0; i < 10;) {
			series1.add(i, Math.cos(i));
			i = i + 0.1;
		}
		
		
		JFreeChart chart = ChartFactory.createXYLineChart("Input Data", "t [s]", "U [V]", dataset);
		StepresponseChartPanel = new ChartPanel(chart);
		add(StepresponseChartPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		XYPlot plot = (XYPlot) chart.getPlot();
		plot.setBackgroundPaint(Color.WHITE);
		plot.setDomainGridlinePaint(Color.black);
		plot.setRangeGridlinePaint(Color.black);
		
		
		    	

		
		
	}	
	
	
	
	public void addSeries(XYSeries series) {
		dataset.addSeries(series);
	}
	
	public void clearSeries(){
		dataset.removeAllSeries();
	}
	
	
    public static void zoomChartAxis(ChartPanel chartP, boolean increase)
    {              
        int width = chartP.getMaximumDrawWidth() - chartP.getMinimumDrawWidth();
        int height = chartP.getMaximumDrawHeight() - chartP.getMinimumDrawWidth();        
        if(increase){
        	if(count<6){
        		chartP.zoomInBoth(width/2, height/2);
        		count++;
        	}
        	else{
        		StatusBar.showStatus("Zoom in nicht möglich");
        	}
        	
        }else{
        	if(count>0){
            chartP.zoomOutBoth(width/2, height/2);
            count--;
        	}
        	else{
        		StatusBar.showStatus("Zoom out nicht möglich");
        	}
        }
    }
}
