package coop.client;

import java.awt.*;
import javax.swing.*;

public class MessagePanel extends JPanel {
	public MessagePanel() {
		this.setLayout( new BorderLayout() );			
		this.add( new JLabel( "Messages:" ), BorderLayout.NORTH );
		this.add( new JTextArea(), BorderLayout.CENTER );
	}
}