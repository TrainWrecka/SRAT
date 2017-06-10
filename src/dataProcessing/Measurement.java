package dataProcessing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.complex.Complex;

import mathUtilites.Filtfilt;
import mathUtilites.Matlab;
import mathUtilites.SVTools;
import sun.swing.SwingUtilities2.Section;
import userinterface.StatusBar;

/**
 * 
 * @author Thomas Frei
 *
 */
public class Measurement {

	//================================================================================
	// Properties
	//================================================================================

	private double[] inputData;
	private double[] timeData;
	private double[] stepData;
	private double[] stepDataOriginal;
	private double[] approxData;
	private double[] errorData;
	private double[][] polesData;

	private int order;
	private double K;
	private double wqp[][];
	private double sigma;
	private double meanError;

	private int stepIndex;

	private double nor = (175 * 30);
	private double offset = 0;
	private boolean noise;
	private boolean input = false;
	private boolean approximated = false;

	private Approximation approximation = new Approximation();

	//================================================================================
	// Constructor
	//================================================================================

	public Measurement() {

	}

	//================================================================================
	// Public Methods (excl. Setter and Getter)
	//================================================================================

	/**
	 * Setzt die Messdaten aus der übergebenen List und extrahiert deren Komponenten. Ausserdem
	 * wird überprüft, ob ein Einheitsschritt vorhanden ist und ob die Sprungantwort Rauschen enthält.
	 * @param measurementList List mit String Array, enthält daten aus dem CSV File.
	 */
	public void setMeasurement(List<String[]> measurementList) {
		double[][] tempData = convertList(measurementList);
		extractData(tempData);
		stepIndex = (input) ? getStepLocation(inputData) : 10;
		noise = checkNoise(stepData, stepIndex);
		approximated = false;
	}

	/**
	 * Filtert die Schrittantwort, falls diese Rauschen enthält.
	 * @param autoFilter Automatisches filtern ein/aus.
	 * @param filterPercentage Stärke des manuellen Filters.
	 */
	public void filtData(boolean autoFilter, int filterPercentage) {
		if (noise) {
			stepData = signalFilter(stepDataOriginal, autoFilter, filterPercentage);
		}
	}

	/**
	 * Stellt die ursprüngliche Schrittantwort wieder her.
	 */
	public void undoFilter() {
		stepData = stepDataOriginal;
	}

	/**
	 * Bereitet die Schrittantwort und Zeitdaten vor durch Entfernen des Offsets, der Totzeit und der 
	 * Normierung der Zeitachse. Anschliessend wird eine Approximation durchgeführt, wessen resultierende
	 * Werte für die Ausgabe berechnet werden. Zusätzlich wird der Fehler zwischen der originalen und der
	 * approximierten Schrittantwort berechnet.
	 * @param nelderSteps Nelder Simplex Steps des Optimierers.
	 * @param simplexOpt Relative und Absolute Goals des Optimierers.
	 * @param maxEval Maximale Evaluationen des optimierers.
	 */
	public void approximateAuto(double nelderSteps, double[] simplexOpt, int maxEval) {
		double[] stepDataTemp = new double[stepData.length];
		double[] timeDataTemp = new double[timeData.length];
		System.arraycopy(stepData, 0, stepDataTemp, 0, stepData.length);
		System.arraycopy(timeData, 0, timeDataTemp, 0, timeData.length);

		offset = getOffset(stepDataTemp, stepIndex);
		stepDataTemp = removeOffset(stepDataTemp, offset);

		if (!input) {
			stepIndex = getLastOffsetIntersection(stepDataTemp, offset);
		}

		timeDataTemp = removeTime(timeData, 0, timeData.length - stepIndex);
		double norm = calculateNorm(timeDataTemp, nor);
		timeDataTemp = normTime(timeDataTemp, norm);

		stepDataTemp = removeTime(stepDataTemp, stepIndex, stepDataTemp.length);

		double[][] approxRet = approximation.approximate(timeDataTemp, stepDataTemp, order, nelderSteps, simplexOpt, maxEval);
		approxData = approxRet[0];
		double[] A = approxRet[1];

		approxData = addOffset(addTime(approxData, timeData.length - approxData.length), offset);

		errorData = calculateDifference(approxData, stepData);

		Complex[] polesComplex = denormPoles(Matlab.roots(A), norm);
		polesComplex = Matlab.sort(polesComplex);
		polesData = convertPoles(polesComplex);

		calculateVariables(polesComplex, approxData, errorData, offset);
		approximated = true;
	}

	/**
	 * Führt eine manuelle Approximation durch, indem eine Schrittantwort aus den momentanen
	 * Werten generiert wird. Aktualiesert auch Daten wie Error und Pole.
	 */
	public void approximateManual() {
		double[] timeDataTemp = removeTime(timeData, 0, timeData.length - stepIndex);
		double[][] poly = calculatePoles(K, wqp, sigma);
		approxData = (double[]) SVTools.step(poly[0], poly[1], timeDataTemp)[0];
		approxData = addOffset(addTime(approxData, timeData.length - approxData.length), offset);

		Complex[] polesComplex = Matlab.roots(poly[1]);
		polesComplex = Matlab.sort(polesComplex);
		polesData = convertPoles(polesComplex);

		errorData = calculateDifference(approxData, stepDataOriginal);
		meanError = Matlab.mean(errorData);
	}

	/**
	 * Prüft ob die Approximation abgeschlossen ist.
	 * @return false falls die Approximation noch nicht komplett 
	 * durchgeführt wurde.
	 */
	public boolean approximated() {
		return approximated;
	}

	/**
	 * Prüft ob ein Einheitsschritt vorhanden ist.
	 * @return false falls kein Einheitsschritt vorhanden ist.
	 */
	public boolean inputExisting() {
		return input;
	}

	/**
	 * Prüft ob die Approximation noch am laufen ist.
	 * @return false falls der LaguerreSolver das Programm blockiert.
	 */
	public boolean checkRunning() {
		return approximation.checkRunning();
	}

	/**
	 * Aktualisiert den Error.
	 */
	public void recalculateError() {
		errorData = calculateDifference(approxData, stepData);
		meanError = Matlab.mean(errorData);
	}

	//================================================================================
	// Private Methods
	//================================================================================

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

	private void extractData(double[][] measurement) throws RuntimeException {
		int index;

		timeData = new double[measurement.length];
		stepData = new double[timeData.length];

		if (measurement[0].length == 3) {
			inputData = new double[timeData.length];
			input = true;
			index = 2;
		} else if (measurement[0].length == 2) {
			input = false;
			index = 1;
		} else {
			throw new RuntimeException();
		}

		for (int i = 0; i < timeData.length; i++) {
			timeData[i] = measurement[i][0];
			stepData[i] = measurement[i][index];
			if (input) {
				inputData[i] = measurement[i][1];
			}
		}

		stepDataOriginal = new double[stepData.length];
		System.arraycopy(stepData, 0, stepDataOriginal, 0, stepData.length);
	}

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

	private double[] signalFilter(double[] signal, boolean modeAuto, int percent) {
		double errorDetection[] = new double[] { 2e-7, 3.2e-9, 3e-5, 4e-3 };
		double errorCompare[] = new double[] { 5e-7, 10e-7, 5e-4, 5e-3 };
		double noiseError;

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

	private double getOffset(double[] signal, int range) {
		double[] tempArray = Arrays.copyOfRange(signal, 0, range);
		double offset = Matlab.mean(tempArray);
		return offset;
	}

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

	private double calculateNorm(double[] timeData, double norm) {
		return norm / Matlab.norm(timeData);
	}

	private double[] normTime(double[] timeData, double norm) {
		for (int i = 0; i < timeData.length; i++) {
			timeData[i] = timeData[i] * norm;
		}
		return timeData;
	}

	private double[] removeTime(double[] signal, int rangeFrom, int rangeTo) {
		return Arrays.copyOfRange(signal, rangeFrom, rangeTo);
	}

	private double[] addTime(double[] signal, int length) {
		double[] ret = new double[signal.length + length];
		Arrays.fill(ret, 0.0);
		System.arraycopy(signal, 0, ret, length, signal.length);

		return ret;
	}

	private double[] calculateDifference(double[] dataA, double[] dataB) {
		double[] error = new double[dataA.length];

		for (int i = 0; i < error.length; i++) {
			error[i] = dataA[i] - dataB[i];
		}
		return error;
	}

	private Complex[] denormPoles(Complex[] poles, double norm) {
		for (int i = 0; i < poles.length; i++) {
			poles[i] = poles[i].multiply(norm);
		}

		return poles;
	}

	private double[][] convertPoles(Complex[] poles) {
		double[][] polesData = new double[2][poles.length];

		for (int i = 0; i < poles.length; i++) {
			polesData[0][i] = poles[i].getReal();
			polesData[1][i] = poles[i].getImaginary();
		}

		return polesData;
	}

	private void calculateVariables(Complex[] poles, double[] approxData, double[] errorData, double offset) {
		wqp = new double[2][(int) Math.floor(order / 2)];

		K = approxData[approxData.length - 1] - offset;

		for (int i = 0; i < wqp[0].length; i++) {
			//if (i < Math.floor(poles.length / 2)) {
			wqp[0][i] = Math.sqrt(poles[2 * i].multiply(poles[2 * i + 1]).getReal());
			double temp1 = Math.sqrt(poles[2 * i].multiply(poles[2 * i + 1]).getReal());
			double temp2 = poles[2 * i].add(poles[2 * i + 1]).getReal();
			wqp[1][i] = (-(temp1 / temp2));
		}

		meanError = Matlab.mean(errorData);

		if (order % 2 == 1) {
			sigma = poles[poles.length - 1].getReal();
		}
	}

	private double[][] calculatePoles(double K, double[][] wqp, double sigma) {
		double[] A = new double[1];
		A[0] = 1;

		double[] B = new double[1];
		B[0] = K;

		for (int i = 0; i < wqp[0].length; i++) {
			B[0] *= Math.pow(wqp[0][i], 2);
		}

		for (int i = 0; i < order / 2; i++) {
			double[] tempA = { 1, wqp[0][i] / wqp[1][i], Math.pow(wqp[0][i], 2) };
			A = Matlab.conv(tempA, A);
		}

		if (order % 2 == 1) {
			A = Matlab.conv(A, new double[] { 1, (-1) * sigma });
			B[0] *= Math.abs(sigma);
		}

		return new double[][] { B, A };
	}

	//================================================================================
	// Setters and Getters
	//================================================================================

	public double[] getTimeData() {
		return timeData;
	}

	public double[] getInputData() {
		return inputData;
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

	public double[][] getPolesData() {
		return polesData;
	}

	public double[] getErrorData() {
		return errorData;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public int getOrder() {
		return order;
	}

	public double getK() {
		return K;
	}

	public void setK(double K) {
		this.K = K;
	}

	public double[][] getWqp() {
		return wqp;
	}

	public void setWqp(double[][] wqp) {
		this.wqp = new double[2][wqp[0].length];
		System.arraycopy(wqp[0], 0, this.wqp[0], 0, wqp[0].length);
		System.arraycopy(wqp[1], 0, this.wqp[1], 0, wqp[0].length);
	}

	public double getSigma() {
		return sigma;
	}

	public void setSigma(double sigma) {
		this.sigma = sigma;
	}

	public double getMeanError() {
		return meanError;
	}
}
