package coop.client;


// import java.awt.*;
import javax.swing.JButton;
import java.awt.event.*;

import coop.util.Identity;
import coop.actions.Action;
import coop.action.TestObject;

public class MyButton extends JButton {

	private Action action;
	private CoopSockJsClient client;
	private InputPanel inputPanel;
	private ButtonPanel buttonPanel;

	public MyButton(ButtonPanel buttonPanel, CoopSockJsClient client, InputPanel inputPanel) {		
//		super("My Button");	
		this.client = client;
		this.inputPanel = inputPanel;
		this.buttonPanel = buttonPanel;
		this.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	buttonPanel.processAction(action);
            	if (action.needsInput()) {
            		System.out.println("Needs more input: " + action);
            		getInput();
            	}
            	else {
            		System.out.println("Sending to client: " + action);
            		sendAction();
            	}            	
            }              
        });
	}

	public void setAction(Action action) {

		this.action = action;
		String objName = "";
		if (action.getObjects() != null && action.getObjects().size() > 0) {
			objName = action.getObjects().get(0).getName();
		}
		this.setText(action.getAction() + " " + objName);
	}

	private void getInput() {
		inputPanel.processAction(action, client);
	}

	private void sendAction() {
//		client.sendTest("This is a test");
//		client.sendUserTest("This is a test");
//		TestObject testObj = new TestObject("test", "object");
//		client.sendTestObject(testObj);
		if (action.getAction().equals("quit")) {
			client.quit();
		}
		else if (action.getAction().equals("exit")) {
			// no server message required...handled by processAction 
		}
		else {
			client.sendAction(action);
		}
	}
}