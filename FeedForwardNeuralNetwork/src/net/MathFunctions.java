package net;
import java.io.Serializable;
import java.util.ArrayList;

public class MathFunctions implements Serializable {

	public float[][] relu(float[][] x) {
		float[][] temp = new float[x.length][x[0].length];
		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < x[0].length; j++) {
				if (x[i][j] < 0) {
					temp[i][j] = x[i][j] * 0.01f;
				} else {
					temp[i][j] = x[i][j];
				}
			}
		}

		return temp;
	}

	public float[] relu(float[] x) {
		float[] temp = new float[x.length];
		for (int i = 0; i < x.length; i++) {
			if (x[i] < 0) {
				temp[i] = x[i] * 0.01f;
			} else {
				temp[i] = x[i];
			}
		}
		return temp;
	}

	public float[] reluDerivative(float[] x) {
		float[] temp = new float[x.length];
		for (int i = 0; i < x.length; i++) {
			if (x[i] < 0) {
				temp[i] = 0;
			} else {
				temp[i] = 1;
			}
		}
		return temp;
	}

	public float[][] reluDerivative(float[][] x) {
		float[][] temp = new float[x.length][x[0].length];
		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < x[0].length; j++) {
				if (x[i][j] < 0) {
					temp[i][j] = 0;
				} else {
					temp[i][j] = 1;
				}
			}
		}
		return temp;
	}

	public float[][] sigmoid(float[][] x) {
		float[][] tempX = new float[x.length][x[0].length];
		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < x[0].length; j++) {
				tempX[i][j] = (float) (1 / (1 + Math.exp(-x[i][j])));
			}
		}
		return tempX;
	}

	public float[] sigmoid(float[] x) {
		float[] tempX = new float[x.length];
		for (int i = 0; i < x.length; i++) {
			tempX[i] = (float) (1 / (1 + Math.exp(-x[i])));
		}
		return tempX;
	}

	public float[][] sigmoidPrime(float[][] x) {
		float[][] tempX = new float[x.length][x[0].length];
		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < x[i].length; j++) {
				tempX[i][j] = x[i][j] * (1 - x[i][j]);
			}
		}
		return tempX;
	}

	public float[] sigmoidPrime(float[] x) {
		float[] tempX = new float[x.length];
		for (int i = 0; i < x.length; i++) {
			tempX[i] = x[i] * (1 - x[i]);
		}
		return tempX;
	}

	public float[] hyperbolicSigmoid(float[] x) {
		for (int i = 0; i < x.length; i++) {
			x[i] = (float) ((float) (Math.exp(x[i] * 2) - 1) / (Math.exp(x[i] * 2) + 1));
		}
		return x;
	}

	public float[][] hyperbolicSigmoid(float[][] x) {
		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < x[0].length; j++) {
				x[i][j] = (float) ((float) (1 + Math.exp(-x[i][j] * 2)) / (1 - Math.exp(-x[i][j] * 2)));
				System.out.println(x[i][j]);
			}
		}
		return x;
	}

	public float[] matrixMultiplication(float[][] weights, float[] nodes) {
		float[] nextLayer = new float[weights[0].length];
		for (int i = 0; i < weights.length; i++) {
			for (int j = 0; j < weights[0].length; j++) {
				nextLayer[j] += weights[i][j] * nodes[i];
			}
		}
		return nextLayer;
	}

	public float[] matrixMultiplicationWithBias(float[][] weights, float[] nodes, float[] biasWeights) {
		float[] nextLayer = new float[weights[0].length];
		for (int i = 0; i < weights.length; i++) {
			for (int j = 0; j < weights[0].length; j++) {
				nextLayer[j] += weights[i][j] * nodes[i];
			}
		}
		for (int i = 0; i < nextLayer.length; i++) {
			nextLayer[i] += biasWeights[i];
		}
		return nextLayer;
	}

	public float[] multiplication(float[] x, float[] y) {
		float[] z = new float[y.length];
		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < y.length; j++) {
				z[j] += x[i] * y[j];
			}
		}
		return z;
	}

	public float[][] arrayMultiplication(float[][] x, float[][] y) {
		float[][] product = new float[x.length][x[0].length];
		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < x[0].length; j++) {
				product[i][j] = x[i][j] * y[i][j];
			}
		}
		return product;
	}

	public float[] arrayMultiplication(float[] x, float[] y) {
		float[] product = new float[x.length];
		for (int i = 0; i < x.length; i++) {
			product[i] = x[i] * y[i];
		}
		return product;
	}

	public float[] arrayMultiplication(float[] x, float y) {
		float[] product = new float[x.length];
		for (int i = 0; i < x.length; i++) {
			product[i] = x[i] * y;
		}
		return product;
	}

	public float[][] matrixMultiplication(float[][] x, float[][] y) {

		float[][] product;
		// x.length == y[0].length
		// this would do it!::
		// System.out.println("x: " + x.length + " " + x[0].length);
		// System.out.println("y: " + y.length + " " + y[0].length);
		// returning length y, x[0].length
		product = new float[y.length][x[0].length];
		for (int i = 0; i < x[0].length; i++) {
			for (int h = 0; h < y.length; h++) {
				float temp = 0;
				for (int j = 0; j < x.length; j++) {
					temp += x[j][i] * y[h][j];
				}
				product[h][i] = temp;
			}
		}
		return product;
	}

	public float[][] matrixMultiplicationWithBias(float[][] x, float[][] y, float[] z) {

		float[][] product;
		// x.length == y[0].length
		// this would do it!::
		// System.out.println("x: " + x.length + " " + x[0].length);
		// System.out.println("y: " + y.length + " " + y[0].length);
		// returning length y, x[0].length
		product = new float[y.length][x[0].length];
		for (int i = 0; i < x[0].length; i++) {
			for (int h = 0; h < y.length; h++) {
				float temp = 0;
				for (int j = 0; j < x.length; j++) {
					temp += x[j][i] * y[h][j];
				}
				temp += z[i];
				product[h][i] = temp;
			}
		}
		return product;
	}

	public float[][] difference(float[][] x, float[][] y) {
		float[][] error = new float[x.length][x[0].length];
		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < x[0].length; j++) {
				error[i][j] = x[i][j] - y[i][j];
			}
		}
		return error;
	}

	public float[] difference(float[] x, float[] y) {
		float[] error = new float[x.length];
		for (int i = 0; i < x.length; i++) {
			error[i] = x[i] - y[i];
		}
		return error;
	}

	public float[][] reverseArray(float[][] x) {
		float[][] T = new float[x[0].length][x.length];
		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < x[0].length; j++) {
				T[j][i] = x[i][j];
			}
		}
		return T;
	}

	public float[][] sumArrays(float[][] x, float[][] y) {
		float sum[][] = new float[x.length][x[0].length];
		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < x[0].length; j++) {
				sum[i][j] = x[i][j] + y[i][j];
			}
		}
		return sum;
	}

	public float[][] sumArrays(float[][] x, float[] y) {
		float sum[][] = new float[x.length][x[0].length];
		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < x[0].length; j++) {
				sum[i][j] = x[i][j] + y[i];
			}
		}
		return sum;
	}

	public float[][] sumBias(float[] y, float[][] x) {
		//method also makes mean value
		float sum[][] = new float[x.length][x[0].length];
		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < x[0].length; j++) {
				sum[i][j] = x[i][j] + y[j];
			}
		}
		return sum;
	}

	public float[] getSumBias(float[] y, float[][] x) {
		//method also makes mean value
		float sum[] = new float[y.length];
		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < x[0].length; j++) {
				sum[j] = x[i][j] + y[j];
			}
		}
		return sum;
	}

	public float[][] subArrays(float[][] x, float[][] y) {
		float sum[][] = new float[x.length][x[0].length];
		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < x[0].length; j++) {
				sum[i][j] = x[i][j] - y[i][j];
			}
		}
		return sum;
	}

	public float[][] subArrays(float[][] x, float[] y) {
		float sum[][] = new float[x.length][x[0].length];
		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < x[0].length; j++) {
				sum[i][j] = x[i][j] - y[i];
			}
		}
		return sum;
	}

	public float[][] reverseSumArrays(float[][] x, float[] y) {
		float sum[][] = new float[x.length][x[0].length];
		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < x[0].length; j++) {
				sum[i][j] = x[i][j] + y[j];
			}
		}
		return sum;
	}

	public float[] squareArray(float[] x) {
		float[] temp = new float[x.length];
		for (int i = 0; i < x.length; i++) {
			temp[i] = (float) Math.pow(x[i], 2);
		}
		return temp;
	}

	public float[][] squareArray(float[][] x) {
		float[][] temp = new float[x.length][x[0].length];
		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < x[0].length; j++) {
				temp[i][j] = (float) Math.pow(x[i][j], 2);
			}
		}
		return temp;
	}

	public float meanValue(float[][] x) {
		float temp = 0;
		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < x[0].length; j++) {
				temp += x[i][j];
			}
		}
		return temp / (x.length * x[0].length);
	}

	public float meanValue(float[] x) {
		float temp = 0;
		for (int i = 0; i < x.length; i++) {

			temp += x[i];

		}
		return temp / x.length;
	}

	public float[][] setWeightsMeanValue(float[][] x, float[][] y) {
		float[][] tempZ = new float[x.length][x[0].length];
		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < x[0].length; j++) {
				tempZ[i][j] = (x[i][j] + y[i][j]) / 2;
			}
		}
		return tempZ;
	}

	public Network getNetworkWithLowestMeanError(ArrayList<Network> networks) {
		Network tempNetwork = null;

		float meanError = 1000;
		for (Network network : networks) {
			if (meanError > meanValue(network.getOutputLayer().getError())) {
				meanError = meanValue(network.getOutputLayer().getError());
				tempNetwork = network;
			}
		}
		return tempNetwork;
	}

	public float[][] getMeanWeights(float[][] weights1, float[][] weights2) {
		float[][] weights = new float[weights1.length][weights1[0].length];

		for (int i = 0; i < weights1.length; i++) {
			for (int h = 0; h < weights1[0].length; h++) {
				weights[i][h] = (weights1[i][h] + weights2[i][h]) / 2;
			}
		}
		return weights;
	}
	
	

	public float[][] lambda(float l, float[][] x) {
		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < x[0].length; j++) {
				x[i][j] = l * x[i][j];
			}
		}
		return x;
	}

	public float[] lambda(float l, float[] x) {
		for (int i = 0; i < x.length; i++) {
			x[i] = l * x[i];
		}
		return x;
	}
	
/*	public float[] getMeanError(float[][] x) {
		float[] mean = new float[x.length];
		for(int i = 0; i < x.length; i++) {
			for(int j = 0; j < x[0].length; i++) {
				mean[i] = (x[i][j]/j); 
			}		
		}
		return mean;
	}
	*/
	public float[][] getMeanValues(float[][] x) {
		
		for(int i = 0; i < x.length; i++) {
			for(int j = 0; j < x[0].length; j++) {
				x[i][j] = x[i][j]/x.length; 
			}		
		}
		return x;
	}
}
