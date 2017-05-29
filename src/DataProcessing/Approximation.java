package DataProcessing;

import java.util.Arrays;

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
import userinterface.StatusBar;

public class Approximation {

	public static Complex test1 = new Complex(12, 2);
	public static Complex test2 = new Complex(12, 2);
	public static Complex test3 = new Complex(12, 2);
	public static double[] polstellenReal = new double[10];
	public static double[] polstellenImag;
	public Complex[] Polstellen;

	public double[] yIst;
	public double[] t;

	//chng
	//	public Complex[] res = new Complex[10];

	private static PlotData plotData = new PlotData();

	public Approximation() {

	}

	public static double[] Awert(int order, Complex[] polesReal) {
		double[] x0 = new double[order + 1];

		x0[0] = 0.5;
		for (int i = 1; i <= polesReal.length; i++) {
			x0[i] = polesReal[i - 1].getReal();
		}

		return x0;
	}

	public static final Object[] schritt(double[] poles, double[] t, int order) {
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

		Object[] ret = new Object[3];
		Object[] step = SVTools.step(B, doubleA, t);

		ret[0] = step[0];
		ret[1] = step[1];
		ret[2] = doubleA;

		return ret;
	}

	//t, y_soll, x,N
	public static double errorFunction(double[] t, double[] y_soll, double[] poles, int order) {
		double error = 0;

		Object[] ret = Approximation.schritt(poles, t, order);

		double[] y_ist = (double[]) ret[0];
		double[] t1 = (double[]) ret[1];

		for (int i = 0; i < y_soll.length; i++) {
			error += Math.pow(y_soll[i] - y_ist[i], 2);
		}

		return error;
	}

	private static class Target implements MultivariateFunction {
		double[] t;
		double[] y_soll;
		int order;
		double evals = 0;
		double[] coeffs;
		double error;

		public Target(double[] t, double[] y_soll, int order) {
			this.t = t;
			this.y_soll = y_soll;
			this.order = order;
		}

		public double value(double[] variables) {

			final double[] coeffs = variables;
			double error = errorFunction(t, y_soll, coeffs, order);

			evals++;
			System.out.println("Evals: " + evals);
			System.out.println("Error: " + error);

			this.error = error;
			this.coeffs = coeffs;

			return error;
		}
	}

	public static Object[] approximate(double[] timeData, double[] stepData, int order, double nelderSteps, double[] simplexOpt,
			int maxEval) {
		Filter filt = FilterFactory.createButter(order, 1.0);

		Object[] resi = Matlab.residue(filt.B, filt.A);

		Complex[] R = (Complex[]) resi[0];
		Complex[] P = (Complex[]) resi[1];
		double K = (double) resi[2];

		double[] initCoeffs = Awert(order, P);

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

		Object[] result = Approximation.schritt(approxPoles, timeData, order);

		return result;
	}
}
