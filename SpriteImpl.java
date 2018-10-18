import java.awt.*;
import java.awt.geom.*;

public abstract class SpriteImpl implements Sprite {

	// drawing
	private Shape shape;
	private final Color border;
	private final Color fill;

	// movement
	private float dx, dy;
	private final Rectangle2D bounds;
	private final boolean isBoundsEnforced;

	protected SpriteImpl(Shape shape, Rectangle2D bounds, boolean boundsEnforced, Color border, Color fill) {
		this.shape = shape;
		this.bounds = bounds;
		this.isBoundsEnforced = boundsEnforced;
		this.border = border;
		this.fill = fill;
		
	}
	protected SpriteImpl(Shape shape, Rectangle2D bounds, boolean boundsEnforced, Color fill) {
		this(shape, bounds, boundsEnforced, null, fill);
	}

	public Shape getShape() {
		return shape;
	}

	public void setVelocity(float dx, float dy) {
		this.dx = dx;
		this.dy = dy;
	}

	public int getX() {
		return shape.getBounds().x; // gets the x position of the ship
	}
	public int getY() {
		return shape.getBounds().y; // gets the y position of the ship
	}

	public void move() {
		// TODO
		
		
		if(isBoundsEnforced) { // if the bounds are enforced move the shape
			shape = AffineTransform.getTranslateInstance(this.dx, this.dy).createTransformedShape(shape);
			if (!isInBounds()) { // if the bounds are enforced but the shape is not in bounds move the shape back to where it was
				shape = AffineTransform.getTranslateInstance(-this.dx, -this.dy).createTransformedShape(shape);
			}
		} else { // move the shape
			shape = AffineTransform.getTranslateInstance(this.dx, this.dy).createTransformedShape(shape);
		}

		
	}

	public boolean isOutOfBounds() {
		// TODO

		return !shape.intersects(bounds); // If the shape intersects the bounds return false
	}
	public boolean isInBounds() {	
		return bounds.contains(shape.getBounds2D());	
		//return isInBounds(bounds, shape);
	}
	private static boolean isInBounds(Rectangle2D bounds, Shape s) {
		// TODO
		if (bounds.contains(s.getBounds2D())) { // If the shape is contained inside the bounds return true
			return true;
		}
		return false;
	}

	public void draw(Graphics2D g2) {
		// TODO
		// Draw a shape
		g2.setColor(fill);
		g2.fill(shape);
		g2.setColor(border);
		g2.draw(shape);
	}

	// check if the ship intersects a asteroid
	public boolean intersects(Sprite other) {
		return intersects(other.getShape());
	}
	private boolean intersects(Shape other) {
		// TODO
		return shape.intersects(other.getBounds())
			&& intersects(new Area(shape), new Area(other));
	}
	private static boolean intersects(Area a, Area b) {
		// TODO
		a.intersect(b); // Returns the intersected area of a and b to a
		return !a.isEmpty();
	}
}
