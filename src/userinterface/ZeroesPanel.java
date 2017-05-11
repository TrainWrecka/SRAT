package userinterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

import java.awt.GridBagLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayoutInfo;
import java.awt.Insets;
import java.awt.Toolkit;

import JFreeChart.ZeroesPlot;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.apache.commons.math3.complex.Complex;

import javax.swing.JLabel;



public class ZeroesPanel extends JPanel implements ActionListener{
	
	
	public ZeroesPlot Zeroesplot=new ZeroesPlot("null");
	
	
	private JLabel lbk =new JLabel("K:");
    private JLabel lbwp = new JLabel("\u03C9p:");
    private JLabel lbqp = new JLabel("qp:");
    private JLabel lbSigma=new JLabel("\u03C3:");
    private JLabel lbError=new JLabel("Error:");
	private Font myFont= new Font("Serif", Font.BOLD, 20);

	public ZeroesPanel(){
		super(new GridBagLayout());
		setFont(myFont);
		
//		Dimension screensize = new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/10, Toolkit.getDefaultToolkit().getScreenSize().height/7);
//		setMaximumSize(screensize);
//		Zeroesplot.setMaximumSize(screensize);
		
		add(Zeroesplot, new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,  
				new Insets(0, 0, 0, 0), 0, 0));

		
		
		
		
		setBorder(MyBorderFactory.createMyBorder("Zeroes"));
		
		
		
	}
	
	
	public void update(Observable obs, Object obj) {}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}




