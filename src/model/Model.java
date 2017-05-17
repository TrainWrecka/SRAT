package model;
import java.util.List;
import java.util.Observable;

import DataProcessing.Approximation;
import DataProcessing.Measurement;
import DataProcessing.PlotData;

import org.jfree.data.xy.XYSeries;

public class Model extends Observable {
	private Approximation approximation;
	private PlotData plotData;
	private Measurement measurement = new Measurement();
	
	public Model() {}
	
	
	public void setMeasurement(List<String[]> measurementList){
		measurement.setMeasurement(measurementList);
		notifyObservers();
	}
	
	public void approximateMeasurement(){
		measurement.approximateMeasurement();
		notifyObservers();
	}
	
	public void setSettings(Object[] settings){
		measurement.setSettings(settings);
	}
	
//	public double[][] getMeasurement(){
//		return measurement.getMeasurement();
//	}
	
	/*public List<String[]> getMeasurementList(){
		return measurement.getMeasurementList();
	}*/
	
	public XYSeries[] getStepresponseData(){
		return measurement.getStepresponseData();
	}
	
	public XYSeries[] getErrorData(){
		return measurement.getErrorData();
	}
	
	public XYSeries[] getPolesData(){
		return measurement.getPolesData();
	}
	
	
	public boolean inputExisting(){
		return measurement.inputExisting();
	}
	
	public void setValues(Object[] val){
		measurement.setValues(val);
		notifyObservers();
	}
	
	public Object[] getValues(){
		return measurement.getValues();
	}
	
	
	
	public void notifyObservers() {
		setChanged();
		super.notifyObservers();
	}
	
	public void setOrder(int order){
		measurement.setOrder(order);
	}

	
}
