package game;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import javax.swing.Action;
import java.awt.GridLayout;
import java.awt.Graphics;
import java.awt.Font;
import net.Network;

@SuppressWarnings("serial")
public class MainMenu extends JPanel {

	private static final int FRAMEWIDTH = 600;
	private static final int FRAMEHEIGHT = 400;

	private Truck truck1, truck2;
	private int posX1 = 0;
	private int posX2 = 0;

	private JFrame frame;
	private final Action quickGame = new SwingAction();
	private final Action mediumGame = new SwingAction_1();
	private final Action longGame = new SwingAction_2();

	private ArrayList<Game> games = new ArrayList<Game>();
	private ArrayList<AIBot> bots = new ArrayList<AIBot>();
	private boolean runAi = true;
	private FileOutputStream fos;
	private ObjectOutputStream oos;
	private int save = 0;
	Network network;

	public MainMenu(JFrame frame) {
		truck1 = new Truck(1, 2);
		truck2 = new Truck(2, 2);

		this.frame = frame;
		this.frame.setBounds(0, 0, FRAMEWIDTH, FRAMEHEIGHT);
		this.frame.getContentPane().add(this);
		setBounds(0, 0, FRAMEWIDTH, FRAMEHEIGHT);

		setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(FRAMEWIDTH - 400, 0, 200, 200);

		add(panel);
		animateTrucks();

		JButton btnLongGame = new JButton("Long Game");
		btnLongGame.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
		btnLongGame.setAction(longGame);

		JButton btnNormalGame = new JButton("Normal Game");
		btnNormalGame.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
		btnNormalGame.setAction(mediumGame);
		panel.setLayout(new GridLayout(3, 0, 0, 0));
		panel.add(btnLongGame);
		panel.add(btnNormalGame);

		JButton btnQuickGame = new JButton("Quick Game");
		btnQuickGame.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
		btnQuickGame.setAction(quickGame);
		panel.add(btnQuickGame);

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.drawImage(truck1.getEntityImage(), truck1.getPosX(), truck1.getPosY(), this);
		g.drawImage(truck2.getEntityImage(), truck2.getPosX(), truck2.getPosY(), this);
	}

	private void animateTrucks() {
		new Thread() {
			public void run() {
				while (true) {
					// moving trucks with different speed:
					posX1++;
					posX2 += 2;

					truck1.setPosX(posX1);
					truck1.setPosY(FRAMEHEIGHT - 120);
					truck2.setPosX(posX2);
					truck2.setPosY(FRAMEHEIGHT - 120);
					repaint();
					if (posX2 > FRAMEWIDTH + 250) {
						posX2 = -100;
					}
					if (posX1 > FRAMEWIDTH + 50) {
						posX1 = -100;
					}
					try {
						TimeUnit.MILLISECONDS.sleep(20);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "Quick Game");
		}

		public void actionPerformed(ActionEvent e) {
			setVisible(false);
			new Game(frame, 6, 6, 2, 8, 6);
		}
	}

	private class SwingAction_1 extends AbstractAction {
		public SwingAction_1() {
			putValue(NAME, "Medium Game");
		}

		public void actionPerformed(ActionEvent e) {

			setVisible(false);

			new Thread() {
				public void run() {
					int x = 8;
					int y = 8;
					int trucksForAPlayer = 3;
					int rounds = 10;
					int packages = 10;
					int count = 0;
					int distancesToTrucksBoxes = trucksForAPlayer * 2 * 2;
					int packagesHasBeenPickedOrDelivered = packages * 2;
					int inputs = trucksForAPlayer * 2 * packages * 2 + packages + 4 + distancesToTrucksBoxes + packagesHasBeenPickedOrDelivered;
					Network network1 = new Network(inputs, 2, 64, 1);
					Network network2 = new Network(inputs, 2, 64, 1);
					Game game = new Game(frame, x, y, trucksForAPlayer, rounds, packages);
					AIBot bot = new AIBot(game,game.getmPlayers().get(0), game.getmPlayers().get(1), network1, inputs);

					AIBot bot2 = new AIBot(game,game.getmPlayers().get(1),game.getmPlayers().get(0), network2, inputs);
					
					game.addBot(bot);
					game.addBot(bot2);
					//games.add(game);
					//bots.add(bot);
					//bots.add(bot2);
					boolean aiGo = true;
					game.events();
					while (aiGo) {
						
						
						 if(game.getgameOver() == true) {		
							aiGo = true;
							game.setGameOver(false);
							game.startGame();
							game.events();
						}
					}
				}
			}.start();
	
		}

	}

	/*private synchronized void setNetworkToBots() {
		network = bestNetwork(bots);
		for (int i = 0; i < bots.size(); i++) {
			bots.get(i).getNetwork().getInputLayer().setWeights(network.getInputLayer().getWeights());
			bots.get(i).getNetwork().getInputLayer().setDataActivation(network.getInputLayer().getActivated());
			for (int h = 0; h < network.getHiddenLayers().size(); h++) {
				bots.get(i).getNetwork().getHiddenLayers().get(h)
						.setWeights(network.getHiddenLayers().get(h).getWeights());
				bots.get(i).getNetwork().getHiddenLayers().get(h)
						.setDataActivation(network.getHiddenLayers().get(h).getActivated());
			}

			saveNetwork(network);
		}

	}*/

	/*private void saveNetwork(NeuralNetwork network) {
		save++;
		if (save > 2000) {
			try {
				System.out.println("happening");
				fos = new FileOutputStream("bot.ser");
				oos = new ObjectOutputStream(fos);
				oos.flush();
				oos.writeObject(network);

				oos.close();
				fos.close();
				System.out.println("done");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			save = 0;
		}
	}
*/
	/*
	 * private NeuralNetwork getBestAi(ArrayList<AIBot> bots) { NeuralNetwork
	 * network; int highestScore = 0; for (int i = 0; i < bots.size(); i++) { int
	 * tempScore = 0; if (bots.get(i).score > tempScore) { tempScore =
	 * bots.get(i).prevScore; highestScore = i; } } network =
	 * bots.get(highestScore).getNetwork(); return network; }
	 */
	private class SwingAction_2 extends AbstractAction {
		public SwingAction_2() {
			putValue(NAME, "Long Game");
		}

		public void actionPerformed(ActionEvent e) {
			setVisible(false);
			new Game(frame, 12, 8, 4, 20, 15);
		}
	}

	/*private synchronized NeuralNetwork bestNetwork(ArrayList<AIBot> bots) {
		int bestNetwork = 0;
		int secondBestNetwork = 0;
		int tempScore = 0;
		int tempScoreSecond = 0;
		for (int i = 0; i < bots.size(); i++) {
			if (bots.get(i).score > tempScore) {
				tempScore = bots.get(i).score;
				secondBestNetwork = bestNetwork;
				bestNetwork = i;
			} else if (bots.get(i).score > tempScoreSecond) {
				tempScoreSecond = bots.get(i).score;
				secondBestNetwork = i;
			}
		}

		boolean[] activation = bots.get(bestNetwork).getNetwork().getInputLayer().getActivated();
		for (int j = 0; j < bots.get(secondBestNetwork).getNetwork().getInputLayer().getActivated().length; j++) {
			if (bots.get(secondBestNetwork).getNetwork().getInputLayer().getActivated()[j] == true) {
				activation[j] = true;
			}
		}
		bots.get(bestNetwork).getNetwork().getInputLayer().setDataActivation(activation);

		for (int i = 0; i < bots.get(secondBestNetwork).getNetwork().getHiddenLayers().size(); i++) {
			boolean[] activationHiddenLayer = bots.get(bestNetwork).getNetwork().getHiddenLayers().get(i)
					.getActivated();
			for (int y = 0; y < bots.get(secondBestNetwork).getNetwork().getHiddenLayers().get(i)
					.getActivated().length; y++) {
				if (bots.get(secondBestNetwork).getNetwork().getHiddenLayers().get(i).getActivated()[y] == true) {
					activationHiddenLayer[y] = true;
				}
				bots.get(bestNetwork).getNetwork().getHiddenLayers().get(i).setDataActivation(activationHiddenLayer);
			}
		}

		// return getMeanNetwork(bots.get(bestNetwork).getNetwork(),
		// bots.get(secondBestNetwork).getNetwork());
		return getMeanNetwork(bots.get(bestNetwork).getNetwork(), bots.get(0).getNetwork());
	}

	private NeuralNetwork getMeanNetwork(NeuralNetwork network1, NeuralNetwork network2) {
		MathFunctions function = new MathFunctions();
		network1.getInputLayer().setWeights(
				function.getMeanWeights(network1.getInputLayer().getWeights(), network2.getInputLayer().getWeights()));
		for (int i = 0; i < network1.getHiddenLayers().size(); i++) {
			network1.getHiddenLayers().get(i).setWeights(function.getMeanWeights(
					network1.getHiddenLayers().get(i).getWeights(), network2.getHiddenLayers().get(i).getWeights()));
		}

		return network1;
	}*/
}
