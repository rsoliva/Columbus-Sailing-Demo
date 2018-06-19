package columbus;

import javafx.scene.image.ImageView;

//This is the interface for the monsters
public interface Monster {	
	
	public ImageView getImage();
	
	public void setX(int x);
	
	void setY(int y);
	
	int getX();
	
	int getY();
	
	public void setPositionX(int x);
	
	public void setPositionY(int y);
	
	public void move();
}
