package userinterface;

import java.awt.GridBagLayout;

import javax.swing.JPanel;

import org.jfree.data.xy.XYSeries;

import plots.Plots;
import programUtilities.MyBorderFactory;

/**
 * 
 * @author Thomas Frei
 *
 */
public class DataPanel extends JPanel {

	//================================================================================
	// Properties
	//================================================================================

	Plots plot;

	//================================================================================
	// Constructor
	//================================================================================

	public DataPanel(Plots plot) {
		super(new GridBagLayout());
		setBorder(MyBorderFactory.createMyBorder(""));
		this.plot = plot;
	}

	//================================================================================
	// Public Methods
	//================================================================================

	/**
	 * Fügt dem Plot eine Serie mit Daten hinzu.
	 * @param series Serie mit Daten.
	 */
	public void addData(XYSeries series) {
		plot.addSeries(series);
	}

	/**
	 * Löscht alle Daten der Serie vom Plot.
	 */
	public void clearData() {
		plot.clearSeries();
	}
}
