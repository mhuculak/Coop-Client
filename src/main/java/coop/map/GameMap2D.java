package coop.map;

import coop.util.HttpClient;
import coop.player.Player;
import coop.map.Position;
import coop.client.GameClient;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import javax.swing.Timer;

import java.io.*;

import java.net.URI;

public class GameMap2D extends JComponent {

    private BufferedImage img;
    private GameClient gameClient;
    private int emptyHeight = 500;
    private int emptyWidth = 500;
    private int maxHeight = 1000;
    private int maxWidth = 1000;
    private Player player;
    private Timer timer;
    private int timerInterval = 100; // msec

    public GameMap2D() {
    }

    public GameMap2D(GameClient gameClient, String mapURI, Player player) {
    	this.player = player; 
        this.gameClient = gameClient;
    	try {
    		HttpClient httpClient = new HttpClient();
    		httpClient.doGet(mapURI);
    		img = ImageIO.read(httpClient.getInputStream());
    	} 
    	catch (IOException e) {
            e.printStackTrace();
        }

        timer = new Timer(timerInterval, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                boolean playerMoved = false;
                if (player.getDestination() != null) {
//                    System.out.println("Moving player to " + player.getDestination());               
                    playerMoved = player.move(timerInterval);
                }
                if (playerMoved) {
                    gameClient.sendMove(player.getPosition());
                }
                else {                    
                    timer.stop();
                }

                repaint();
            }
        });
        
    }
/*
    public GameMap2D(String path) {
         try {
            img = ImageIO.read(new File(path));
         } catch (IOException e) {
            e.printStackTrace();
         }
    }
*/

    public void startTimer() {
        timer.start();
    }

    public Dimension getPreferredSize() {
         if (img == null) {
            return new Dimension(500,500);
         } else {
            int preferedHeight = (img.getHeight() > maxHeight) ? maxHeight : img.getHeight();
            int preferedWidth = (img.getWidth() > maxWidth) ? maxWidth : img.getWidth();
            return new Dimension(preferedWidth, preferedHeight);
         }
    }

    public int getHeight() {
    	if (img == null) {
    		return emptyHeight;
    	}
    	return img.getHeight();
    }

    public int getWidth() {
    	if (img == null) {
    		return emptyWidth;
    	}
    	return img.getWidth();
    }

    public void paint(Graphics g) {
        g.drawImage(img, 0, 0, null);
        if (player != null) {
            player.draw(g);
        }        
    }
}