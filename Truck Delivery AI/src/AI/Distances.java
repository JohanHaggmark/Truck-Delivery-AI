package AI;

import java.util.ArrayList;

public class Distances {
	
	public static int normalize = 10;

	public static void setDistanceToPackagesAndPostBoxes(TruckAI truck, ArrayList<PackAI> packages, ArrayList<PostBoxAI> postBoxes ) {
		for(int i = 0; i < packages.size(); i++) {
			truck.distanceToPackages[i] = (Math.abs(truck.x - packages.get(i).x) + Math.abs(truck.y - packages.get(i).y))/ normalize;
			truck.distanceToPostBoxes[i] = (Math.abs(truck.x - postBoxes.get(i).x) + Math.abs(truck.y - postBoxes.get(i).y))/ normalize;
		}
		for(int i = 0; i < 2; i++) {
			if(truck.packageId[i] != 0) {
				truck.distanceToItsPackagesBoxes[i] = (Math.abs(truck.x - postBoxes.get(truck.packageId[i]-1).x) + Math.abs(truck.y - postBoxes.get(truck.packageId[i]-1).y))/ normalize;
				
			}else {
				truck.distanceToItsPackagesBoxes[i] = -1;
			}
			
		}
	}
	
	public static void setDistanceFromPackageToPostBox(ArrayList<PackAI> packages, ArrayList<PostBoxAI> postBoxes) {
		for(int i = 0; i < packages.size(); i++) {
			packages.get(i).distanceToPostBox = (Math.abs(packages.get(i).x - 
					postBoxes.get(i).x) + Math.abs(packages.get(i).y - postBoxes.get(i).y))/ normalize;
		}
	}
}
