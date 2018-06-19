package columbus;

import java.awt.Point;

public interface ShipInterface {
	//Provides methods for moving each direction
	public void moveNorth(int velocity);
	public void moveEast(int velocity);
	public void moveSouth(int velocity);
	public void moveWest(int velocity);
	public Point getShipLocation();
	public String getType();
	public void stopGame();
	public String getAbilities();
	public int getVelocity();
}
