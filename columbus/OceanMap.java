package columbus;

import java.awt.Point;
import java.util.Random;

public class OceanMap {
	//Creates an OceanMap instance for the singleton pattern
	static OceanMap oceanMap;
	
	//Creates a 2 dimensional integer array to store the map
	static int[][] map;
	
	//Integer variables for map dimensions, normal island count, and pirate island count
	int dimension;
	int islandCount;
	int pirateIslandCount;
	int paddleCount;
	int boozeCount;
	int rocketCount;
	int whirlCount;
	
	
	
	//Creates a random object to generate island locations
	Random rand = new Random();
	
	private OceanMap() {
		//Sets the dimensions and island counts
		this.dimension = 30;
		this.islandCount = 30;
		this.pirateIslandCount = 2;
		this.paddleCount = 1;
		this.boozeCount = 1;
		this.rocketCount = 1;
		this.whirlCount = 2;
		
		//Creates the 2 dimensional array with the dimensions specified
		map = new int[dimension][dimension];
		
		//Creates local variables for current island count and island coordinates
		spawnIslands("island", islandCount);
		spawnIslands("pirateIsland", pirateIslandCount);
		spawnIslands("treasureIsland", 1);
		spawnIslands("paddle", paddleCount);
		spawnIslands("rocket", rocketCount);
		spawnIslands("booze", boozeCount);
		//spawnIslands("whirlpool", whirlCount);
			
	}
	
	public void spawnIslands(String type, int Count) {
		int num, x, y;
		int currentIslands = 0;
		
		if(type.equals("island")) 
			num = 1;
		else if(type.equals("pirateIsland"))
			num = 2;
		else if(type.equals("treasureIsland"))
			num = 3;
		else if(type.equals("paddle"))
			num = 4;
		else if(type.equals("rocket"))
			num = 5;
		else 
			num = 6;
		
		while(currentIslands < Count) {
			if(type.equals("treasureIsland")) 
				x = rand.nextInt(3) + dimension -3;
			else 
				x = rand.nextInt(dimension);
			
			y = rand.nextInt(dimension);
				
			if(map[x][y] == 0) {
				map[x][y] = num;
				
				currentIslands++;
			}
		}
	}
	public static OceanMap getInstance() {
		//If there isn't a global OceanMap already, create one
		if(oceanMap == null) {
			oceanMap = new OceanMap();
		}
		
		//Return the global OceanMap
		return oceanMap;
	}
	
	public static void resetMap() {
		//Generates a new OceanMap for the global (resets the map)
		oceanMap = new OceanMap();
	}
	
	public int[][] getMap() {
		//Returns the 2 dimensional map array
		return map;
	}
	
	public int getDimension() {
		return dimension;
	}
	
	
	public static String whatIs(int x, int y) {
		//This method returns an integer signifying the object at the given "coordinates"
		//Used for JUnit testing
		if (map[x][y] == (0)) return "ocean";
		else if (map[x][y] == 1) return "island";
		else if (map[x][y] == 2) return "pirateIsland";
		else if (map[x][y] == 3) return "paddle";
		else if (map[x][y] == 4) return "treasueIsland";
		else if (map[x][y] == 5) return "rocket";
		else if (map[x][y] == 6) return "booze";
		else return "";
	}
	
	public static String whatIs(Point p) {
		//This method returns an integer signifying the object at the given "coordinates"
		//Used for JUnit testing
		int x = p.x;
		int y = p.y;
		if (map[x][y] == (0)) return "ocean";
		else if (map[x][y] == 1) return "island";
		else if (map[x][y] == 2) return "pirateIsland";
		else if (map[x][y] == 3) return "paddle";
		else if (map[x][y] == 4) return "treasueIsland";
		else if (map[x][y] == 5) return "rocket";
		else if (map[x][y] == 6) return "booze";
		else return "";
	}
	
}
