package net;
import java.util.Random;
import java.io.Serializable;

public class InputLayer implements Serializable{

	float[] inputData;
	float[][] weights;
	float[] biasWeights;

	public InputLayer(int numberOfNodesInputLayer, int numberOfNodesHiddenLayer) {

		weights = new float[numberOfNodesInputLayer][numberOfNodesHiddenLayer];
		biasWeights = new float[numberOfNodesHiddenLayer];
		inputData = new float[numberOfNodesInputLayer];
		randomizeWeights();
	}

	private void randomizeWeights() {
		Random random = new Random();
		for (int i = 0; i < weights.length; i++) {
			for (int j = 0; j < weights[0].length; j++) {
				weights[i][j] = random.nextFloat() * (2 - 0) + -1;
			}
		}
		for (int j = 0; j < biasWeights.length; j++) {
			biasWeights[j] = random.nextFloat() * (2 - 0) + -1;
		}
	}

	public void setInputData(float[] inputData) {
		this.inputData = inputData;
	}

	public float[] getInputData() {
		return inputData;
	}

	public float[][] getWeights() {
		return weights;
	}

	public void setWeights(float[][] x) {
		weights = x;
	}

	public float[] getBiasWeights() {
		return biasWeights;
	}

	public void setBiasWeights(float[] biasWeights) {
		this.biasWeights = biasWeights;
	}
}
