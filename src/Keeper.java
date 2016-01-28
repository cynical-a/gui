import java.awt.Color;
import java.awt.Graphics;

public class Keeper extends MovingObject {
public Keeper(double x, double y, int left, int right, int top, int bottom) {
	super(x, y, left + 10, right - 10, top + 10, bottom - 10);
		// TODO Auto-generated constructor stub
	}

boolean alive = true;
public boolean stopped = false;
private int size = 20;

@Override
public void draw(Graphics g) {
	// TODO Auto-generated method stub
	g.setColor(color.YELLOW);
	g.fillRect((int)getX(),(int)getY(), size, size);;
}

@Override
public void animateOneStep() {
	
}

public int getSize() {
	return size;
}

public void setSize(int size) {
	this.size = size;
}
}
