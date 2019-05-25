package net;
import java.io.Serializable;

public class OutputLayer implements Serializable{

	private float[] nodes;
	private float[][] error;
	private float[][] myNodes;
	private float[][] delta;
	private float[] singleDelta;

	public OutputLayer(int numberOfNodesOutputLayer) {

		nodes = new float[numberOfNodesOutputLayer];

	}

	public float[][] getError() {
		return error;
	}

	public void setError(float[][] error) {
		this.error = error;
	}

	public float[][] getDelta() {
		return delta;
	}

	public void setDelta(float[][] delta) {
		this.delta = delta;
	}
	public void setDelta(float[] delta) {
		this.singleDelta = delta;
	}
	public void setSingleDelta(float[] delta) {
		this.singleDelta = delta;
	}
	
	public float[] getSingleDelta() {
		return singleDelta;
	}
	public float[][] getTrainingNodes() {
		return myNodes;
	}	
	public float[] getNodes() {
		return nodes;
	}

	public void setNodes(float[][] myNodes) {
		this.myNodes = myNodes;
		
	}
	public void setNodes(float[] nodes) {
		this.nodes = nodes;
		
	}
}
