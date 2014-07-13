package au.edu.jcu.IT.sketch;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MyView extends View {
	private Paint paint;
	private float x, y;
	private int radius;
	private int colour;
	private ArrayList<Circle> circles;

	public MyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint = new Paint();
		circles = new ArrayList<Circle>();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		x = event.getX();
		y = event.getY();
		radius = MainActivity.getRadius();
		colour = MainActivity.getColour();
		String message = String.format("myview x: %f y: %f Radius: %d", x, y, radius);
		System.out.println(message);
		circles.add(new Circle(x,y,radius,colour));
		
		
		
		invalidate(); // Force a redraw, calls the OnDraw()
		
		return true;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		for (Circle circle : circles) {
			paint.setColor(circle.getColour());
			canvas.drawCircle(circle.getX(), circle.getY(), circle.getRadius(), paint);
		}
		
		
	}

}
