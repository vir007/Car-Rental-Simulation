package SMRental;

import java.util.ArrayList;

public class Vans {

    ArrayList<Customer> group = new ArrayList<>();
    int numPassengers;
        
    boolean finishedExiting; //flag used to ensure that the van would only move once all the passengers had 
    //finished exiting
    boolean isExiting = false; //flag used to make sure that there is only one customer that is exiting at a time
    boolean isBoarding = false;//flag used to ensure that the van will not leave while there is a passenger boarding
        
    int location;//debug
    int destination;//debug
    boolean isMoving = false;//debug
        
        
    // Required methods to manipulate the group

    protected int getN() { return group.size(); }  // Attribute n

    public Vans() { 
        this.numPassengers = 0;
        this.finishedExiting = true;
    }
}
