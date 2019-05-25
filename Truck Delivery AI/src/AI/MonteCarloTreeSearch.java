package AI;

import java.util.ArrayList;

import net.Network;

public class MonteCarloTreeSearch {

	Visits visits;
	SimulationMove simulationMove;
	HeuristicValue heuristicValue;
	SimulatedGameState sgsToSimulate;
	float Exploration = 0.1f;

	public MonteCarloTreeSearch(SimulatedGameState sgs) {
		visits = new Visits();
		simulationMove = new SimulationMove();
		heuristicValue = new HeuristicValue();
		sgsToSimulate = DeepCopy.getSimulatedGameStateCopy(sgs);
	}

	public float search(SimulatedGameState sgs, Network network, String s, int N) {
		if(sgs.roundsLeft == 0) {
			return gameOver(sgs);
		}
		
		if (!visits.get().containsKey(s)) {
			FloatInteger vn = new FloatInteger();
			vn.N = 0;
			for(int i = 0; i < sgs.trucks.size(); i++) {
				Distances.setDistanceToPackagesAndPostBoxes(sgs.trucks.get(i), sgs.packages, sgs.postBoxes);
			}
			Distances.setDistanceFromPackageToPostBox(sgs.packages, sgs.postBoxes);
			vn.V = heuristicValue.getValue(network, sgs);
			//System.out.println(vn.V);
			vn.Q = 0;
			vn.STATE = network.getInputLayer().getInputData();
			vn.simulatedGameState = DeepCopy.getSimulatedGameStateCopy(sgs);
			visits.get().put(s, vn);
			return vn.V;
		}
	   	SimulatedGameState currentGameState = visits.table.get(s).simulatedGameState;
		TruckAI truck = sgs.trucks.get(getTruckToMove(sgs));
		ArrayList<Position> positions = getMoves(truck, sgs);
		
		float u;
		float maxU = 0;
		int bestMove = 0;
		for (int i = 0; i < positions.size(); i++) {
			simulationMove.moveTruck(truck, positions.get(i), currentGameState);
			float Q = tryGetQValueForState(s + Integer.toString(i));
			float V = heuristicValue.getValue(network, currentGameState);
			int n = numberOfSimulationsForMove(s + Integer.toString(i));

			// Si(score for the current state, update it from recursive moves ie, future
			// moves in this move.
			u = (float) (Q + Exploration * V * Math.sqrt((float) n / 1 + N));

			if (u > maxU) {
				maxU = u;
				bestMove = i;
			}
		copyAndPasteGameState(currentGameState,sgs);
		}
		simulationMove.moveTruck(truck, positions.get(bestMove), sgs);
		float v = search(sgs, network, s + Integer.toString(bestMove), N);
		copyAndPasteGameState(sgs, currentGameState);
		FloatInteger fi = visits.table.get(s);
		fi.Q =(float)(fi.N*fi.Q + v)/(float)(fi.N+1);
		fi.N++;
		N++;
		return v;
	}
	
	
	public float johansSearch(SimulatedGameState sgs, Network network) {
		TruckAI truck = sgs.trucks.get(getTruckToMove(sgs));
		ArrayList<Position> positions = getMoves(truck, sgs);
		
		
		float v = 0;
		
		
		
		return v;
	}
	
	
	private float gameOver(SimulatedGameState sgs) {
		if(sgs.myScore > sgs.opponentScore) {
			return 1;
		} else if(sgs.opponentScore >sgs.myScore) {
			return 0;
		} else {
			return 0.5f;
		}
	}
	

	private float tryGetQValueForState(String s) {
		float Q = 0;
		try {
			FloatInteger fi = visits.table.get(s);
			Q = fi.Q;
		} catch (NullPointerException e) {

		}
		return Q;
	}

	private int numberOfSimulationsForMove(String s) {
		int n = 0;
		try {
			FloatInteger fi = visits.table.get(s);
			n = fi.N;
		} catch (NullPointerException e) {

		}
		return n;
	}

	private int getTruckToMove(SimulatedGameState sgs) {
		for (int i = 0; i < sgs.trucks.size(); i++) {
			if (sgs.trucks.get(i).hasMoved == 0) {
				return i;
			}
		}
		simulationMove.nextRound(sgs.trucks, sgs.roundsLeft);
		return 0;
	}

	private ArrayList<Position> getMoves(TruckAI truck, SimulatedGameState sgs) {
		return simulationMove.getPossibleMoves(truck, sgs);
	}
	
	private SimulatedGameState copyAndPasteGameState(SimulatedGameState sgsPaste, SimulatedGameState sgsCopy) {
		sgsPaste.myScore = sgsCopy.myScore;
		sgsPaste.opponentScore = sgsCopy.opponentScore;
		sgsPaste.roundsLeft = sgsCopy.roundsLeft;
		for (int i = 0; i < sgsCopy.trucks.size(); i++) {
			sgsPaste.trucks.get(i).id = sgsCopy.trucks.get(i).id;
			sgsPaste.trucks.get(i).x = sgsCopy.trucks.get(i).x;
			sgsPaste.trucks.get(i).y = sgsCopy.trucks.get(i).y;
			sgsPaste.trucks.get(i).recentX = sgsCopy.trucks.get(i).recentX;
			sgsPaste.trucks.get(i).recentY = sgsCopy.trucks.get(i).recentY;
			sgsPaste.trucks.get(i).inMyTeam = sgsCopy.trucks.get(i).inMyTeam;
			sgsPaste.trucks.get(i).myTurn = sgsCopy.trucks.get(i).myTurn;
			sgsPaste.trucks.get(i).hasMoved = sgsCopy.trucks.get(i).hasMoved;
			sgsPaste.trucks.get(i).hasMovedb = sgsCopy.trucks.get(i).hasMovedb;
			sgsPaste.trucks.get(i).packageNum = sgsCopy.trucks.get(i).packageNum;
			sgsPaste.trucks.get(i).distanceToMove = sgsCopy.trucks.get(i).distanceToMove;
			for (int j = 0; j < sgsCopy.packages.size(); j++) {
				sgsPaste.trucks.get(i).distanceToPackages[j] = sgsCopy.trucks.get(i).distanceToPackages[j];
				sgsPaste.trucks.get(i).distanceToPostBoxes[j] = sgsCopy.trucks.get(i).distanceToPostBoxes[j];
			}
			for (int h = 0; h < 2; h++) {
				sgsPaste.trucks.get(i).packages[h] = sgsCopy.trucks.get(i).packages[h];
				sgsPaste.trucks.get(i).packageId[h] = sgsCopy.trucks.get(i).packageId[h];
			}
		}

		for (int i = 0; i < sgsCopy.packages.size(); i++) {
			sgsPaste.packages.get(i).id = sgsCopy.packages.get(i).id;
			sgsPaste.packages.get(i).x = sgsCopy.packages.get(i).x;
			sgsPaste.packages.get(i).y = sgsCopy.packages.get(i).y;
			sgsPaste.packages.get(i).distanceToPostBox = sgsCopy.packages.get(i).distanceToPostBox;
			sgsPaste.packages.get(i).delivered = sgsCopy.packages.get(i).delivered;
			sgsPaste.packages.get(i).isPickedUp = sgsCopy.packages.get(i).isPickedUp;

			sgsPaste.postBoxes.get(i).id = sgsCopy.postBoxes.get(i).id;
			sgsPaste.postBoxes.get(i).x = sgsCopy.postBoxes.get(i).x;
			sgsPaste.postBoxes.get(i).y = sgsCopy.postBoxes.get(i).y;
		}

		return sgsPaste;
	}
}
