 package userinterface;
 import java.awt.Dimension;
 import java.awt.GridBagConstraints;
 import java.awt.GridBagLayout;
 import java.awt.Insets;
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
 import java.io.File;
 import java.util.Observable;
 import java.util.Observer;
 import java.awt.Color;
 import java.awt.GridBagConstraints;
 import java.awt.GridBagLayout;
 import java.awt.Insets;
 import java.util.List;
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
 import java.io.File;
 import java.io.FileNotFoundException;
 import java.io.FileReader;
 import java.io.IOException;
 import java.util.Observable;
 import java.util.Observer;
 import javax.swing.JButton;
 import javax.swing.JFileChooser;
 import javax.swing.JPanel;
 import javax.swing.filechooser.FileNameExtensionFilter;

 import org.jfree.chart.ChartFactory;
 import org.jfree.chart.ChartPanel;
 import org.jfree.chart.JFreeChart;
 import org.jfree.chart.plot.XYPlot;
 import org.jfree.data.xy.XYSeries;
 import org.jfree.data.xy.XYSeriesCollection;

 import userinterface.MenuBar;

 import model.Model;
 import javax.swing.JButton;
 import javax.swing.JFileChooser;
 import javax.swing.JPanel;
 import com.opencsv.CSVReader;
  
 import javafx.stage.FileChooser;
  
  public class View extends JPanel implements Observer, ActionListener{
  	
  	
  	private InputPanel inputPanel=new InputPanel();
  	private OutputPanel outputPanel = new OutputPanel();
	private Controller controller;
	private File file;
//	public SettingsPanel settingsPanel=new SettingsPanel();

	double[][] measurementData;

	
  
  	public View(Controller controller) {
  		super(new GridBagLayout());
  		this.controller = controller;
  		inputPanel.setController(controller);
  		add(inputPanel,new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.VERTICAL,
  				new Insets(0, 0, 10, 10), 0, 0));
 		add(outputPanel,new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
  				new Insets(0, 0, 10, 10), 0, 0));
 		
  	}
  
  	public void update(Observable obs, Object obj) {
		outputPanel.update(obs, obj);
		inputPanel.update(obs, obj);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub	
	}
  	
 
  
 }
