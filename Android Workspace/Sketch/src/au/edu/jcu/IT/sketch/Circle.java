package au.edu.jcu.IT.sketch;


public class Circle {
	private float x, y;
	private int radius;
	private int colour;
	
	public Circle(float x, float y,int radius, int colour) {
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.colour = colour;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public int getRadius() {
		return radius;
	}
	
	public int getColour() {
		return colour;
	}
}
