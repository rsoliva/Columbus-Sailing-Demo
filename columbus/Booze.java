package columbus;

import java.awt.Point;

public class Booze implements ShipInterface{
	ShipInterface ability;
	int velocity;
	public Booze(ShipInterface ability) {
		this.ability = ability;
	}

	@Override
	public String getAbilities() {
		return ability.getAbilities() + ", booze";
	}

	@Override
	public void moveNorth(int velocity) {
		ability.moveSouth(velocity);
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveEast(int velocity) {
		ability.moveWest(velocity);
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveSouth(int velocity) {
		ability.moveNorth(velocity);
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveWest(int velocity) {
		ability.moveEast(velocity);
		// TODO Auto-generated method stub
		
	}

	@Override
	public Point getShipLocation() {
		// TODO Auto-generated method stub
		return ability.getShipLocation();
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return ability.getType();
	}

	@Override
	public void stopGame() {
		ability.stopGame();
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getVelocity() {
		// TODO Auto-generated method stub
		return ability.getVelocity();
	}
	
	

}
