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

public class Plots extends JPanel {

	// create a dataset...
	public XYSeriesCollection dataset = new XYSeriesCollection();



	public ChartPanel stepresponseChartPanel;
	public ChartPanel zeroesChartPanel;
	public ChartPanel errorChartPanel;

	//	public ChartPanel ErrorChartPanel;
//	public static ChartPanel StepresponseChartPanel;
//	public ChartPanel ZeroesChartPanel;
	private static int count = 0;

	public Plots(String title, String xylineOderscatter, String xAchse, String yAchse) {
		this.setLayout(new GridBagLayout());
//		switch (type.toLowerCase()) {
//
//		case "stepresponse":
//			JFreeChart stepresponsChart = ChartFactory.createXYLineChart(type, "t [s]", "U [V]", dataset);
//			StepresponseChartPanel = new ChartPanel(stepresponsChart);
//			add(StepresponseChartPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
//					GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
//			XYPlot stepresponsePlot = (XYPlot) stepresponsChart.getPlot();
//			stepresponsePlot.setBackgroundPaint(Color.WHITE);
//			stepresponsePlot.setDomainGridlinePaint(Color.black);
//			stepresponsePlot.setRangeGridlinePaint(Color.black);
//			break;
//			
//		case "zeroes":
//			JFreeChart zeroesChart = ChartFactory.createScatterPlot(type, "Real", "Imaginary", dataset);
//			ZeroesChartPanel = new ChartPanel(zeroesChart);
//			add(ZeroesChartPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
//					GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
//			XYPlot zeroesPlot = (XYPlot) zeroesChart.getPlot();
//			zeroesPlot.setBackgroundPaint(Color.WHITE);
//			zeroesPlot.setDomainGridlinePaint(Color.black);
//			zeroesPlot.setRangeGridlinePaint(Color.black);
//			break;
//		case "error":
//			
		
		if(xylineOderscatter.toLowerCase()=="xyline"){
			if(title.toLowerCase()=="stepresponse"){
				JFreeChart chart = ChartFactory.createXYLineChart(title, xAchse, yAchse, dataset);
				stepresponseChartPanel = new ChartPanel(chart);
				add(stepresponseChartPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				XYPlot plot = (XYPlot) chart.getPlot();
				plot.setBackgroundPaint(Color.WHITE);
				plot.setDomainGridlinePaint(Color.black);
				plot.setRangeGridlinePaint(Color.black);
			}
			else if(title.toLowerCase()=="error"){
				JFreeChart chart = ChartFactory.createXYLineChart(title, xAchse, yAchse, dataset);
				errorChartPanel = new ChartPanel(chart);
				add(errorChartPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				XYPlot plot = (XYPlot) chart.getPlot();
				plot.setBackgroundPaint(Color.WHITE);
				plot.setDomainGridlinePaint(Color.black);
				plot.setRangeGridlinePaint(Color.black);
			}
//			XYPlot plot = (XYPlot) chart.getPlot();
//			plot.setBackgroundPaint(Color.WHITE);
//			plot.setDomainGridlinePaint(Color.black);
//			plot.setRangeGridlinePaint(Color.black);
		}
		else if(xylineOderscatter.toLowerCase()=="scatter"){
			JFreeChart chart = ChartFactory.createScatterPlot(title, "Real", "Imaginary", dataset);
			zeroesChartPanel = new ChartPanel(chart);
			add(zeroesChartPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0));
			XYPlot plot = (XYPlot) chart.getPlot();
			plot.setBackgroundPaint(Color.WHITE);
			plot.setDomainGridlinePaint(Color.black);
			plot.setRangeGridlinePaint(Color.black);
		}
		

	}

//	public void createStepresponsePlot(String title, String xylineOderscatter){
//		JFreeChart chart = ChartFactory.createXYLineChart(title, "t [s]", "yAchse", dataset);
//		StepresponseChartPanel = new ChartPanel(chart);
//		add(StepresponseChartPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
//				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
//		XYPlot stepresponsePlot = (XYPlot) chart.getPlot();
//		stepresponsePlot.setBackgroundPaint(Color.WHITE);
//		stepresponsePlot.setDomainGridlinePaint(Color.black);
//		stepresponsePlot.setRangeGridlinePaint(Color.black);
//	}
	
//	public void createZeroesPlot(String title){
//		JFreeChart chart = ChartFactory.createScatterPlot(title, "Real", "Imaginary", dataset);
//		ZeroesChartPanel = new ChartPanel(chart);
//		add(ZeroesChartPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
//				new Insets(0, 0, 0, 0), 0, 0));
//		XYPlot plot = (XYPlot) chart.getPlot();
//		plot.setBackgroundPaint(Color.WHITE);
//		plot.setDomainGridlinePaint(Color.black);
//		plot.setRangeGridlinePaint(Color.black);
//	}
	
	public void addSeries(XYSeries series) {
		dataset.addSeries(series);
	}

	public void clearSeries() {
		dataset.removeAllSeries();
	}

	public static void zoomChartAxis(ChartPanel chartP, boolean increase) {
		int width = chartP.getMaximumDrawWidth() - chartP.getMinimumDrawWidth();
		int height = chartP.getMaximumDrawHeight() - chartP.getMinimumDrawWidth();
		if (increase) {
			if (count < 6) {
				chartP.zoomInBoth(width / 2, height / 2);
				count++;
			} else {
				StatusBar.showStatus("Zoom in nicht möglich");
			}

		} else {
			if (count > 0) {
				chartP.zoomOutBoth(width / 2, height / 2);
				count--;
			} else {
				StatusBar.showStatus("Zoom out nicht möglich");
			}
		}

	}
}
