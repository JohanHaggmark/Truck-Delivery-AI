package AI;

import net.Network;

public class HeuristicValue {

	public float getValue(Network network, SimulatedGameState sgs) {
		setInputData(network, sgs);
		// network.forwardRelu();
		network.forward();
		return network.getOutputLayer().getNodes()[0];
	}

	private void setInputData(Network network, SimulatedGameState sgs) {
		for (int i = 0; i < sgs.trucks.size(); i++) {
			System.arraycopy(sgs.trucks.get(i).distanceToPackages, 0, network.getInputLayer().getInputData(),
					(2 + sgs.trucks.get(i).distanceToPackages.length * 2) * i,
					sgs.trucks.get(i).distanceToPackages.length);

			System.arraycopy(sgs.trucks.get(i).distanceToPostBoxes, 0, network.getInputLayer().getInputData(),
					sgs.trucks.get(i).distanceToPackages.length
							+ (2 + sgs.trucks.get(i).distanceToPackages.length * 2) * i,
					sgs.trucks.get(i).distanceToPackages.length);

			System.arraycopy(sgs.trucks.get(i).distanceToItsPackagesBoxes, 0, network.getInputLayer().getInputData(),
					sgs.trucks.get(i).distanceToPackages.length * 2
							+ (2 + sgs.trucks.get(i).distanceToPackages.length * 2) * i,
					2);
		}

		for (int i = 0; i < sgs.packages.size(); i++) {
			network.getInputLayer().getInputData()[(sgs.packages.size() * 2 + 2) * sgs.trucks.size() + i*3] = sgs.packages
					.get(i).distanceToPostBox;

			network.getInputLayer().getInputData()[(sgs.packages.size() * 2 + 2) * sgs.trucks.size() + 1 + i*3] = sgs.packages
					.get(i).isPickedUp;
			
			network.getInputLayer().getInputData()[(sgs.packages.size() * 2 + 2) * sgs.trucks.size() + 2 + i*3] = sgs.packages
					.get(i).delivered;
			
		}

		network.getInputLayer().getInputData()[(sgs.packages.size() * 2 + 2) * sgs.trucks.size()
				+ (sgs.packages.size()*3)] = sgs.roundsLeft;
		network.getInputLayer().getInputData()[(sgs.packages.size() * 2 + 2) * sgs.trucks.size() + (sgs.packages.size()*3)
				+ 1] = sgs.myScore/8;
		network.getInputLayer().getInputData()[(sgs.packages.size() * 2 + 2) * sgs.trucks.size() + (sgs.packages.size()*3)
				+ 2] = sgs.opponentScore/8;
		if(sgs.opponentScore>sgs.myScore) {
			network.getInputLayer().getInputData()[(sgs.packages.size() * 2 + 2) * sgs.trucks.size() + (sgs.packages.size()*3)
				                       				+ 2 +1] = -1;
		}else if(sgs.opponentScore<sgs.myScore) {
			network.getInputLayer().getInputData()[(sgs.packages.size() * 2 + 2) * sgs.trucks.size() + (sgs.packages.size()*3)
				                       				+ 2 +1] = 1;
		} else {
			network.getInputLayer().getInputData()[(sgs.packages.size() * 2 + 2) * sgs.trucks.size() + (sgs.packages.size()*3)
				                       				+ 2 +1] = 0;
		}
		
	}

}
