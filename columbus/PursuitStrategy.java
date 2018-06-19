package columbus;

import java.awt.Point;

public interface PursuitStrategy {
	public String decideMove(Point location, Point otherLocation);
}
