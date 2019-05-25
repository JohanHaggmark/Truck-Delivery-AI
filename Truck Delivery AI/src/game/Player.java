package game;

import java.util.ArrayList;

public class Player {

	private CrossHair crossHair;
	private Board board;
	private boolean myTurn = false;
	private String playerColor;
	public int score;

	public ArrayList<Truck> mTrucks;

	public Player(int team, CrossHair crossHair, Board board) {
		score = 0;
		this.crossHair = crossHair;
		this.board = board;
		if (team == 1) {
			playerColor = "Red";
		} else if (team == 2) {
			playerColor = "blue";
		}
		mTrucks = new ArrayList<Truck>();
	}

	public void addTrucks(Truck truck) {
		mTrucks.add(truck);
		for (Square square : board.getmSquares()) {
			if (square.getPosX() == truck.getPosX() && square.getPosY() == truck.getPosY()) {
				square.setOccupied(true);
			}
		}
	}

	public void nextRound() {
		myTurn = false;
		crossHair.setVisible(false);
	}

	public void crossHairUp() {
		if (crossHair.getPosY() > 0) {
			crossHair.setPosY(crossHair.getPosY() - board.getSQUAREHEIGHT());
		}
	}

	public void crossHairDown() {
		if (crossHair.getPosY() < (board.getSQUAREHEIGHT() * (board.getNUMBEROFSQUARESY() - 1))) {
			crossHair.setPosY(crossHair.getPosY() + board.getSQUAREHEIGHT());
		}
	}

	public void crossHairRight() {
		if (crossHair.getPosX() < (board.getSQUAREWIDTH() * (board.getNUMBEROFSQUARESX() - 1))) {
			crossHair.setPosX(crossHair.getPosX() + board.getSQUAREWIDTH());
		}
	}

	public void crossHairLeft() {
		if (crossHair.getPosX() > 0) {
			crossHair.setPosX(crossHair.getPosX() - board.getSQUAREWIDTH());
		}
	}

	// The CrossHair is on the same position as the truck:
	public boolean playersCrossHairIsOnItsTruck() {
		for (Truck truck : mTrucks) {
			if (truck.getPosX() == crossHair.getPosX() && truck.getPosY() == crossHair.getPosY()) {
				return true;
			}
		}
		return false;
	}

	// if truck is available then truck is able to move
	public boolean isTruckAvailable() {
		for (Truck truck : mTrucks) {
			if (truck.getPosX() == crossHair.getPosX() && truck.getPosY() == crossHair.getPosY()
					&& !truck.isTurnOver()) {
				return true;
			}
		}
		return false;
	}

	// truck will be moved if its possible:
	public void tryMoveTruckToSquare() {
		for (Truck truck : mTrucks) {
			if (truck.isSelect()) {
				truck.recentX = truck.getPosX();
				truck.recentY = truck.getPosY();
				for (Square square : board.getmSquares()) {
					if (square.getPosX() == crossHair.getPosX() && square.getPosY() == crossHair.getPosY()
							&& square.isPossibleMove()) {
						truck.moveTruckToSquare(square);
						square.setOccupied(true);
						// will pickup package or deliver package, if its possible:
						tryPickUpPackage(truck, square);
						tryDeliverPackage(truck, square);
						crossHair.setHasMarkedATruck(false);
					}
				}
			}
		}
	}

	public void aiMoveTruck(Truck truck, int x, int y) {
		truck.recentX = truck.getPosX();
		truck.recentY = truck.getPosY();
		for (int i = 0; i < mTrucks.size(); i++) {
			if (truck == mTrucks.get(i)) {
				for (Square square : board.getmSquares()) {
					if (square.getPosX() == x * board.getSQUAREWIDTH()
							&& square.getPosY() == y * board.getSQUAREHEIGHT()) {
						truck.moveTruckToSquare(square);
						square.setOccupied(true);
						// will pickup package or deliver package, if its possible:
						tryPickUpPackage(truck, square);
						tryDeliverPackage(truck, square);
					}
				}
			}
		}
	}

	// if a square has a package, the truck will pick it up
	public void tryPickUpPackage(Truck truck, Square square) {
		if (square.getPackage() != null && truck.getPackages().size() < 2) {
			truck.addPackage(square.getPackage());
			square.getPackage().setVisible(false);
			square.getPackage().setRemoveMe(true);
			square.setPackage(null);
		}
	}

	// if a square has a post box, and the ID of the post box is equal to the ID of
	// a package that the truck is carrying, the truck will deliver a package
	public void tryDeliverPackage(Truck truck, Square square) {
		if (square.getPostBox() != null) {
			for (PostPackage postPackage : truck.getPackages()) {
				if (postPackage.getEntityInfo().equals(square.getPostBox().getEntityInfo())) {
					square.getPostBox().setRemoveMe(true);
					square.setHasPostBoxOrPackage(false);
					postPackage.setDelivered(true);
					score++;
				}
			}
			truck.removeDeliveredPackage();
		}
	}

	private void unOccupieSquare(Truck truck) {
		for (Square square : board.getmSquares()) {
			if (square.getPosX() == truck.getPosX() && square.getPosY() == truck.getPosY()) {
				square.setOccupied(false);
			}
		}
	}
	
	public void aiUnselectSquare(Truck truck) {
		unOccupieSquare(truck);
	}

	public void trySelectTruck() {
		System.out.println("try select tr");
		for (Truck truck : mTrucks) {
			if (crossHair.getPosX() == truck.getPosX() && crossHair.getPosY() == truck.getPosY() && !truck.isTurnOver()
					&& !crossHair.isHasMarkedATruck()) {
				truck.setSelect(true);
				unOccupieSquare(truck);
				crossHair.setHasMarkedATruck(true);
			}
		}
	}

	public Truck getTruckInCrossHair() {
		for (Truck truck : mTrucks) {
			if (truck.getPosX() == crossHair.getPosX() && truck.getPosY() == crossHair.getPosY()) {
				return truck;
			}
		}
		return null;
	}

	public boolean isMyTurn() {
		return myTurn;
	}

	public void setMyTurn(boolean myTurn) {
		crossHair.setVisible(true);
		this.myTurn = myTurn;
	}

	public ArrayList<Truck> getmTrucks() {
		return mTrucks;
	}

	public int getScore() {
		return score;
	}

	public String getPlayerColor() {
		return playerColor;
	}

	public CrossHair getCrossHair() {
		return crossHair;
	}
}
