package columbus;

public class GiantSquidFactory extends MonsterFactory {
	
	int scalingFactor = 20;
	
	//concrete factory to make GiantSquids
	public Monster makeMonster(int x, int y) {
		
		return new GiantSquid(x,y,scalingFactor);
	}

}
