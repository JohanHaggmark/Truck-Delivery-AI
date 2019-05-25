package game;

import java.io.Serializable;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.swing.plaf.synth.SynthSeparatorUI;

import AI.EvolutionTrainingGym;
import AI.GameState;
import AI.TruckAI;
import AI.Write;
import net.Network;

public class AIBot {
	Game game;
	boolean myTurn = false;
	private FileOutputStream fos;
	private ObjectOutputStream oos;
	private FileInputStream fis;
	private ObjectInputStream ois;
	private Network network;
	private int numberOfInputs;
	private static final int THERTI_MINUTES = 60 * 60 * 1000;
	private long lastTime;
	private float error;
	int num = 42;

	public AIBot(Game game, Player player, Player player2) {
		this.game = game;
	}

	public AIBot(Game game, Player player, Player player2, Network network, int numberOfInputs) {
		this.game = game;
		lastTime = System.currentTimeMillis() + THERTI_MINUTES;
		this.network = network;
		// trucks distances To packages and boxes,
		// package distance to its box
		// roundsleft + myScore + opponentScore
		//numberOfInputs = game.getNumberOfTrucksForAPlayer() * game.getNumberOfPlayers() * game.getNumberOfPackages() * 3
			//	+ 3;

		//this.network = new Network(numberOfInputs, 8, 64, 1);
		openSavedNetwork();
		
	}

	private void openSavedNetwork() {
		try {
			fis = new FileInputStream("41bok26.ser");
			ois = new ObjectInputStream(fis);
			network = (Network) ois.readObject();

			System.out.println("read file ok");
			TimeUnit.SECONDS.sleep(3);
			ois.close();
			fis.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void saveNetwork() {
		try {
			fos = new FileOutputStream(Integer.toString(num)+"bok26.ser");
			oos = new ObjectOutputStream(fos);
			oos.flush();
			oos.writeObject(network);

			oos.close();
			fos.close();
			num++;
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void updateExcelStatics() {
		Write write = new Write();
		write.newRow();
		if(game.getmPlayers().get(0).score > game.getmPlayers().get(1).score) {
			write.append(0, 0, 1);
		} else if(game.getmPlayers().get(1).score > game.getmPlayers().get(0).score) {
			write.append(0, 0, -1);
		} else {
			write.append(0, 0, 0);
		}
		//error = error/(float)(game.rounds+3);
		error = error/(float)(game.rounds);
		write.append(0, 1, error);	
		write.close();
		error=0;
		//saveNetwork();
	}

	public Network getNetwork() {
		return network;
	}

	public void moveTrucks(Player player, ArrayList<Truck> trucks, ArrayList<TruckAI> trucksAI) {
		// monteCarloTreeSearch(trucks);

		for (int i = 0; i < trucks.size(); i++) {
			player.aiUnselectSquare(trucks.get(i));
			player.aiMoveTruck(trucks.get(i), trucksAI.get(i).x, trucksAI.get(i).y);
			// trucks.get(i).move(trucksAI.get(i).x, trucksAI.get(i).y);
		}
		//saveNetwork();
		myTurn = false;
		game.nextRound();
	}
	
	public void moveTrucksNetwork(Player player, ArrayList<Truck> trucks, ArrayList<TruckAI> trucksAI) {
		// monteCarloTreeSearch(trucks);

		for (int i = 0; i < trucks.size(); i++) {
			player.aiUnselectSquare(trucks.get(i));
			player.aiMoveTruck(trucks.get(i), trucksAI.get(i).x, trucksAI.get(i).y);
			// trucks.get(i).move(trucksAI.get(i).x, trucksAI.get(i).y);
		}
		//saveNetwork();
		myTurn = false;
		checkIfTimeToSaveNetwork();
		game.nextRound();
	}

	private void checkIfTimeToSaveNetwork() {
		if(System.currentTimeMillis() > lastTime) {
			lastTime = System.currentTimeMillis() + THERTI_MINUTES;
			saveNetwork();
		}
	}

	public void monteCarloTreeSearch(Player player, ArrayList<Truck> trucks) {
		GameState gamestate = new GameState(game.getNumberOfTrucksForAPlayer(), game.getNumberOfPackages(),
				game.getNUMBEROFSQUARESX(), game.getNUMBEROFSQUARESY(), network, game);
		ArrayList<TruckAI> trucksAI = gamestate.getMoves();
		// ArrayList<TruckAI> trucksAI;
		moveTrucks(player, trucks, trucksAI);
	}

	public void monteCarloTreeSearchWithNeuralNetwork(Player player, ArrayList<Truck> trucks) {
		GameState gamestate = new GameState(game.getNumberOfTrucksForAPlayer(), game.getNumberOfPackages(),
				game.getNUMBEROFSQUARESX(), game.getNUMBEROFSQUARESY(), network, game);
		ArrayList<TruckAI> trucksAI = gamestate.getMovesFromNeuralNetwork();
		// ArrayList<TruckAI> trucksAI;
		moveTrucksNetwork(player, trucks, trucksAI);
	}
	public void semiMonteCarloTreeSearchWithNeuralNetwork(Player player, ArrayList<Truck> trucks) {
		GameState gamestate = new GameState(game.getNumberOfTrucksForAPlayer(), game.getNumberOfPackages(),
				game.getNUMBEROFSQUARESX(), game.getNUMBEROFSQUARESY(), network, game);
		ArrayList<TruckAI> trucksAI = gamestate.semiGetMovesFromNeuralNetwork();
		error += gamestate.error;
		// ArrayList<TruckAI> trucksAI;
		moveTrucksNetwork(player, trucks, trucksAI);
	}
}
