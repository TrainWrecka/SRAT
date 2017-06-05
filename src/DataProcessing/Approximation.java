package DataProcessing;

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

import matlabfunctions.Filter;
import matlabfunctions.FilterFactory;
import matlabfunctions.Matlab;
import matlabfunctions.SVTools;
import sun.swing.SwingUtilities2.Section;
import userinterface.StatusBar;

public class Approximation {

	//================================================================================
	// Properties
	//================================================================================

	private boolean running;

	//================================================================================
	// Constructor 
	//================================================================================

	public Approximation() {

	}

	//================================================================================
	// Public Methods
	//================================================================================

	/**
	 * TODO!!!
	 * @param timeData Zeitachse.
	 * @param stepData Zu approximierender Schritt.
	 * @param order Zu approximiernede Ordnung.
	 * @param nelderSteps Nelder Simplex Steps des Optimierers.
	 * @param simplexOpt Relative und Absolute Goals des Optimierers.
	 * @param maxEval Maximale Evaluationen des optimierers.
	 * @return Gibt den generierten Schritt und die dazugehörigen Pole zurück.
	 */
	public double[][] approximate(double[] timeData, double[] stepData, int order, double nelderSteps, double[] simplexOpt,
			int maxEval) {
		Filter filt = FilterFactory.createButter(order, 1.0);

		Complex[] P = (Complex[]) Matlab.residue(filt.B, filt.A)[1];

		double[] initCoeffs = initialCoeffs(order, P);
		double[] nelderValues = new double[order + 1];

		for (int i = 0; i < nelderValues.length; i++) {
			nelderValues[i] = nelderSteps;
		}

		SimplexOptimizer optimizer = new SimplexOptimizer(simplexOpt[0], simplexOpt[1]);
		Target target = new Target(timeData, stepData, order);
		PointValuePair optimum = null;
		double[] approxPoles = null;
		try {
			optimum = optimizer.optimize(new MaxEval(maxEval), new ObjectiveFunction(target), GoalType.MINIMIZE,
					new InitialGuess(initCoeffs), new NelderMeadSimplex(nelderValues));
			approxPoles = optimum.getPoint();
		} catch (TooManyEvaluationsException e) {
			approxPoles = target.coeffs;
		}

		StatusBar.clear();
		StatusBar.showStatus("Approximation finished");
		StatusBar.showStatus("Evaluations: " + target.evals);

		return generateStep(approxPoles, timeData, order);
	}

	/**
	 * Überprüft ob die Approximation noch am laufen ist.
	 * @return false falls die Approximaiton steckengeblieben ist.
	 */
	public boolean checkRunning() {
		boolean ret = running;
		running = false;
		return ret;
	}

	//================================================================================
	// Private Methods
	//================================================================================

	private double[] initialCoeffs(int order, Complex[] polesReal) {
		double[] coeffs = new double[order + 1];

		coeffs[0] = 0.5;
		for (int i = 1; i <= polesReal.length; i++) {
			coeffs[i] = polesReal[i - 1].getReal();
		}

		return coeffs;
	}

	private double errorFunction(double[] timeAxis, double[] stepGoal, double[] poles, int order) {
		double error = 0;

		double[] stepCurrent = (double[]) generateStep(poles, timeAxis, order)[0];

		for (int i = 0; i < stepGoal.length; i += 10) {
			error += Math.pow(stepGoal[i] - stepCurrent[i], 2);
		}
		
		return error;
	}

	private double[][] generateStep(double[] poles, double[] timeAxis, int order) {
		double[] B = new double[1];
		B[0] = poles[0];

		Complex[][] tempA = new Complex[order][2];
		Complex[] poles2 = new Complex[order];

		int j = 1;
		for (int i = 0; i < order; i++) {
			if (i % 2 == 0) {
				if (i == order - 1) {
					poles2[i] = new Complex(poles[j], 0);
				} else {
					poles2[i] = new Complex(poles[j++], poles[j++]);
				}
			} else {
				poles2[i] = poles2[i - 1].conjugate();
			}
		}

		for (int i = 0; i < order; i++) {
			tempA[i][0] = new Complex(1, 0);
			tempA[i][1] = poles2[i].multiply(-1);
		}

		Complex[] A = new Complex[order / 2 + (order % 2)];

		j = 0;
		A = tempA[j++];

		while (j < tempA.length) {
			A = Matlab.conv(A, tempA[j++]);
		}

		double[] doubleA = new double[A.length];

		for (int i = 0; i < A.length; i++) {
			doubleA[i] = A[i].getReal();
		}

		double[] step = (double[]) SVTools.step(B, doubleA, timeAxis)[0];

		return new double[][] { step, doubleA };
	}

	private class Target implements MultivariateFunction {
		double[] t;
		double[] y_soll;
		int order;
		int evals = 0;
		double[] coeffs;
		double error;

		public Target(double[] t, double[] y_soll, int order) {
			this.t = t;
			this.y_soll = y_soll;
			this.order = order;

			running = true;
		}

		public double value(double[] variables) {
			final double[] coeffs = variables;
			double error = errorFunction(t, y_soll, coeffs, order);

			if (evals % 100 == 0) {
				StatusBar.clear();
				StatusBar.showStatus("Approximating...");
				StatusBar.showStatus("Evaluations: " + evals);
			}

			evals++;

			this.error = error;
			this.coeffs = coeffs;

			return error;
		}
	}
}
