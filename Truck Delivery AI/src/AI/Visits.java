package AI;

import java.util.HashMap;

public class Visits {
	public HashMap<String, FloatInteger> table;
	
	public Visits() {
		table = new HashMap<String, FloatInteger>();
	}
	
	public HashMap<String, FloatInteger> get(){
		return table;
	}	
}
