import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.stream.Stream;
import javax.swing.JComponent;
import javax.swing.Timer;
//import java.awt.geom.Rectangle2D.Float;
//import java.awt.geom.Ellipse2D.Float;
//import java.awt.geom.Line2D;
import java.awt.geom.*;
import java.awt.geom.Rectangle2D;





/**
 * This program is a gradius game where a player controls a ship, if a ship collides with 
 * one of the numerous asteroids flying at it the game will end
 * 
 * @author Toji Nakabayashi 100274019
 * @version 2017-10-28
 */
@SuppressWarnings("serial")
public class GradiusComp extends JComponent {
	// Make a fancier space ship DONE
	// create a cannon the can shoot once every 5 seconds MADE CANNON HAVENT DONE 
	// ship gets a life bar of 3 DONE
	// every 15 seconds the speed of the asteroids by the original times the level DONE
	// introduce a astroid that travels diagonally and bounces off the boundaries
	// introduce a timer and show how long you survived at the end of the game DONE
	// asteroids explode and disapear when a ship or cannon shot hits them DONE does not have explosion animation
	// set the background to black with white dots scattered across the background?
	// create hearts that give an extra life to the ship


	private final static int GAME_TICK = 1000 / 60;
	private final static int ASTEROID_MAKE_TICK = 1000/4;

	private final static int SHIP_INIT_X = 10;
	private final static int SHIP_INIT_Y = Gradius.HEIGHT/3;
	private final static int SHIP_VEL_BASE = 2;
	private final static int SHIP_VEL_FAST = 4;

	//private Ship shipBoundary;
	private Ship ship; 
	private Ship circle;
	private Ship cannon1;
	private Ship cannon2;
	private Ship[] shipParts;
	private Timer[] gameTick;
	private Timer reload;
	Collection<Asteroid> roids;
	private boolean gameover = false;
	private Asteroid toRemove;
	private int level = 1; // level will go up every 15 sec and double the speed of asteroids
	private int time; // will display how long you survive
	
	
	//missle stuff
	private Missle missle;
	private int missleLoadTime;
	private boolean fireMissle;
	private int lives = 3;
	private boolean reloading;
	
	private final static int MISSLE_SPEED = 8;
	
	public GradiusComp() {
		// TODO
		
		gameTick = new Timer[4];
		gameTick[0] = new Timer(GAME_TICK, a -> update(a));
		gameTick[1] = new Timer(ASTEROID_MAKE_TICK, b -> makeAsteroids());		
		gameTick[2] = new Timer((int)(1000/.15), d -> level += 1);
		gameTick[3] = new Timer(1000, e -> time += 1);

		reload = new Timer(1000, c -> missleLoadTime += 1); // reload timer must be seperate from the game clocks
		addKeyListener(new ShipKeyListener());
		roids = new HashSet<>();
		start();
	}

	public void makeAsteroids() {
		AsteroidFactory.getInstance();
		Asteroid a = AsteroidFactory.getInstance().makeAsteroid(level); // Makes an asteroid
		roids.add(a); // Adds the new asteroid to the roids list
	}

	public void update(ActionEvent a) {
		requestFocusInWindow();
		
		
		Arrays.stream(shipParts).forEach(part -> part.move());
		
		if (fireMissle) {
			missle.move();
			roids.stream().forEach(roid -> { // Every update check if a ship collides with a asteroid
				if (roid.intersects(missle)) {
					toRemove = roid; // sets the toRemove asteroid to the asteroid that collides with the missle
					fireMissle = false;
				}
			});
			roids.remove(toRemove);
		}

		roids.stream().forEach(roid -> roid.move()); // evertime we update move all the asteroids
		roids.removeIf(roid -> roid.isOutOfBounds()); // test if this works
		//System.out.println(roids.size()); // test if asteroids are being removed (works)
		roids.stream().forEach(roid -> { // Every update check if a ship collides with a asteroid
			if (roid.intersects(ship)) {
				toRemove = roid;
				lives -= 1;
				if (lives == 0) { // when lives hits 0 than we end the game
					Arrays.stream(gameTick).forEach(timer -> timer.stop()); // Stop timer if asteroid collides
					// draw GAME OVER in red on the component
					gameover = true; // flag game over to draw the game over screen
				}				
			}
		});
		if (lives > 0) { // last roid will stay on the screen
			roids.remove(toRemove);
		}
		if(missleLoadTime >= 6) {
			reload.stop();
			missleLoadTime = 0; 
			reloading = false; 
		}
		repaint();
	}

	public void paintComponent(Graphics g) {
		requestFocusInWindow();
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(
			RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		paintComponent(g2);

	}
	private void paintComponent(Graphics2D g2) {
		requestFocusInWindow();
		g2.setColor(Color.BLACK);
		// Clear canvas to whie
		g2.fillRect(0,0, getWidth(), getHeight());	

		Arrays.stream(shipParts).forEach(part -> part.draw(g2));
		
		if (fireMissle) {				
			missle.draw(g2); // draw missle			
		}
		g2.setColor(Color.GREEN);
		Rectangle2D fillLifeBar = new Rectangle2D.Float(10, 10, 50 * lives, 60);
		g2.fill(fillLifeBar);
		roids.stream().forEach(roid -> roid.draw(g2));
		g2.setColor(Color.RED);
		g2.setFont(new Font("TimesRoman", Font.PLAIN, 25));
		g2.drawString("Lives left: " +  lives, 10, 50);
		
		Rectangle2D lifeBar = new Rectangle2D.Float( 10, 10, 150, 60);
		g2.setColor(Color.BLACK);
		g2.draw(lifeBar);
		

		g2.setFont(new Font("TimesRoman", Font.PLAIN, 50));
		g2.setColor(Color.RED);
		if(gameover) {
			g2.drawString("GAME OVER", getWidth()/2 - 150, getHeight()/2); // Draw a game over message if gameover is true
			g2.drawString( "YOU SURVIVED FOR " + time + " SECONDS", getWidth()/2 - 400, getHeight()/2 + 40);
		}
		g2.setFont(new Font("TimesRoman", Font.PLAIN, 25));
		if (reloading) { 			
			g2.drawString("RELOADING...", 10, 110);
		} else {
			g2.drawString("LOADED", 10, 110);
		}
		g2.drawString("TIME " + time, 10, getHeight() - 15);
		
	}

	public void start() {
		// TODO		
		//AsteroidFactory.getInstance()
		shipParts = new Ship[4]; // create the ship
		shipParts[0] = (ship = new ShipImpl(new Polygon(new int[] {SHIP_INIT_X, SHIP_INIT_X + 20, SHIP_INIT_X}
		,new int[]{SHIP_INIT_Y, SHIP_INIT_Y + 10, SHIP_INIT_Y + 20}, 3), 
		SHIP_INIT_X, SHIP_INIT_Y, getBounds())); // create a new ship

		shipParts[1] = (circle = new ShipImpl(new Ellipse2D.Float(SHIP_INIT_X + 2, SHIP_INIT_Y + 4 , 10, 10),
		new Rectangle2D.Float(2, 4, getWidth() - 10, getHeight() - 10), Color.BLUE, Color.BLACK));
		
		shipParts[2] = (cannon1 = new ShipImpl(new Rectangle2D.Float(SHIP_INIT_X, SHIP_INIT_Y, SHIP_INIT_X + 10, 1),
		new Rectangle2D.Float(0,0, getWidth(), getHeight() - 20), Color.WHITE, Color.WHITE));
		shipParts[3] = (cannon2 = new ShipImpl(new Rectangle2D.Float(SHIP_INIT_X, SHIP_INIT_Y + 20, SHIP_INIT_X + 10, 1), 
		new Rectangle2D.Float(0, 20, getWidth(), getHeight() - 20), Color.WHITE, Color.WHITE));

		AsteroidFactory.getInstance().setStartBounds(new Rectangle(getWidth(), 0, getWidth(), getHeight())); // area from which roids can spawn
		AsteroidFactory.getInstance().setMoveBounds(new Rectangle(0, 0, getWidth(), getHeight())); // roid can move within the component
		
		
		Arrays.stream(gameTick).forEach(timer -> timer.start());
		makeAsteroids();
	}

	private class ShipKeyListener extends KeyAdapter {

		private boolean up;
		private boolean down;
		private boolean left;
		private boolean right;
		private boolean pressed2;
	


		@Override
		public void keyPressed(KeyEvent e) {
			// TODO
			setVelocity(e);
		}
		@Override
		public void keyReleased(KeyEvent e) {
			// TODO
			setVelocity(e);
		}

		public void setVelocity(KeyEvent e) {
			boolean pressed = false;
			if (e.getID() == KeyEvent.KEY_PRESSED) {
				pressed = true;
				setDirection(e, pressed);
			} else if (e.getID() == KeyEvent.KEY_RELEASED) {
				pressed = false;
				setDirection(e, pressed);
			}
			if (e.isShiftDown()) {
				moveShip(SHIP_VEL_FAST);
			} else {
				moveShip(SHIP_VEL_BASE);
			}
			if (e.getID() == KeyEvent.KEY_PRESSED) {
				if (e.getKeyCode() == KeyEvent.VK_F) {
					if (missleLoadTime == 0 || missleLoadTime >= 6) { // if the missleloadtime is 0 where it is loaded
						// or if its after 6 which means i has finished loading fire a missle
						missleLoadTime = 1; // set the load timer to 1 so you can only fire one missle within the 0 - 6 second period
						// without this you could fire as many missles as you want in the 0 - 1 second time frame
						reloading = true;
						reload.start(); // start the reload count
						
						fireMissle = true;
						// now misslefire is true
						missle = new MissleImpl(ship.getX(), ship.getY(), getBounds()); // creates a missle inside the ship
						shoot(); // shoot the missle
					}
				}

				//fireMissle = false; // After a missle is fired set back to false and start the reload timer
			}
			
		}

		public void shoot() {
			missle.setVelocity(MISSLE_SPEED, 0);
		}

		public void moveShip(int vel) {
			int vx = 0; // velocity x and y
			int vy = 0;
			if (up && !down) {
				vy = -vel;
			} else if (down && !up) {
				vy = vel;
			}
			if (right && !left) {
				vx = vel;
			} else if (left && !right) {
				vx = -vel;
			}
			final int fvx = vx; // so that i can use a stream
			final int fvy = vy;
			//ship.setVelocity((float)vx, (float)vy);
			//circle.setVelocity((float)vx, (float)vy);
			//cannon1.setVelocity((float)vx, (float)vy);
			//cannon2.setVelocity((float)vx, (float)vy);
			Arrays.stream(shipParts).forEach(part -> part.setVelocity(fvx,fvy));

		}

		public void setDirection(KeyEvent e, boolean pressed) {
			switch (e.getKeyCode()) {
				case KeyEvent.VK_W:
				case KeyEvent.VK_UP:
				case KeyEvent.VK_KP_UP:
					up = pressed;
					break;
				case KeyEvent.VK_S:
				case KeyEvent.VK_DOWN:
				case KeyEvent.VK_KP_DOWN:
					down = pressed;
					break;
				case KeyEvent.VK_A:
				case KeyEvent.VK_LEFT:
				case KeyEvent.VK_KP_LEFT:
					left = pressed;
					break;
				case KeyEvent.VK_D:
				case KeyEvent.VK_RIGHT:
				case KeyEvent.VK_KP_RIGHT:
					right = pressed;
					break;
				
			}
		}

		/**public void missleKey(KeyEvent e, boolean p2) {
			switch (e.getKeyCode()) {
				case KeyEvent.VK_F:
					fireMissle = p2;
					break;
			}
		}*/
	};
}
