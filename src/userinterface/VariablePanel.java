package userinterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
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
import javax.swing.JTextField;
import javax.swing.JLabel;

public class VariablePanel extends JPanel implements ActionListener{
	
	private JLabel lbK =new JLabel("K:");
    private JLabel lbSigma=new JLabel("\u03C3:");
    private JLabel lbError=new JLabel("Error:");
    private JLabel[] lbwp = new JLabel[5];
    private JLabel[] lbqp = new JLabel[5];
    
    
    
    public JLabel lbKValues =new JLabel();
    public JLabel lbSigmaValues = new JLabel();
    public JLabel lbErrorValues = new JLabel();
    public JLabel[] lbwpValues = new JLabel[5];
    public JLabel[] lbqpValues = new JLabel[5];
    
    private int wpPlacement = 1;
	private int qpPlacement = 2;

    

	
	public VariablePanel(){
		super(new GridBagLayout());
		setBorder(MyBorderFactory.createMyBorder("Variables"));
		
		add(lbK, new GridBagConstraints( 0, 0, 1, 1, 0.0, 1.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.VERTICAL,  
				new Insets(0, 0, 0, 0), 0, 0));
		
		for(int i=0; i<lbwp.length;i++){
			
			lbwp[i]= new JLabel("\u03C9p"+(i+1)+":");
			lbqp[i]= new JLabel("qp"+(i+1)+":");
			lbwpValues[i]= new JLabel();
			lbqpValues[i]= new JLabel();
			
			add(lbwp[i], new GridBagConstraints( 0, (wpPlacement), 1, 1, 0.0, 1.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.VERTICAL,  
					new Insets(0, 0, 0, 0), 0, 0));
			add(lbqp[i], new GridBagConstraints( 0, (qpPlacement), 1, 1, 0.0, 1.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.VERTICAL,  
					new Insets(0, 0, 0, 0), 0, 0));
			
			add(lbwpValues[i], new GridBagConstraints( 1, (wpPlacement), 1, 1, 1.0, 1.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.VERTICAL,  
					new Insets(0, 50, 0, 0), 0, 0));
			add(lbqpValues[i], new GridBagConstraints( 1, (qpPlacement), 1, 1, 1.0, 1.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.VERTICAL,  
					new Insets(0, 50, 0, 0), 0, 0));
		
			wpPlacement=wpPlacement+2;
			qpPlacement=qpPlacement+2;
		}
		
		add(lbSigma, new GridBagConstraints( 0, 11, 1, 1, 0.0, 1.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.VERTICAL,  
				new Insets(0, 0, 0, 0), 0, 0));
		add(lbError, new GridBagConstraints( 0, 12, 1, 1, 0.0, 1.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.VERTICAL,  
				new Insets(0, 0, 0, 0), 0, 0));
		
		
		add(lbKValues, new GridBagConstraints( 1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.VERTICAL,  
				new Insets(10, 50, 0, 0), 0, 0));
		
		
		
		
		add(lbSigmaValues, new GridBagConstraints( 1, 11, 1, 1, 1.0, 1.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.VERTICAL,  
				new Insets(0, 50, 0, 0), 0, 0));
		add(lbErrorValues, new GridBagConstraints( 1, 12, 1, 1, 1.0, 1.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.VERTICAL,  
				new Insets(0, 50, 10, 0), 0, 0));	
		
	}
	
	public void setValues(Object[] values){
		String K = (String) values[0];
		String[] wp = (String[]) values[1];
		String[] qp = (String[]) values[2];
		String sigma = (String) values[3];
		String meanError = (String) values[4];
		
		
		
		for(int i = 0; i < lbwp.length; i++){
			lbwpValues[i].setText(wp[i]);
			lbqpValues[i].setText(qp[i]);
		}
		 
		lbKValues.setText(K);
		lbSigmaValues.setText(sigma);
		lbErrorValues.setText(meanError);
	}

	
	
	public void update(Observable obs, Object obj) {}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
