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
	private double[] errorData;

	private double tNorm;
	private double nor = (175 * 30);
	double offset = 0;

	String K;
	String[] wp;
	String[] qp;
	String sigma;
	String meanError;
	

	//private List<String[]> measurementList;

	private PlotData stepPlotData = new PlotData();
	private PlotData errorPlotData = new PlotData();
	private PlotData polesPlotData = new PlotData();

	private boolean input = false;

	int stepIndex = 0;
	int order = 2;

	double laguerreAcc = 1e-10;
	double[] simplexOpt = { 1e-24, 1e-24 };
	double nelderSteps = 0.0001;
	int maxEval = 5000;
	boolean filter = true;
	boolean showConditioned = true;

	public Measurement() {

	}

	public void setMeasurement(List<String[]> measurementList) {
		//this.measurementList = measurementList;

		extractData(convertList(measurementList));

		//stepPlotData.removeStepresponseData();

		if (input) {
			stepPlotData.setPlotData(
					new Object[][] { { timeData, "Time" }, { inputData, "Input" }, new Object[] { stepData, "Step" } });
		} else {
			stepPlotData.setPlotData(new Object[][] { { timeData, "Time" }, new Object[] { stepData, "Step" } });
		}

	}

	public void approximateMeasurement() {

		int unitStepLocation = 0;
		
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

		Object[] approxRet;
		approxRet = Approximation.approximate(timeDataConditioned, stepDataConditioned, order, nelderSteps, simplexOpt,
				maxEval);

		approxData = (double[]) approxRet[0];
		double[] A = (double[]) approxRet[2];

		stepPlotData.removeStepresponseData();

		if (input) {
			if (showConditioned) {
				stepPlotData.setPlotData(new Object[][] { { timeDataConditioned, "Time" }, { inputDataConditioned, "Input" },
						new Object[] { stepDataConditioned, "Step" }, { approxData, "Approximation" } });
			} else {
				stepPlotData.setPlotData(new Object[][] { { timeDataConditioned, "Time" }, { inputDataConditioned, "Input" },
						new Object[] { stepData, "Step" }, { approxData, "Approximation" } });
			}

		} else {
			if (showConditioned) {
				stepPlotData.setPlotData(new Object[][] { { timeDataConditioned, "Time" }, { stepDataConditioned, "Step" },
						{ approxData, "Approximation" } });
			} else {
				stepPlotData.setPlotData(new Object[][] { { timeDataConditioned, "Time" }, { stepData, "Step" },
						{ approxData, "Approximation" } });
			}

		}
		errorData = new double[timeDataConditioned.length];

		for (int i = 0; i < timeDataConditioned.length; i++) {
			errorData[i] = stepDataConditioned[i] - approxData[i];
		}

		errorPlotData.setPlotData(new Object[][] { { timeDataConditioned, "Time" }, { errorData, "Error" } });

		Complex[] polesComplex = getPoles(A);
		double[][] polesData = convPoles(polesComplex);

		polesPlotData.setPlotData(new Object[][] { { polesData[0], "-" }, { polesData[1], "Poles" } });

		
		setValues(polesComplex);
	}

	public void setSettings(Object[] settings) {
		Matlab.laguerreAcc = (double) settings[0];
		this.simplexOpt = (double[]) settings[1];
		this.nelderSteps = (double) settings[2];
		this.maxEval = (int) settings[3];
		this.filter = (boolean) settings[4];
		this.showConditioned = (boolean) settings[5];
	}

	

	//	public double[][] getMeasurement() {
	//		return measurement;
	//	}

	/*	public List<String[]> getMeasurementList() {
			return measurementList;
		}*/

	public XYSeries[] getStepresponseData() {
		return stepPlotData.getData();
	}

	public XYSeries[] getErrorData() {
		return errorPlotData.getData();
	}

	public XYSeries[] getPolesData() {
		return polesPlotData.getData();
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
		tNorm = Matlab.norm(timeData);

		for (int i = 0; i < timeData.length; i++) {
			timeData[i] = timeData[i] / tNorm * nor;
		}
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public static void main(String[] args) {

	}

	//	nort=30*175; %Zeitnormierungsfaktor
	//	faktt=norm(t);
	//	t=t./faktt;
	//	t=t.*nort;

	private Complex[] getPoles(double[] A) {

		Complex[] poles = Matlab.roots(A);

		for (int i = 0; i < poles.length; i++) {
			poles[i] = poles[i].divide(tNorm).multiply(nor);
		}

		return poles;
	}
	
	private double[][] convPoles(Complex[] poles){
		double[][] polesData = new double[2][poles.length];

		for (int i = 0; i < poles.length; i++) {
			polesData[0][i] = poles[i].getReal();
			polesData[1][i] = poles[i].getImaginary();
		}
		
		return polesData;
	}

	private void setValues(Complex[] poles) {
		sigma = "-";
		wp = new String[(int) Math.floor(poles.length / 2)];
		qp = new String[wp.length];

		for (int i = 0; i < wp.length; i++) {
			wp[i] = Double.toString(Math.sqrt(poles[2 * i].multiply(poles[2 * i + 1]).getReal()));
			double temp1 = Math.sqrt(poles[2 * i].multiply(poles[2 * i + 1]).getReal());
			double temp2 = poles[2 * i].add(poles[2 * i + 1]).getReal();
			qp[i] = Double.toString(-(temp1/temp2));
		}

		if (poles.length % 2 == 0) {
			sigma = "-";
		} else {
			sigma = Double.toString(poles[poles.length-1].getReal());
		}
		
		meanError = Double.toString(Matlab.mean(errorData));
		
		K = Double.toString(approxData[approxData.length-1]-offset);
	}
	
	private Object[] getValues(){
		return new Object[] {K, wp, qp, sigma, meanError};
	}
}
