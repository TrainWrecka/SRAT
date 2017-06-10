package plots;

import org.jfree.data.xy.XYSeries;

public class PlotData {
	
	//================================================================================
	// Properties
	//================================================================================
	
	private XYSeries[] xySeries;
	private double[] xData;
	private int seriesCount;


	//================================================================================
	// Constructor
	//================================================================================
	
	public PlotData() {
		xySeries = new XYSeries[seriesCount + 1];
	}
	
	//================================================================================
	// Public Methods
	//================================================================================

	/** 
	 * Setzt die Daten der X-Achse der Serie.
	 * @param xData Daten der X-Achse.
	 */
	public void setXData(double[] xData) {
		this.xData = xData;
	}

	/**
	 * Setzt die Daten der Y-Achse der Serie. Wnenn schon ein Datenset existiert, werden die Daten
	 * zu den bestehenden Daten dazugefügt.
	 * @param yData Daten der Y-Achse.
	 * @param name Name der Daten.
	 */
	public void setYData(double[] yData, String name) {
		if (yData != null) {
			if (seriesCount > 0) {
				XYSeries[] tempSeries = xySeries;

				xySeries = new XYSeries[seriesCount + 1];

				for (int i = 0; i < tempSeries.length; i++) {
					xySeries[i] = tempSeries[i];
				}
			}

			xySeries[seriesCount] = new XYSeries(name);
			for (int i = 0; i < yData.length; i++) {
				xySeries[seriesCount].add(xData[i], yData[i]);
			}
			
			seriesCount++;
		}
	}

	public void setData(double[] xData, double[] yData, String name) {
		if (seriesCount > 0) {
			XYSeries[] tempSeries = xySeries;

			xySeries = new XYSeries[seriesCount + 1];

			for (int i = 0; i < tempSeries.length; i++) {
				xySeries[i] = tempSeries[i];
			}
		}

		xySeries[seriesCount] = new XYSeries(name);
		for (int i = 0; i < yData.length; i++) {
			xySeries[seriesCount].add(xData[i], yData[i]);
		}

		seriesCount++;
	}

	/**
	 * Gibt die Serie der PlotData zurück.
	 * @return Serie der PlotData.
	 */
	public XYSeries[] getPlotData() {
		return xySeries;
	}

	/**
	 * Entfernt gesamte Daten in der Serie.
	 */
	public void removePlotData() {
		if (xySeries != null) {
			for (int i = 0; i < xySeries.length; i++) {
				if (xySeries[i] != null) {
					xySeries[i].clear();
				}
			}
		}
		seriesCount = 0;
		xySeries = new XYSeries[seriesCount + 1];
	}
}
