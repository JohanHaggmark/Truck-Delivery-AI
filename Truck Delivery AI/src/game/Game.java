package game;

import javax.swing.JFrame;
import net.Network;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Game {

	private GUI gui;
	private Board board;
	private EntityCollection entityCollection;
	private ScoreBoard scoreBoard;
	Controller controller;

	private static int numberOfSquaresX;
	private static int numberOfSquaresY;

	private static final int SCOREBOARDHEIGHT = 200;
	private final int SCOREBOARDWIDTH;

	private static int frameWidth;
	private static int frameHeight;

	private static int squareWidth;
	private static int squareHeight;

	private char packageAndBoxChar = 'A';

	private int numberOfTrucksForAPlayer;

	public int getNumberOfTrucksForAPlayer() {
		return numberOfTrucksForAPlayer;
	}

	private int playerTurn = 0;
	public int roundsLeft;
	public int rounds;
	private int packageLeft;
	private int numberOfPackages;

	public int getNumberOfPackages() {
		return numberOfPackages;
	}

	public boolean ready = true;

	private boolean gameOver = false;
	private ArrayList<Player> mPlayers;
	private ArrayList<PostPackage> mPostPackages;
	private ArrayList<PostBox> mPostBoxes;

	private ArrayList<AIBot> bots;

	private int numberOfPlayers = 2;

	private JFrame frame;

	public Game(JFrame frame, int numberOfSquaresX, int numberOfSquaresY, int numberOfTrucksForAPlayer, int rounds,
			int packages) {
		mPlayers = new ArrayList<Player>();
		mPostPackages = new ArrayList<PostPackage>();
		mPostBoxes = new ArrayList<PostBox>();
		Game.numberOfSquaresX = numberOfSquaresX;
		Game.numberOfSquaresY = numberOfSquaresY;
		frameWidth = numberOfSquaresX * 100 + 10;
		frameHeight = numberOfSquaresY * 100 + SCOREBOARDHEIGHT;
		SCOREBOARDWIDTH = frameWidth;
		squareWidth = (frameWidth - 10) / numberOfSquaresX;
		squareHeight = (frameHeight - 200) / numberOfSquaresY;

		this.numberOfTrucksForAPlayer = numberOfTrucksForAPlayer;
		roundsLeft = rounds;
		this.rounds = rounds;
		packageLeft = packages;
		numberOfPackages = packageLeft;
		board = new Board(numberOfSquaresX, numberOfSquaresY, squareWidth, squareHeight);
		entityCollection = new EntityCollection();
		addPlayersAndTrucksAndCrossHairs();
		scoreBoard = new ScoreBoard(mPlayers, this, SCOREBOARDWIDTH, SCOREBOARDHEIGHT);
		gui = new GUI(entityCollection, board, scoreBoard);

		this.frame = frame;
		this.frame.setBounds(10, 10, frameWidth, frameHeight);
		this.frame.add(gui);
		controller = new Controller(this, gui);
		bots = new ArrayList<AIBot>();

		startGame();
		for (int i = 0; i < packageLeft; i++) {
			randomSpawnPackagesAndPostBoxes();
		}
		System.out.println("Game constructor");
	}

	public void startGame() {
		playerTurn = 0;
		mPlayers.get(playerTurn).setMyTurn(true);
		mPlayers.get(playerTurn + 1).setMyTurn(false);
		gui.repaint();
		entityCollection.reset();
		// events();
	}

	private void addPlayersAndTrucksAndCrossHairs() {
		for (int team = 1; team <= numberOfPlayers; team++) {
			CrossHair crossHair = new CrossHair(team);
			entityCollection.addEntity(crossHair);
			mPlayers.add(new Player(team, crossHair, board));

			for (int j = 1; j <= numberOfTrucksForAPlayer; j++) {
				Truck truck = new Truck(team, j);
				entityCollection.addEntity(truck);
				mPlayers.get(team - 1).addTrucks(truck);
			}
		}
	}

	// called when keyPressed
	public synchronized void events() {
		// entityCollection.removeDeadEntities();
		// int tempOutput;
		gui.repaint();

		boolean aiAwaken = true;
		while (aiAwaken) {
			System.out.println("aiawaken: " + aiAwaken);
			board.setColorOfSquares(mPlayers.get(playerTurn));
			scoreBoard.update();

			try {
				TimeUnit.MILLISECONDS.sleep(1);
			} catch (InterruptedException e) { //
				e.printStackTrace();
			}
			if(playerTurn == 0) {
				bots.get(playerTurn).semiMonteCarloTreeSearchWithNeuralNetwork(mPlayers.get(playerTurn),
						mPlayers.get(playerTurn).getmTrucks());
				//bots.get(playerTurn).monteCarloTreeSearch(mPlayers.get(playerTurn),
					//	mPlayers.get(playerTurn).getmTrucks());
			}
			else if (playerTurn < bots.size()) {
				System.out.println(bots.size() + " size of bots");
				bots.get(playerTurn).monteCarloTreeSearch(mPlayers.get(playerTurn),
						mPlayers.get(playerTurn).getmTrucks());

			}
		//	animateTrucks();
			gui.repaint();
			aiAwaken = isAiAwaken();
		}
	}

	private void animateTrucks() {
		for (int i = 0; i < numberOfTrucksForAPlayer; i++) {
			animateTruck(mPlayers.get(getRecentPlayer()).mTrucks.get(i));
		}
	}

	private void animateTruck(Truck truck) {
		int posX = truck.getPosX();
		int posY = truck.getPosY();
		int x = posX - truck.recentX;
		int y = posY - truck.recentY;
		x = x / 60;
		y = y / 60;
		for (int i = 0; i < 60; i++) {
			
			truck.setPosX(truck.recentX + 1);
			truck.setPosY(truck.recentY + 1);
			gui.repaint();
			try {
				TimeUnit.MILLISECONDS.sleep(10);
			} catch (InterruptedException e) { //
				e.printStackTrace();
			}
		}
		truck.setPosX(posX);
		truck.setPosY(posY);
	}

	private boolean isAiAwaken() {
		for (int i = 0; i < bots.size(); i++) {
			if (bots.get(i).myTurn) {
				return true;
			}
		}
		return false;
	}

	private int getRecentPlayer() {
		if (playerTurn == 0) {
			return 1;
		} else {
			return 0;
		}
	}

	public void output(int x) {
		int tempPlayerTurn = playerTurn;
		int tempScore = mPlayers.get(playerTurn).getScore();
		switch (x) {
		case 0:
			controller.moveCrossHairDown();
			break;
		case 1:
			controller.moveCrossHairLeft();
			break;
		case 2:
			controller.moveCrossHairRight();
			break;
		case 3:
			controller.moveCrossHairUp();
			break;
		case 4:
			controller.tryMoveTruck();
		case 5:
			controller.trySelectTruck();
			break;
		case 6:
			controller.nextRound();
			break;
		case 7:
			controller.nextRound();
			break;
		}
		tempScore = mPlayers.get(tempPlayerTurn).getScore() - tempScore;

	}

	public void nextRound() {
		if (roundsLeft > 0) {
			mPlayers.get(playerTurn).nextRound();

			playerTurn++;
			if (playerTurn >= numberOfPlayers) {
				playerTurn = 0;
			}
			mPlayers.get(playerTurn).setMyTurn(true);
			for (Truck truck : mPlayers.get(playerTurn).getmTrucks()) {
				truck.setTurnOver(false);
			}

			roundsLeft--;

			if (roundsLeft == 0) {
				// gui.setVisible(false);
				bots.get(0).updateExcelStatics();
				gameOver();
			}
			try {
				System.out.println("game 215 set aiturn " + playerTurn);
				bots.get(playerTurn).myTurn = true;
			} catch (IndexOutOfBoundsException e) {
				System.out.println("out");
			}
		}
	}

	private void gameOver() {
		try {
			TimeUnit.MILLISECONDS.sleep(2000);
		} catch (InterruptedException e) { //
			e.printStackTrace();
		}
		gameOver = true;
		for (int i = 0; i < board.getmSquares().size(); i++) {
			board.getmSquares().get(i).setPackage(null);
			board.getmSquares().get(i).setPostBox(null);
			board.getmSquares().get(i).setHasPostBoxOrPackage(false);
			board.getmSquares().get(i).setPossibleMove(true);
			board.getmSquares().get(i).setOccupied(false);
		}
		entityCollection.reset();

		for(int i = 0; i < mPlayers.size(); i++) {
			for(int j = 0; j < mPlayers.get(i).mTrucks.size(); j++) {
				//mPlayers.get(i).mTrucks.get(i).removePackages();
				mPlayers.get(i).mTrucks.get(j).start(0,0,0,0);
			}
		
		}
		
		startGame();
		
		nextGameRandomSpawnPackagesAndPostBoxesResetTrucks();
		for (int y = 0; y < 2; y++) {
			// bots.get(y).score += mPlayers.get(y).getScore();
			// bots.get(y).actions = 0;
			mPlayers.get(y).score = 0;
		}

		roundsLeft = rounds;
	}

	private ArrayList<ArrayList<Square>> getSquaresWithNoPostBoxesOrPackages() {

		ArrayList<ArrayList<Square>> pairSquares = new ArrayList<ArrayList<Square>>();

		for (int i = 0; i < board.getmSquares().size(); i++) {
			// two squares that is inverted must not have PostBox Or Package in squares:
			if (!board.getmSquares().get(i).isHasPostBoxOrPacket()
					&& !board.getmSquares().get((board.getmSquares().size() - 1) - i).isHasPostBoxOrPacket()) {
				ArrayList<Square> squares = new ArrayList<Square>();
				squares.add(board.getmSquares().get(i));
				squares.add(board.getmSquares().get(board.getmSquares().size() - 1 - i));
				// adds pair with squares:
				pairSquares.add(squares);
			}
		}
		return pairSquares;
	}

	private ArrayList<Square> getRandomSquares(ArrayList<ArrayList<Square>> pairSquares) {
		Random random = new Random();
		// returns a pair of squares:
		return pairSquares.get(random.nextInt(pairSquares.size()));
	}

	private boolean checkIfAbleToSpawnTwoPackagesAndPostBoxes() {
		for (int i = 0; i < board.getmSquares().size(); i++) {
			if (!board.getmSquares().get(i).isHasPostBoxOrPacket()
					&& !board.getmSquares().get((board.getmSquares().size() - 1) - i).isHasPostBoxOrPacket()) {

				for (int j = i + 1; j < board.getmSquares().size(); j++) {
					if (!board.getmSquares().get(j).isHasPostBoxOrPacket()
							&& !board.getmSquares().get((board.getmSquares().size() - 1) - j).isHasPostBoxOrPacket()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	// Guarantees, spawning two Packet's and two MailBoxes in symmetric positions of
	// the Board
	private void randomSpawnPackagesAndPostBoxes() {
		if (checkIfAbleToSpawnTwoPackagesAndPostBoxes() && packageLeft > 0) {
			char tempChar = packageAndBoxChar;
			tempChar++;

			// for (int y = 0; y < numberOfTrucksForAPlayer * 2 + 2; y++) {
			// reset trucks and crosshair
			// entityCollection.getEntitiesOnPlayground().get(y).start(0, 0, 0, 0);
			// }

			ArrayList<Square> squares = getRandomSquares(getSquaresWithNoPostBoxesOrPackages());
			PostPackage postPackage = new PostPackage(squares.get(0).getPosX(), squares.get(0).getPosY(),
					squares.get(0).x, squares.get(0).y, packageAndBoxChar);
			entityCollection.addEntity(postPackage);
			mPostPackages.add(postPackage);
			squares.get(0).setPackage(postPackage);
			squares.get(0).setHasPostBoxOrPackage(true);

			postPackage = new PostPackage(squares.get(1).getPosX(), squares.get(1).getPosY(), squares.get(1).x,
					squares.get(1).y, tempChar);
			entityCollection.addEntity(postPackage);
			mPostPackages.add(postPackage);
			squares.get(1).setPackage(postPackage);
			squares.get(1).setHasPostBoxOrPackage(true);

			squares = getRandomSquares(getSquaresWithNoPostBoxesOrPackages());
			PostBox postBox = new PostBox(squares.get(0).getPosX(), squares.get(0).getPosY(), squares.get(0).x,
					squares.get(0).y, (packageAndBoxChar));
			entityCollection.addEntity(postBox);
			mPostBoxes.add(postBox);
			squares.get(0).setPostBox(postBox);
			squares.get(0).setHasPostBoxOrPackage(true);

			postBox = new PostBox(squares.get(1).getPosX(), squares.get(1).getPosY(), squares.get(0).x,
					squares.get(0).y, tempChar);
			entityCollection.addEntity(postBox);
			mPostBoxes.add(postBox);
			squares.get(1).setPostBox(postBox);
			squares.get(1).setHasPostBoxOrPackage(true);

			packageAndBoxChar += 2;
			packageLeft--;
		}
	}

	private void nextGameRandomSpawnPackagesAndPostBoxesResetTrucks() {
		for (int i = 0; i < mPostPackages.size(); i++) {
			mPostPackages.get(i).start(0, 0, 0, 0);
		}
		for (int y = 0; y < numberOfTrucksForAPlayer * 2 + 2; y++) {
			// reset trucks and crosshair
			entityCollection.getEntitiesOnPlayground().get(y).start(0, 0, 0, 0);
		}
		for (int x = 8; x < entityCollection.getEntitiesOnPlayground().size();) {
			ArrayList<Square> squares = getRandomSquares(getSquaresWithNoPostBoxesOrPackages());
			for (Square square : squares) {
				entityCollection.getEntitiesOnPlayground().get(x).setPosX(square.getPosX());
				entityCollection.getEntitiesOnPlayground().get(x).setPosY(square.getPosY());
				square.setPackage((PostPackage) entityCollection.getEntitiesOnPlayground().get(x));
				square.setHasPostBoxOrPackage(true);
				x++;
			}
			squares = getRandomSquares(getSquaresWithNoPostBoxesOrPackages());
			for (Square square : squares) {

				entityCollection.getEntitiesOnPlayground().get(x).setPosX(square.getPosX());
				entityCollection.getEntitiesOnPlayground().get(x).setPosY(square.getPosY());

				square.setPostBox((PostBox) entityCollection.getEntitiesOnPlayground().get(x));
				square.setHasPostBoxOrPackage(true);
				x++;

			}
		}

	}

	public void addBot(AIBot bot) {
		bots.add(bot);
	}

	public static int getSQUAREWIDTH() {
		return squareWidth;
	}

	public static int getSQUAREHEIGHT() {
		return squareHeight;
	}

	public static int getNUMBEROFSQUARESX() {
		return numberOfSquaresX;
	}

	public static int getNUMBEROFSQUARESY() {
		return numberOfSquaresY;
	}

	public ArrayList<Player> getmPlayers() {
		return mPlayers;
	}

	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}

	public int getPlayerTurn() {
		return playerTurn;
	}

	public boolean getgameOver() {
		return gameOver;
	}

	public int getRoundsLeft() {
		return roundsLeft;
	}

	public ArrayList<PostPackage> getmPostPackages() {
		return mPostPackages;
	}

	public ArrayList<OnPlayground> getEntitiesOnPlayground() {
		return entityCollection.getEntitiesOnPlayground();
	}

	public Board getBoard() {
		return board;
	}

	public Controller getController() {
		return controller;
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	public ArrayList<PostBox> getmPostBoxes() {
		return mPostBoxes;
	}

	public void setmPostBoxes(ArrayList<PostBox> mPostBoxes) {
		this.mPostBoxes = mPostBoxes;
	}
}
