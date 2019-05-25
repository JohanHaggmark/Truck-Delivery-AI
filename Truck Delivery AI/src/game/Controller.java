package game;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Controller implements KeyListener {
	private Game game;
	public int key = 0;

	public Controller(Game game, GUI gui) {
		this.game = game;
		gui.addKeyListener(this);
		gui.requestFocusInWindow();
		
	}

	// In the functions below, game.getPlayerTurn() == the index of the ArrayList
	// game.mPlayers. this is a way to avoid a for loop, to check who's turn it is
	public void moveCrossHairUp() {
		game.getmPlayers().get(game.getPlayerTurn()).crossHairUp();
		key = 3;
	}

	public void moveCrossHairDown() {
		game.getmPlayers().get(game.getPlayerTurn()).crossHairDown();
		key = 0;
	}

	public void moveCrossHairRight() {
		game.getmPlayers().get(game.getPlayerTurn()).crossHairRight();
		key = 2;
	}

	public void moveCrossHairLeft() {
		game.getmPlayers().get(game.getPlayerTurn()).crossHairLeft();
		key = 1;
	}

	public void trySelectTruck() {
		System.out.println("trying slect truck from controller 39");
		game.getmPlayers().get(game.getPlayerTurn()).trySelectTruck();
		key = 5;
	}

	public void tryMoveTruck() {
		game.getmPlayers().get(game.getPlayerTurn()).tryMoveTruckToSquare();
		key = 4;
	}
	public void nextRound() {
		game.nextRound();
		key = 6;
	}
	///////////////////////////////////////////////////////////////////////////////////

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			moveCrossHairRight();
		
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			moveCrossHairLeft();
		} else if (e.getKeyCode() == KeyEvent.VK_UP) {
			moveCrossHairUp();
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			moveCrossHairDown();
		} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			System.out.println("controller 68");
			tryMoveTruck();
			trySelectTruck();
		} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			System.out.println("Controller 70");
			nextRound();
		} 
		System.out.println("trying to read key contrlller 72");
		game.events();
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}
}
