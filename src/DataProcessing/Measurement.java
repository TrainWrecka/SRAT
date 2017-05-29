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
import org.jfree.util.ArrayUtilities;

import com.sun.org.apache.bcel.internal.generic.FMUL;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import matlabfunctions.Filter;
import matlabfunctions.FilterFactory;
import matlabfunctions.Filtfilt;
import matlabfunctions.Matlab;
import matlabfunctions.Polynom;
import matlabfunctions.SVTools;
import userinterface.StatusBar;

public class Measurement {

	private double[] inputData;
	private double[] timeData;
	private double[] stepData;

	private double[] stepDataOriginal;
	private double[] timeDataOriginal;

	private double[] approxData;
	private double[] errorData;

	double[][] polesData;

	private double tNorm;
	private double nor = (175 * 30);
	double offset = 0;

	String K;
	String[] wp = new String[5];
	String[] qp = new String[5];
	String sigma;
	String meanError;

	public boolean input = false;
	public boolean approximated = false;

	int stepIndex = 0;
	int order = 2;

	boolean noise;
	int unitStepLocation;

	public Measurement() {

	}

	public double[] getInputData() {
		return inputData;
	}

	public double[] getErrorData() {
		return errorData;
	}

	public double[][] getPolesData() {
		return polesData;
	}

	public double[] getTimeData() {
		return timeDataOriginal;
	}

	public double[] getStepData(boolean showFiltered) {
		if (showFiltered) {
			return stepData;
		} else {
			return stepDataOriginal;
		}

	}

	public double[] getApproxData() {
		return approxData;
	}

	public void setMeasurement(List<String[]> measurementList) {
		extractData(convertList(measurementList));
		stepIndex = (input) ? getStepLocation(inputData) : 10;
		noise = checkNoise(stepData, stepIndex);
		approximated = false;
	}
	
	public void undoFilter(){
		stepData = stepDataOriginal;
	}

	public void filtData(boolean autoFilter, int filterPercentage) {
		if (noise) {
			stepData = signalFilter(stepDataOriginal, autoFilter, filterPercentage);
		}
	}


	public void approximateMeasurement(double nelderSteps, double[] simplexOpt, int maxEval) {
		double[] stepDataTemp = new double[stepData.length];
		System.arraycopy(stepData, 0, stepDataTemp, 0, stepData.length);
		
		offset = getOffset(stepDataTemp, stepIndex);
		stepDataTemp = removeOffset(stepDataTemp, offset);
		
		if(!input){
			stepIndex = getLastOffsetIntersection(stepDataTemp, offset);
		}

		timeData = removeTime(timeDataOriginal, 0, timeDataOriginal.length - stepIndex);
		normTime();
		
		stepDataTemp = removeTime(stepDataTemp, stepIndex, stepDataTemp.length);


		Object[] approxRet;
		approxRet = Approximation.approximate(timeData, stepDataTemp, order, nelderSteps, simplexOpt,
				maxEval);

		approxData = (double[]) approxRet[0];
		double[] A = (double[]) approxRet[2];

		approxData = addOffset(addTime(approxData), offset);

		calulateError();

		Complex[] polesComplex = getPoles(A);
		polesComplex = Matlab.sort(polesComplex);
		polesData = convPoles(polesComplex);

		calcValues(polesComplex);
		approximated = true;
	}
	
	public void calulateError(){
		errorData = new double[timeDataOriginal.length];

		for (int i = 0; i < timeDataOriginal.length; i++) {
			errorData[i] = stepData[i] - approxData[i];
		}
	}

	public void setValues(Object[] val) {
		timeData = removeTime(timeDataOriginal, 0, timeDataOriginal.length - stepIndex);

		approxData = setPoles(val);

		approxData = addOffset(addTime(approxData), offset);
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

		stepDataOriginal = new double[stepData.length];
		System.arraycopy(stepData, 0, stepDataOriginal, 0, stepData.length);
		timeDataOriginal = new double[timeData.length];
		System.arraycopy(timeData, 0, timeDataOriginal, 0, timeData.length);
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
		return offset;
	}

	/*
	 * removes the offset from the signal
	 */
	private double[] removeOffset(double[] signal, double offset) {
		double[] temp = new double[signal.length];
		System.arraycopy(signal, 0, temp, 0, signal.length);
		for (int i = 0; i < temp.length; i++) {
			temp[i] -= offset;
		}
		return temp;
	}

	private double[] addOffset(double[] signal, double offset) {
		for (int i = 0; i < signal.length; i++) {
			signal[i] += offset;
		}
		return signal;
	}

	/*
	 * removes the deadtime
	 */
	private double[] removeTime(double[] signal, int rangeFrom, int rangeTo) {
		return Arrays.copyOfRange(signal, rangeFrom, rangeTo);
	}

	private double[] addTime(double[] signal) {

		double[] res = new double[timeDataOriginal.length];
		Arrays.fill(res, 0.0);
		System.arraycopy(signal, 0, res, timeDataOriginal.length - approxData.length, signal.length);

		return res;
	}

	/*
	 * returns if input value exists
	 */
	public boolean inputExisting() {
		return input;
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

	private Complex[] getPoles(double[] A) {

		Complex[] poles = Matlab.roots(A);

		for (int i = 0; i < poles.length; i++) {
			poles[i] = poles[i].divide(tNorm).multiply(nor);
		}

		return poles;
	}

	private double[][] convPoles(Complex[] poles) {
		double[][] polesData = new double[2][poles.length];

		for (int i = 0; i < poles.length; i++) {
			polesData[0][i] = poles[i].getReal();
			polesData[1][i] = poles[i].getImaginary();
		}

		return polesData;
	}

	private void calcValues(Complex[] poles) {
		sigma = "-";

		for (int i = 0; i < wp.length; i++) {
			if (i < Math.floor(poles.length / 2)) {
				wp[i] = Double.toString(Math.sqrt(poles[2 * i].multiply(poles[2 * i + 1]).getReal()));
				double temp1 = Math.sqrt(poles[2 * i].multiply(poles[2 * i + 1]).getReal());
				double temp2 = poles[2 * i].add(poles[2 * i + 1]).getReal();
				qp[i] = Double.toString(-(temp1 / temp2));
			} else {
				wp[i] = "-";
				qp[i] = "-";
			}

		}

		if (poles.length % 2 == 0) {
			sigma = "-";
		} else {
			sigma = Double.toString(poles[poles.length - 1].getReal());
		}

		meanError = Double.toString(Matlab.mean(errorData));

		K = Double.toString(approxData[approxData.length/* - 1*/-10] - offset);
	}

	public Object[] getValues() {
		return new Object[] { K, wp, qp, sigma, meanError };
	}

	private double[] setPoles(Object[] val) {
		double K = (double) val[0]; //???
		double[] wp = (double[]) val[1];
		double[] qp = (double[]) val[2];
		double sigma = (double) val[3];
		double[] denom = new double[1];
		denom[0] = 1;

		double[] B = new double[1];
		B[0] = K;

		for (int i = 0; i < wp.length; i++) {
			B[0] *= Math.pow(wp[i], 2);
		}

		for (int i = 0; i < order / 2; i++) {
			double[] poly = { 1, wp[i] / qp[i], Math.pow(wp[i], 2) };
			denom = Matlab.conv(poly, denom);
		}

		if (order % 2 == 1) {
			denom = Matlab.conv(denom, new double[] { 1, (-1) * sigma });
			B[0] *= Math.abs(sigma);
		}

		for (int i = 0; i < this.wp.length; i++) {
			if (i < Math.floor(wp.length)) {
				this.wp[i] = Double.toString(wp[i]);
				this.qp[i] = Double.toString(qp[i]);
			} else {
				this.wp[i] = "-";
				this.qp[i] = "-";
			}
		}

		this.K = Double.toString(K);
		this.sigma = Double.toString(sigma);
		this.meanError = "-";

		return (double[]) SVTools.step(B, denom, timeData)[0];
	}

	private double[] signalFilter(double[] signal, boolean modeAuto, int percent) {
		double errorDetection[] = new double[] { 2e-7, 3.2e-9, 3e-5, 4e-3 };
		double errorCompare[] = new double[] { 5e-7, 10e-7, 5e-4, 5e-3 };

		//if(noise)
		//	ret
		double noiseError;
		int stepLoc = getStepLocation(inputData);
		double offset = getOffset(signal, stepLoc);

		int max_iN = 30;
		int index = 0;
		double[] diff = new double[signal.length];
		double[] signalFiltered = new double[signal.length];

		signalFiltered = filtfiltSimple(signal, max_iN);

		double percentRatio = 0;

		for (int i = 0; i < signalFiltered.length; i++) {
			diff[i] = Math.pow((signal[i] - signalFiltered[i]), 6);
		}
		noiseError = Matlab.max(diff);

		for (int i = 0; i < errorCompare.length; i++) {
			if (noiseError < errorCompare[i]) {
				index = i;
			}
		}

		for (int iN = max_iN; iN > 0; iN--) {
			signalFiltered = filtfiltSimple(signal, iN);
			for (int i = 0; i < signalFiltered.length; i++) {
				diff[i] = Math.pow((signal[i] - signalFiltered[i]), 6);
			}
			noiseError = Matlab.max(diff);

			if (noiseError < errorDetection[index]) {
				percentRatio = iN / 100.0;
				break;
			}
		}

		if (modeAuto == false) {
			int iN = (int) Math.ceil(percent * percentRatio);
			iN = (iN <= 2) ? 2 : iN;

			signalFiltered = filtfiltSimple(signal, iN);
		}

		return signalFiltered;
	}

	private double[] filtfiltSimple(double[] signal, int iN) {
		ArrayList<Double> signalFilteredList;
		double[] signalFiltered = new double[signal.length];

		ArrayList<Double> signalList = new ArrayList<Double>();
		for (double d : signal)
			signalList.add(d);

		ArrayList<Double> vectorA = new ArrayList<Double>();
		vectorA.add(1.0);

		ArrayList<Double> vectorB = new ArrayList<Double>();
		for (int i = 0; i < iN; i++) {
			vectorB.add(1.0 / iN);
		}

		signalFilteredList = Filtfilt.doFiltfilt(vectorB, vectorA, signalList);

		for (int i = 0; i < signalFilteredList.size(); i++) {
			signalFiltered[i] = signalFilteredList.get(i);
		}

		return signalFiltered;
	}

}
