package AI;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.swing.text.html.HTMLDocument.Iterator;

import net.Network;

public class TreeSearch {

	public ArrayList<TruckAI> trucks;
	public ArrayList<PackAI> packages;
	public ArrayList<PostBoxAI> postBoxes;

	public SimulatedGameState simulatedGameStateOriginal;
	public SimulatedGameState simulatedGameState;
	public SimulatedGameState sgs;
	private Network network;
	private HeuristicValue heuristicValue;
	private SimulationMove simulationMove;
	public float error;
	int roundsLeft;
	int myScore;
	int opponentScore;

	public TreeSearch(ArrayList<TruckAI> trucks, ArrayList<PackAI> packages, ArrayList<PostBoxAI> postBoxes,
			int roundsLeft, int myScore, int opponentScore) {

		simulatedGameState = new SimulatedGameState(trucks.size(), packages.size());
		simulatedGameStateOriginal = new SimulatedGameState(trucks, packages, postBoxes, roundsLeft, myScore,
				opponentScore);
		restoreSimulation();
		simulationMove = new SimulationMove();
	}

	public TreeSearch(ArrayList<TruckAI> trucks, ArrayList<PackAI> packages, ArrayList<PostBoxAI> postBoxes,
			int roundsLeft, int myScore, int opponentScore, Network network) {
		this.network = network;
		heuristicValue = new HeuristicValue();

		simulatedGameState = new SimulatedGameState(trucks.size(), packages.size());
		simulatedGameStateOriginal = new SimulatedGameState(trucks, packages, postBoxes, roundsLeft, myScore,
				opponentScore);
		restoreSimulation();
		simulationMove = new SimulationMove();
	}

	private SimulatedGameState copySimulatedGameState(SimulatedGameState sgsPaste, SimulatedGameState sgs) {
		// return new DeepCopy().getSimulatedGameStateCopy(sgs);
		return copyGameStateNoDeepCopy(sgsPaste, sgs);
	}

	private SimulatedGameState copyGameStateNoDeepCopy(SimulatedGameState sgsPaste, SimulatedGameState sgsCopy) {
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

	private void restoreSimulation() {
		simulatedGameState = copySimulatedGameState(simulatedGameState, simulatedGameStateOriginal);
		this.trucks = simulatedGameState.trucks;
		this.packages = simulatedGameState.packages;
		this.postBoxes = simulatedGameState.postBoxes;
		this.roundsLeft = simulatedGameState.roundsLeft;
		this.myScore = simulatedGameState.myScore;
		this.opponentScore = simulatedGameState.opponentScore;

	}

	public void setTreeSearchGameState(SimulatedGameState sgs) {
		this.trucks = sgs.trucks;
		this.packages = sgs.packages;
		this.postBoxes = sgs.postBoxes;
		this.roundsLeft = sgs.roundsLeft;
		this.myScore = sgs.myScore;
		this.opponentScore = sgs.opponentScore;
	}

	private void updateSimulatedGameStateOriginal() {
		simulatedGameStateOriginal.trucks = trucks;
		simulatedGameStateOriginal.packages = packages;
		simulatedGameStateOriginal.postBoxes = postBoxes;
		simulatedGameStateOriginal.roundsLeft = roundsLeft;
		simulatedGameStateOriginal.myScore = myScore;
		simulatedGameStateOriginal.opponentScore = opponentScore;
	}

	public Position treeSearch(TruckAI truck) {
		simulatedGameState = copySimulatedGameState(simulatedGameState, simulatedGameStateOriginal);
		setTreeSearchGameState(simulatedGameState);
		// ArrayList<Position> positions = getPossibleMoves(truck);
		Position pos = calculateHeuristicValueForAMove(truck);
		// updateSimulatedGameStateOriginal();
		simulationMove.moveTruck(truck, pos, simulatedGameState);
		return pos;

	}

	public Position treeSearchNeuralNetwork(TruckAI truck) {
		simulatedGameState = copySimulatedGameState(simulatedGameState, simulatedGameStateOriginal);
		setTreeSearchGameState(simulatedGameState);
		// ArrayList<Position> positions = getPossibleMoves(truck);
		Position pos = getHeuristicValueFromNetwork(truck);
		// updateSimulatedGameStateOriginal();
		simulationMove.moveTruck(truck, pos, simulatedGameState);
		return pos;

	}

	public Position semiTreeSearchNeuralNetwork(TruckAI truck) {
		simulatedGameState = copySimulatedGameState(simulatedGameState, simulatedGameStateOriginal);
		setTreeSearchGameState(simulatedGameState);
		// ArrayList<Position> positions = getPossibleMoves(truck);
		Position pos = getSemiHeuristicValueFromNetwork(truck);
		// updateSimulatedGameStateOriginal();
		simulationMove.moveTruck(truck, pos, simulatedGameState);
		return pos;
	}

	private Position getSemiHeuristicValueFromNetwork(TruckAI truck) {
		HeuristicValue heuristicValue = new HeuristicValue();
		int timesSimulation = 50000;

		ArrayList<Position> positions = simulationMove.getPossibleMoves(truck, simulatedGameState);
		int indexForTruck = 0;
		for (int i = 0; i < trucks.size(); i++) {
			if (trucks.get(i).x == truck.x && trucks.get(i).x == truck.x) {
				indexForTruck = i;
			}
		}

		float[] heuristicValues = new float[positions.size()];

		// float value[][] = new
		// float[positions.size()][network.getOutputLayer().getNodes().length];
		for (int i = 0; i < positions.size(); i++) {
			simulationMove.moveTruck(trucks.get(indexForTruck), positions.get(i), simulatedGameState);
			for (int j = 0; j < trucks.size(); j++) {
				Distances.setDistanceToPackagesAndPostBoxes(trucks.get(j), packages, postBoxes);
			}
			Distances.setDistanceFromPackageToPostBox(packages, postBoxes);
			heuristicValues[i] = heuristicValue.getValue(network, simulatedGameState);
//			for(int j = 0; j < network.getInputLayer().getInputData().length; j++) {
//				inputData[i][j] = network.getInputLayer().getInputData()[j];
//			}

			restoreSimulation();
		}
		float topValues[];
		float lowValues[];
		int indexes[];
		int[] bestMoves;
		float[][] qValue;
		float inputData[][];

		if (positions.size() >= 6) {

			topValues = new float[4];
			lowValues = new float[2];
			for(int i = 0; i < lowValues.length;i++) {
				lowValues[i] = 1;
			}
			indexes = new int[6];
			bestMoves = new int[6];
			qValue = new float[6][network.getOutputLayer().getNodes().length];
			inputData = new float[6][network.getInputLayer().getInputData().length];
			for (int i = 0; i < heuristicValues.length; i++) {
				float temp;
				int tempi = i;
				int tempIndex;
				for (int j = 0; j < topValues.length; j++) {
					{
						if (heuristicValues[i] > topValues[j]) {
							tempIndex = indexes[j];
							indexes[j] = tempi;
							tempi = tempIndex;

							temp = topValues[j];
							topValues[j] = heuristicValues[i];
							heuristicValues[i] = temp;
						}
					}

				}
				temp = 0;
				tempi = i;
				tempIndex = 0;

				for (int j = 0; j < lowValues.length; j++) {
					{
						if (heuristicValues[i] < lowValues[j] && heuristicValues[i] != 0) {
							tempIndex = indexes[j + topValues.length];
							indexes[j + topValues.length] = tempi;
							tempi = tempIndex;

							temp = lowValues[j];
							lowValues[j] = heuristicValues[i];
							heuristicValues[i] = temp;
						}
					}

				}

			}
		} else {
			topValues = new float[positions.size()];
			lowValues = new float[0];
			indexes = new int[positions.size()];
			bestMoves = new int[positions.size()];
			qValue = new float[positions.size()][network.getOutputLayer().getNodes().length];
			inputData = new float[positions.size()][network.getInputLayer().getInputData().length];
			for (int i = 0; i < heuristicValues.length; i++) {
				float temp;
				int tempi = i;
				int tempIndex;
				for (int j = 0; j < topValues.length; j++) {
					{
						if (heuristicValues[i] > topValues[j]) {
							tempIndex = indexes[j];
							indexes[j] = tempi;
							tempi = tempIndex;

							temp = topValues[j];
							topValues[j] = heuristicValues[i];
							heuristicValues[i] = temp;
						}
					}

				}		
			}
			
		}
		for (int i = 0; i < topValues.length + lowValues.length; i++) {
			simulationMove.moveTruck(trucks.get(indexForTruck), positions.get(indexes[i]), simulatedGameState);
			for (int j = 0; j < trucks.size(); j++) {
				Distances.setDistanceToPackagesAndPostBoxes(trucks.get(j), packages, postBoxes);
			}
			Distances.setDistanceFromPackageToPostBox(packages, postBoxes);
			heuristicValue.getValue(network, simulatedGameState);
			for (int j = 0; j < inputData[0].length; j++) {
				inputData[i][j] = network.getInputLayer().getInputData()[j];
			}
			restoreSimulation();

		}

		for (int i = 0; i < topValues.length + lowValues.length; i++) {

			float timesWon = 0;
			//Simulate all full simulations:
			for (int j = 0; j < timesSimulation / (topValues.length+lowValues.length); j++) {

				simulationMove.moveTruck(trucks.get(indexForTruck), positions.get(indexes[i]), simulatedGameState);

				int score = simulateUntilEndGetHighScore();

				if (simulatedGameState.myScore > simulatedGameState.opponentScore) {
					timesWon = timesWon + 0.9999f;
					bestMoves[i] += score;
				} else if (simulatedGameState.myScore == simulatedGameState.opponentScore) {
					timesWon += 0.5f;
					bestMoves[i] += score;
				} else {
					timesWon = timesWon + 0.0001f;
				}
				restoreSimulation();
			}
			///////////////////////////////////////////
			qValue[i][0] = timesWon / (float) (timesSimulation / (topValues.length+lowValues.length));
		}

		int highest = 0;
		int index = 0;
		float facit = 0;
		float out = 0;
		network.setInputTrainingData(inputData);
		network.forwardingTrainingNodes();
		for (int h = 0; h < bestMoves.length; h++) {
			if (bestMoves[h] > highest) {
				highest = bestMoves[h];
				index = h;

			}
			facit += qValue[h][0];
			out += network.getOutputLayer().getTrainingNodes()[h][0];
		}

		facit = facit / bestMoves.length;
		out = out / bestMoves.length;
		error = (float) Math.sqrt(Math.pow(facit - out, 2));

		network.setInputTrainingData(inputData);
		network.setNodesFacit(qValue);
		// network.forwardingTrainingNodesRelu();
		// network.backPropagationRelu();

		System.out.println(
				" Facit : " + facit + "\n" + " out   : " + out + "\nmean error output layer value: " + (facit - out));

		// network.forwardingTrainingNodes();
		// network.backPropagation();
		network.trainNetworkWithFacit(10, 1f);
//		System.out.println(" Facit : " + network.getNodesFacit()[0][0] + "\n" + 
//				" out   : " + network.getOutputLayer().getTrainingNodes()[0][0] +
//						"\nmean error output layer value: " + network.functions.meanValue(network.getOutputLayer().getError()));

		return positions.get(indexes[index]);

	}

	private Position getHeuristicValueFromNetwork(TruckAI truck) {
		ArrayList<Position> positions = simulationMove.getPossibleMoves(truck, simulatedGameState);
		int indexForTruck = 0;
		for (int i = 0; i < trucks.size(); i++) {
			if (trucks.get(i).x == truck.x && trucks.get(i).x == truck.x) {
				indexForTruck = i;
			}
		}
		float[] heuristicValue = new float[positions.size()];
		int[] bestMoves = new int[positions.size()];
		for (int i = 0; i < positions.size(); i++) {

			simulationMove.moveTruck(trucks.get(indexForTruck), positions.get(i), simulatedGameState);
			MonteCarloTreeSearch mcts = new MonteCarloTreeSearch(simulatedGameState);
			for (int j = 0; j < 10; j++) {

				// monte carlo tree search with help of network to check how good this move is:
				heuristicValue[i] += mcts.search(simulatedGameState, network, "1", j);

				// heuristicValue[i] = getHeuristicValue(network);
				// int score = simulateUntilEnd();
				// searchTreeWithNetwork();
				// if (myScore > opponentScore) {
				// bestMoves[i] += score;
				// }
				restoreSimulation();
				// bestMoves[i] += simulateUntilEndGetHighScore();

			}

			// backpropagate the results::
			float inputData[][] = new float[mcts.visits.table.size()][network.getInputLayer().getInputData().length];
			float facit[][] = new float[mcts.visits.table.size()][network.getOutputLayer().getNodes().length];
			int h = 0;
			for (Object value : mcts.visits.table.values()) {
				FloatInteger fi = (FloatInteger) value;
				for (int j = 0; j < fi.STATE.length; j++) {
					inputData[h][j] = fi.STATE[j];

				}
				for (int j = 0; j < fi.Q; j++) {
					facit[h][j] = fi.Q;

				}
				h++;
				// System.out.println(inputData[18][0] + " predict value");
				// System.out.println(inputData[18][0] + " facit value");
			}
			System.out.println(network.getHiddenLayers().get(network.getHiddenLayers().size() - 1).getWeights()[0][0]
					+ " last weight");
			network.setInputTrainingData(inputData);
			network.setNodesFacit(facit);
			// network.forwardingTrainingNodesRelu();
			// network.backPropagationRelu();

			// network.forwardingTrainingNodes();
			// network.backPropagation();
			network.trainNetworkWithFacit(50);

			// System.out.println(bestMoves[i] + " score of move :" + i);
		}
		float highest = 0;
		int index = 0;
		for (int h = 0; h < heuristicValue.length; h++) {
			if (heuristicValue[h] > highest) {
				highest = heuristicValue[h];
				index = h;
			}

		}
		System.out.println("highest value: " + highest);
		return positions.get(index);
	}

	private float getHeuristicValue(Network network) {
		return heuristicValue.getValue(network, simulatedGameState);
	}

	private Position calculateHeuristicValueForAMove(TruckAI truck) {
		ArrayList<Position> positions = simulationMove.getPossibleMoves(truck, simulatedGameState);
		int indexForTruck = 0;
		for (int i = 0; i < trucks.size(); i++) {

			if (trucks.get(i).x == truck.x && trucks.get(i).x == truck.x) {
				indexForTruck = i;
			}

		}

		int[] bestMoves = new int[positions.size()];
		for (int i = 0; i < positions.size(); i++) {
			// setTreeSearchGameState(simulatedGameState);
			restoreSimulation();

			// this.sgs = copySimulatedGameState(simulatedGameState);
			// setTreeSearchGameState(this.sgs);

			for (int j = 0; j < 50000 / positions.size(); j++) {

				simulationMove.moveTruck(trucks.get(indexForTruck), positions.get(i), simulatedGameState);
				int score = simulateUntilEndGetHighScore();
				if (simulatedGameState.myScore > simulatedGameState.opponentScore) {

					bestMoves[i] += score;
				}
				restoreSimulation();
				// bestMoves[i] += simulateUntilEndGetHighScore();

			}
			// System.out.println(bestMoves[i] + " score of move :" + i);
		}
		int highest = 0;
		int index = 0;
		for (int h = 0; h < bestMoves.length; h++) {
			if (bestMoves[h] > highest) {
				highest = bestMoves[h];
				index = h;
			}

		}
		// return findPackage(positions, truck);

		// simulatedGameStateOriginal = copySimulatedGameState(simulatedGameState);
		// setTreeSearchGameState(simulatedGameStateOriginal);

		return positions.get(index);

	}

	private Position findPackage(ArrayList<Position> positions, TruckAI truck) {
		int index = 0;
		if (truck.packageId[0] == 0) {
			for (int i = 0; i < positions.size(); i++) {
				for (int j = 0; j < packages.size(); j++) {
					if (positions.get(i).x == packages.get(j).x && positions.get(i).y == packages.get(j).y
							&& packages.get(j).delivered == 0) {
						index = i;
					}
				}

			}
		}

		else {
			for (int i = 0; i < positions.size(); i++) {
				for (int j = 0; j < postBoxes.size(); j++) {
					if (positions.get(i).x == postBoxes.get(j).x && positions.get(i).y == postBoxes.get(j).y
							&& postBoxes.get(j).id == truck.packageId[0]) {
						index = i;
					}
				}
			}
		}
		return positions.get(index);
	}

	private void searchTreeWithNetwork() {
		for (int i = 0; i < roundsLeft; i++) {

		}
	}

	private int simulateUntilEnd() {
		for (int i = 0; i < roundsLeft; i++) {
			oneRound();
		}
		if (myScore > opponentScore) {
			return 1;
		} else if (myScore == opponentScore) {
			return 0;
		} else {
			return -1;
		}
	}

	private int simulateUntilEndGetHighScore() {
		for (int i = 0; i < roundsLeft; i++) {
			oneRound();
		}
		return simulatedGameState.myScore + simulatedGameState.opponentScore;
	}

	private void oneRound() {
		for (int i = 0; i < trucks.size() / 2; i++) {
			simulationMove.moveTruck(trucks.get(i), getRandomMove(trucks.get(i)), simulatedGameState);
		}
		nextRound();
		for (int i = trucks.size() / 2; i < trucks.size(); i++) {
			simulationMove.moveTruck(trucks.get(i), getRandomMove(trucks.get(i)), simulatedGameState);
		}

		nextRound();
	}

	private Position getRandomMove(TruckAI truck) {
		Random random = new Random();
		ArrayList<Position> positions = simulationMove.getPossibleMoves(truck, simulatedGameState);
		return positions.get(random.nextInt(positions.size()));
	}

	private void nextRound() {
		for (TruckAI truck : trucks) {
			truck.hasMoved = 0;
		}
		roundsLeft--;
	}
}
