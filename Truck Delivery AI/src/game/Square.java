package game;
import java.awt.Color;

public class Square {

	private final int WIDTH;
	private final int HEIGHT;

	private int posX;
	private int posY;
	int x;
	int y;
	private Color color;
	private boolean possibleMove = false;
	private boolean Occupied = false;
	private boolean hasPostBoxOrPackage = false;
	private PostBox postBox = null;
	private PostPackage postPackage = null;

	public Square(int posX, int posY, int squareWidth, int squareHeight, Color color) {
		this.posX = posX;
		this.posY = posY;
		x = posX / squareWidth;
		y = posY / squareHeight;
		
		WIDTH = squareWidth;
		HEIGHT = squareHeight;
		this.color = color;
	}

	public boolean isHasPostBoxOrPacket() {
		return hasPostBoxOrPackage;
	}

	public void setHasPostBoxOrPackage(boolean hasPostBoxOrPackage) {
		this.hasPostBoxOrPackage = hasPostBoxOrPackage;
	}

	public boolean isOccupied() {
		return Occupied;
	}

	public void setOccupied(boolean occupied) {
		Occupied = occupied;
		if (Occupied) {
		}
	}

	public boolean isPossibleMove() {
		return possibleMove;
	}

	public void setPossibleMove(boolean possibleMove) {
		this.possibleMove = possibleMove;
	}

	public void setPackage(PostPackage postPackage) {
		this.postPackage = postPackage;
	}

	public void setPostBox(PostBox postBox) {
		this.postBox = postBox;
	}

	public int getWIDTH() {
		return WIDTH;
	}

	public int getHEIGHT() {
		return HEIGHT;
	}

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public PostBox getPostBox() {
		return postBox;
	}

	public PostPackage getPackage() {
		return postPackage;
	}
}
