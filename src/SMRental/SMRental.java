package SMRental;


import java.util.Arrays;

import simulationModelling.AOSimulationModel;
import simulationModelling.Behaviour;

public class SMRental extends AOSimulationModel {
        
    // Parameters
    int numVans;
    int vanCapacity;

    // Entities
    protected Vans [] rgVans;
    protected CustomerLine [] qCustomerLine = new CustomerLine[Constants.NUM_CUST_LINES];
    protected VanLines [] qVanLines = new VanLines[Constants.NUM_CUST_LINES];
    protected RentalCounter rgRentalCounter;

    // Random variate procedures
    RVPs rvp;

    // Determinate Variate Procedures
    DVPs dvp=new DVPs();
        
    // User Defined Procedures
    UDPs udp = new UDPs(this);
        
    // Outputs
    public Output output;
    
    public Output getOutput() {
        return this.output; 
    }
    
    boolean traceFlag;
    
    // Constructor
    public SMRental(double t0time, double tftime,int numAgents, int vanCapacity,int numVans,Seeds sd, boolean log) {

        // Adding references to model object to classes
                
        // For turning on logging
        traceFlag = log;
                
        // Create RVP object with given seed
        rvp = new RVPs(this, sd);
                

        this.numVans = numVans;
        this.vanCapacity = Constants.getVanCapacity()[vanCapacity];
        rgRentalCounter = new RentalCounter(numAgents);
        output = new Output(numAgents, numVans, vanCapacity, tftime);
                
        initAOSimulModel(t0time, tftime);  // set stop condition to ensure SBL is not empty.
                

        // Schedule the first arrivals and employee scheduling
        Initialise init = new Initialise(this);
        scheduleAction(init); // Should always be first one scheduled.
                
                
        CustomerArrivalAtTerminal1 arrivalT1 = new CustomerArrivalAtTerminal1(this);
        scheduleAction(arrivalT1); // customer arrival at  terminal 1
        CustomerArrivalAtTerminal2 arrivalT2 = new CustomerArrivalAtTerminal2(this);
        scheduleAction(arrivalT2); // customer arrival at  terminal 2
        CustomerArrivalAtRentalCounter arrivalRC = new CustomerArrivalAtRentalCounter(this);
        scheduleAction(arrivalRC); // customer arrival at Rental Counter

    }
        

    @Override
    protected void testPreconditions(Behaviour behObj) {
                
        reschedule(behObj);
        while (BoardVan.precondition(this)) {
            BoardVan boardC = new BoardVan(this);
            boardC.startingEvent();
            scheduleActivity(boardC);
        }
        while (MoveToNextVanLine.precondition(this)) {
            MoveToNextVanLine move = new MoveToNextVanLine(this);
            move.startingEvent();
            scheduleActivity(move);
        }
        while (ExitVan.precondition(this)) {
            ExitVan exitvan = new ExitVan(this);
            exitvan.startingEvent();
            scheduleActivity(exitvan);
        }
        while (CheckInOut.precondition(this)) {
            CheckInOut checkio = new CheckInOut(this);
            checkio.startingEvent();
            scheduleActivity(checkio);
        }
                
    }
    @Override
    public void eventOccured() {
        if(traceFlag){
            System.out.println("Clock: "+getClock() + 
                               "; Total Customers in queue: " + totalCustomersEnqueued() +
                               "; Van Locations: " + vanLocations() +
                               "; RG.RentalCounter.n: " + rgRentalCounter.getN());
            for (int i = 0; i < numVans; i++) {
                System.out.println("Van " + (i + 1) + " has " + rgVans[i].numPassengers + " Passengers and " + vanStatus(rgVans[i]));
            }
            showSBL();                     
        }
                 
    }
    public String totalCustomersEnqueued () {
        int []locs = new int[Constants.NUM_CUST_LINES];
        for (int j = 0; j < Constants.NUM_CUST_LINES; j++) {
            locs[j] = qCustomerLine[j].getN();
        }
        return Arrays.toString(locs);
    }
        
    public String vanLocations() {
        int []locs = new int[Constants.NUM_CUST_LINES];
        for (int i = 0; i < Constants.NUM_CUST_LINES; i++) {
            locs[i] = qVanLines[i].getN();
                        
        }
        return Arrays.toString(locs);
    }
    public String vanStatus(Vans v) {
        final String prefix = "is enqueued in vanline ";
        if(v.isMoving) {
            return "is moving from " + Constants.VANLINE_LOCATIONS[v.location] + " to " + Constants.VANLINE_LOCATIONS[v.destination] + ". "; 
        }else if (v.isBoarding){
            return prefix + Constants.VANLINE_LOCATIONS[v.location] + " and is boarding ";
        }
        else if (v.isExiting) {
            return prefix + Constants.VANLINE_LOCATIONS[v.location] + " and is exiting ";
        }
        else {
            return prefix + Constants.VANLINE_LOCATIONS[v.location] + " and is waiting ";
        }
    }
}

