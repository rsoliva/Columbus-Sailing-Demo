package columbus;

import java.net.URL;
import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Shark implements Monster {

	int x;
	int y;
	int scalingFactor;
	OceanMap oceanMap;
	Random random  = new Random();
	
	ImageView sharkImage;
	
	//Constructor
	public Shark(int x, int y, int scalingFactor) {
		this.x = x;
		this.y = y;
		URL url = getClass().getResource("/images/shark.png");
		Image shark = new Image(url.toString(), scalingFactor, scalingFactor, false, false);
		sharkImage = new ImageView(shark);
		setPositionX(x);
		setPositionY(y);
		this.scalingFactor = scalingFactor;
		oceanMap = OceanMap.getInstance();
	}
	
	public ImageView getImage() {
		return sharkImage;
	}

	@Override
	public void setX(int x) {
		this.x = x;
		setPositionX(x);
	}

	@Override
	public void setY(int y) {
		this.y = y;
		setPositionY(y);
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}
	
	@Override
	public void setPositionX(int x){
		sharkImage.setX(x * scalingFactor);
	}
	
	@Override
	public void setPositionY(int y){
		sharkImage.setY(y * scalingFactor);
	}
	
	//moves the shark from top to bottom and moves them back
	//to the top when they reach the bottom
	@Override
	public void move() {
		
		int xMove = getX() + random.nextInt(3)-1;
		int yMove = getY() + 1;
		if(isValidMove(xMove, yMove)) {
			setX(xMove);
			setY(yMove);
		}
		if(yMove >= oceanMap.dimension)
			setY(0);
		
		
	}
	//checks to see if the coordinate is an ocean
	public boolean isValidMove(int x, int y) {
		if(x >= 0 && x < 30)
			if(y>= 0 && y < 30)
				if(oceanMap.getMap()[x][y] == 0)
					return true;
		
		return false;
	}

}
