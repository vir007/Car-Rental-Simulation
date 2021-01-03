package SMRental;

import java.util.ArrayList;

public class VanLines {
	//Identifiers used to select an individual vanline are 
	//Constants. (TERM1, TERM2, RENTAL, DROPOFF). 
	//The first three match the identifier used to select customer line.
	//This is because at those points the vanline and the customerline are in the
	//same location.
    protected ArrayList<Integer> qVans = new ArrayList<>();
    //vans are represented by their global identifier
    //that is, their index of rgVans[]
    protected int getN() {
        return this.qVans.size(); 
    }  // Attribute n
    protected void spInsertQue(Integer vanId){ 
        qVans.add(vanId); 
    }
    protected int spRemoveQue() {
        return qVans.remove(0);
    }
}
