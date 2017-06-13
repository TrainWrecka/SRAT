package plots;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Shape;
import java.awt.Toolkit;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.ShapeUtilities;

import programUtilities.JFreeChartDPIFix;

/**
 * 
 * @author Lukas Loosli
 *
 */
public class Plots extends JPanel {

	//================================================================================
	// Properties
	//================================================================================

	public XYSeriesCollection dataset = new XYSeriesCollection();

	public ChartPanel stepresponseChartPanel;
	public ChartPanel zeroesChartPanel;
	public ChartPanel errorChartPanel;

	//================================================================================
	// Constructor
	//================================================================================

	public Plots(String title, String xylineOderscatter, String xAchse, String yAchse) {
		this.setLayout(new GridBagLayout());

		if (xylineOderscatter.toLowerCase() == "xyline") {
			if (title == "Stepresponse") {
				createStepresponsePlot(title, xAchse, yAchse);
			} else if (title == "Error") {
				createErrorPlot(title, xAchse, yAchse);
			}

		} else if (xylineOderscatter.toLowerCase() == "scatter") {
			createPolesPlot(title);
		}
	}
	//================================================================================
	// Public Methods
	//================================================================================

	/**
	 * Fügt dem Plot eine Serie mit Daten hinzu.
	 * @param series Serie mit Daten.
	 */
	public void addSeries(XYSeries series) {
		dataset.addSeries(series);
	}

	/**
	 * Löscht alle Daten der Serie vom Plot.
	 */
	public void clearSeries() {
		dataset.removeAllSeries();
	}

	//================================================================================
	// Private Methods
	//================================================================================

	private void createStepresponsePlot(String title, String xAchse, String yAchse) {
		JFreeChart chart = ChartFactory.createXYLineChart(title, xAchse, yAchse, dataset);
		JFreeChartDPIFix.applyChartTheme(chart);
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

	private void createErrorPlot(String title, String xAchse, String yAchse) {
		JFreeChart chart = ChartFactory.createXYLineChart(title, xAchse, yAchse, dataset);
		errorChartPanel = new ChartPanel(chart);
		JFreeChartDPIFix.applyChartTheme(chart);
		add(errorChartPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		errorChartPanel.setMouseWheelEnabled(true);
		errorChartPanel.setMouseZoomable(true);
		XYPlot plot = (XYPlot) chart.getPlot();
		plot.setBackgroundPaint(Color.WHITE);
		plot.setDomainGridlinePaint(Color.black);
		plot.setRangeGridlinePaint(Color.black);
	}

	private void createPolesPlot(String title) {
		JFreeChart chart = ChartFactory.createScatterPlot(title, "Real", "Imaginary", dataset);
		JFreeChartDPIFix.applyChartTheme(chart);
		zeroesChartPanel = new ChartPanel(chart);
		add(zeroesChartPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		XYPlot plot = (XYPlot) chart.getPlot();
		zeroesChartPanel.setMouseWheelEnabled(true);
		zeroesChartPanel.setMouseZoomable(true);
		plot.setBackgroundPaint(Color.WHITE);
		plot.setDomainGridlinePaint(Color.black);
		plot.setRangeGridlinePaint(Color.black);

		Shape cross;

		if (Toolkit.getDefaultToolkit().getScreenSize().getWidth() >= 3700) {
			cross = ShapeUtilities.createDiagonalCross(15, 3.0f);
		} else {
			cross = ShapeUtilities.createDiagonalCross(10, 1.5f);
		}
		XYItemRenderer renderer = plot.getRenderer();
		renderer.setSeriesShape(0, cross);
	}
}
