import java.awt.Color;
import java.awt.Polygon;
import java.awt.geom.Rectangle2D;
import java.awt.Shape;
public class ShipImpl  extends SpriteImpl implements Ship {

	private final static Color FILL = Color.GREEN;
	private final static Color BORDER = Color.BLACK;

	private final static int HEIGHT = 20;
	private final static int WIDTH = HEIGHT;

	

	public ShipImpl(Shape shape, int x, int y, Rectangle2D moveBounds) {
		// TODO
		
		super(shape , moveBounds, true, BORDER, FILL);
		
		// new Polygon(new int[] {x, x + WIDTH, x},new int[]{y, y + HEIGHT/2, y + HEIGHT}, 3)
		
	}

	public ShipImpl(Shape shape, Rectangle2D moveBounds, Color c, Color f) {
		super(shape, moveBounds, true, f, c);
	}

	
}
