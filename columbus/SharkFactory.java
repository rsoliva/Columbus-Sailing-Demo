package columbus;

public class SharkFactory extends MonsterFactory{

	int scalingFactor = 20;
	
	//concrete factory to make Sharks
	public Monster makeMonster(int x, int y) {
		
		return new Shark(x,y,scalingFactor);
	}

}
