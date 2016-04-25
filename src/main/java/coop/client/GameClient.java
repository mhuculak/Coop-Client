package coop.client;

import coop.util.*;
import coop.player.PlayerOptions;
import coop.player.Player;
import coop.player.PlayerPosition;
import coop.actions.Action;
import coop.map.GameMap2D;
import coop.map.Position;
import coop.actions.ActionObject;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class GameClient extends JFrame {

	private	JSplitPane	splitPaneV;
	private	JSplitPane	splitPaneH1;
	private	JSplitPane	splitPaneH2;
	private JPanel topPanel;
	private	ButtonPanel	buttonPanel;
	private	InputPanel inputPanel;
	private	JPanel	mapPanel;
	private MessagePanel  messagePanel;
	private int buttonHeight;
	private int buttonWidth;
	private int messageWidth;	
	private int maxButtons;
	private String mapPath;
	private GameMap2D map2D;	
	private String gameUrl = "http://192.168.1.10:8081/coop-game/begin";
	private String startUrl = "http://192.168.1.10:8081/coop-game/start";
	private String loginUrl = "http://192.168.1.10:8081/coop-game/login";
	private String mapUrl = "http://192.168.1.10:8081/coop-game/resources/map.jpg";
	private String playerUrl = "http://192.168.1.10:8081/coop-game/resources/man.gif";
	private Position playerImageOffset = new Position(20,40); // offset of feet from top left
	private CoopSockJsClient sockJsClient; // handles connection with game server
	private Player player;

	public GameClient() {
		buttonHeight = 30;
		buttonWidth = 200;
		messageWidth = 200;
		
		player = new Player(playerUrl);
		player.setImageOffset(playerImageOffset);
 		map2D = new GameMap2D(this, mapUrl, player); 
 		
 		sockJsClient = new CoopSockJsClient(gameUrl);
 		maxButtons = map2D.getHeight() / buttonHeight;

 		buttonPanel = new ButtonPanel(this, sockJsClient, inputPanel, maxButtons);
		buttonPanel.setPreferredSize(new Dimension( buttonWidth, map2D.getHeight()));
		initDisplay();

 		HttpClient httpClient = new HttpClient();
    	httpClient.doGet(startUrl);		
    	PlayerOptions initalOptions = new PlayerOptions("Press to join the game", "");
	    inputPanel.login(this, sockJsClient, loginUrl, initalOptions);	   
	}

	public void showOptions(PlayerOptions playerOptions) {
/*		
		Action quitAction = new Action("quit");
*/		
		player.setPosition(playerOptions.getPosition());
		mapPanel.updateUI();	
		System.out.println("showOptions: ");
		System.out.println("message: " + playerOptions.getMessage());
		System.out.println("position: " + playerOptions.getPosition());
		int i=0;
		buttonPanel.clear();
		for ( Action action : playerOptions.getActions()) {
			System.out.println("use button " + i + " for: " + action);
			if (action.getAction().equals("Play As")) {
				action.getObjects().add(new ActionObject(sockJsClient.getUserName()));
			}
			buttonPanel.setAction(action, i);
			i++;
		}
	}

	public void processAction(Action action) {
		System.out.println("Processing action " + action.getAction());		
		if (action.getAction().equals("Play As")) {
			player.setName(action.getObjects().get(0).getName());
			sockJsClient.setPlayerName(player.getName());
			System.out.println("Player name set to " + player.getName());
		}		
		else if (action.getAction().equals("exit")) {
			sockJsClient.disconnect();
			System.exit(0);
		}
		
	}

	public void sendMove(Position pos) {
		PlayerPosition playerPos = new PlayerPosition(player.getName(), pos);
		sockJsClient.sendMove(playerPos);
	}

	public void setDest(Position dest) {
		player.setDestination(dest);
		mapPanel.updateUI();
	}

	public void initDisplay() {
		setTitle("Coop Client");
		setBackground( Color.gray );

 		topPanel = new JPanel();
		topPanel.setLayout( new BorderLayout() );
		getContentPane().add( topPanel );

		createMapPanel();

		inputPanel = new InputPanel();		
		inputPanel.setPreferredSize( new Dimension( map2D.getWidth() + buttonWidth + messageWidth, 50 ) );
	
		messagePanel = new MessagePanel();				
		messagePanel.setPreferredSize(new Dimension( messageWidth, map2D.getHeight()));

		splitPaneV = new JSplitPane( JSplitPane.VERTICAL_SPLIT );
//		splitPaneV.setDividerLocation(0.99);

		splitPaneH1 = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT );
		splitPaneH1.setLeftComponent( buttonPanel );
		splitPaneH1.setRightComponent( mapPanel );

		splitPaneH2 = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT );
		splitPaneH2.setLeftComponent( splitPaneH1 );		
		splitPaneH2.setRightComponent( messagePanel );

		splitPaneV.setLeftComponent( inputPanel );
		splitPaneV.setRightComponent( splitPaneH2 );

		topPanel.add( splitPaneV, BorderLayout.CENTER );

//		pack();

	}

	public void createMapPanel()
	{
		mapPanel = new JPanel();
		mapPanel.setLayout(new FlowLayout());		
		mapPanel.add(map2D);
		mapPanel.addMouseListener(new MouseAdapter() { 
        	public void mousePressed(MouseEvent me) {
        		System.out.println("Got mouse event " +  me.getX() + " " + me.getY());
        		Position mousePos = new Position( me.getX(), me.getY());
        		player.setDestination(mousePos);
        		map2D.startTimer();
        		PlayerPosition playerPos = new PlayerPosition(player.getName(), mousePos);
        		sockJsClient.sendDestination(playerPos);
        	}
        });
//		mapPanel.setPreferredSize(map.getPreferredSize());
	}	

	public static void main( String args[]) {
		GameClient gameClient = new GameClient();
		gameClient.pack();
		gameClient.setVisible(true);
	}
	
}