package coop.client;

import coop.actions.Action;

import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JButton;

import java.util.*;

public class ButtonPanel extends JPanel {
	
	private MyButton[] buttons;	
	private GameClient gameClient;
//	private List<JButton> buttons;

	public ButtonPanel(GameClient gameClient, CoopSockJsClient sockJsClient, InputPanel inputPanel, int maxButtons) {			
		this.setLayout( new GridLayout(maxButtons, 1) );		
		this.gameClient = gameClient;
		buttons = new MyButton[maxButtons];
//		buttons = new ArrayList<JButton>();
		for ( int i=0 ; i<maxButtons ; i++) {
			buttons[i] = new MyButton(this, sockJsClient, inputPanel);
//			JButton button = new JButton("Button " + i);
//			buttons.add(button);
//			System.out.println("Adding button " + button.getText());
//			this.add(button);
			this.add(buttons[i]);
		}	
	}

	public void setAction(Action action, int index) {
		buttons[index].setAction(action);
	}

	public void clear() {
		Action nullAction = new Action("");
		for ( int i=0 ; i<buttons.length ; i++) {
			buttons[i].setAction(nullAction);
		}		
	}	

	public void processAction(Action action) {		
		gameClient.processAction(action);
	}

}