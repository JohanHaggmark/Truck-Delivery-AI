package game;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.FlowLayout;

@SuppressWarnings("serial")
public class GameOver extends JPanel {

	private JFrame frame;
	ScoreBoard scoreBoard;

	public GameOver(JFrame frame, ScoreBoard scoreBoard, int FRAMEWIDTH, int FRAMEHEIGHT) {
		this.scoreBoard = scoreBoard;
		this.frame = frame;
		this.frame.getContentPane().add(this);
		setBounds(0, 0, FRAMEWIDTH, FRAMEHEIGHT);
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		results();
	}

	public void results() {
		JLabel label = new JLabel();
		label.setFont(new Font("Comic Sans MS", Font.BOLD, 20));

		if (scoreBoard.getPlayers().get(0).getScore() > scoreBoard.getPlayers().get(1).getScore()) {
			label.setText("Red player won the game!");
		} else if (scoreBoard.getPlayers().get(0).getScore() < scoreBoard.getPlayers().get(1).getScore()) {
			label.setText("Blue player won the game!");
		} else if (scoreBoard.getPlayers().get(0).getScore() == scoreBoard.getPlayers().get(1).getScore()) {
			label.setText("It's a tie!");
		}
		add(label);
	}
}
