package userinterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

import java.awt.GridBagLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayoutInfo;
import java.awt.Insets;
import java.awt.Toolkit;

import JFreeChart.ErrorPlot;
//import JFreeChart.Plots;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;

public class ErrorPanel extends JPanel implements ActionListener{
	
	public ErrorPlot Errorplot = new ErrorPlot("Error");
	
	
	private JLabel lbk =new JLabel("K:");
    private JLabel lbwp = new JLabel("\u03C9p:");
    private JLabel lbqp = new JLabel("qp:");
    private JLabel lbSigma=new JLabel("\u03C3:");
    private JLabel lbError=new JLabel("Error:");

    
	
	public ErrorPanel(){
		super(new GridBagLayout());
		
//		Dimension screensize = new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/10, Toolkit.getDefaultToolkit().getScreenSize().height/7);
//		setMaximumSize(screensize);
//		Errorplot.setMaximumSize(screensize);
		
		add(Errorplot, new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,  
				new Insets(0, 0, 0, 0), 0, 0));
		
		setBorder(MyBorderFactory.createMyBorder("Error"));
	}
	
	
	public void update(Observable obs, Object obj) {}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
