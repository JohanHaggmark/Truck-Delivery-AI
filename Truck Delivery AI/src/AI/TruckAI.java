package AI;

import java.io.Serializable;
import java.util.ArrayList;

public class TruckAI implements Serializable{

	int id;
	public int x, y;
	public int recentX, recentY;
	int inMyTeam;
	int myTurn;
	int hasMoved;
	boolean hasMovedb;
	int packageNum;
	int[] packageId;

	int distanceToMove;
	float[] distanceToPackages;
	float[] distanceToPostBoxes;
	float[] distanceToItsPackagesBoxes;
	PackAI[] packages;
	
	public TruckAI(int numberOfPostBoxes) {
		distanceToPostBoxes = new float[numberOfPostBoxes];
		distanceToPackages = new float[numberOfPostBoxes];
		distanceToItsPackagesBoxes = new float[2];
		packages = new PackAI[2];
		packages[0] = new PackAI();
		packages[1] = new PackAI();
		packageId = new int[2];
		packageId[0] = 0;
		packageId[1] = 0;
	}
	
	public void setHasMoved(boolean moved) {
		hasMovedb = moved;
		if(moved) {
			hasMoved = 1;
		} else {
			hasMoved = 0;
		}
	}
}
