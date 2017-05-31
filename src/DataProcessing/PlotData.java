package DataProcessing;

import org.jfree.data.xy.XYSeries;

public class PlotData {
	private XYSeries[] xySeries;
	private double[] xData;
	private int seriesCount;

	public PlotData() {
		xySeries = new XYSeries[seriesCount + 1];
	}

	/** 
	 * sets the data of the x-Axis of the series
	 * @param xData data of the x-Axis to be set
	 */
	public void setXData(double[] xData) {
		this.xData = xData;
	}

	/**
	 * sets/adds the data of the y-Axis of the series
	 * @param yData data of the y-Axis to be set
	 * @param name name of the data to be displayed in the graph
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

	/**
	 * returns the series of the plot data
	 * @return series of the plot data
	 */
	public XYSeries[] getPlotData() {
		return xySeries;
	}

	/**
	 * removes all data in the series
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
