package game;
import java.util.ArrayList;

public class ScoreBoard {

	private ArrayList<Player> players;
	private Game game;

	private String score;
	private final int POSX = 0;
	private final int POSY;
	private final int WIDTH;
	public final int HEIGHT;

	public ScoreBoard(ArrayList<Player> players, Game game, int width, int height) {
		this.players = players;
		this.game = game;
		POSY = Game.getNUMBEROFSQUARESY() * Game.getSQUAREHEIGHT();
		WIDTH = width;
		HEIGHT = height;
	}

	public void update() {
		score();
	}

	private void score() {
		score = "";
		for (Player player : players) {
			score += player.getPlayerColor() + " team Score " + Integer.toString(player.getScore()) + "\n";
		}
	}

	public String getScore() {
		score = "";
		for (Player player : players) {
			score += player.getPlayerColor() + " team Score " + Integer.toString(player.getScore()) + "\n";
		}
		return score;
	}

	public String getRoundsLeft() {
		return Integer.toString(game.getRoundsLeft()) + " rounds to remain";
	}
	
	public ArrayList<Player> getPlayers(){
		return players;
	}

	public int getPOSX() {
		return POSX;
	}

	public int getPOSY() {
		return POSY;
	}

	public int getWIDTH() {
		return WIDTH;
	}

	public int getHEIGHT() {
		return HEIGHT;
	}
	
}
