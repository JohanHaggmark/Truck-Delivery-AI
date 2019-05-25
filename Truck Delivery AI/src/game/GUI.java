package game;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GUI extends JPanel {

	private ArrayList<OnPlayground> entitiesOnPlayground;
	private Board board;
	private ScoreBoard scoreBoard;

	public GUI(EntityCollection entityCollection, Board board, ScoreBoard scoreBoard) {
		entitiesOnPlayground = entityCollection.getEntitiesOnPlayground();
		this.board = board;
		this.scoreBoard = scoreBoard;
	}

	public void paint(Graphics g) {
		//Painting the Board:
		for (Square square : board.getmSquares()) {

			g.setColor(square.getColor());
			g.fillRect(square.getPosX(), square.getPosY(), square.getWIDTH(), square.getHEIGHT());
			
			g.setColor(Color.BLACK);	
			g.fillRect(square.getPosX(), square.getPosY(), square.getWIDTH(), board.getBORDERTHICKNESS());
			g.fillRect(square.getPosX(), square.getPosY() + square.getHEIGHT(), square.getWIDTH() + board.getBORDERTHICKNESS(), board.getBORDERTHICKNESS());
			
			g.fillRect(square.getPosX(), square.getPosY(), board.getBORDERTHICKNESS(), square.getHEIGHT());
			g.fillRect(square.getPosX() + square.getWIDTH(), square.getPosY(), board.getBORDERTHICKNESS(), square.getHEIGHT() + board.getBORDERTHICKNESS());
		}
		//Painting all entities:
		for (OnPlayground entity : entitiesOnPlayground) {
			if (entity.isVisible()) {
				g.drawImage(entity.getEntityImage(), entity.getPosX(), entity.getPosY(), null);
				g.setFont(new Font("Comic sans MS", Font.PLAIN, 30));
				g.setColor(Color.WHITE);
				g.drawString(entity.getEntityInfo(), entity.getPosX() + entity.getInfoPosX(),
						entity.getPosY() + entity.getInfoPosY());
			}
		}
		//Painting the Score and rounds left of the game:
		g.fillRect(scoreBoard.getPOSX(), scoreBoard.getPOSY(), scoreBoard.getWIDTH(), scoreBoard.getHEIGHT());
		g.setFont(new Font("Comic sans MS", Font.PLAIN, 25));
		g.setColor(Color.black);
		int posY = scoreBoard.getPOSY();
		for (String line : scoreBoard.getScore().split("\n")) {
			g.drawString(line, 10, posY += g.getFontMetrics().getHeight());
		}
		g.drawString(scoreBoard.getRoundsLeft(), scoreBoard.getWIDTH() - scoreBoard.getRoundsLeft().length() * 13, posY -= g.getFontMetrics().getHeight());
	}
}
