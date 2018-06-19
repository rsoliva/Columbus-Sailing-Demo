package columbus;

import java.awt.Point;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class SmartPursuit implements PursuitStrategy {
	
	OceanMap oceanMap;
	int[][] map;
	
	public SmartPursuit() {
		//Gets an instance of the ocean map and grabs the map from it
		oceanMap = OceanMap.getInstance();
		map = oceanMap.getMap();
	}
	
	String smartPersuit(Point start, Point end) {
		//Creates a hashmap to store best moves per point, and queue to check each point, and a visited array
		HashMap<Point, String> firstMove = new HashMap<Point, String>();
		Queue<Point> queue = new LinkedList<Point>();
		boolean[][] visited = new boolean[oceanMap.getDimension()][oceanMap.getDimension()];
		
		//Sets the initial point as visited
		visited[start.x][start.y] = true;
		
		for(Point neighbor : getNeighbors(start, firstMove)) {
			//If the target ship is directly next to the pirate ship it will move directly that spot
			if(neighbor.equals(end))
				return firstMove.get(neighbor);
			
			//Adds the neighbors of the initial point to the queue and sets them to visited
			queue.add(neighbor);
			visited[neighbor.x][neighbor.y] = true;
		}
		
		while(!queue.isEmpty()) {
			Point point = null;
			
			//Tries to take a point from the queue
			point = queue.remove();
			
			//If the current point is the end point, break the loop
			if(point.equals(end)) break;
			
			//Adds each valid neighbor of the given point to the queue and sets it to visitied
			for(Point neighbor : getNeighbors(point, firstMove)) {
				if(visited[neighbor.x][neighbor.y] == false) {
					queue.add(neighbor);
					visited[neighbor.x][neighbor.y] = true;
				}
			}
		}
		
		//Returns the first move the ship should make to get to the player
		return firstMove.get(end);
	}
	
	public LinkedList<Point> getNeighbors(Point point, HashMap<Point, String> firstMove) {
		//Creates a linked list of points for each neighbor of the point passed in
		LinkedList<Point> neighbors = new LinkedList<Point>();
		
		//Creates points for the square above, below, left, and right of the point
		Point up, down, left, right;
		
		//Assigns these points to actual values
		up = new Point(point.x, point.y - 1);
		down = new Point(point.x, point.y + 1);
		left = new Point(point.x - 1, point.y);
		right = new Point(point.x + 1, point.y);
		
		//Checks if the above point is valid, and adds it if it is
		if(checkValidSquare(up)) {
			neighbors.add(up);

			if(firstMove.get(point) == null)
				firstMove.put(up, "UP");
			else
				firstMove.put(up, firstMove.get(point));
		}
		
		//Checks if the right point is valid, and adds it if it is
		if(checkValidSquare(right)) {
			neighbors.add(right);

			if(firstMove.get(point) == null)
				firstMove.put(right, "RIGHT");
			else
				firstMove.put(right, firstMove.get(point));
		}
		
		//Checks if the below point is valid, and adds it if it is
		if(checkValidSquare(down)) {
			neighbors.add(down);

			if(firstMove.get(point) == null)
				firstMove.put(down, "DOWN");
			else
				firstMove.put(down, firstMove.get(point));
		}
		
		//Checks if the left point is valid, and adds it if it is
		if(checkValidSquare(left)) {
			neighbors.add(left);

			if(firstMove.get(point) == null)
				firstMove.put(left, "LEFT");
			else
				firstMove.put(left, firstMove.get(point));
		}

		//Returns the list of all valid neighbors
		return neighbors;
	}
	
	public boolean checkValidSquare(Point point) {
		//Gets the dimensions of the map
		int dimension = oceanMap.getDimension() - 1;
		
		//Returns false if either of the dimensions are less than zero or greater than the maps dimensions
		if(point.x > dimension || point.x < 0) return false;
		if(point.y > dimension || point.y < 0) return false;
		
		//Returns false if the square of the map is not ocean
		if(map[point.x][point.y] > 0 && map[point.x][point.y] < 4) return false;
		
		return true;
	}

	public String decideMove(Point location, Point otherLocation) {
		//Finds the best move using the smart pursuit strategy
		String move = smartPersuit(location, otherLocation);
		
		//Returns the best move found
		return move;	
	}
}
