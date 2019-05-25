package game;
import java.awt.Image;

import javax.swing.ImageIcon;

public class PostPackage implements OnPlayground {

	private int posX;
	private int posY;
	//info is a char that match with a PostBox's info
	private String info;
	private boolean visible = true;
	private boolean delivered = false;
	private boolean removeMe = false;
	private Image packageImage;
	public int number = 0;
	public int x = 0;
	public int y = 0;
	public int distanceToPostBoxes;

	public PostPackage(int posX, int posY, int x, int y ,char info) {
		ImageIcon packageIcon = new ImageIcon(getClass().getResource("Package.png"));
		packageImage = packageIcon.getImage();
		this.info = "" + info;
		number = (int) info - 64;
		start(posX, posY, x, y);
	}
	
	public void start(int posX, int posY, int x, int y) {
		this.posX = posX;
		this.posY = posY;
		this.x = x;
		this.y = y;
		delivered = false;
		visible = true;
	}

	@Override
	public int getPosX() {
		return posX;
	}

	@Override
	public int getPosY() {
		return posY;
	}

	@Override
	public void setPosX(int posX) {
		this.posX = posX;
		x = posX /100;
	}

	@Override
	public void setPosY(int posY) {
		this.posY = posY;
		y = posY /100;
	}

	@Override
	public Image getEntityImage() {

		return packageImage;
	}

	@Override
	public String getEntityInfo() {
		return info;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean alive) {
		this.visible = alive;
	}

	public boolean isDelivered() {
		return delivered;
	}

	public void setDelivered(boolean delivered) {
		this.delivered = delivered;
	}
	
	public boolean isRemoveMe() {
		return removeMe;
	}

	public void setRemoveMe(boolean removeMe) {
		this.removeMe = removeMe;
		//this.visible = false;
	}

	@Override
	public int getInfoPosX() {
		return 5;
	}

	@Override
	public int getInfoPosY() {
		return 30;
	}
}
