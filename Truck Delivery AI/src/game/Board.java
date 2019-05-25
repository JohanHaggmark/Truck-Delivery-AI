package game;
import java.awt.Color;
import java.util.ArrayList;

public class Board {

	private int numberOfSquaresX;
	private int numberOfSquaresY;
	private int squareWidth;
	private int squareHeght;
	private final int BORDERTHICKNESS = 5;

	private ArrayList<Square> mSquares = new ArrayList<Square>();

	public ArrayList<Square> getmSquares() {
		return mSquares;
	}

	public Board(int numberOfSquaresX, int numberOfSquaresY, int squareWidth, int squareHeight) {
		this.numberOfSquaresX = numberOfSquaresX;
		this.numberOfSquaresY = numberOfSquaresY;
		this.squareWidth = squareWidth;
		this.squareHeght = squareHeight;

		for (int i = 0; i < numberOfSquaresX; i++) {
			for (int j = 0; j < numberOfSquaresY; j++) {
				mSquares.add(new Square(i * squareWidth, j * squareHeight, squareWidth, squareHeight, Color.DARK_GRAY));
			}
		}
	}

	public void setColorOfSquares(Player player) {
		setSquaresColorToGray();
		if (player.playersCrossHairIsOnItsTruck() && player.isTruckAvailable()) {
			showPossibleMove(player);
		}
		if (isTruckSelected(player)) {
			showPossibleMoveWhenTruckIsSelected(player);
		}
	}

	private void setSquaresColorToGray() {
		for (Square square : getmSquares()) {
			square.setColor(Color.DARK_GRAY);
			square.setPossibleMove(false);
		}
	}

	private void showPossibleMoveWhenTruckIsSelected(Player player) {
		for (Truck truck : player.getmTrucks()) {
			if (truck.isSelect()) {
				int distanceX;
				int distanceY;
				for (Square square : mSquares) {
					distanceX = Math.abs(truck.getPosX() / squareWidth - square.getPosX() / squareWidth);
					distanceY = Math.abs(truck.getPosY() / squareHeght - square.getPosY() / squareHeght);
					if (distanceX + distanceY < truck.getNumberOfSquaresToMove() && !square.isOccupied()) {
						square.setColor(Color.gray);
						square.setPossibleMove(true);
					}
				}
			}
		}
	}

	private void showPossibleMove(Player player) {
		Truck truck = player.getTruckInCrossHair();
		int distanceX;
		int distanceY;
		for (Square square : mSquares) {
			distanceX = Math.abs(truck.getPosX() / squareWidth - square.getPosX() / squareWidth);
			distanceY = Math.abs(truck.getPosY() / squareHeght - square.getPosY() / squareHeght);
			if (distanceX + distanceY < truck.getNumberOfSquaresToMove() && !square.isOccupied()) {
				square.setColor(Color.gray);
			}
		}
	}

	private boolean isTruckSelected(Player player) {
		for (Truck truck : player.getmTrucks()) {
			if (truck.isSelect()) {
				return true;
			}
		}
		return false;
	}

	public int getNUMBEROFSQUARESX() {
		return numberOfSquaresX;
	}

	public int getNUMBEROFSQUARESY() {
		return numberOfSquaresY;
	}

	public int getSQUAREWIDTH() {
		return squareWidth;
	}

	public int getSQUAREHEIGHT() {
		return squareHeght;
	}

	public int getBORDERTHICKNESS() {
		return BORDERTHICKNESS;
	}
}
