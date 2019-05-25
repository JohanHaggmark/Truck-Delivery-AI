package AI;

import java.io.Serializable;
import java.util.ArrayList;

public class SimulatedGameState implements Serializable {

	public ArrayList<TruckAI> trucks;
	public ArrayList<PackAI> packages;
	public ArrayList<PostBoxAI> postBoxes;

	int roundsLeft;
	int myScore;
	int opponentScore;
	
	public SimulatedGameState(int numberOfTrucks, int numberOfPackages) {
		this.trucks = new ArrayList<TruckAI> ();
		this.packages = new ArrayList<PackAI>();
		this.postBoxes = new ArrayList<PostBoxAI>();
		allocateMemory(numberOfTrucks, numberOfPackages);
	}
	
	public void allocateMemory(int numberOfTrucks, int numberOfPackages) {
		for(int i = 0; i < numberOfTrucks; i++) {
			trucks.add(new TruckAI(numberOfPackages));
	
			
		}
		for(int i = 0; i < numberOfPackages; i++) {
			packages.add(new PackAI());
			postBoxes.add(new PostBoxAI());
		}
	}

	public SimulatedGameState(ArrayList<TruckAI> trucks, ArrayList<PackAI> packages, ArrayList<PostBoxAI> postBoxes,
			int roundsLeft, int myScore, int opponentScore) {

		this.trucks = trucks;
		this.packages = packages;
		this.postBoxes = postBoxes;
		this.roundsLeft = roundsLeft;
		this.myScore = myScore;
		this.opponentScore = opponentScore;

	}
}
