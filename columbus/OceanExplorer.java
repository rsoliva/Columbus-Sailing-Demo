package columbus;

//import java.io.File;
import java.net.URL;
import java.util.LinkedList;
//import java.util.Optional;
import java.util.Optional;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
//import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;


import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
//import javafx.scene.control.Button;
//import javafx.scene.control.Dialog;
//import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class OceanExplorer extends Application{

	int scale = 20;
	public static int dimensions;
	Image shipImage, pirateImage, IslandImage, columbusImage;
	ImageView shipImageView, paddleImageView, rocketImageView, boozeImageView, whirlImageView;
	OceanMap oceanMap;
	int[][] map;
	Scene scene1, scene2;
	AnchorPane root;
	ShipInterface ship;	
	LinkedList<ShipInterface> pirates = new LinkedList<ShipInterface>();
	LinkedList<ImageView> pirateImages = new LinkedList<ImageView>();
	LinkedList<Rectangle> healthBar = new LinkedList<Rectangle>();
	MonsterSpawner monsterSpawner;
	Thread monstersThread;
	Stage menu;
	private GameMenu gamemenu;
	boolean playing = true;
	boolean hard = true;
	int damage = 0;
	
	@Override
	public void start(Stage oceanStage) throws Exception {
		//Gets an instance of the ocean map, gets its dimensions, and gets the map from it
		menu = oceanStage;
		Pane root1 = new Pane();
		
		
		//creates Menu background image
		Image colimg = new Image("/images/columbusship.jpg");
		ImageView colimgview = new ImageView(colimg);
		gamemenu = new GameMenu();
				
		//Menu dimensions
		colimgview.setFitWidth(800);
		colimgview.setFitHeight(600);
				
		//calls title class, creates title for menu
		Title title = new Title("C O L U M B U S    S A I L S    T H E    D E E P   B L U E ");
		title.setTranslateX(10);
		title.setTranslateY(20);
		
		
		//adds all menu items
		root1.getChildren().addAll(colimgview, gamemenu, title);
				
		//created menu scene
		scene1 = new Scene(root1);
		menu.setScene(scene1);
		menu.setTitle("Columbus Sails the Ocean Blue");
		menu.show();
		
		
		oceanMap = OceanMap.getInstance();
		dimensions = oceanMap.getDimension();
		map = oceanMap.getMap();
		
		//Creates a ship for the player
		ship = new PlayerShip();
		shipImageView = createShipImage(ship);
		
		//Creates a pane, scene, and draws the map
		//currently this is done while the menu is open.
		root = new AnchorPane();
		scene2 = new Scene(root, scale * dimensions, (scale * dimensions) + (scale * 2));
		drawMap();
		root.getChildren().add(shipImageView);
	
		Button difficulty = new Button("Switch Difficulty to Easy");
		difficulty.setMinHeight(2 * scale);
		difficulty.setMinWidth((dimensions * scale) / 2);
		difficulty.setLayoutX((dimensions * scale) / 2);
		difficulty.setLayoutY(dimensions * scale);
		root.getChildren().add(difficulty);
		
		difficulty.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				changeDifficulty(difficulty);
			}
		});
		
		//Create the health bar
		for(int i = 1; i <= 3; i++) {
			Rectangle rect = new Rectangle();
			rect.setWidth((scale * dimensions) / 6);
			rect.setHeight(scale * 2);
			rect.setX(((scale * dimensions) / 2) - (i * rect.getWidth()));
			rect.setY(scale * dimensions);
			rect.setFill(Color.FORESTGREEN);
			rect.setStroke(Color.DARKGREEN);
			rect.setStrokeWidth(1);
			root.getChildren().add(rect);
			healthBar.add(rect);
		}
		
		//Spawns the monsters and starts the thread
		monsterSpawner = new MonsterSpawner(20, this);
		monsterSpawner.addToPane(root.getChildren());
		
		//Thread now starts on Press of menu button play
		monstersThread = new Thread(monsterSpawner);
	   // monstersThread.start();
	    
		//Start listening for user input and moving the boat
		startSailing();
	}
	
	public void changeDifficulty(Button button) {
		//Switches difficulty
		hard = !hard;
		
		//Sets each pirate to use either smart or simple pursuit based on the hard variable
		for(ShipInterface p : pirates) {
			((PirateShip) p).setStrategy(hard ? new SmartPursuit() : new SimplePursuit());
		}
		
		//Sets the button text
		button.setText(hard ? "Set Difficulty to Easy" : "Set Difficulty to Hard");
	}
	
	public void startSailing() {
		if(playing) {
			//Creates a new key event handler to move the player
			scene2.setOnKeyPressed(new EventHandler<KeyEvent>() {
				public void handle(KeyEvent ke) {
					switch(ke.getCode()) {
					case D:
					case RIGHT:
						ship.moveEast(ship.getVelocity());
						break;
					case A:	
					case LEFT:
						ship.moveWest(ship.getVelocity());
						break;
					case W:
					case UP:
						ship.moveNorth(ship.getVelocity());
						break;
					case S:	
					case DOWN:
						ship.moveSouth(ship.getVelocity());
						break;
					case Q:
						//to avoid accidental quitting, player must confirm action
						Alert quitAlert = new Alert(AlertType.CONFIRMATION);
						URL plank = getClass().getResource("/images/plank.png");
						Image plankImage = new Image(plank.toString(), 100, 100, false, false);
						ImageView plankImageView = new ImageView(plankImage);
						quitAlert.setHeaderText("Walk the plank?");
						quitAlert.setContentText("Are you sure you want to quit?");
						quitAlert.setGraphic(plankImageView);
						ButtonType ok = ButtonType.OK;
						Optional<ButtonType> result = quitAlert.showAndWait();
						if (result.isPresent() && result.get() == ok) {
							System.exit(0);
						}
						
						
					default:
						break;
					}
					
					//updates ship with powerups
					checkPowerUp();
					//Checks if the ship should take damage from a monster
					checkDamage();
					//Updates the locations of all ships
					updateShips();
				}
			});
		}
	}
	
	public void drawMap() {
		//Iterates through every square on the map
		for(int x = 0; x < dimensions; x++) {
			for(int y = 0; y < dimensions; y++) {
				//Creates a rectangle for a square on the map
				Rectangle rect = new Rectangle(x * scale, y * scale, scale, scale);
				
				//Sets the rectangle to be turquoise with a pale turquoise stroke
				rect.setStroke(Color.TURQUOISE);
				rect.setFill(Color.PALETURQUOISE);
				
				//Adds a blue square to the pane and pushes it to the back so ships display over them
				root.getChildren().add(rect);
				rect.toBack();
				
				//Draws an island of the desired type
				if(map[x][y] == 1) {
					drawIsland(x, y, "normal");
				} else if(map[x][y] == 2) {
					drawIsland(x, y, "pirate");
					createPirate(x, y);
				} else if(map[x][y] == 3) {
					drawIsland(x, y, "treasure");
				} else if(map[x][y] == 4) {
					drawIsland(x, y, "paddle");
				} else if(map[x][y] == 5) {
					drawIsland(x,y, "Rocket");
				}else if(map[x][y] == 6) {
					drawIsland(x,y,"booze");
				}
				
			}
		}	
	}
	
	public void drawIsland(int x, int y, String type) {
		//Creates a string called fileName to store the name of the island image
		String fileName = null;
		
		//Decides which island image to use based on the type variable
		switch(type) {
		case("normal"):
			fileName = "/images/island.jpg";
			break;
		case("pirate"):
			fileName = "/images/pirateIsland.png";
			break;
		case("treasure"):
			fileName = "/images/treasureIsland.png";
			break;
		case("paddle"):
			fileName = "/images/paddle.png";
			break;
		case("Rocket"):
			fileName = "/images/rocket.png";
			break;
		case("booze"):
			fileName = "/images/booze.png";
			break;
		}
		
		//Creates an Image and ImageView based on the file name
		URL url = getClass().getResource(fileName);
		Image island = new Image(url.toString(), scale, scale, false, false);
		ImageView islandImageView = new ImageView(island);
		
		//Sets the image view to the location passed in
		islandImageView.setX(x * scale);
		islandImageView.setY(y * scale);

		if(fileName == "/images/paddle.png") {
			paddleImageView = islandImageView;
		} else if(fileName == "/images/rocket.png") {
			rocketImageView = islandImageView;
		} else if(fileName == "/images/booze.png") {
			boozeImageView = islandImageView;
		} 

		//Adds the image view to the pane
		root.getChildren().add(islandImageView);
	}
	
	public void createPirate(int x, int y) {
		//Creates a new ship and image view for the new pirate
		ShipInterface pirateShip = new PirateShip(x, y);
		ImageView pirateShipImageView = createShipImage(pirateShip);
		
		//Adds each new pirate ship to the list of observers for the player ship
		((PlayerShip) ship).addObserver((PirateShip) pirateShip);
		
		//Adds the ships image view to the pane
		root.getChildren().add(pirateShipImageView);
		
		//Adds both the ship and the image view to their lists
		pirates.add(pirateShip);
		pirateImages.add(pirateShipImageView);
	}
	
	public ImageView createShipImage(ShipInterface ship) {
		//Gets the ship type and creates a string for the file name
		String type = ship.getType();
		String fileName = null;
		
		//Decides which island image to use based on the type variable
		switch(type) {
		case("player"):
			fileName = "/images/ship.png";
			break;
		case("pirate"):
			fileName = "/images/pirateShip.png";
			break;
		}
		
		//Creates an image and image view with the given file name
		URL url = getClass().getResource(fileName);
		Image shipImage = new Image(url.toString(), scale, scale, false, false);
		ImageView shipImageView = new ImageView(shipImage);
		
		//Sets the image views location to the location of the ship
		shipImageView.setX(ship.getShipLocation().x * scale);
		shipImageView.setY(ship.getShipLocation().y * scale);
		
		//Returns the ship image view
		return shipImageView;
	}
	
	public void updateShips() {
		//Updates the image view for the player ship
		shipImageView.setX(ship.getShipLocation().x * scale);
		shipImageView.setY(ship.getShipLocation().y * scale);
		
		//TODO This is the win condition but it currently only prints to the console
		if(checkWin()) {
			System.out.println("you win");
			stopPlaying();
			Alert alert = new Alert(AlertType.INFORMATION);
			URL url = getClass().getResource("/images/treasureIsland.png");
			Image treasure = new Image(url.toString(), 100, 100, false, false);
			ImageView TreasureIsland = new ImageView(treasure);
			TreasureIsland.setFitWidth(100);
			TreasureIsland.setFitHeight(100);
			alert.setTitle("End Game");
			alert.setHeaderText("You Found the Treasure!");
			alert.setContentText("You Win!\n Total Damage: " + damage);
			alert.setGraphic(TreasureIsland);
			alert.getButtonTypes().remove(0);
			alert.getButtonTypes().add(new ButtonType("Quit"));
			alert.showAndWait();
		
			if(alert.getResult().getText() == "Quit")
				System.exit(0);
		}
		
		//Updates the image views for each pirate ship
		for(int i = 0; i < pirates.size(); i++) {
			pirateImages.get(i).setX(pirates.get(i).getShipLocation().x * scale);
			pirateImages.get(i).setY(pirates.get(i).getShipLocation().y * scale);
			
			//TODO This is the lose condition but it currently only prints to the console
			if(pirates.get(i).getShipLocation().equals(ship.getShipLocation())) {
				Alert alert = new Alert(AlertType.INFORMATION);
				URL url = getClass().getResource("/images/pirateShip.png");
				Image pirate = new Image(url.toString(), 100, 100, false, false);
				ImageView pirateShip = new ImageView(pirate);
				pirateShip.setFitWidth(100);
				pirateShip.setFitHeight(100);
				alert.setTitle("End Game");
				alert.setHeaderText("The Pirates Boarded your ship!");

				alert.setGraphic(pirateShip);
				alert.setContentText("You Lose!\n Total Damage: " + damage);
				alert.showAndWait();
				
				
				stopPlaying();
				System.exit(0);

				alert.setContentText("You Loose!");
				alert.getButtonTypes().remove(0);
				alert.getButtonTypes().add(new ButtonType("Quit"));
				alert.showAndWait();
				
				if(alert.getResult().getText() == "Quit")
					System.exit(0);

			}
		}
	}
	
	public void checkPowerUp() {
		//Checks if the square the player ship is on is a powerup and consumes the powerup and applies it to the ship
		if(map[ship.getShipLocation().x][ship.getShipLocation().y] == 4) {
			ship = new Paddle(ship);
			map[ship.getShipLocation().x][ship.getShipLocation().y] = 0;
			paddleImageView.setX(-100);
		}
		if(map[ship.getShipLocation().x][ship.getShipLocation().y] == 5) {
			ship = new RocketBooster(ship);
			map[ship.getShipLocation().x][ship.getShipLocation().y] = 0;
			rocketImageView.setX(-100);
		}
		if(map[ship.getShipLocation().x][ship.getShipLocation().y] == 6) {
			ship = new Booze(ship);
			map[ship.getShipLocation().x][ship.getShipLocation().y] = 0;
			boozeImageView.setX(-100);	
		}
		
	}
	
	public void checkDamage() {
		//Creates a generic monster
		Monster monster = new Shark(0,0,0);
		
		//Checks through every monster to see if it makes contact with the player ship
		for(Monster monsterSprite : monsterSpawner.monsterSprites)
			if(ship.getShipLocation().x == monsterSprite.getX() && ship.getShipLocation().y == monsterSprite.getY()) {
				//Adds a point of damage and also keeps track of which monster hit the ship
				damage++;
				monster = monsterSprite;
			}
		
		//Handles the health bar based on how much damage the player ship has taken
		if(damage <= 3) {
			for(int i = 0; i < damage; i++) {
				healthBar.get(i).setFill(Color.ORANGERED);
				healthBar.get(i).setStroke(Color.RED);
			}
		}
		
		//If the player damage is 3, the game ends
		if(damage == 3) {
			Alert alert = new Alert(AlertType.INFORMATION);
			URL url = getClass().getResource(monster.getClass() == Shark.class ? "/images/shark.png" : "/images/squid.png");
			Image pirate = new Image(url.toString(), 100, 100, false, false);
			ImageView pirateShip = new ImageView(pirate);
			pirateShip.setFitWidth(100);
			pirateShip.setFitHeight(100);
			alert.setTitle("End Game");
			alert.setHeaderText("Your ship took too much damage and sunk!");

			alert.setGraphic(pirateShip);
			alert.setContentText("You Lose!");
			alert.showAndWait();
			
			
			stopPlaying();
			System.exit(0);
		}
	}
	
	public boolean checkWin() {
		boolean output = false;
		//checks right adj cell
		if(ship.getShipLocation().x < dimensions - 1)
			if(map[ship.getShipLocation().x+1][ship.getShipLocation().y] == 3) 
				output = true;
			
		
		//checks left adj cell
		if(ship.getShipLocation().x > 0)
			if(map[ship.getShipLocation().x-1][ship.getShipLocation().y] == 3) 
				output = true;
		
		
		//checks top adj cell
		if(ship.getShipLocation().y < dimensions - 1) 
			if(map[ship.getShipLocation().x][ship.getShipLocation().y+1] == 3) 
				output = true;
			
		
		//checks bot adj cell
		if(ship.getShipLocation().y > 0)
			if(map[ship.getShipLocation().x][ship.getShipLocation().y-1] == 3) 
				output = true;
			
		return output;
	}
	
	@SuppressWarnings("deprecation")
	public void stopPlaying() {
		playing = false;
		ship.stopGame();
		monstersThread.stop();
		for(ShipInterface p : pirates) {
			p.stopGame();
		}
	}
	
	public static void main(String[] args) {
		//Launches the game
		launch(args);
	}
	

	/**
	 * Below are private classes that create the Game Menu
	 * @author James
	 *
	 */
	
	
	
	private class GameMenu extends Parent{
		public GameMenu() {
			
			//Creates the buttons in the menus
			VBox menu0 = new VBox(15);
			VBox menu1 = new VBox(15);
			VBox menu2 = new VBox(15);
		
			
			//sets the button placement 
			menu0.setTranslateX(20);
			menu0.setTranslateY(125);
		
			//sets the button placement
			menu1.setTranslateX(100);
			menu1.setTranslateY(200);
			
			/*
			 * sets the text for the main menu
			 */
			
			Text textmain = new Text("Welcome to the Ocean Blue \n"
								+ "                              \n"
								+ "Find the treasure before the \n "
								+ "pirates capture you and beware\n "
								+ "the mighty leviathans of the deep"
								);
			textmain.setFill(Color.DARKBLUE);
			textmain.setTranslateX(445);
			textmain.setTranslateY(150);
			textmain.setFont(Font.font("Serif", FontWeight.BOLD, FontPosture.ITALIC, 25));
			
			/*
			 * Sets the text for the Controls menu
			 */
			
			Text textOptions = new Text("W - move Up \n"
										+"A - move Left\n"
										+ "S - move Down\n"
										+ "D - move Right\n"
										+"------------------\n"
										+"Squids and Sharks Deal\n"
										+ "one damage upon attacking\n"
										+ "avoid them at all costs"
										);
			textOptions.setFill(Color.BLACK);
			textOptions.setTranslateX(525);
			textOptions.setTranslateY(175);
			textOptions.setFont(Font.font("Serif", FontWeight.BOLD, 20));
			
			
			
			
			menu2.setTranslateX(100);
			menu2.setTranslateY(200);
			
			final int offset = 400;
			
			menu1.setTranslateX(offset);
			menu2.setTranslateX(offset);
			/**
			 * Main Menu Buttons
			 */
			//Creates the Play button in Main Menu
			MenuButton btnPlay = new MenuButton("PLAY");
			btnPlay.setOnMouseClicked(event -> {
				menu.setScene(scene2);
				//starts monster movement
				monstersThread.start();
				FadeTransition ft = new FadeTransition(Duration.seconds(1), this);
				ft.setFromValue(1);
				ft.setToValue(0);
				ft.setOnFinished(evt -> this.setVisible(false));
				ft.play();
			});
			
			//Creates Option button in MainMenu
			MenuButton btnOptions = new MenuButton("Options");
			btnOptions.setOnMouseClicked(event -> {
				getChildren().addAll(menu1);
				TranslateTransition ot = new TranslateTransition(Duration.seconds(0.25), menu0);
				ot.setToX(menu0.getTranslateX() - offset);
				
				TranslateTransition ot1 = new TranslateTransition(Duration.seconds(0.5), menu1);				
				ot1.setToX(menu0.getTranslateX());
				
	
				
				ot.play();
				ot1.play();
			
				ot.setOnFinished(evt -> {
					getChildren().removeAll(menu0, textmain);
				});

			});
			
			//Creates Quit button in Main menu
			MenuButton btnQuit = new MenuButton("Quit");
			btnQuit.setOnMouseClicked(event -> {
				System.exit(0);
				FadeTransition ft = new FadeTransition(Duration.seconds(0.5), this);
				ft.setFromValue(1);
				ft.setToValue(0);
				ft.setOnFinished(evt -> this.setVisible(false));
				ft.play();
			});
			
			
			
			/**
			 * Options Menu Buttons
			 */
			//Creates the back button in the options menu
			MenuButton btnBack = new MenuButton("Go Back");
			btnBack.setOnMouseClicked(event -> {
				getChildren().addAll(menu0, textmain);
				TranslateTransition ot = new TranslateTransition(Duration.seconds(0.25), menu1);
				ot.setToX(menu1.getTranslateX() + offset);
				
				TranslateTransition ot1 = new TranslateTransition(Duration.seconds(0.5), menu0);				
				ot1.setToX(menu1.getTranslateX());
				
				ot.play();
				ot1.play();
				
				ot.setOnFinished(evt -> {
					getChildren().removeAll(menu1);
			});
		});
			//creates the controls button in the options menu
			MenuButton btnControls = new MenuButton("Controls");
			btnControls.setOnMouseClicked(event -> {
				getChildren().addAll(menu2, textOptions);
				TranslateTransition ot = new TranslateTransition(Duration.seconds(0.25), menu1);
				ot.setToX(menu1.getTranslateX() - offset);
			
				TranslateTransition ot1 = new TranslateTransition(Duration.seconds(0.5), menu2);				
				ot1.setToX(menu1.getTranslateX());
			
				ot.play();
				ot1.play();
			
				ot.setOnFinished(evt -> {
					getChildren().removeAll(menu1, textmain);
		});
	});
			
			
			/**
			 * Buttons for Controls Menu
			 */
			//creates the  main menu button in the controls menu
			MenuButton btnMain = new MenuButton("Go To Main Menu");
			btnMain.setOnMouseClicked(event -> {
				getChildren().addAll(menu0, textmain);
				TranslateTransition ot = new TranslateTransition(Duration.seconds(0.25), menu2);
				ot.setToX(menu2.getTranslateX() + offset);
				
				TranslateTransition ot1 = new TranslateTransition(Duration.seconds(0.25), menu0);				
				ot1.setToX(menu2.getTranslateX());
				
				ot.play();
				ot1.play();
				
				ot.setOnFinished(evt -> {
					getChildren().removeAll(menu2,textOptions);
			});
		});
			
			//creates the go back button in the controls menu
			MenuButton btnBack2 = new MenuButton("Go Back");
			btnBack2.setOnMouseClicked(event -> {
				getChildren().add(menu1);
				TranslateTransition ot = new TranslateTransition(Duration.seconds(0.25), menu2);
				ot.setToX(menu2.getTranslateX() + offset);
				
				TranslateTransition ot1 = new TranslateTransition(Duration.seconds(0.5), menu1);				
				ot1.setToX(menu2.getTranslateX());
				
				ot.play();
				ot1.play();
				
				ot.setOnFinished(evt -> {
					getChildren().removeAll(menu2, textOptions);
			});
		});
			
			
						
			
			
			menu0.getChildren().addAll(btnPlay, btnOptions, btnQuit);
			menu1.getChildren().addAll(btnBack, btnControls);
			menu2.getChildren().addAll(btnMain, btnBack2);
			
			
			//creates a grey film to make white text more pronounced
			Rectangle bg = new Rectangle(800,550);
			bg.setFill(Color.GREY);
			bg.setOpacity(0.4);
			
			getChildren().addAll(bg, menu0, textmain);
			
		}
	}
	//Create the Menu Title
	public static class Title extends StackPane{
		public Title(String name) {
			Rectangle bg = new Rectangle(650,60);
			bg.setStroke(Color.WHITE);
			bg.setStrokeWidth(2);
			bg.setFill(null);
			
			Text text = new Text(name);
			text.setFill(Color.WHITE);
			text.setFont(Font.font("Serif", 25));
			
			setAlignment(Pos.CENTER);
			getChildren().addAll(bg,text);
		}
	}
	
	/**
	 * Creates the design for the menu Buttons
	 * @author James
	 *
	 */
	
	
	private class MenuButton extends StackPane{
		
		
		private Text text;
		
		
		public MenuButton(String name) {
			text = new Text(name);
			text.setFont(Font.font(20));
			
			//Creates the size of all Menu buttons
			Rectangle bg = new Rectangle(250, 30);
			bg.setOpacity(0.5);
			bg.setFill(Color.BLACK);
			bg.setEffect(new GaussianBlur(3.5));
			
			//sets position of buttons
			setAlignment(Pos.CENTER_LEFT);
			setRotate(-0.5);
			getChildren().addAll(bg, text);
			
			//Highlights and moves button when hovered over
			setOnMouseEntered(event -> {
				bg.setTranslateX(10);
				text.setTranslateX(10);
				bg.setFill(Color.WHITE);
				text.setFill(Color.BLACK);
				
			});
	
			//Undoes previous effects when mouse exits hover
			setOnMouseExited(event -> {
				bg.setFill(Color.BLACK);
				bg.setOpacity(0.5);
				bg.setEffect(new GaussianBlur(3.5));
				bg.setTranslateX(0);
				text.setTranslateX(0);
				
			});
			//sets a glow effect when click on ends effect when click released
			DropShadow drop = new DropShadow(50, Color.WHITE);
			drop.setInput(new Glow());
			
			setOnMousePressed(event -> setEffect(drop));
			setOnMouseReleased(event -> setEffect(null));
			
		}

	}

	
}