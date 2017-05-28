package model;

import java.util.List;
import java.util.Observable;

import DataProcessing.Approximation;
import DataProcessing.Measurement;
import DataProcessing.PlotData;
import matlabfunctions.Matlab;

import org.jfree.data.xy.XYSeries;

public class Model extends Observable {
	private Approximation approximation;
	private PlotData plotData;
	private Measurement measurement = new Measurement();

	private PlotData stepPlotData = new PlotData();
	private PlotData errorPlotData = new PlotData();
	private PlotData polesPlotData = new PlotData();
	
	double laguerreAcc = 1e-10;
	double[] simplexOpt = { 1e-24, 1e-24 };
	double nelderSteps = 0.01;
	int maxEval = 5000;
	boolean doFilter = false;
	boolean showFiltered = true;
	boolean autoFilter = true;
	int filterPercentage = 80;
	

	public Model() {}

	public void setMeasurement(List<String[]> measurementList) {
		stepPlotData.removePlotData();
		errorPlotData.removePlotData();
		polesPlotData.removePlotData();

		measurement.setMeasurement(measurementList);
		
		if(doFilter){
			filtMeasurement();
		}

		updateMeasurement();
		notifyObservers();
	}
	
	public void filtMeasurement(){
		measurement.filtData(autoFilter, filterPercentage);
	}

	
	private void updateMeasurement(){
		stepPlotData.removePlotData();
		
		stepPlotData.setXData(measurement.getTimeData());
		if (inputExisting()) {
			stepPlotData.setYData(measurement.getInputData(), "Input");
		}
		stepPlotData.setYData(measurement.getStepData(showFiltered), "Step");
		if(measurement.approximated){
			stepPlotData.setYData(measurement.getApproxData(), "Approximation");
		}
	}
	
	private void updateError(){
		errorPlotData.removePlotData();
		errorPlotData.setXData(measurement.getTimeData());
		errorPlotData.setYData(measurement.getErrorData(), "Error");
	}
	
	private void updatePoles(){
		polesPlotData.removePlotData();
		polesPlotData.setXData(measurement.getPolesData()[0]);
		polesPlotData.setYData(measurement.getPolesData()[1], "Poles");
	}

	public void approximateMeasurement() {

		measurement.approximateMeasurement(nelderSteps, simplexOpt, maxEval);

		updateMeasurement();
		updateError();
		updatePoles();

		notifyObservers();
	}

//	public void setSettings(Object[] settings) {
//		measurement.setSettings(settings);
//	}
	
	public void setSettings(Object[] settings) {
		Matlab.laguerreAcc = (double) settings[0];
		this.simplexOpt = (double[]) settings[1];
		this.nelderSteps = (double) settings[2];
		this.maxEval = (int) settings[3];
		this.doFilter = (boolean) settings[4];
		this.showFiltered = (boolean) settings[5];
		this.autoFilter = (boolean) settings[6];
		this.filterPercentage = (int) settings[7];
		
		if(doFilter){
			filtMeasurement();
		} else {
			measurement.undoFilter();
		}
		if(measurement.approximated){
			measurement.calulateError();
			updateError();
		}
		updateMeasurement();

		notifyObservers();
	}

	public XYSeries[] getStepresponseData() {
		return stepPlotData.getPlotData();
	}

	public XYSeries[] getErrorData() {
		return errorPlotData.getPlotData();
	}

	public XYSeries[] getPolesData() {
		return polesPlotData.getPlotData();
	}

	public boolean inputExisting() {
		return measurement.inputExisting();
	}

	public void setValues(Object[] val) {
		measurement.setValues(val);

		updateMeasurement();
		updateError();
		updatePoles();

		notifyObservers();
	}

	public Object[] getValues() {
		return measurement.getValues();
	}

	public void notifyObservers() {
		setChanged();
		super.notifyObservers();
	}

	public void setOrder(int order) {
		measurement.setOrder(order);
	}
	/*
	public int getSeriesCount(){
		return stepPlotData.getSeriesCount();
	}*/

}
