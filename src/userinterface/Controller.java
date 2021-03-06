package userinterface;

import java.util.List;

import dataProcessing.Model;

/**
 * 
 * @author Thomas Frei
 *
 */
public class Controller {

	//================================================================================
	// Properties
	//================================================================================

	private Model model;
	private View view;

	private Thread threadApproximation;

	//================================================================================
	// Constructor
	//================================================================================

	public Controller(Model model) {
		this.model = model;
	}

	//================================================================================
	// Public Methods (excl. Setter and Getter)
	//================================================================================

	/**
	 * �bergibt dem Controller die View zum Ein- und Ausschalten der Buttons.
	 * @param view view der Applikation.
	 */
	public void setView(View view) {
		this.view = view;
	}

	/**
	 * Setzt die Messdaten aus der �bergebenen List und deaktiviert den Run-Button w�hrend
	 * der Operation. Falls die Messung erfolgreich geladen wurde, wird dieser wieder aktiviert.
	 * @param measurementList Liste mit Messdaten.
	 */
	public void setMeasurement(List<String[]> measurementList) {
		view.inputPanel.btRun.setEnabled(false);
		model.setMeasurement(measurementList);
		if (model.measurementLoaded()) {
			view.inputPanel.btRun.setEnabled(true);
		}
	}

	/**
	 * F�hrt die automatische Approximation in einem separaten Thread durch. Der
	 * Run-Button wird w�hrenddessen deaktiviert.
	 */
	public void approximateAuto() {
		threadApproximation = new Thread(new Runnable() {

			@Override
			public void run() {
				view.inputPanel.btRun.setEnabled(false);
				view.inputPanel.btCancel.setEnabled(true);
				model.approximateAuto();
				view.inputPanel.btRun.setEnabled(true);
				view.inputPanel.btCancel.setEnabled(false);
			}
		});
		threadApproximation.start();
	}

	/**
	 * F�hrt die manuelle Approximation durch.
	 */
	public void approximateManual() {
		model.approximateManual();
	}

	/**
	 * Stoppt die Approximation indem der Thread unterbrochen wird.
	 * Der Run-Button wird dabei wieder aktiviert.
	 */
	public void stopApproximation() {
		threadApproximation.interrupt();
		view.inputPanel.btRun.setEnabled(true);
		view.inputPanel.btCancel.setEnabled(false);
	}

	//================================================================================
	// Setter and Getter
	//================================================================================

	public void setSettings(Object[] settings) {
		model.setSettings(settings);
	}

	public void setOrder(int order) {
		model.setOrder(order);
	}

	public void setK(double K) {
		model.setK(K);
	}

	public void setSigma(double sigma) {
		model.setSigma(sigma);
	}

	public void setWqp(double[][] wqp) {
		model.setWqp(wqp);
	}
}
