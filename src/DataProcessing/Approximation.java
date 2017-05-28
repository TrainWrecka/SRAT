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
	/*
	public static final Object[] schritt(double[] poles, double[] t, int Ordnung) {
		double[] B = new double[1];
		B[0] = poles[0];
	
		Complex Help = new Complex(1,0);
	
		Complex[] A1 = new Complex[2];
		Complex[] A2 = new Complex[2];
		Complex[] A3 = new Complex[2];
		Complex[] A4 = new Complex[2];
		Complex[] A5 = new Complex[2];
		Complex[] A6 = new Complex[2];
		Complex[] A7 = new Complex[2];
		Complex[] A8 = new Complex[2];
		Complex[] A9 = new Complex[2];
		Complex[] A10 = new Complex[2];	
	
		Complex[] A12;
		Complex[] A34;
		Complex[] A56;
		Complex[] A78;
		Complex[] A910;	
		
	
		Complex[] PolesNenner = new Complex[Ordnung];
		Complex[][] AHelp = new Complex[Ordnung][Ordnung];
		Complex[] Poles = new Complex[Ordnung];
	
		for(int k=0;k<Ordnung/2;k++){
			Poles[k*2]=new Complex((-1)*Math.sqrt(Math.pow(poles[k*2+1],2)),poles[k*2+2]);
			Poles[k*2+1]=Poles[k*2].conjugate();	
		}
	
		switch(Ordnung){
			
		case 2:
			A1[0] = Help;
			A1[1] = Poles[0].multiply(-1);
	
			A2[0] = Help;
			A2[1] = Poles[1].multiply(-1);
	
			
			A12 = Matlab.conv(A1,A2);
			
			PolesNenner = A12;
			break;	
		case 3:
			A1[0] = Help;
			A1[1] = Poles[0].multiply(-1);
	
			A2[0] = Help;
			A2[1] = Poles[1].multiply(-1);
	
			A3[0] = Help;
			A3[1] = Poles[2].multiply(-1);
			
			A12 = Matlab.conv(A1,A2);
			
			PolesNenner = Matlab.conv(A12,A3);
			break;
		case 4:
			A1[0] = Help;
			A1[1] = Poles[0].multiply(-1);
	
			A2[0] = Help;
			A2[1] = Poles[1].multiply(-1);
	
			A3[0] = Help;
			A3[1] = Poles[2].multiply(-1);
	
			A4[0] = Help;
			A4[1] = Poles[3].multiply(-1);
			
			A12 = Matlab.conv(A1,A2);
			A34 = Matlab.conv(A3,A4);
		
			PolesNenner = Matlab.conv(A12,A34);
			break;
		case 5:
			A1[0] = Help;
			A1[1] = Poles[0].multiply(-1);
	
			A2[0] = Help;
			A2[1] = Poles[1].multiply(-1);
	
			A3[0] = Help;
			A3[1] = Poles[2].multiply(-1);
	
			A4[0] = Help;
			A4[1] = Poles[3].multiply(-1);
	
			A5[0] = Help;
			A5[1] = Poles[4].multiply(-1);
	
			A12 = Matlab.conv(A1,A2);
			A34 = Matlab.conv(A3,A4);
			
			PolesNenner = Matlab.conv(Matlab.conv(A12,A34), A5);
			break;
		case 6:
			A1[0] = Help;
			A1[1] = Poles[0].multiply(-1);
	
			A2[0] = Help;
			A2[1] = Poles[1].multiply(-1);
	
			A3[0] = Help;
			A3[1] = Poles[2].multiply(-1);
	
			A4[0] = Help;
			A4[1] = Poles[3].multiply(-1);
	
			A5[0] = Help;
			A5[1] = Poles[4].multiply(-1);
	
			A6[0] = Help;
			A6[1] = Poles[5].multiply(-1);	
	
			A12 = Matlab.conv(A1,A2);
			A34 = Matlab.conv(A3,A4);
			A56 = Matlab.conv(A5,A6);
	
			PolesNenner = Matlab.conv(Matlab.conv(A12, A34), A56);
			break;
		case 7:
			A1[0] = Help;
			A1[1] = Poles[0].multiply(-1);
	
			A2[0] = Help;
			A2[1] = Poles[1].multiply(-1);
	
			A3[0] = Help;
			A3[1] = Poles[2].multiply(-1);
	
			A4[0] = Help;
			A4[1] = Poles[3].multiply(-1);
	
			A5[0] = Help;
			A5[1] = Poles[4].multiply(-1);
	
			A6[0] = Help;
			A6[1] = Poles[5].multiply(-1);
	
			A7[0] = Help;
			A7[1] = Poles[6].multiply(-1);
		
			
			A12 = Matlab.conv(A1,A2);
			A34 = Matlab.conv(A3,A4);
			A56 = Matlab.conv(A5,A6);	
			
			PolesNenner = Matlab.conv(Matlab.conv(A12, A34), Matlab.conv(A56, A7)); 
			break;
		case 8:
			A1[0] = Help;
			A1[1] = Poles[0].multiply(-1);
	
			A2[0] = Help;
			A2[1] = Poles[1].multiply(-1);
	
			A3[0] = Help;
			A3[1] = Poles[2].multiply(-1);
	
			A4[0] = Help;
			A4[1] = Poles[3].multiply(-1);
	
			A5[0] = Help;
			A5[1] = Poles[4].multiply(-1);
	
			A6[0] = Help;
			A6[1] = Poles[5].multiply(-1);
	
			A7[0] = Help;
			A7[1] = Poles[6].multiply(-1);
	
			A8[0] = Help;
			A8[1] = Poles[7].multiply(-1);	
	
			A12 = Matlab.conv(A1,A2);
			A34 = Matlab.conv(A3,A4);
			A56 = Matlab.conv(A5,A6);
			A78 = Matlab.conv(A7,A8);
	
			PolesNenner = Matlab.conv(Matlab.conv(A12, A34), Matlab.conv(A56, A78));
			break;
	
		case 9: 
			A1[0] = Help;
			A1[1] = Poles[0].multiply(-1);
	
			A2[0] = Help;
			A2[1] = Poles[1].multiply(-1);
	
			A3[0] = Help;
			A3[1] = Poles[2].multiply(-1);
	
			A4[0] = Help;
			A4[1] = Poles[3].multiply(-1);
	
			A5[0] = Help;
			A5[1] = Poles[4].multiply(-1);
	
			A6[0] = Help;
			A6[1] = Poles[5].multiply(-1);
	
			A7[0] = Help;
			A7[1] = Poles[6].multiply(-1);
	
			A8[0] = Help;
			A8[1] = Poles[7].multiply(-1);
	
			A9[0] = Help;
			A9[1] = Poles[8].multiply(-1);
				
			A12 = Matlab.conv(A1,A2);
			A34 = Matlab.conv(A3,A4);
			A56 = Matlab.conv(A5,A6);
			A78 = Matlab.conv(A7,A8);
	
			PolesNenner = Matlab.conv(Matlab.conv(Matlab.conv(A12, A34), Matlab.conv(A56, A78)), A9);
			break;
		case 10:
			A1[0] = Help;
			A1[1] = Poles[0].multiply(-1);
	
			A2[0] = Help;
			A2[1] = Poles[1].multiply(-1);
	
			A3[0] = Help;
			A3[1] = Poles[2].multiply(-1);
	
			A4[0] = Help;
			A4[1] = Poles[3].multiply(-1);
	
			A5[0] = Help;
			A5[1] = Poles[4].multiply(-1);
	
			A6[0] = Help;
			A6[1] = Poles[5].multiply(-1);
	
			A7[0] = Help;
			A7[1] = Poles[6].multiply(-1);
	
			A8[0] = Help;
			A8[1] = Poles[7].multiply(-1);
	
			A9[0] = Help;
			A9[1] = Poles[8].multiply(-1);
	
			A10[0] = Help;
			A10[1] = Poles[9].multiply(-1);
	
			A12 = Matlab.conv(A1,A2);
			A34 = Matlab.conv(A3,A4);
			A56 = Matlab.conv(A5,A6);
			A78 = Matlab.conv(A7,A8);
			A910 = Matlab.conv(A9,A10);
				
			PolesNenner = Matlab.conv(Matlab.conv(Matlab.conv(A12,A34), Matlab.conv(A56,A78)), A910);
			break;
	}
	
		double[] doublePolesNenner = new double[PolesNenner.length];
	
		for (int i = 0; i < PolesNenner.length; i++) {
			doublePolesNenner[i] = PolesNenner[i].getReal();
		}
	
		Object[] ret = new Object[3];
		Object[] step = SVTools.step(B, doublePolesNenner, t);
	
		ret[0] = step[0];
		ret[1] = step[1];
		//ret = Arrays.copyOfRange(step, 0, 1);
		ret[2] = doublePolesNenner;
		//ret[2] = doubleA;
	
		return ret;
	}*/

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
		
		
		while(j < tempA.length){
			A = Matlab.conv(tempA[j++], tempA[j++]);
		}
		/*
		for (int i = 0; i < order - (order % 2); i += 2) {
			A = Matlab.conv(tempA[i], tempA[i + 1]);
		}

		if (order % 2 == 1) {
			A = Matlab.conv(A, tempA[tempA.length-1]);
		}*/

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

//		public static final Object[] schritt(double[] poles, double[] t, int Ordnung) {
//			double[] B = new double[1];
//			B[0] = poles[0];
//	
//			Complex[] A = new Complex[Ordnung];
//			Complex p1 = new Complex(poles[1], poles[2]);
//			Complex p2 = p1.conjugate();
//			Complex p3;
//			Complex p4;
//			Complex p5;
//			Complex p6;
//			Complex p7;
//			Complex p8;
//			Complex p9;
//			Complex p10;
//			Complex[] A1 = new Complex[2];
//			Complex[] A2 = new Complex[2];
//			Complex[] A3 = new Complex[2];
//			Complex[] A4 = new Complex[2];
//			Complex[] A5 = new Complex[2];
//			Complex[] A6 = new Complex[2];
//			Complex[] A7 = new Complex[2];
//			Complex[] A8 = new Complex[2];
//			Complex[] A9 = new Complex[2];
//			Complex[] A10 = new Complex[2];
//	
//			A1[0] = new Complex(1, 0);
//			A1[1] = p1.multiply(-1);
//	
//			A2[0] = new Complex(1, 0);
//			A2[1] = p2.multiply(-1);
//	
//			Complex[] AA = Matlab.conv(A1, A2);
//			Complex[] AAA;
//			Complex[] AAAA;
//			Complex[] AAAAA;
//			Complex[] AAAAAA;
//	
//			switch (Ordnung) {
//				case 2:
//					A = AA;
//					break;
//				case 3:
//					p3 = new Complex(poles[3], 0);
//	
//					A3[0] = new Complex(1, 0);
//					A3[1] = p3.multiply(-1);
//	
//					A = Matlab.conv(AA, A3);
//					break;
//				case 4:
//					p3 = new Complex(poles[3], poles[4]);
//					p4 = p3.conjugate();
//	
//					A3[0] = new Complex(1, 0);
//					A3[1] = p3.multiply(-1);
//	
//					A4[0] = new Complex(1, 0);
//					A4[1] = p4.multiply(-1);
//	
//					AAA = Matlab.conv(A3, A4);
//					A = Matlab.conv(AA, AAA);
//					break;
//				case 5:
//					p3 = new Complex(poles[3], poles[4]);
//					p4 = p3.conjugate();
//					p5 = new Complex(poles[4], 0);
//	
//					A3[0] = new Complex(1, 0);
//					A3[1] = p3.multiply(-1);
//	
//					A4[0] = new Complex(1, 0);
//					A4[1] = p4.multiply(-1);
//	
//					A5[0] = new Complex(1, 0);
//					A5[1] = p5.multiply(-1);
//	
//					AAA = Matlab.conv(A3, A4);
//					AAAA = Matlab.conv(AAA, AA);
//	
//					A = Matlab.conv(AAAA, A5);
//					break;
//				case 6:
//					p3 = new Complex(poles[3], poles[4]);
//					p4 = p3.conjugate();
//					p5 = new Complex(poles[5], poles[6]);
//					p6 = p5.conjugate();
//	
//					A3[0] = new Complex(1, 0);
//					A3[1] = p3.multiply(-1);
//	
//					A4[0] = new Complex(1, 0);
//					A4[1] = p4.multiply(-1);
//	
//					A5[0] = new Complex(1, 0);
//					A5[1] = p5.multiply(-1);
//	
//					A6[0] = new Complex(1, 0);
//					A6[1] = p6.multiply(-1);
//	
//					AAA = Matlab.conv(A3, A4);
//					AAAA = Matlab.conv(A5, A6);
//					A = Matlab.conv(AAAA, Matlab.conv(AA, AAA));
//	
//					break;
//				case 7:
//					p3 = new Complex(poles[3], poles[4]);
//					p4 = p3.conjugate();
//					p5 = new Complex(poles[5], poles[6]);
//					p6 = p5.conjugate();
//					p7 = new Complex(poles[6], 0);
//	
//					A3[0] = new Complex(1, 0);
//					A3[1] = p3.multiply(-1);
//	
//					A4[0] = new Complex(1, 0);
//					A4[1] = p4.multiply(-1);
//	
//					A5[0] = new Complex(1, 0);
//					A5[1] = p5.multiply(-1);
//	
//					A6[0] = new Complex(1, 0);
//					A6[1] = p6.multiply(-1);
//	
//					A7[0] = new Complex(1, 0);
//					A7[1] = p7.multiply(-1);
//	
//					AAA = Matlab.conv(A3, A4);
//					AAAA = Matlab.conv(A5, A6);
//	
//					A = Matlab.conv(Matlab.conv(AAAA, Matlab.conv(AA, AAA)), A7);
//					break;
//				case 8:
//					p3 = new Complex(poles[3], poles[4]);
//					p4 = p3.conjugate();
//					p5 = new Complex(poles[5], poles[6]);
//					p6 = p5.conjugate();
//					p7 = new Complex(poles[7], poles[8]);
//					p8 = p7.conjugate();
//	
//					A3[0] = new Complex(1, 0);
//					A3[1] = p3.multiply(-1);
//	
//					A4[0] = new Complex(1, 0);
//					A4[1] = p4.multiply(-1);
//	
//					A5[0] = new Complex(1, 0);
//					A5[1] = p5.multiply(-1);
//	
//					A6[0] = new Complex(1, 0);
//					A6[1] = p6.multiply(-1);
//	
//					A7[0] = new Complex(1, 0);
//					A7[1] = p7.multiply(-1);
//	
//					A8[0] = new Complex(1, 0);
//					A8[1] = p8.multiply(-1);
//	
//					AAA = Matlab.conv(A3, A4);
//					AAAA = Matlab.conv(A5, A6);
//					AAAAA = Matlab.conv(A7, A8);
//	
//					A = Matlab.conv(Matlab.conv(AA, AAA), Matlab.conv(AAAA, AAAAA));
//					break;
//				case 9:
//					p3 = new Complex(poles[3], poles[4]);
//					p4 = p3.conjugate();
//					p5 = new Complex(poles[5], poles[6]);
//					p6 = p5.conjugate();
//					p7 = new Complex(poles[7], poles[8]);
//					p8 = p7.conjugate();
//					p9 = new Complex(poles[9], 0);
//	
//					A3[0] = new Complex(1, 0);
//					A3[1] = p3.multiply(-1);
//	
//					A4[0] = new Complex(1, 0);
//					A4[1] = p4.multiply(-1);
//	
//					A5[0] = new Complex(1, 0);
//					A5[1] = p5.multiply(-1);
//	
//					A6[0] = new Complex(1, 0);
//					A6[1] = p6.multiply(-1);
//	
//					A7[0] = new Complex(1, 0);
//					A7[1] = p7.multiply(-1);
//	
//					A8[0] = new Complex(1, 0);
//					A8[1] = p8.multiply(-1);
//	
//					A9[0] = new Complex(1, 0);
//					A9[1] = p9.multiply(-1);
//	
//					AAA = Matlab.conv(A3, A4);
//					AAAA = Matlab.conv(A5, A6);
//					AAAAA = Matlab.conv(A7, A8);
//	
//					A = Matlab.conv(Matlab.conv(Matlab.conv(AA, AAA), Matlab.conv(AAAA, AAAAA)), A9);
//	
//					break;
//				case 10:
//					p3 = new Complex(poles[3], poles[4]);
//					p4 = p3.conjugate();
//					p5 = new Complex(poles[5], poles[6]);
//					p6 = p5.conjugate();
//					p7 = new Complex(poles[7], poles[8]);
//					p8 = p7.conjugate();
//					p9 = new Complex(poles[9], poles[10]);
//					p10 = p9.conjugate();
//	
//					A3[0] = new Complex(1, 0);
//					A3[1] = p3.multiply(-1);
//	
//					A4[0] = new Complex(1, 0);
//					A4[1] = p4.multiply(-1);
//	
//					A5[0] = new Complex(1, 0);
//					A5[1] = p5.multiply(-1);
//	
//					A6[0] = new Complex(1, 0);
//					A6[1] = p6.multiply(-1);
//	
//					A7[0] = new Complex(1, 0);
//					A7[1] = p7.multiply(-1);
//	
//					A8[0] = new Complex(1, 0);
//					A8[1] = p8.multiply(-1);
//	
//					A9[0] = new Complex(1, 0);
//					A9[1] = p9.multiply(-1);
//	
//					A10[0] = new Complex(1, 0);
//					A10[1] = p10.multiply(-1);
//	
//					AAA = Matlab.conv(A3, A4);
//					AAAA = Matlab.conv(A5, A6);
//					AAAAA = Matlab.conv(A7, A8);
//					AAAAAA = Matlab.conv(A9, A10);
//	
//					A = Matlab.conv(Matlab.conv(Matlab.conv(AA, AAA), Matlab.conv(AAAA, AAAAA)), AAAAAA);
//					break;
//			}
//	
//			double[] doubleA = new double[A.length];
//	
//			for (int i = 0; i < A.length; i++) {
//				doubleA[i] = A[i].getReal();
//			}
//	
//			Object[] ret = new Object[3];
//			Object[] step = SVTools.step(B, doubleA, t);
//	
//			ret[0] = step[0];
//			ret[1] = step[1];
//			ret[2] = doubleA;
//	
//			return ret;
//		}

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

		//		Object[] resi1 = Approximation.Awert(order, P);
		//		double[] initCoeffs = (double[])resi1[0];
		//		int k = (int)resi1[1];

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
