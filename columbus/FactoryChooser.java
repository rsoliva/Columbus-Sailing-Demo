package columbus;

public class FactoryChooser {
	
	//method to return specific factory type
	public static MonsterFactory getFactory(String factory) {
		if(factory.equals("Shark"))
			return new SharkFactory();
		else if(factory.equals("GiantSquid"))
			return new GiantSquidFactory();
		else return null;
	}

}
