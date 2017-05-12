package userinterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Observable;

import java.awt.GridBagLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayoutInfo;
import java.awt.Insets;
import java.awt.Toolkit;

import JFreeChart.ErrorPlot;
import JFreeChart.Plots;
import JFreeChart.StepResponsePlot;
import JFreeChart.ZeroesPlot;
import model.Model;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class OutputPanel extends JPanel implements ActionListener, ChangeListener, MouseWheelListener {

	JTabbedPane tabpane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
	
	private JPanel DefaultPanel = new JPanel(new GridBagLayout());
	
	
	public ErrorPlot Errorplot = new ErrorPlot("ERROR");

	
	private JPanel DefaultVariablePanel = new VariablePanel();

	private DataPanel StepresponsePanel = new DataPanel();
	private DataPanel ZeroesPanel = new DataPanel();
	private DataPanel ErrorPanel = new DataPanel();
	
	
	private Plots ErrorPlot = new Plots("error", "xyline", "time" , "In/Out");
	private Plots StepresponsePlot = new Plots("stepresponse", "xyline", "time" , "In/Out");
	private Plots ZeroesPlot = new Plots ("zereos", "scatter", "", "");
	 
	
	
	private JPanel TabStepresponsePanel=new JPanel(new GridBagLayout());
	private JPanel TabErrorPanel=new JPanel(new GridBagLayout());
	private JPanel TabZeroesPanel=new JPanel(new GridBagLayout());
	public Font myFont= new Font("Serif", Font.BOLD, 20);
	
	public Dimension ErrorPanelDimension;
	
	
	public OutputPanel() {
		super(new GridBagLayout());	
//		setFont(myFont);

		
		
		DefaultPanel.add(StepresponsePanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		DefaultPanel.add(ZeroesPanel, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHEAST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		DefaultPanel.add(ErrorPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.SOUTHWEST,	// Wenn y ausdehnung 0.0 in 4k screen nicht symetrisch und wenn full hd dasselbe wenn y ausdehnung 1.0
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		StepresponsePanel.add(StepresponsePlot, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.BOTH, new Insets(10, 5, 5, 5), 0, 0));
		ZeroesPanel.add(ZeroesPlot, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHEAST,
				GridBagConstraints.BOTH, new Insets(10, 5, 5, 5), 0, 0));
		ErrorPanel.add(ErrorPlot, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.SOUTHWEST,	// Wenn y ausdehnung 0.0 in 4k screen nicht symetrisch und wenn full hd dasselbe wenn y ausdehnung 1.0
				GridBagConstraints.BOTH, new Insets(5, 5, 10, 5), 0, 0));
		
		
		
//		DefaultVariablePanel.setMinimumSize(StepresponsePanel.StepResponseplot.getMinimumSize());
		DefaultPanel.add(DefaultVariablePanel, new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0, GridBagConstraints.SOUTHEAST,
				GridBagConstraints.BOTH, new Insets(5, 5, 10, 5), 0, 0));
		
		
		
		
		
		
		
		tabpane.addTab("Default", DefaultPanel);
		tabpane.addTab("StepresponsePanel", TabStepresponsePanel);
		tabpane.addTab("Zeroes", TabZeroesPanel);
		tabpane.addTab("Error", TabErrorPanel);
		
		
		
		
	
		add(tabpane, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		
		tabpane.addChangeListener(this);
		StepresponsePanel.addMouseWheelListener(this);
		ZeroesPanel.addMouseWheelListener(this);
		ErrorPanel.addMouseWheelListener(this);
	}
	
	

	public void update(Observable obs, Object obj) {
		Model model = (Model) obs;
		

		StepresponsePanel.clearStepresponseData(StepresponsePlot);
		StepresponsePanel.addStepresponseData(model.getStepresponseData()[0], StepresponsePlot);
		StepresponsePanel.addStepresponseData(model.getStepresponseData()[1], StepresponsePlot);
		if(model.inputExisting()){
			StepresponsePanel.addStepresponseData(model.getStepresponseData()[2], StepresponsePlot);
		}
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}


	@Override
	public void stateChanged(ChangeEvent e) {
		JTabbedPane sourceTabbedPane = (JTabbedPane) e.getSource();
        int index = sourceTabbedPane.getSelectedIndex();
        System.out.println("Tab changed to: " + sourceTabbedPane.getSelectedIndex());
        
        switch(index){
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
        	TabStepresponsePanel.add(StepresponsePanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
        			GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
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






	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		
		
		if(e.getSource()==StepresponsePanel){
			if(e.getWheelRotation()<0){
				Plots.zoomChartAxis(StepresponsePlot.stepresponseChartPanel, true);
			}else{
				Plots.zoomChartAxis(StepresponsePlot.stepresponseChartPanel, false);				
			}
		}
		
		if(e.getSource()==ZeroesPanel){
			if(e.getWheelRotation()<0){
				Plots.zoomChartAxis(ZeroesPlot.zeroesChartPanel, true);
			}else{
				Plots.zoomChartAxis(ZeroesPlot.zeroesChartPanel, false);
			}
		}

		if(e.getSource()==ErrorPanel){
			if(e.getWheelRotation()<0){
				Plots.zoomChartAxis(ErrorPlot.errorChartPanel, true);
			}else{
				Plots.zoomChartAxis(ErrorPlot.errorChartPanel, false);
			}
		}
	}



	
}

