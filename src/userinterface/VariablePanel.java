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


import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;

public class VariablePanel extends JPanel implements ActionListener{
	
	private JLabel lbk =new JLabel("K:");
    private JLabel lbwp = new JLabel("\u03C9p:");
    private JLabel lbqp = new JLabel("qp:");
    private JLabel lbSigma=new JLabel("\u03C3:");
    private JLabel lbError=new JLabel("Error:");
    
//    private Font myfont= new Font("myFont",1,100);
    
    private int Height;
    private Dimension SizeLabels;
	
	public VariablePanel(){
		super(new GridBagLayout());
				
//		setFont(myfont);
		add(lbk, new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.VERTICAL,  
				new Insets(10, 0, 30, 0), 0, 0));
		add(lbwp,new GridBagConstraints( 0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.VERTICAL,  
				new Insets(30, 0, 30, 0), 0, 0));
		add(lbqp, new GridBagConstraints( 0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.VERTICAL,  
				new Insets(30, 0, 30, 0), 0, 0));
		add(lbSigma, new GridBagConstraints( 0, 3, 1, 1, 1.0, 1.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.VERTICAL,  
				new Insets(30, 0, 30, 0), 0, 0));
		add(lbError, new GridBagConstraints( 0, 4, 1, 1, 1.0, 1.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.VERTICAL,  
				new Insets(30, 0, 10, 0), 0, 0));	
		
//		add(lbk, new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.VERTICAL,  
//				new Insets(0, 0, 0, 0), 0, 0));
//		add(lbwp,new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.VERTICAL,  
//				new Insets(0, 0, 0, 0), 0, 0));
//		add(lbqp, new GridBagConstraints( 0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.VERTICAL,  
//				new Insets(0, 0, 0, 0), 0, 0));
//		add(lbSigma, new GridBagConstraints( 0, 3, 1, 1, 1.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.VERTICAL,  
//				new Insets(0, 0, 0, 0), 0, 0));
//		add(lbError, new GridBagConstraints( 0, 4, 1, 1, 1.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.VERTICAL,  
//				new Insets(0, 0, 0, 0), 0, 0));	
		
		
//		SizeLabels=new Dimension(getWidth(), getHeight());
//				
//		lbk.setPreferredSize(SizeLabels);
//		lbwp.setPreferredSize(SizeLabels);
//		lbqp.setPreferredSize(SizeLabels);
//		lbSigma.setPreferredSize(SizeLabels);
//		lbError.setPreferredSize(SizeLabels);
//		this.
		
//		add(lbk, new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,  
//				new Insets(0, 0, 0, 0), 0, 0));
//		add(lbwp,new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,  
//				new Insets(0, 0, 0, 0), 0, 0));
//		add(lbqp, new GridBagConstraints( 0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,  
//				new Insets(0, 0, 0, 0), 0, 0));
//		add(lbSigma, new GridBagConstraints( 0, 3, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,  
//				new Insets(0, 0, 0, 0), 0, 0));
//		add(lbError, new GridBagConstraints( 0, 4, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,  
//				new Insets(0, 0, 0, 0), 0, 0));	
		
		setBorder(MyBorderFactory.createMyBorder("Variables"));
	}
	
	
	public void update(Observable obs, Object obj) {}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
