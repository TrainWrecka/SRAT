package model;

import java.util.List;
import java.util.Observable;

import DataProcessing.Measurement;
import matlabfunctions.Matlab;
import userinterface.StatusBar;

public class Model extends Observable {

	//================================================================================
	// Properties
	//================================================================================

	private double[] simplexOpt;
	private double nelderSteps;
	private int maxEval;
	private boolean doFilter;
	private boolean showFiltered;
	private boolean autoFilter;
	private int filterPercentage;

	private boolean measurementLoaded = false;

	private Measurement measurement;

	//================================================================================
	// Constructor
	//================================================================================

	public Model() {}

	//================================================================================
	// Public Methods (excl.Setters and Getters)
	//================================================================================

	/**
	 * Setzt die Messdaten aus der Übergebenen List und filtert die Schrittantwort anhand
	 * der gesetzten Einstellungen. Falls eine Exception während dem Einlesen auftritt,
	 * wird der User darüber benachrichtigt und die Daten werden nicht geladen.
	 * Ruft notifyObservers auf um die Plots zu aktualisieren.
	 * @param measurementList Liste mit Messdaten.
	 */
	public void setMeasurement(List<String[]> measurementList) {
		measurementLoaded = false;

		try {
			measurement = new Measurement();
			measurement.setMeasurement(measurementList);
			if (doFilter) {
				filtMeasurement();
			}
			measurementLoaded = true;

		} catch (NumberFormatException e) {
			StatusBar.showStatus("Wrong Number format");
		} catch (ArrayIndexOutOfBoundsException e) {
			StatusBar.showStatus("Wrong Data format");
		} catch (RuntimeException e) {
			StatusBar.showStatus("Incorrect data columns");
		}

		notifyObservers();
	}

	/**
	 * Approximiert die Schrittantwort automatisch anhand der gesetzten Einstellungen. 
	 * Ruft notifyObservers auf um die Plots zu aktualisieren.
	 */
	public void approximateAuto() {
		measurement.approximateAuto(nelderSteps, simplexOpt, maxEval);
		notifyObservers();
	}

	/**
	 * Approximiert die Schrittantwort manuell anhand der gesetzten Parameter. 
	 * Ruft notifyObservers auf um die Plots zu aktualisieren.
	 */
	public void approximateManual() {
		measurement.approximateManual();
		notifyObservers();
	}

	/**
	 * Setzt die Einstellungen. Falls die Filtereinstellungen verändert wurden
	 * @param settings Objekt mit den Einstellungen.
	 */
	public void setSettings(Object[] settings) {
		Matlab.laguerreAccuracy = (double) settings[0];
		this.simplexOpt = (double[]) settings[1];
		this.nelderSteps = (double) settings[2];
		this.maxEval = (int) settings[3];
		this.doFilter = (boolean) settings[4];
		this.showFiltered = (boolean) settings[5];
		this.autoFilter = (boolean) settings[6];
		this.filterPercentage = (int) settings[7];

		if (measurementLoaded) {
			if (doFilter) {
				filtMeasurement();
			} else {
				measurement.undoFilter();
			}

			if (measurement.approximated()) {
				measurement.recalculateError();
			}
			notifyObservers();
		}

	}

	/**
	 * Prüft ob ein Einheitsschritt vorhanden ist.
	 * @return false falls kein Einheitsschritt vorhanden ist.
	 */
	public boolean inputExisting() {
		return measurement.inputExisting();
	}

	/**
	 * Prüft ob die Approximation abgeschlossen ist.
	 * @return false falls die Approximation noch nicht komplett 
	 * durchgeführt wurde.
	 */
	public boolean approximated() {
		return measurement.approximated();
	}

	/**
	 * Prüft ob die Approximation noch am laufen ist.
	 * @return false falls der LaguerreSolver das Programm blockiert.
	 */
	public boolean checkRunning() {
		return measurement.checkRunning();
	}

	/**
	 * Prüft ob eine Messung geladen ist.
	 * @return false falls keine Messung geladen ist.
	 */
	public boolean measurementLoaded() {
		return measurementLoaded;
	}

	/**
	 * Notifiziert Observers.
	 */
	public void notifyObservers() {
		setChanged();
		super.notifyObservers();
	}

	//================================================================================
	// Private Methods
	//================================================================================

	public void filtMeasurement() {
		measurement.filtData(autoFilter, filterPercentage);
	}

	//================================================================================
	// Setters and Getters
	//================================================================================

	public double[] getTimeData() {
		return measurement.getTimeData();
	}

	public double[] getInputData() {
		return measurement.getInputData();

	}

	/**
	 * Gibt die Schrittantwort zurück, abhängig ob diese gefiltert
	 * angezeigt werden soll.
	 * @return Schrittantwort.
	 */
	public double[] getStepData() {
		return measurement.getStepData(showFiltered);
	}

	public double[] getApproxData() {
		return measurement.getApproxData();
	}

	public double[][] getPolesData() {
		return measurement.getPolesData();
	}

	public double[] getErrorData() {
		return measurement.getErrorData();
	}

	public void setOrder(int order) {
		measurement.setOrder(order);
	}

	public int getOrder() {
		return measurement.getOrder();
	}

	public void setK(double K) {
		measurement.setK(K);
	}

	public double getK() {
		return measurement.getK();
	}

	public void setWqp(double[][] wqp) {
		measurement.setWqp(wqp);
	}

	public double[][] getWqp() {
		return measurement.getWqp();
	}

	public void setSigma(double sigma) {
		measurement.setSigma(sigma);
	}

	public double getSigma() {
		return measurement.getSigma();
	}

	public double getMeanError() {
		return measurement.getMeanError();
	}
}
