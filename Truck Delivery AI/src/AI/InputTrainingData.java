package AI;

public class InputTrainingData {
	
	public float[][] inputData;
	public float[][] qValues;
	
	public InputTrainingData(int batchSize, int numberOfNodes) {
		inputData = new float[batchSize][numberOfNodes];
		qValues = new float[batchSize][1];
	}
	
	

}
