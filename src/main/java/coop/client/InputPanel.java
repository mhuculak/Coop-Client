package coop.client;

import coop.util.Identity;
import coop.actions.Action;
import coop.player.PlayerOptions;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JSplitPane;
import javax.swing.JButton;
import javax.swing.JLabel;

public class InputPanel extends JPanel {

	private JLabel label1;
	private JLabel label2;
	private JLabel passwordLabel;
	private JTextField text1;
	private JTextField text2;
	private JPasswordField passwordText;
	private JPanel input1;
	private JPanel input2;
	private JPanel passwordPanel;
	private JPanel buttonPanel;
//	private	JSplitPane	splitPaneV;
//	private	JSplitPane	splitPaneV2;
	private JButton submitButton;

	public InputPanel() {
//		this.setLayout( new GridLayout(1,5) );	   
		this.setLayout( new FlowLayout() );
		label1 = new JLabel();
		label2 = new JLabel();
		passwordLabel = new JLabel("Password: ");
		text1 = new JTextField(10);
		text2 = new JTextField(10);
		passwordText = new JPasswordField(10);
		submitButton = new JButton("Submit");
		input1 = new JPanel();
		input1.setLayout(new FlowLayout());
		input2 = new JPanel();
		input2.setLayout(new FlowLayout());
		passwordPanel = new JPanel();
		passwordPanel.setLayout(new FlowLayout());
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		input1.add(label1);
		input1.add(text1);
		input2.add(label2);
		input2.add(text2);
		passwordPanel.add(passwordLabel);
		passwordPanel.add(passwordText);
		buttonPanel.add(submitButton);
//		splitPaneV = new JSplitPane( JSplitPane.VERTICAL_SPLIT );
//		splitPaneV2 = new JSplitPane( JSplitPane.VERTICAL_SPLIT );
	}

	public void processAction(Action action, CoopSockJsClient client) {
		this.removeAll();		
		this.add(input1);
		this.add(buttonPanel);
		label1.setText(action.getInputText());		
//		splitPaneV.setLeftComponent(input1);
//		splitPaneV.setRightComponent(buttonPanel);
//		this.add(splitPaneV);
		submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	action.addInput(text1.getText());
            	System.out.println("Sending to client: " + action);
            	client.sendAction(action);
            	clear();
            }              
        });
	}

	public void clear() {
		this.removeAll();
	}

	public void login(GameClient gameClient, CoopSockJsClient sockJsClient, String loginUrl, PlayerOptions initialOptions) {
		label1.setText("User Name:");		
//		splitPaneV.setLeftComponent(input1);
//		splitPaneV.setRightComponent(passwordPanel);
//		this.add(splitPaneV);
//		splitPaneV2.setLeftComponent(splitPaneV);
//		splitPaneV2.setRightComponent(buttonPanel);
//		this.add(splitPaneV2);
		this.removeAll();
		this.add(input1);
		this.add(passwordPanel);
		this.add(buttonPanel);
		submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	Identity i = getLoginIdentity();
            	sockJsClient.login(i, loginUrl);
            	clear();            	
            	sockJsClient.connect(gameClient);
            	System.out.println("login call back showing initial options...");
            	gameClient.showOptions(initialOptions);
            }              
        });
	}

	private Identity getLoginIdentity() {
//		return new Identity(text1.getText(), passwordText.getPassword());
		char[] dummy = new char[0];
		return new Identity("mhuculak", dummy);
	}
}