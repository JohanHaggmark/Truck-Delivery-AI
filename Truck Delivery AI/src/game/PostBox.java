package game;
import java.awt.Image;
import javax.swing.ImageIcon;

public class PostBox implements OnPlayground {

	private int posX;
	private int posY;
	public int number = 0;
	public int x = 0;
	public int y = 0;
	//info is a char that match with a PostPackage's info
	private String info;
	private boolean visible;
	private boolean removeMe = false;
	private Image postBoxImage;

	public PostBox(int posX, int posY, int x, int y,  char info) {
		ImageIcon postBoxIcon = new ImageIcon(getClass().getResource("PostBox.png"));
		postBoxImage = postBoxIcon.getImage();
		this.info = "" + info;
		number = (int) info - 64;
		start(posX, posY, x, y);
	}

	public void start(int posX, int posY, int x, int y) {
		this.x = x;
		this.y = y;
		this.posX = posX;
		this.posY = posY;
		removeMe = false;
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
		return postBoxImage;
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

	public boolean isRemoveMe() {
		return removeMe;
	}

	public void setRemoveMe(boolean removeMe) {
		this.removeMe = removeMe;
		visible = !removeMe;
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
