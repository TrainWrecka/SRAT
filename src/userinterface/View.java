package userinterface;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;


public class View extends JPanel implements Observer, ActionListener {
	
	//================================================================================
	// Properties
	//================================================================================

	public InputPanel inputPanel = new InputPanel();
	private OutputPanel outputPanel = new OutputPanel();
	double[][] measurementData;

	//================================================================================
	// Constructor
	//================================================================================
	
	public View(Controller controller) {
		super(new GridBagLayout());
		controller.setView(this);
		inputPanel.setController(controller);
		add(inputPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0, GridBagConstraints.FIRST_LINE_START,
				GridBagConstraints.VERTICAL, new Insets(0, 0, 10, 10), 0, 0));
		add(outputPanel, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 10, 10), 0, 0));

	}

	//================================================================================
	// Public Methods
	//================================================================================
	
	/**
	 * Updated das Input- und Output Panel.
	 */
	public void update(Observable obs, Object obj) {
		outputPanel.update(obs, obj);
		inputPanel.update(obs, obj);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub	
	}

}
