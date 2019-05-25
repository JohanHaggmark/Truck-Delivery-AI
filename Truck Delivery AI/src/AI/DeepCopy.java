package AI;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DeepCopy {

	
	public static Object getACopyOf(Object copy) {
		Object object;
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		byte[] byteData;
		try {
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(copy);
			oos.flush();
			oos.close();
			bos.close();
			byteData = bos.toByteArray();

			ByteArrayInputStream bais = new ByteArrayInputStream(byteData);
			try {
				object = (Object) new ObjectInputStream(bais).readObject();
				
				return object;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("not ok");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			return null;
	}
	
	
	public static SimulatedGameState getSimulatedGameStateCopy(SimulatedGameState sgs) {
		SimulatedGameState sgsPasted;
		sgsPasted = (SimulatedGameState)getACopyOf(sgs);	
		return sgsPasted;
	}
}
