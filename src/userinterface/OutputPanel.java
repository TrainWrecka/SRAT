package userinterface;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import JFreeChart.PlotData;
import JFreeChart.Plots;
import dataProcessing.Model;

public class OutputPanel extends JPanel implements ChangeListener {

	//================================================================================
	// Properties
	//================================================================================

	JTabbedPane tabpane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
	private JPanel DefaultPanel = new JPanel(new GridBagLayout());

	private VariablePanel DefaultVariablePanel = new VariablePanel();

	private Plots ErrorPlot = new Plots("Error", "xyline", "time", "In/Out");
	private Plots StepresponsePlot = new Plots("Stepresponse", "xyline", "time", "In/Out");
	private Plots ZeroesPlot = new Plots("Poles", "scatter", "", "");

	private DataPanel StepresponsePanel = new DataPanel(StepresponsePlot);
	private DataPanel ZeroesPanel = new DataPanel(ZeroesPlot);
	private DataPanel ErrorPanel = new DataPanel(ErrorPlot);

	private JPanel TabStepresponsePanel = new JPanel(new GridBagLayout());
	private JPanel TabErrorPanel = new JPanel(new GridBagLayout());
	private JPanel TabZeroesPanel = new JPanel(new GridBagLayout());
	public Font myFont = new Font("Serif", Font.BOLD, 20);

	public Dimension ErrorPanelDimension;

	private PlotData stepData = new PlotData();
	private PlotData errorData = new PlotData();
	private PlotData polesData = new PlotData();

	//================================================================================
	// Constructor
	//================================================================================

	public OutputPanel() {
		super(new GridBagLayout());

		DefaultPanel.add(StepresponsePanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		DefaultPanel.add(ZeroesPanel, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHEAST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		DefaultPanel.add(ErrorPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.SOUTHWEST, // Wenn y ausdehnung 0.0 in 4k screen nicht symetrisch und wenn full hd dasselbe wenn y ausdehnung 1.0
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		StepresponsePanel.add(StepresponsePlot, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.BOTH, new Insets(10, 5, 5, 5), 0, 0));
		ZeroesPanel.add(ZeroesPlot, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHEAST,
				GridBagConstraints.BOTH, new Insets(10, 5, 5, 5), 0, 0));
		ErrorPanel.add(ErrorPlot, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.SOUTHWEST, // Wenn y ausdehnung 0.0 in 4k screen nicht symetrisch und wenn full hd dasselbe wenn y ausdehnung 1.0
				GridBagConstraints.BOTH, new Insets(5, 5, 10, 5), 0, 0));

		DefaultVariablePanel.setMinimumSize(StepresponsePlot.getMinimumSize());
		DefaultPanel.add(DefaultVariablePanel, new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0, GridBagConstraints.SOUTHEAST,
				GridBagConstraints.BOTH, new Insets(5, 5, 10, 5), 0, 0));

		tabpane.addTab("Default", DefaultPanel);
		tabpane.addTab("Stepresponse", TabStepresponsePanel);
		tabpane.addTab("Poles", TabZeroesPanel);
		tabpane.addTab("Error", TabErrorPanel);

		add(tabpane, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		tabpane.addChangeListener(this);
		tabpane.setBorder(MyBorderFactory.createMyBorder("Output"));
	}

	//================================================================================
	// Public Methods
	//================================================================================

	/**
	 * Bei jedem Aufruf werden alle Panels gelöscht. Falls eine Messung geladen wurde wird
	 * der Plot mit der Schrittantwort aktualisiert, mit Einheitsschritt falss vorhanden. Ausserdem
	 * werden die Variablen aktualisiert. Wenn die Schrittantwort approximiert wurde, wird die Approximation
	 * zusammen mit dem Error und den Polen geladen.
	 * @param obs the observable object.
	 * @param obj an argument passed to the notifyObservers method.
	 */
	public void update(Observable obs, Object obj) {
		Model model = (Model) obs;

		StepresponsePanel.clearData();
		ErrorPanel.clearData();
		ZeroesPanel.clearData();

		if (model.measurementLoaded()) {

			stepData.removePlotData();
			stepData.setXData(model.getTimeData());
			if (model.inputExisting()) {
				stepData.setYData(model.getInputData(), "Input");
			}

			stepData.setYData(model.getStepData(), "Step");

			DefaultVariablePanel.update(obs, obj);

			if (model.approximated()) {
				errorData.removePlotData();
				stepData.setYData(model.getApproxData(), "Apprximation");

				//renderer.setSeriesPaint(0, new Color(0, 0, 255, 0));

				errorData.setXData(model.getTimeData());
				errorData.setYData(model.getErrorData(), "Error");
				ErrorPanel.addData(errorData.getPlotData()[0]);

				polesData.removePlotData();
				polesData.setXData(model.getPolesData()[0]);
				polesData.setYData(model.getPolesData()[1], "Poles");
				ZeroesPanel.addData(polesData.getPlotData()[0]);
			}

			for (int i = 0; i < stepData.getPlotData().length; i++) {
				StepresponsePanel.addData(stepData.getPlotData()[i]);
			}

		}

	}


	/**
	 * Wenn Tab wechselt, wird entsprechendes Panel auf den neuen Tab gesetzt und vom alten entfernt.
	 */
	
	@Override
	public void stateChanged(ChangeEvent e) {
		JTabbedPane sourceTabbedPane = (JTabbedPane) e.getSource();
		int index = sourceTabbedPane.getSelectedIndex();
		System.out.println("Tab changed to: " + sourceTabbedPane.getSelectedIndex());

		switch (index) {
			case 0:
				DefaultPanel.add(StepresponsePanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

				DefaultPanel.add(ZeroesPanel, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

				DefaultPanel.add(ErrorPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

				DefaultPanel.add(DefaultVariablePanel, new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				break;
			case 1:
				TabStepresponsePanel.add(StepresponsePanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				break;
			case 2:
				TabZeroesPanel.add(ZeroesPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				break;
			case 3:
				TabErrorPanel.add(ErrorPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				break;
		}
	}
}
