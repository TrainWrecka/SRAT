package DataProcessing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.NelderMeadSimplex;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimplexOptimizer;
import org.apache.commons.math3.util.FastMath;
import org.jfree.data.xy.XYSeries;

import com.sun.org.apache.bcel.internal.generic.FMUL;

import matlabfunctions.Filter;
import matlabfunctions.FilterFactory;
import matlabfunctions.Filtfilt;
import matlabfunctions.Matlab;
import matlabfunctions.SVTools;
import userinterface.StatusBar;

public class Measurement {

	//private double[][] measurement;
	private double[][] cutData;
	private double[] inputData;
	private double[] timeData;
	private double[] stepData;
	private double[] stepDataFiltered;
	private double[] stepDataConditioned;
	private double[] inputDataConditioned;
	private double[] timeDataConditioned;

	private double[] approxData;

	//private List<String[]> measurementList;

	private PlotData plotData = new PlotData();

	private boolean input = false;

	int stepIndex = 0;

	int order = 1;

	public Measurement() {

	}

	public void setMeasurement(List<String[]> measurementList) {
		//this.measurementList = measurementList;

		int unitStepLocation = 0;
		double offset = 0;

		extractData(convertList(measurementList));

		if (input) {
			unitStepLocation = getStepLocation(inputData);
		} else {
			unitStepLocation = 10;
		}

		offset = getOffset(stepData, unitStepLocation);

		boolean noise = checkNoise(stepData, unitStepLocation);

		if (noise) {
			stepIndex = 0;
			stepDataFiltered = filtFunction(stepData, 30, 100.9e-3);
			stepIndex = getLastOffsetIntersection(stepDataFiltered, offset);
		}

		else {
			stepIndex = getFirstSignalChange(stepData);
		}

		if (noise) {
			stepDataConditioned = removeDeadTime(removeOffset(stepDataFiltered, offset),
					new int[] { stepIndex, stepData.length });
		}

		else {
			stepDataConditioned = removeDeadTime(removeOffset(stepData, offset), new int[] { stepIndex, stepData.length });
		}

		if (input) {
			inputDataConditioned = removeDeadTime(removeOffset(inputData, getOffset(inputData, unitStepLocation)),
					new int[] { stepIndex, inputData.length });
		}
		
		normTime();

		timeDataConditioned = removeDeadTime(timeData, new int[] { 0, timeData.length - stepIndex });

		

		approxData = Approximation.approximate(timeDataConditioned, stepDataConditioned, order);

		plotData.removeStepresponseData();

		if (input) {
			plotData.setPlotData(new Object[][] {{ timeDataConditioned, "Time" }, { inputDataConditioned, "Input" },
					new Object[] { stepDataConditioned, "Step" },{approxData, "Approximation"}});
		} else {
			plotData.setPlotData(new Object[][] {{ timeDataConditioned, "Time" },
				new Object[] { stepDataConditioned, "Step" },{approxData, "Approximation"}});
		}
	}
	
	public void approximateMeasurement(){
		
	}

	//	public double[][] getMeasurement() {
	//		return measurement;
	//	}

	/*	public List<String[]> getMeasurementList() {
			return measurementList;
		}*/

	public XYSeries[] getStepresponseData() {
		return plotData.getStepresponseData();
	}

	/*
	 * converts 2D list of strings in 2d array of doubles
	 * uses string array for conversion
	 */
	private double[][] convertList(List<String[]> listString) {
		String[][] tempArrayString = new String[listString.size()][];
		for (int i = 0; i < listString.size(); i++) {
			String[] arrayRow = listString.get(i);
			tempArrayString[i] = arrayRow;
		}

		double[][] tempArrayDouble = new double[tempArrayString.length][tempArrayString[0].length];
		for (int i = 0; i < tempArrayString.length; i++) {
			for (int j = 0; j < tempArrayString[0].length; j++) {
				tempArrayDouble[i][j] = Double.parseDouble(tempArrayString[i][j]);
			}
		}

		return tempArrayDouble;
	}

	/*
	 * removes the unused data
	 */
	private double[][] cutMeasurement(double[][] measurement) {
		double[][] cutData = null;

		if (measurement[0].length != 3) {
			return cutData = measurement;
		}

		int j = 0;
		for (int i = 0; i < measurement.length; i++) {
			if (measurement[i][1] != 0) {
				j = i;
				break;
			}
		}

		cutData = new double[measurement.length - j][measurement[0].length];

		for (int i = 0; i < cutData.length; i++) {
			cutData[i][0] = measurement[i][0];
			cutData[i][1] = measurement[j][1];
			cutData[i][2] = measurement[j][2];
			j++;
		}

		return cutData;
	}

	/*
	 * returns true if a fluctuation exists in
	 * the signal for the specified range
	 */
	private boolean checkNoise(double[] signal, int range) {
		boolean pos = false;
		boolean neg = false;
		for (int i = 0; i < range; i++) {
			if (signal[i] > signal[i + 1]) {
				neg = true;
			} else if (signal[i] < signal[i + 1]) {
				pos = true;
			}

			if ((pos & neg) == true) {
				break;
			}
		}
		return (pos & neg);
	}

	/*
	 * if three columns are found, the format of the data is [time, input, output]
	 * if two columns are found, the format of the data is [time, output]
	 * else an error message is printed
	 * if an input column exist a flag gets set
	 */
	private void extractData(double[][] measurement) {
		input = false;

		if (measurement[0].length == 3) {
			timeData = new double[measurement.length];
			inputData = new double[measurement.length];
			stepData = new double[measurement.length];
			for (int i = 0; i < measurement.length; i++) {
				timeData[i] = measurement[i][0];
				inputData[i] = measurement[i][1];
				stepData[i] = measurement[i][2];
			}
			input = true;
		} else if (measurement[0].length == 2) {
			timeData = new double[measurement.length];
			inputData = new double[1];
			stepData = new double[measurement.length];
			for (int i = 0; i < measurement.length; i++) {
				timeData[i] = measurement[i][0];
				stepData[i] = measurement[i][1];
			}
		} else if (measurement[0].length == 0) {
			StatusBar.showStatus("No data found");
		} else {
			StatusBar.showStatus("Too many data columns");
		}
	}

	/*
	 * gets the location where the step of 
	 * the unit data occurs
	 */
	private int getStepLocation(double[] signal) {
		int unitStepLocation = 0;
		for (int i = 0; i < signal.length; i++) {
			if (signal[i] != signal[i + 1]) {
				unitStepLocation = i;
				break;
			}
		}
		return unitStepLocation;
	}

	/*
	 * returns the offset value
	 */
	private double getOffset(double[] signal, int range) {
		double[] tempArray = Arrays.copyOfRange(signal, 0, range);
		double offset = Matlab.mean(tempArray);
		StatusBar.showStatus("Offset:" + Double.toString(offset));
		return offset;
	}

	/*
	 * removes the offset from the signal
	 */
	private double[] removeOffset(double[] signal, double offset) {
		for (int i = 0; i < signal.length; i++) {
			signal[i] -= offset;
		}
		return signal;
	}

	/*
	 * removes the deadtime
	 */
	private double[] removeDeadTime(double[] signal, int[] range) {
		return Arrays.copyOfRange(signal, range[0], range[1]);
	}

	/*
	 * returns if input value exists
	 */
	public boolean inputExisting() {
		return input;
	}

	/*
	 * filter function
	 */
	private double[] filtFunction(double[] signal, int filterLength, double maxError) {
		double[] diff = new double[signal.length];
		double[] signalFiltered = new double[signal.length];
		double noiseError = 1;
		ArrayList<Double> signalFilteredList;
		ArrayList<Double> signalList = new ArrayList<Double>();
		for (double d : signal)
			signalList.add(d);

		ArrayList<Double> vectorA = new ArrayList<Double>();
		vectorA.add(1.0);

		for (int iN = filterLength; (Math.abs(noiseError) > maxError) && (iN > 0); iN--) {

			ArrayList<Double> vectorB = new ArrayList<Double>();
			for (int i = 0; i < filterLength; i++) {
				vectorB.add(1.0 / filterLength);
			}

			signalFilteredList = Filtfilt.doFiltfilt(vectorB, vectorA, signalList);

			for (int i = 0; i < signalFilteredList.size(); i++) {
				signalFiltered[i] = signalFilteredList.get(i);
				diff[i] = (signal[i] - signalFiltered[i]) / signal[i];
			}

			noiseError = Math.abs(Matlab.mean(diff));
		}

		return signalFiltered;
	}

	/*
	 * get first signal change
	 * only applicable if no noise
	 */
	private int getFirstSignalChange(double[] signal) {
		int index = 0;
		for (int i = 0; i < signal.length; i++) {
			if (signal[i] != signal[i + 1]) {
				index = i;
				break;
			}
		}
		return index;
	}

	/*
	 * returns last intersection of
	 * signal and offset, before signal
	 * reaches the middle of its maximum
	 */
	private int getLastOffsetIntersection(double[] signal, double offset) {
		double max = Matlab.max(signal);
		int index = 0;
		for (int i = 0; i < signal.length; i++) {
			if (signal[i] <= offset && signal[i + 1] >= offset) {
				index = i;
			} else if ((signal[i] - offset) > ((max - offset) / 2)) {
				break;
			}
		}
		return index;
	}

	/*
	 * norms the time axis
	 */
	private void normTime() {
		double nor = (175 * 30);
		double tNorm = Matlab.norm(timeData);

		for (int i = 0; i < timeData.length; i++) {
			timeData[i] = timeData[i] / tNorm * nor;
		}
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public static void main(String[] args) {

	}

}
