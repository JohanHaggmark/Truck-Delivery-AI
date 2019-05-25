package AI;

import java.util.ArrayList;

public class SimulationMove {

	public void moveTruck(TruckAI truck, Position position, SimulatedGameState sgs) {
		if (truck.hasMoved == 0) {
			truck.x = position.x;
			truck.y = position.y;
			truck.hasMoved = 1;

			// pick up package
			for (int i = 0; i < sgs.packages.size(); i++) {
				if (sgs.packages.get(i).x == truck.x && sgs.packages.get(i).y == truck.y) {
					if (sgs.packages.get(i).delivered == 0 && sgs.packages.get(i).isPickedUp == 0) {
						for (int j = 0; j < truck.packageId.length; j++) {
							if (truck.packageId[j] == 0 && sgs.packages.get(i).isPickedUp == 0) {
								truck.packageId[j] = sgs.packages.get(i).id;
								sgs.packages.get(i).isPickedUp = 1;
								truck.packages[j] = sgs.packages.get(i);
								truck.packageNum++;
								// System.out.println("227 picked up package: " + i + " id: " +
								// packages.get(i).id);
							}

							/*
							 * if (truck.packageId[0] == 0) { truck.packageId[0] = packages.get(i).id;
							 * truck.packages[0] = packages.get(i); packages.get(i).isPickedUp = 1;
							 * truck.distanceToMove--; } else if (truck.packageId[1] == 0 &&
							 * truck.packageId[0] != packages.get(i).id) { truck.packageId[1] =
							 * packages.get(i).id; truck.packages[1] = packages.get(i);
							 * truck.distanceToMove--; }
							 */
						}
					}
				}
			}
			for (int i = 0; i < 2; i++) {
				try {
					truck.packages[i].x = truck.x;
					truck.packages[i].y = truck.y;
				} catch (NullPointerException e) {

				}

			}
			//Distances.setDistanceToPackagesAndPostBoxes(truck, sgs.packages, sgs.postBoxes);

			for (int i = 0; i < sgs.postBoxes.size(); i++) {
				if (truck.x == sgs.postBoxes.get(i).x && truck.y == sgs.postBoxes.get(i).y) {

					for (int j = 0; j < truck.packageId.length; j++) {
						// System.out.println("254, trucks packages Id's: " + truck.packageId[j] + "
						// postboxesId: " + postBoxes.get(i).id);
				
						try {
							if (truck.packageId[j] == sgs.postBoxes.get(i).id) {
								truck.packages[j].delivered = 1;
								truck.packageId[j] = 0;
								truck.packages[j] = new PackAI();
								truck.distanceToMove++;
								truck.packageNum--;						
								if (truck.inMyTeam == 1) {
									sgs.myScore++;
								} else {
									sgs.opponentScore++;
								}

							}
						} catch (NullPointerException e) {
							// System.out.println(e);
						}

					}

				}
			}
		}
	}
	
	public ArrayList<Position> getPossibleMoves(TruckAI truck, SimulatedGameState sgs) {
		ArrayList<Position> positions = new ArrayList<Position>();
		int x;
		int y;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				// x = Math.abs(truck.recentX - i);
				// y = Math.abs(truck.recentY - j);
				x = Math.abs(truck.x - i);
				y = Math.abs(truck.y - j);
				if (x + y < truck.distanceToMove) {
					boolean add = true;

					for (int h = 0; h < sgs.trucks.size(); h++) {
						/*
						 * if (i == trucks.get(h).x && j == trucks.get(h).y && !(i == truck.recentX && j
						 * == truck.recentY)) { add = false; // positions.remove(positions.size() - 1);
						 * }
						 */
						if (i == sgs.trucks.get(h).x && j == sgs.trucks.get(h).y && !(i == truck.x && j == truck.y)) {
							add = false;
							// positions.remove(positions.size() - 1);
						}
					}
					if (add) {
						positions.add(new Position(i, j));
					}
				}
			}
		}
		return positions;
	}
	public void nextRound(ArrayList<TruckAI> trucks, int roundsLeft) {
		for (TruckAI truck : trucks) {
			truck.hasMoved = 0;
		}
		roundsLeft--;
	}
}
