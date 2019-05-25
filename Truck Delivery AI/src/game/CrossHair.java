package game;
import java.awt.Image;
import javax.swing.ImageIcon;

public class CrossHair implements OnPlayground {

	private int posX;
	private int posY;
	private int belongsToPlayer;
	private boolean Visible = true;
	private boolean removeMe = false;
	private boolean hasMarkedATruck = false;

	public CrossHair(int belongsToPlayer) {		
		start(0,0, 0,0);
		this.belongsToPlayer = belongsToPlayer;
		getEntityImage();
	}

	public void start(int posX, int posY, int x, int y) {
		this.posX = Game.getNUMBEROFSQUARESX() * Game.getSQUAREWIDTH() / 2;
		this.posY = Game.getNUMBEROFSQUARESY() * Game.getSQUAREHEIGHT() / 2;	
	}
	public Image getEntityImage() {
		if (belongsToPlayer == 1) {
			ImageIcon CrossHairIcon = new ImageIcon(getClass().getResource("RedCrossHair.png"));
			Image crossHairImage = CrossHairIcon.getImage();
			return crossHairImage;
		} else if (belongsToPlayer == 2) {
			ImageIcon CrossHairIcon = new ImageIcon(getClass().getResource("BlueCrossHair.png"));
			Image crossHairImage = CrossHairIcon.getImage();
			return crossHairImage;
		}
		return null;
	}

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosX(int crossHairPosX) {
		posX = crossHairPosX;
	}

	public void setPosY(int crossHairPosY) {
		posY = crossHairPosY;
	}

	public int getbelongsToPlayer() {
		return belongsToPlayer;
	}

	public String getEntityInfo() {
		String nothing = "";
		return nothing;
	}

	public boolean isVisible() {
		return Visible;
	}

	public void setVisible(boolean alive) {
		this.Visible = alive;
	}

	public boolean isRemoveMe() {
		return removeMe;
	}

	public void setRemoveMe(boolean removeMe) {
		this.removeMe = removeMe;
	}

	@Override
	public int getInfoPosX() {
		return 0;
	}

	@Override
	public int getInfoPosY() {
		return 0;
	}

	public boolean isHasMarkedATruck() {
		return hasMarkedATruck;
	}

	public void setHasMarkedATruck(boolean hasMarkedATruck) {
		this.hasMarkedATruck = hasMarkedATruck;
	}
}
