package columbus;

import java.awt.Point;

public class SimplePursuit implements PursuitStrategy {
	
	OceanMap oceanMap = OceanMap.getInstance();
	int[][] map = oceanMap.getMap();
	
	String simplePersuit(Point location, Point otherLocation) {
		String move = null;
		
		//Decides where to move just based on location in comparison to the player ship
		if(location.x < otherLocation.x && map[location.x + 1][location.y] != 1 && map[location.x + 1][location.y] != 3)
			move = "RIGHT";
		else if(location.x > otherLocation.x && map[location.x - 1][location.y] != 1 && map[location.x - 1][location.y] != 3)
			move = "LEFT";
		else if(location.y < otherLocation.y && map[location.x][location.y + 1] != 1 && map[location.x][location.y + 1] != 3)
			move = "DOWN";
		else if(location.y > otherLocation.y && map[location.x][location.y - 1] != 1 && map[location.x][location.y - 1] != 3)
			move = "UP";
		else
			move = "NONE";
		
		return move;
	}
		
	public String decideMove(Point location, Point otherLocation) {
		//Gets the move from the pursuit strategy
		return simplePersuit(location, otherLocation);
	}
}
