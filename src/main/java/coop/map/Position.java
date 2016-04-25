package coop.map;

public class Position {
	private double x;
	private double y;

	public Position() {

	}

	public Position(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getX() {
		return x;		
	}

	public double getY() {
		return y;
	}

	public String toString() {
		return x + " " + y;
	}

	public void scale(double factor) {
		x = x*factor;
		y = y*factor;
	}

	public static double distance(Position p1, Position p2) {
		double dx = p2.getX() - p1.getX();
		double dy = p2.getY() - p1.getY();
		return Math.sqrt(dx*dx + dy*dy);
	}

	public static Position subtract(Position p1, Position p2) {
		double dx = p2.getX() - p1.getX();
		double dy = p2.getY() - p1.getY();
		return new Position(dx,dy);
	}

	public static Position add(Position p1, Position p2) {
		return new Position(p2.getX() + p1.getX(), p2.getY() + p1.getY());
	}
}