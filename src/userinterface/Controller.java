package userinterface;

import java.util.List;

import StepResponseApproximationTool.StepResponseApproximationTool;
import model.Model;



public class Controller {
	private Model model;
	private View view;
	private StepResponseApproximationTool mvcFramework;

	public Controller(Model model, StepResponseApproximationTool mvcFramework) {
		this.model = model;
		this.mvcFramework = mvcFramework;
	}
	
	public void setView(View view){
		this.view = view;
	}
	
	public void setMeasurement(List<String[]> measurementList){
		model.setMeasurement(measurementList);
	}
	
	public void approximateMeasurement(){
		model.approximateMeasurement();
	}
	
	public void setSettings(Object[] settings){
		model.setSettings(settings);
	}
	
	public void setValues(Object[] val){
		model.setValues(val);
	}
	
	public Object[] getValues(){
		return model.getValues();
	}

	public void setOrder(int order){
		model.setOrder(order);
	}
	
}
