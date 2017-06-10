package userinterface;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import org.jfree.data.xy.XYSeries;

import plots.Plots;
import programUtilites.MyBorderFactory;

public class DataPanel extends JPanel{

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
	 * F�gt dem Plot eine Serie mit Daten hinzu.
	 * @param series Serie mit Daten.
	 */
	public void addData(XYSeries series) {
		plot.addSeries(series);
	}
	
	/**
	 * L�scht alle Daten der Serie vom Plot.
	 */
	public void clearData(){
		plot.clearSeries();
	}
}
