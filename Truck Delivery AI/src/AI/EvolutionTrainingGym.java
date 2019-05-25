package AI;



import java.util.Random;

import net.Network;
import net.HiddenLayer;


public class EvolutionTrainingGym {
	Random random;
	private Network network;
	int[] randomOutputs;

	public EvolutionTrainingGym(Network network) {
		this.network = network;
		random = new Random();
	}


	public float[][] randomWeightsInput(float[][] x, boolean[] y) {
		// for(int n = 0; n < network.getInputLayer().getWeights()[0].length; n++) {
		// System.out.println(network.getInputLayer().getWeights()[0][n]);
		// }
		float temp = 0;
		for (int i = 0; i < x.length; i++) {
			if (y[i] == true) {

				for (int j = 0; j < x[0].length; j++) {
					temp = random.nextFloat() * (2f - 0) - 1f;
					// if (x[i][j] > 4 || x[i][j] < -4) {
					// x[i][j] = x[i][j] / 2;
					// }
					if (random.nextInt(100) > 90 * x[i][j]) {
						x[i][j] = temp;
					}
				}
			}
		}
		return x;
	}

	public float[][] randomWeights(float[][] x) {
		float temp = 0;
		// System.out.println(x[2][1]);
		// for(int n = 0; n < network.getHiddenLayers().get(0).getNodes().length; n++) {
		// System.out.println(network.getHiddenLayers().get(0).getNodes()[n]);
		// }
		for (int i = 0; i < x.length; i++) {
			temp = random.nextFloat() * (4f - 0) - 2f;
			for (int j = 0; j < x[0].length; j++) {
				if (random.nextInt(100) > 80) {
					x[i][j] = temp;
					System.out.println("new weight gym 58");
				}
				// if(x[i][j] < 0) {
				// x[i][j] = 0;
				// }
			}
		}
		return x;
	}

	public void bringWeightsBackToLife() {
		network.getInputLayer().setWeights(liftWeights(network.getInputLayer().getWeights()));
		for (HiddenLayer layer : network.getHiddenLayers()) {
			layer.setWeights(liftWeights(layer.getWeights()));
		}
	}

	private float[][] liftWeights(float[][] weights) {
		for (int i = 0; i < weights.length; i++) {
			for (int j = 0; j < weights[0].length; j++) {
				if (weights[i][j] < 0.00000001F) {
					weights[i][j] = 0.0000001F;
				}
				else if(weights[i][j] > 3) {
					weights[i][j] = 1;
				}
			}
		}

		return weights;
	}

	public void setWeightsAfterReward(int reward) {
		float error = (float) (1 / (1 + Math.exp(-(reward - 2)))) - 0.5f;
		network.getOutputLayer().setSingleDelta(network.functions
				.arrayMultiplication(network.functions.sigmoidPrime(network.getOutputLayer().getNodes()), error));
		float[] deltaFromNextLayer = network.getOutputLayer().getSingleDelta();
		for (int i = network.getHiddenLayers().size() - 1; i >= 0; i--) {
			network.getHiddenLayers().get(i).setError(deltaFromNextLayer);
			network.getHiddenLayers().get(i).setSingleDelta();

			network.getHiddenLayers().get(i).setWeights(network.functions.sumArrays(
					network.getHiddenLayers().get(i).getWeights(),
					network.functions.multiplication(deltaFromNextLayer, network.getHiddenLayers().get(i).getNodes())));
			deltaFromNextLayer = network.getHiddenLayers().get(i).getSingleDelta();
		}
		network.getInputLayer().setWeights(network.functions.sumArrays(network.getInputLayer().getWeights(),
				network.functions.multiplication(deltaFromNextLayer, network.getInputLayer().getInputData())));

		// System.out.println(network.getInputLayer().getWeights()[214][2]);
	}

	public void generateRandomOutputs(int counts) {
		randomOutputs = new int[counts];
		for (int i = 0; i < counts; i++) {
			randomOutputs[i] = random.nextInt(7);
			// System.out.println(randomOutputs[i] + " gym 94");
		}
	}

	public int[] getRandomOutputs() {
		return randomOutputs;
	}

	public void storeInputs(int counts) {

	}
}
