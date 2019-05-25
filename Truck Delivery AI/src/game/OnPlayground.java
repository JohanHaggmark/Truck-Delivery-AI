package game;
import java.awt.Image;

public interface OnPlayground {
	public int getPosX();

	public int getPosY();

	public void setPosX(int posX);

	public void setPosY(int posY);

	public Image getEntityImage();

	public String getEntityInfo();

	public boolean isVisible();

	public void setVisible(boolean visible);

	public boolean isRemoveMe();
	
	public void setRemoveMe(boolean remove);

	public int getInfoPosX();

	public int getInfoPosY();
	
	public void start(int posX, int posY, int x, int y);
}
