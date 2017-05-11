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
	
	/*public List<String[]> getMeasurementList(){
		return model.getMeasurementList();
	}*/
	
//	public double [][] getMeasurement(){
//		return model.getMeasurement();
//	}
	
	public void setOrder(int order){
		model.setOrder(order);
	}
	
}
