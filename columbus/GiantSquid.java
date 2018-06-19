package columbus;

import java.net.URL;
import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GiantSquid implements Monster{

	int x;
	int y;
	int scalingFactor;
	OceanMap oceanMap;
	Random random = new Random();
	
	ImageView squidImage;
	
	//constructor 
	public GiantSquid(int x, int y, int scalingFactor) {
		this.x = x;
		this.y = y;
		URL url = getClass().getResource("/images/squid.png");
		Image squid = new Image(url.toString(), scalingFactor, scalingFactor, false, false);
		squidImage = new ImageView(squid);
		setPositionX(x);
		setPositionY(y);
		this.scalingFactor = scalingFactor;
		oceanMap = OceanMap.getInstance();
	}
	
	public ImageView getImage() {
		return squidImage;
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
	public void setPositionX(int x) {
		squidImage.setX(x * scalingFactor);
	}
	
	@Override
	public void setPositionY(int y) {
		squidImage.setY(y * scalingFactor);
	}
	
	//moves monster left to right and moves them to the left edge
	//when they reach the right edge
	@Override
	public void move() {
		int xMove = getX() + 1;
		int yMove = getY() + random.nextInt(3)-1;
		if(isValidMove(xMove, yMove)) {
			setX(xMove);
			setY(yMove);
		}
		if(xMove >= oceanMap.dimension)
			setX(0);
		
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
