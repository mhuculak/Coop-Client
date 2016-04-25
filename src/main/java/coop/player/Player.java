package coop.player;

import coop.map.Position;
import coop.util.HttpClient;

import java.awt.*;
// import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.io.*;

public class Player {
	private String name;
	private Position position;
	private Position destination;  // left click
	private Position selection;    // right click (or is drag better?)
	private BufferedImage image;
	private double speed; // pixels per msec
	private double minDist;
	private Position imageOffset; // offset of feet from top left

	public Player(String imageUrl) {
		speed = 0.1;
		minDist = 1.0;
		try {
    		HttpClient httpClient = new HttpClient();
    		httpClient.doGet(imageUrl);
    		image = ImageIO.read(httpClient.getInputStream());
    	} 
    	catch (IOException e) {
            e.printStackTrace();
        }
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public void setDestination(Position destination) {
		this.destination = destination;
	}

	public void setSelection(Position selection) {
		this.selection = selection;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setImageOffset(Position offset) {
		this.imageOffset = offset;
	}

	public BufferedImage getImage() {
		return image;
	}

	public Position getPosition() {
		return position;
	}

	public Position getDisplayPosition() {
		return Position.subtract(imageOffset, position);
	}

	public Position getDestination() {
		return destination;
	}

	public Position getSelection() {
		return selection;
	}

	public String getName() {
		return name;
	}

	public boolean move(int timeInterval) {
		double distToDest = Position.distance(position, destination);
		if (distToDest < minDist) {
			return false;
		}
		double distToMove = speed * timeInterval;
		if (distToMove > distToDest) {
			distToMove = distToDest;
		}
		Position toMove = Position.subtract(position, destination);
		toMove.scale(distToMove/distToDest);
		position = Position.add(position, toMove);
//		System.out.println("Moving player to " + position);
		return true;
	}

	public void draw(Graphics g) {
		if (position != null) {
			Position displayPos = Position.subtract(imageOffset, position);		
            g.drawImage(image, (int)displayPos.getX(), (int)displayPos.getY(), null);                
        }
        if (destination != null) {
        	int len = 5;
        	Graphics2D g2 = (Graphics2D) g;
        	g2.setColor(Color.red);
        	g2.drawLine((int)destination.getX()-len, (int)destination.getY(), (int)destination.getX()+len, (int)destination.getY());        	
        	g2.drawLine((int)destination.getX(), (int)destination.getY()-len, (int)destination.getX(), (int)destination.getY()+len);        	
        }
	}
}