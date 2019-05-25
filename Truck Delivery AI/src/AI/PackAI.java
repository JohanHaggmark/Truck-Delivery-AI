package AI;

import java.io.Serializable;

public class PackAI implements Serializable{

	int id = 0;
	int x = -1;
	int y = -1;
	int distanceToPostBox;
	int delivered = 0;
	int isPickedUp = 0;
	
	
	public void setIsDelivered(Boolean delivered) {
		if(delivered) {
			this.delivered = 1;
		} else {
			this.delivered = 0;
		}
	}
}
