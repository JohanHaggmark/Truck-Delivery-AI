package AI;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import game.MainMenu;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	/*	
		ArrayList<NeuralNetwork> networks = new ArrayList<NeuralNetwork>();
		NeuralNetwork network = new NeuralNetwork(3,6,3,3);
		EvolutionTrainingGym gym = new EvolutionTrainingGym(network);
		gym.setWeights();
*/
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame();
				frame.setTitle("Truck Delivery");
				frame.setResizable(false);
				frame.setVisible(true);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				new MainMenu(frame);
			}
		});
		System.out.println("seems ok");
		
	}

}
