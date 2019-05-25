package net;
import java.io.Serializable;
import java.util.Random;
import java.io.Serializable;

public class HiddenLayer implements Serializable{

	private float[][] trainingNodes;
	private float[][] delta;
	private float[] singleDelta;
	private float[][] error;
	private float[] singleError;
	private float[] biasWeights;
	private float[][] weights;
	private float[] nodes;
	private MathFunctions functions;
	private int numberOfNodes;
	private int weightsPerNode;

	public HiddenLayer(int numberOfNodesHiddenLayer, int numberOfWeights, MathFunctions functions) {
		this.functions = functions;
		numberOfNodes = numberOfNodesHiddenLayer;
		weightsPerNode = numberOfWeights;
		weights = new float[numberOfNodesHiddenLayer][numberOfWeights];
		biasWeights = new float[numberOfWeights];
		nodes = new float[numberOfNodesHiddenLayer];
		randomizeWeights();
	}

	private void randomizeWeights() {
		Random random = new Random();
		for (int i = 0; i < numberOfNodes; i++) {
			for (int j = 0; j < weightsPerNode; j++) {
				weights[i][j] = random.nextFloat() * (2 - 0) + -1;
			}
		}
		for (int j = 0; j < biasWeights.length; j++) {
			biasWeights[j] = random.nextFloat() * (2 - 0) + -1;
		}
	}

	public float[][] getWeights() {
		return weights;
	}

	public float[] getNodes() {
		return nodes;
	}

	public void setNodes(float[] nodes) {
		this.nodes = nodes;
	}

	public void setTrainingNodes(float[][] x) {
		trainingNodes = x;
	}

	public float[][] getTraingingNodes() {
		return trainingNodes;
	}

	public void setWeights(float[][] x) {
		weights = x;
	}

	public float[][] getDelta() {
		return delta;
	}

	public float[] getSingleDelta() {
		return singleDelta;
	}

	public void setDelta() {
		delta = functions.arrayMultiplication(error, functions.sigmoidPrime(trainingNodes));
		//singleDelta = functions.getMeanValues(functions.arrayMultiplication(error, functions.sigmoidPrime(trainingNodes)));
	}
	
	public void setDeltaRelu() {
		delta = functions.arrayMultiplication(error,
				functions.reluDerivative(trainingNodes));
	}

	public void setSingleDelta() {
		singleDelta = functions.arrayMultiplication(singleError, functions.sigmoidPrime(nodes));
	}

	public float[][] getError() {
		return error;
	}

	public void setError(float[][] deltaFromNextLayer) {
		error = functions.matrixMultiplication(functions.reverseArray(weights), deltaFromNextLayer);
	}

	public void setError(float[] deltaFromNextLayer) {
		singleError = functions.matrixMultiplication(functions.reverseArray(weights), deltaFromNextLayer);
	}
	public float[] getBiasWeights() {
		return biasWeights;
	}

	public void setBiasWeights(float[] biasWeights) {
		this.biasWeights = biasWeights;
	}
}
