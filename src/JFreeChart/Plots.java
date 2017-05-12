package JFreeChart;

import org.jfree.chart.ChartPanel;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.Renderer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.ShapeUtilities;

import userinterface.StatusBar;

import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYDotRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
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
		
		XYSeries series3 = new XYSeries("Third");
    	series3.add(17.0, 4.0);
//    	series3.add(18.0, 3.0);
//    	series3.add(19.0, 2.0);
//    	series3.add(20.0, -3.0);
//    	series3.add(21.0, 6.0);
//    	series3.add(22.0, 3.0);
    	series3.add(23.0, -4.0);
    	series3.add(24.0, 3.0);
		
		XYSeriesCollection dataset1=new XYSeriesCollection();
		dataset1.addSeries(series3);
		
		
			
		
		if(xylineOderscatter.toLowerCase()=="xyline"){
			if(title.toLowerCase()=="stepresponse"){
				JFreeChart chart = ChartFactory.createXYLineChart(title, xAchse, yAchse, dataset);
				stepresponseChartPanel = new ChartPanel(chart);
				add(stepresponseChartPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				stepresponseChartPanel.setMouseWheelEnabled(true);
				stepresponseChartPanel.setMouseZoomable(true);
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
				errorChartPanel.setMouseWheelEnabled(true);
				errorChartPanel.setMouseZoomable(true);
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
			JFreeChart chart = ChartFactory.createScatterPlot(title, "Real", "Imaginary", dataset1);
			zeroesChartPanel = new ChartPanel(chart);
			add(zeroesChartPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0));
			XYPlot plot = (XYPlot) chart.getPlot();
			plot.setBackgroundPaint(Color.WHITE);
			plot.setDomainGridlinePaint(Color.black);
			plot.setRangeGridlinePaint(Color.black);
			
			Shape cross = ShapeUtilities.createDiagonalCross(5, 0.3f);
			XYItemRenderer renderer = plot.getRenderer();
			renderer.setSeriesShape(0, cross);

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

//	public static void zoomChartAxis(ChartPanel chartP, boolean increase, Point zoomPoint) {
//		int width = chartP.getMaximumDrawWidth() - chartP.getMinimumDrawWidth();
//		int height = chartP.getMaximumDrawHeight() - chartP.getMinimumDrawWidth();
//		
//		if (increase) {
////			if (count < 10000000) {
//				chartP.zoomInBoth(zoomPoint.getX(), zoomPoint.getY());
//				chartP.setMouseZoomable(true);
////				count++;
////			}
//
//		} else {
////			if (count > 0) {
//				chartP.zoomOutBoth(zoomPoint.getX(), zoomPoint.getY());
////				count--;
////			} 
//		}

	}

