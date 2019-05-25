package game;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Truck extends JPanel implements OnPlayground {

	private int possibleMove = 3;
	private int PosX;
	private int PosY;
	public int recentX, recentY;
	public int x;
	public int y;
	private int playerNumber;
	private int numberOfSquaresToMove;
	private int numberOfSquaresToMoveEmpyTruck = 4;
	private String colorOfTruck;
	// info holds the info of Packages that are attached to the truck
	private String info = "";
	private boolean select = false;
	private boolean turnOver = false;
	private boolean visible = true;
	private boolean removeMe = false;
	private Image truckImage;

	private ArrayList<PostPackage> postPackages;

	int team;
	int numberOfTruck;

	public Truck(int team, int numberOfTruck) {
		this.team = team;
		this.numberOfTruck = numberOfTruck;
		postPackages = new ArrayList<PostPackage>();
		if (team == 1) {
			ImageIcon truckIcon = new ImageIcon(getClass().getResource("RedTruck.png"));
			truckImage = truckIcon.getImage();
		} else {
			ImageIcon truckIcon = new ImageIcon(getClass().getResource("BlueTruck.png"));
			truckImage = truckIcon.getImage();
		}
		start(0, 0, 0,0);

	}

	public void start(int posX, int posY, int x, int y) {
		

		this.playerNumber = team;
		numberOfSquaresToMove = numberOfSquaresToMoveEmpyTruck;
		if (team == 1) {
			PosY = 0;			
			this.y = 0;
			recentY = this.y;
			PosX = numberOfTruck * Game.getSQUAREWIDTH();
			this.x = numberOfTruck; 
			recentX = this.x;
		} else if (team == 2) {
			PosY = Game.getNUMBEROFSQUARESY() * Game.getSQUAREHEIGHT() - Game.getSQUAREHEIGHT();
			this.y = Game.getNUMBEROFSQUARESY()-1;
			recentY = this.y;
			PosX = (Game.getNUMBEROFSQUARESX() - 1) * Game.getSQUAREWIDTH() - numberOfTruck * Game.getSQUAREWIDTH();
			this.x = Game.getNUMBEROFSQUARESX()-1 - numberOfTruck;
			recentX = this.x;
		}
		for (int i = 0; i < 2; i++) {
			if (postPackages.size() > i) {
				postPackages.remove(i);
			}
			info = "";
		}
		visible = true;
	}

	public Image getEntityImage() {
		return truckImage;
	}

	public void addPackage(PostPackage postPackage) {
		postPackages.add(postPackage);
		setNumberOfSquaresToMove(numberOfSquaresToMoveEmpyTruck);
	}
	
//	public void removePackages() {
//		for(int i = 0; i < postPackages.size(); i++) {
//			postPackages.get(i).setVisible(true);
//			postPackages.get(i).setDelivered(false);
//			postPackages.remove(i);	
//		}
//		info = "";
//	}

	public void removeDeliveredPackage() {
		for (int i = postPackages.size() - 1; i >= 0; i--) {
			if (postPackages.get(i).isDelivered()) {
				postPackages.get(i).setVisible(false);
				postPackages.remove(i);
				setNumberOfSquaresToMove(numberOfSquaresToMoveEmpyTruck);
			}
		}
	}

	public void moveTruckToSquare(Square square) {
		select = false;
		this.PosX = square.getPosX();
		this.PosY = square.getPosY();
		movePacketToTruck(PosX, PosY);
		square.setPossibleMove(false);
		setSelect(false);
		setTurnOver(true);
		x = PosX / Game.getSQUAREWIDTH();
		y = PosY / Game.getSQUAREHEIGHT();
	}
	
	public void moveTruck(int x, int y) {
		
	}

	private void movePacketToTruck(int posX, int posY) {
		for (PostPackage postPackage : postPackages) {
			postPackage.setPosX(posX);
			postPackage.setPosY(posY);
		}
	}

	public int getPosX() {
		return PosX;
	}

	public void setPosX(int posX) {
		PosX = posX;
		x = posX / 100;
	}

	public int getPosY() {
		return PosY;
		
	}

	public void setPosY(int posY) {
		PosY = posY;
		y = posY /100;
	}

	public int getNumberOfSquaresToMove() {
		return numberOfSquaresToMove;
	}

	public void setNumberOfSquaresToMove(int numberOfSquaresToMoveEmpyTruck) {
		this.numberOfSquaresToMove = numberOfSquaresToMoveEmpyTruck - postPackages.size();
	}

	public String getColorOfTruck() {
		return colorOfTruck;
	}

	public void setColorOfTruck(String colorOfTruck) {
		this.colorOfTruck = colorOfTruck;
	}

	public int getPossibleMove() {
		return possibleMove;
	}

	public boolean isSelect() {
		return select;
	}

	public void setSelect(boolean select) {
		this.select = select;
	}

	public boolean isTurnOver() {
		return turnOver;
	}

	public void setTurnOver(boolean turnOver) {
		this.turnOver = turnOver;
	}

	public String getEntityInfo() {
		info = "";
		for (PostPackage postPackage : postPackages) {
			info += postPackage.getEntityInfo();
		}
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public ArrayList<PostPackage> getPackages() {
		return postPackages;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean alive) {
		this.visible = alive;
	}

	public boolean isRemoveMe() {
		return removeMe;
	}

	public void setRemoveMe(boolean removeMe) {
		this.removeMe = removeMe;
	}

	@Override
	public int getInfoPosX() {
		return 5;
	}

	@Override
	public int getInfoPosY() {
		return 80;
	}

}
