import java.awt.Rectangle;
import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.util.Random;
import java.awt.geom.Rectangle2D;

public class AsteroidFactory {

	private final static int ASTEROID_SIZE_MIN = 10;
	private final static int ASTEROID_SIZE_MAX = 40;
	private final static int ASTEROID_VEL_MIN = 1;
	private final static int ASTEROID_VEL_MAX = 4;

	private final static AsteroidFactory instance = new AsteroidFactory();

	private static Rectangle startBounds;
	private static Rectangle moveBounds;

	private AsteroidFactory() {}

	public static AsteroidFactory getInstance() {
		return instance;
	}

	public void setStartBounds(Rectangle r) {
		startBounds = r;
	}
	public void setMoveBounds(Rectangle r) {
		// TODO
		moveBounds = r; 
	}

	public Asteroid makeAsteroid(int level) {
		// TODO
		int flag = random(0,1); // 50 50 chance of either a round asteroid or a rectangle asteroid appearing
		if (flag == 0){
			Asteroid a = new AsteroidImpl(startBounds.width, random(0, startBounds.height), 
			random(ASTEROID_SIZE_MIN, ASTEROID_SIZE_MAX), random(ASTEROID_SIZE_MIN, ASTEROID_SIZE_MAX), 
		   (float) random(ASTEROID_VEL_MIN * level, ASTEROID_VEL_MAX *level));
		   return a;
		} else {
			Asteroid a = new AsteroidImpl(startBounds.width, random(0, startBounds.height), 
			random(ASTEROID_SIZE_MIN, ASTEROID_SIZE_MAX), random(ASTEROID_SIZE_MIN, ASTEROID_SIZE_MAX), 
		   (float) random(ASTEROID_VEL_MIN * level, ASTEROID_VEL_MAX *level), 1);
		   return a;
		}
		
		
	}

	private static int random(int min, int max) {
		if(max-min == 0) { return min; }
		Random rand = java.util.concurrent.ThreadLocalRandom.current();
		return min + rand.nextInt(max + 1);
	}

	private static class AsteroidImpl extends SpriteImpl implements Asteroid {
		private final static Color COLOR = Color.getHSBColor(156, 93, 82); // brown

		public AsteroidImpl(int x, int y, int w, int h, float v) {
			// TODO
			super(new Ellipse2D.Float(x,y,w,h), moveBounds, false, COLOR);
			setVelocity(-v, 0); // sets the velocity (negative becuase it will go from the right of the screen to the left)
		}

		public AsteroidImpl(int x, int y, int w, int h, float v, int flag) {
			super(new Rectangle2D.Float(x, y,w, h ), moveBounds, false, COLOR);
			setVelocity(-v, 0);
		}
	}
}
