package DataProcessing;

import org.jfree.data.xy.XYSeries;

public class PlotData {

	private XYSeries[] seriesStepresponse;

	public PlotData() {}

	public void setStepresponseData(double[][] stepresponseData) {
		int index = 1;

		if (stepresponseData[0].length == 3) {

			seriesStepresponse[1] = new XYSeries("Input");
			for (int i = 0; i < stepresponseData.length; i++) {
				seriesStepresponse[1].add(stepresponseData[i][0], stepresponseData[i][index]);
			}

			index++;
		}

		seriesStepresponse[0] = new XYSeries("Output");
		for (int i = 0; i < stepresponseData.length; i++) {
			seriesStepresponse[0].add(stepresponseData[i][0], stepresponseData[i][index]);
		}
	}

	public void setPlotData(Object[][] data) {
		double[] timeAxis = (double[]) data[0][0];
		double[] data1Points;
		double[] data2Points;
		double[] data3Points;

		String data1Name;
		String data2Name;
		String data3Name;

		seriesStepresponse = new XYSeries[data.length - 1];

		switch (data.length) {
			case 4:
				data3Points = (double[]) data[3][0];
				data3Name = (String) data[3][1];

				seriesStepresponse[2] = new XYSeries(data3Name);
				for (int i = 0; i < timeAxis.length; i++) {
					seriesStepresponse[2].add(timeAxis[i], data3Points[i]);
				}
			case 3:
				data2Points = (double[]) data[2][0];
				data2Name = (String) data[2][1];

				seriesStepresponse[1] = new XYSeries(data2Name);
				for (int i = 0; i < timeAxis.length; i++) {
					seriesStepresponse[1].add(timeAxis[i], data2Points[i]);
				}

			case 2:
				data1Points = (double[]) data[1][0];
				data1Name = (String) data[1][1];

				seriesStepresponse[0] = new XYSeries(data1Name);
				for (int i = 0; i < timeAxis.length; i++) {
					seriesStepresponse[0].add(timeAxis[i], data1Points[i]);
				}
		}
	}

	public void setStepresponseData(double[] timeData, double[] stepData) {

		seriesStepresponse[0] = new XYSeries("Output");
		for (int i = 0; i < timeData.length; i++) {
			seriesStepresponse[0].add(timeData[i], stepData[i]);
		}
	}

	public XYSeries[] getData() {
		//Object[] ret;
		//ret[0] = seriesStepresponse;
		//ret[1] = 
		return seriesStepresponse;
	}

	public void removeStepresponseData() {
		for (int i = 0; i < seriesStepresponse.length; i++) {
			if (seriesStepresponse[i] != null) {
				seriesStepresponse[i].clear();
			}	
		} /*
			if (seriesStepresponse[0] != null) {
			seriesStepresponse[0].clear();
			}
			if (seriesStepresponse[1] != null) {
			seriesStepresponse[1].clear();
			}*/

	}

	public void setErrorData() {

	}

	public void setZeroesData() {

	}

}
