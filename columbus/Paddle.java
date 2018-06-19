package columbus;

import java.awt.Point;

public class Paddle extends PowerUps{
	ShipInterface ability;
	int velocity;
	public Paddle(ShipInterface ability) {
		this.ability = ability;
	}
	
	public String getAbilities() {
		return ability.getAbilities() + ", Paddle";
	}

	@Override
	public void moveNorth(int velocity) {
		ability.moveNorth(velocity);
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveEast(int velocity) {
		ability.moveEast(velocity);
		//ability.moveEast();
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveSouth(int velocity) {
		ability.moveSouth(velocity);
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveWest(int velocity) {
		ability.moveWest(velocity);
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
		return ability.getVelocity()+1;
	}
	

}
