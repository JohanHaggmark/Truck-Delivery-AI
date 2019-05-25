package AI;

import java.util.ArrayList;

import game.Game;
import game.Truck;
import net.Network;

public class GameState {
	private ArrayList<TruckAI> trucks;
	private ArrayList<PackAI> packages;
	private ArrayList<PostBoxAI> postBoxes;
	private Network network;
	int roundsLeft;
	int myScore;
	int opponentScore;
	public float error;
	
	
	public GameState(int numberOfTrucksForAPlayer, int numberOfPackages, int boardSizeX, int boardSizeY, Network network, Game game) {
		trucks = new ArrayList<TruckAI>();
		packages = new ArrayList<PackAI>();
		postBoxes = new ArrayList<PostBoxAI>();
		this.roundsLeft = game.roundsLeft;
		this.network = network;
		
		for(int i = 0; i < numberOfTrucksForAPlayer*2; i++) {
			trucks.add(new TruckAI(numberOfPackages));
			for(int j = 0; j < trucks.get(i).packageId.length;j++) {
				trucks.get(i).packages[j] = new PackAI();
			}
		}
		for(int i = 0; i < numberOfPackages; i++) {
			packages.add(new PackAI());
			postBoxes.add(new PostBoxAI());
		}
		setGameState(game);
		
		
	}
	
	public void setGameState(Game game) {
		setPackagesAndBoxes(game);
		int turn = game.getPlayerTurn();
		int theOtherPlayer;	
		if(turn == 1) {
			theOtherPlayer = 0;
		} else {
			theOtherPlayer = 1;
		}
		this.myScore = game.getmPlayers().get(turn).score;
		this.opponentScore = game.getmPlayers().get(theOtherPlayer).score;
		
		//for trucks
		for(int i = 0; i < trucks.size()/2; i++) {		
			trucks.get(i).id = i;
			trucks.get(i).x = game.getmPlayers().get(turn).getmTrucks().get(i).x;
			trucks.get(i).y = game.getmPlayers().get(turn).getmTrucks().get(i).y;
			trucks.get(i).setHasMoved(game.getmPlayers().get(turn).getmTrucks().get(i).isTurnOver());
			trucks.get(i).inMyTeam = 1;
			trucks.get(i).distanceToMove = game.getmPlayers().get(turn).getmTrucks().get(i).getNumberOfSquaresToMove();
			setPackagesToTruck(trucks.get(i), game.getmPlayers().get(turn).getmTrucks().get(i));
			//trucks.get(i).packageNum = game.getmPlayers().get(turn).getmTrucks().get(i).getPackages().size();
			Distances.setDistanceToPackagesAndPostBoxes(trucks.get(i+trucks.size()/2), packages, postBoxes);
			for(int j = 0; j < game.getmPlayers().get(turn).getmTrucks().get(i).getPackages().size(); j++) {
				trucks.get(i).packageId[j] = game.getmPlayers().get(turn).getmTrucks().get(i).getPackages().get(j).number;
			}
			for(int j = 0; j < game.getNumberOfPackages(); j++) {
				
				trucks.get(i).distanceToPackages[j] = 
						Math.abs(game.getmPostPackages().get(j).x - trucks.get(i).x) + Math.abs(game.getmPostPackages().get(j).y - trucks.get(i).y);
				trucks.get(i).distanceToPostBoxes[j] = 
						Math.abs(game.getmPostBoxes().get(j).x - trucks.get(i).x) + Math.abs(game.getmPostBoxes().get(j).y - trucks.get(i).y);
			}
			//trucks.get(i).myTurn = 1;
			
			trucks.get(i+trucks.size()/2);
			trucks.get(i+trucks.size()/2).id = i+trucks.size()/2;
			trucks.get(i+trucks.size()/2).x = game.getmPlayers().get(theOtherPlayer).getmTrucks().get(i).x;
			trucks.get(i+trucks.size()/2).y = game.getmPlayers().get(theOtherPlayer).getmTrucks().get(i).y;
			trucks.get(i+trucks.size()/2).setHasMoved(game.getmPlayers().get(theOtherPlayer).getmTrucks().get(i).isTurnOver());
			trucks.get(i+trucks.size()/2).inMyTeam = 0;
			trucks.get(i+trucks.size()/2).distanceToMove = game.getmPlayers().get(theOtherPlayer).getmTrucks().get(i).getNumberOfSquaresToMove();
			trucks.get(i+trucks.size()/2).packageNum = game.getmPlayers().get(theOtherPlayer).getmTrucks().get(i).getPackages().size();
			Distances.setDistanceToPackagesAndPostBoxes(trucks.get(i+trucks.size()/2), packages, postBoxes);
			
			for(int j = 0; j < game.getmPlayers().get(theOtherPlayer).getmTrucks().get(i).getPackages().size(); j++) {
				trucks.get(i+trucks.size()/2).packageId[j] = game.getmPlayers().get(theOtherPlayer).getmTrucks().get(i).getPackages().get(j).number;
			}
			
			for(int j = 0; j < game.getNumberOfPackages(); j++) {
				
				trucks.get(i+trucks.size()/2).distanceToPackages[j] = 
						Math.abs(game.getmPostPackages().get(j).x - trucks.get(i+trucks.size()/2).x) + Math.abs(game.getmPostPackages().get(j).y - trucks.get(i+trucks.size()/2).y);
				trucks.get(i+trucks.size()/2).distanceToPostBoxes[j] = 
						Math.abs(game.getmPostBoxes().get(j).x - trucks.get(i+trucks.size()/2).x) + Math.abs(game.getmPostBoxes().get(j).y - trucks.get(i+trucks.size()/2).y);
			}
		}	
	}
	
	
	private void setPackagesToTruck(TruckAI truckAi, Truck truck) {
		//trucks.get(i).packageNum = game.getmPlayers().get(turn).getmTrucks().get(i).getPackages().size();
		truckAi.packageNum = truck.getPackages().size();
		for(int i = 0; i < truckAi.packageNum; i++) {
			for(int j = 0; j < packages.size(); j++) {
				if(truck.getPackages().get(i).number == packages.get(j).id) {
					truckAi.packages[i] = packages.get(j);
					truckAi.packageId[i] = packages.get(j).id;
				}
			}
		}
	}
	
	private void setPackagesAndBoxes(Game game) {
		for(int i = 0; i < packages.size(); i++) {
			packages.get(i).id = game.getmPostPackages().get(i).number;
			packages.get(i).x = game.getmPostPackages().get(i).x;
			packages.get(i).y = game.getmPostPackages().get(i).y;
			packages.get(i).setIsDelivered(game.getmPostPackages().get(i).isDelivered());
			
			postBoxes.get(i).id = game.getmPostBoxes().get(i).number;
			postBoxes.get(i).x = game.getmPostBoxes().get(i).x;
			postBoxes.get(i).y = game.getmPostBoxes().get(i).y;

			packages.get(i).distanceToPostBox = Math.abs(packages.get(i).x - postBoxes.get(i).x) + Math.abs(packages.get(i).y - postBoxes.get(i).y);
		}
	}
	

	public ArrayList<TruckAI> getMoves() {
		return simulate();
		//return trucks;
	}
	public ArrayList<TruckAI> getMovesFromNeuralNetwork() {
		return simulateNeuralNetwork();
		//return trucks;
	}
	
	public ArrayList<TruckAI> semiGetMovesFromNeuralNetwork() {
		return semiSimulateNeuralNetwork();
		//return trucks;
	}
	
	private ArrayList<TruckAI> simulateNeuralNetwork() {
		TreeSearch treeSearch = new TreeSearch(trucks, packages, postBoxes, roundsLeft, myScore, opponentScore, network);
		SimulatedGameState simulatedGameState;
		simulatedGameState = treeSearch.simulatedGameStateOriginal;
		//unoccupyMyPositions();
		for(int i = 0; i < trucks.size()/2; i++) {
			treeSearch.treeSearchNeuralNetwork(simulatedGameState.trucks.get(i));
		
		}
		return simulatedGameState.trucks;
	}
	
	private ArrayList<TruckAI> semiSimulateNeuralNetwork() {
		TreeSearch treeSearch = new TreeSearch(trucks, packages, postBoxes, roundsLeft, myScore, opponentScore, network);
		SimulatedGameState simulatedGameState;
		simulatedGameState = treeSearch.simulatedGameStateOriginal;
		//unoccupyMyPositions();
		for(int i = 0; i < trucks.size()/2; i++) {
			treeSearch.semiTreeSearchNeuralNetwork(simulatedGameState.trucks.get(i));
			error += treeSearch.error;
		}
		
		return simulatedGameState.trucks;
	}
	
	private ArrayList<TruckAI> simulate() {
		TreeSearch treeSearch = new TreeSearch(trucks, packages, postBoxes, roundsLeft, myScore, opponentScore);
		SimulatedGameState simulatedGameState;
		simulatedGameState = treeSearch.simulatedGameStateOriginal;
		//unoccupyMyPositions();
		for(int i = 0; i < trucks.size()/2; i++) {
			treeSearch.treeSearch(simulatedGameState.trucks.get(i));
		
		}
		return simulatedGameState.trucks;
	}
	
	private void unoccupyMyPositions() {
		for(int i = 0; i < trucks.size()/2; i++) {
			trucks.get(i).recentX = trucks.get(i).x;
			trucks.get(i).recentY = trucks.get(i).y;
			trucks.get(i).x = -1;
			trucks.get(i).y = -1;
		}
	}
	
	
	public ArrayList<TruckAI> getTrucks(){
		return trucks;
	}
}
