package net;

import java.io.Serializable;
import java.util.ArrayList;



public class Network implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<HiddenLayer> myHiddenLayers = new ArrayList<HiddenLayer>();
	private InputLayer inputLayer;
	private OutputLayer outputLayer;
	public MathFunctions functions;

	private float[][] nodesFacit;
	private float[][] inputTrainingData;
	private float learningRate = 0.1f;

	public Network(int numberOfNodesInputLayer, int numberOfHiddenLayers, int numberOfNodesHiddenLayer,
			int numberOfNodesOutputLayer) {
		functions = new MathFunctions();
		this.inputLayer = new InputLayer(numberOfNodesInputLayer, numberOfNodesHiddenLayer);

		for (int i = 0; i < numberOfHiddenLayers; i++) {
			if (i == numberOfHiddenLayers - 1) {
				myHiddenLayers.add(new HiddenLayer(numberOfNodesHiddenLayer, numberOfNodesOutputLayer, functions));
			} else {
				myHiddenLayers.add(new HiddenLayer(numberOfNodesHiddenLayer, numberOfNodesHiddenLayer, functions));
			}
		}
		this.outputLayer = new OutputLayer(numberOfNodesOutputLayer);
	}

	public void forwardRelu() {

		float[] nextLayer;
		nextLayer = functions.relu(functions.matrixMultiplicationWithBias(inputLayer.getWeights(), inputLayer.getInputData(),
				inputLayer.getBiasWeights()));

		for (HiddenLayer layer : myHiddenLayers) {
			layer.setNodes(nextLayer);
			nextLayer = functions.relu(functions.matrixMultiplicationWithBias(layer.getWeights(), layer.getNodes(),
					layer.getBiasWeights()));
		}
		outputLayer.setNodes(functions.sigmoid(nextLayer));
		System.out.println(outputLayer.getNodes()[0] + " out");
	}

	public void forward() {

		float[] nextLayer;
		// inputLayer.setInputData(inputData);
		nextLayer = functions.sigmoid(functions.matrixMultiplicationWithBias(inputLayer.getWeights(),
				inputLayer.getInputData(), inputLayer.getBiasWeights()));
		for (HiddenLayer layer : myHiddenLayers) {
			layer.setNodes(nextLayer);
			nextLayer = functions.sigmoid(functions.matrixMultiplicationWithBias(layer.getWeights(), layer.getNodes(),
					layer.getBiasWeights()));
		}
		outputLayer.setNodes(nextLayer);
		//System.out.println(outputLayer.getNodes()[0] + " out");
	}

	public void forwardWithoutBias() {
		float[] nextLayer;
		// inputLayer.setInputData(inputData);
		nextLayer = functions
				.sigmoid(functions.matrixMultiplication(inputLayer.getWeights(), inputLayer.getInputData()));
		for (HiddenLayer layer : myHiddenLayers) {
			layer.setNodes(nextLayer);
			nextLayer = functions.sigmoid(functions.matrixMultiplication(layer.getWeights(), layer.getNodes()));
		}
		outputLayer.setNodes(nextLayer);

	}

	public void forwardingTrainingNodes() {
		float[][] nextLayer = new float[inputTrainingData.length][inputLayer.getWeights()[0].length];
		nextLayer = functions.sigmoid(functions.matrixMultiplicationWithBias(inputLayer.getWeights(), inputTrainingData,
				inputLayer.getBiasWeights()));
		for (HiddenLayer layer : myHiddenLayers) {
			layer.setTrainingNodes(nextLayer);
			nextLayer = functions.sigmoid(functions.matrixMultiplicationWithBias(layer.getWeights(),
					layer.getTraingingNodes(), layer.getBiasWeights()));
		}
		outputLayer.setNodes(nextLayer);
	}

	public void forwardingTrainingNodesWithoutBias() {
		float[][] nextLayer = new float[inputTrainingData.length][inputLayer.getWeights()[0].length];
		nextLayer = functions.sigmoid(functions.matrixMultiplication(inputLayer.getWeights(), inputTrainingData));
		for (HiddenLayer layer : myHiddenLayers) {
			layer.setTrainingNodes(nextLayer);
			nextLayer = functions
					.sigmoid(functions.matrixMultiplication(layer.getWeights(), layer.getTraingingNodes()));
		}
		outputLayer.setNodes(nextLayer);
	}

	public void forwardingTrainingNodesRelu() {
		float[][] nextLayer = new float[inputTrainingData.length][inputLayer.getWeights()[0].length];
		nextLayer = functions.relu(functions.matrixMultiplicationWithBias(inputLayer.getWeights(), inputTrainingData,
				inputLayer.getBiasWeights()));
		for (HiddenLayer layer : myHiddenLayers) {
			layer.setTrainingNodes(nextLayer);
			nextLayer = functions.relu(functions.matrixMultiplicationWithBias(layer.getWeights(),
					layer.getTraingingNodes(), layer.getBiasWeights()));
		}
		outputLayer.setNodes(functions.sigmoid(nextLayer));
		System.out.println(outputLayer.getNodes()[0] + " out");
	}

	public void backPropagationRelu() {
		//learningRate = learningRate/inputTrainingData.length;
		outputLayer.setError(functions.difference(nodesFacit, outputLayer.getTrainingNodes()));
		// outputLayer.setDelta(functions.arrayMultiplication(outputLayer.getError(),
		// functions.sigmoidPrime(outputLayer.getTrainingNodes())));
		float[][] deltaFromNextLayer = functions.arrayMultiplication(outputLayer.getError(),
				functions.reluDerivative(outputLayer.getTrainingNodes()));
		for (int h = myHiddenLayers.size() - 1; h >= 0; h--) {
			// functions.normalize(deltaFromNextLayer);
			myHiddenLayers.get(h).setError(deltaFromNextLayer);
			myHiddenLayers.get(h).setDeltaRelu();

			myHiddenLayers.get(h)
					.setBiasWeights(functions.getSumBias(myHiddenLayers.get(h).getBiasWeights(),
							functions.lambda(learningRate, 
									functions.getMeanValues(
											functions.matrixMultiplication(deltaFromNextLayer,
									functions.reverseArray(myHiddenLayers.get(h).getTraingingNodes()))))));

			myHiddenLayers.get(h)
					.setWeights(functions.sumArrays(myHiddenLayers.get(h).getWeights(),
							functions.lambda(learningRate, 
									functions.getMeanValues(
											functions.matrixMultiplication(deltaFromNextLayer,
									functions.reverseArray(myHiddenLayers.get(h).getTraingingNodes()))))));
			deltaFromNextLayer = myHiddenLayers.get(h).getDelta();
		}

		inputLayer.setBiasWeights(functions.getSumBias(inputLayer.getBiasWeights(), functions.lambda(learningRate,
				functions.getMeanValues(
						functions.matrixMultiplication(deltaFromNextLayer, functions.reverseArray(inputTrainingData))))));

		inputLayer.setWeights(functions.sumArrays(inputLayer.getWeights(), functions.lambda(learningRate,
				functions.getMeanValues(
						functions.matrixMultiplication(deltaFromNextLayer, functions.reverseArray(inputTrainingData))))));
	/*	for (int h = 0; h < nodesFacit[0].length; h++) {
			System.out.println(" Facit : " + nodesFacit[0][h]);
			System.out.println(" out   : " + outputLayer.getTrainingNodes()[1][h]);
			System.out.println("mean error output layer value: " + functions.meanValue(outputLayer.getError()));
		}*/
	}

	public void backPropagation() {
		outputLayer.setError(functions.difference(nodesFacit, outputLayer.getTrainingNodes()));
		outputLayer.setDelta((functions.arrayMultiplication(outputLayer.getError(),
				functions.sigmoidPrime(outputLayer.getTrainingNodes()))));
		float[][] deltaFromNextLayer = functions.arrayMultiplication(outputLayer.getError(),
				functions.sigmoidPrime(outputLayer.getTrainingNodes()));
		for (int h = myHiddenLayers.size() - 1; h >= 0; h--) {
			myHiddenLayers.get(h).setError(deltaFromNextLayer);
			myHiddenLayers.get(h).setDelta();
			myHiddenLayers.get(h)
					.setBiasWeights(functions.getSumBias(myHiddenLayers.get(h).getBiasWeights(),
							functions.lambda(learningRate,functions.getMeanValues(functions.matrixMultiplication(deltaFromNextLayer,
									functions.reverseArray(myHiddenLayers.get(h).getTraingingNodes()))))));

			myHiddenLayers.get(h)
					.setWeights(functions.sumArrays(myHiddenLayers.get(h).getWeights(),
							functions.lambda(learningRate,functions.getMeanValues(functions.matrixMultiplication(
							deltaFromNextLayer, functions.reverseArray(myHiddenLayers.get(h).getTraingingNodes()))))));
			deltaFromNextLayer = myHiddenLayers.get(h).getDelta();
		}
		inputLayer.setBiasWeights(functions.getSumBias(inputLayer.getBiasWeights(),functions.lambda(learningRate,
				functions.getMeanValues(
						functions.matrixMultiplication(
								deltaFromNextLayer, functions.reverseArray(inputTrainingData))))));

		inputLayer.setWeights(functions.sumArrays(inputLayer.getWeights(),
				functions.lambda(learningRate,
						functions.getMeanValues(
								functions.matrixMultiplication(
										deltaFromNextLayer, functions.reverseArray(inputTrainingData))))));

		//for (int g = 0; g < nodesFacit.length; g++) {
		//	for (int h = 0; h < nodesFacit[0].length; h++) {
//				System.out.println(" Facit : " + nodesFacit[0][0]);
//				System.out.println(" out   : " + outputLayer.getTrainingNodes()[0][0]);
//			//}
//		//	System.out.println("\n" + "\n");
//		//}
		//System.out.println("mean error output layer value: " + functions.meanValue(outputLayer.getError()));
	}

	public float getMeanError() {
		return functions.meanValue(outputLayer.getError());
	}

	public void trainNetworkWithFacit(int numberOfRounds) {
		for (int i = 0; i < numberOfRounds; i++) {
			forwardingTrainingNodes();
			backPropagation();
		}
	}

	public void setInputTrainingData(float[][] inputTrainingData) {
		this.inputTrainingData = inputTrainingData;
	}

	public void trainNetworkWithFacit(int numberOfRounds, float learningRate) {
		this.learningRate = learningRate/(float)inputTrainingData.length;
		this.learningRate = learningRate;
		for (int i = 0; i < numberOfRounds; i++) {
			forwardingTrainingNodes();
			backPropagation();
		}
	}
	
	public void trainNetworkWithFacit(float[][] inputTrainingData, float[][] nodesFacit, int numberOfRounds, int learningRate) {
		this.learningRate = learningRate;
		setInputTrainingData(inputTrainingData);
		setNodesFacit(nodesFacit);
		for (int i = 0; i < numberOfRounds; i++) {
			forwardingTrainingNodes();
			backPropagation();
		}
	}
	
	public void setNodesFacit(float[][] nodesFacit) {
		this.nodesFacit = nodesFacit;
	}

	public OutputLayer getOutputLayer() {
		return outputLayer;
	}

	public InputLayer getInputLayer() {
		return inputLayer;
	}

	public ArrayList<HiddenLayer> getHiddenLayers() {
		return myHiddenLayers;
	}

	public float[][] getNodesFacit() {
		return nodesFacit;
	}

	public float[][] getInputTrainingData() {
		return inputTrainingData;
	}

}