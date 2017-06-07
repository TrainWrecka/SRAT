package JFreeChart;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import userinterface.StatusBar;

public class ErrorPlot extends JPanel{

	// create a dataset...
	XYSeriesCollection dataset = new XYSeriesCollection();

	public ChartPanel ErrorChartPanel;
	public static ChartPanel StepresponseChartPanel;
	public ChartPanel ZeroesChartPanel;
	private static int count=0;

	public ErrorPlot(String type) {
		this.setLayout(new GridBagLayout());
		JFreeChart chart = ChartFactory.createXYLineChart(type, "t [s]", "U [V]", dataset);
		ErrorChartPanel = new ChartPanel(chart);
		add(ErrorChartPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
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
