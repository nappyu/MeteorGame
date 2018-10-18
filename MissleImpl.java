import java.awt.Color;
import java.awt.Polygon;
import java.awt.geom.*;

public class MissleImpl extends SpriteImpl implements Missle {
    public MissleImpl(int x, int y, Rectangle2D bounds) {
        super(new Rectangle2D.Float(x, y, 10, 10), bounds, false, Color.WHITE, Color.WHITE);
    }
}