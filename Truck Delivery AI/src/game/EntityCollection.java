package game;
import java.util.ArrayList;

public class EntityCollection {

	private ArrayList<OnPlayground> entitiesOnPlayground;

	public EntityCollection() {
		entitiesOnPlayground = new ArrayList<OnPlayground>();
	}

	public void addEntity(OnPlayground entity) {
		entitiesOnPlayground.add(entity);
	}

	public void removeDeadEntities() {
		for (int i = entitiesOnPlayground.size() - 1; i >= 0; i--) {
			if (entitiesOnPlayground.get(i).isRemoveMe()) {
				//entitiesOnPlayground.remove(i);
			//	entitiesOnPlayground.get(i).setVisible(false);
			}
		}
	}

	public ArrayList<OnPlayground> getEntitiesOnPlayground() {
		return entitiesOnPlayground;
	}
	public void setEntitiesOnPlaygorund() {
		entitiesOnPlayground = null;
	}
	public void reset() {
		for(OnPlayground item : entitiesOnPlayground) {
			item.setVisible(true);
			item.setRemoveMe(false);
			//item.start(0, 0, 0, 0);
			
		}
	}
}
