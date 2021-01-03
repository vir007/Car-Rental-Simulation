package SMRental;

public class Output 
{
    static SMRental model;
    
    public static final double DRIVER_COST = 12.50; //dollars per hour
    public static final double AGENT_COST = 11.50; //dollars per hour  
        
    private int numAgents;
    private int numVans;
    private int vanSize;
        
    double totalTravelDistance;
    int totalCustomers;
    int satisfiedCustomers; 
    double customerSatisfaction;

        
    double timeMoving = 0;//all the variables from this point onward are debug
    double timeBoarding = 0;//they aren't useful for the CM or project goal 
    double timeExiting = 0;//but they are useful for analysing if the behaviour is off
    int customersThatEnteredSystem = 0;//customers that have entered the system but 
    							   //haven't left. They aren't counted when calculating 
    							   //the satisfaction.
    double endTime = 0;
    
    double lineWaitTime = 0;
    double vanWaitTime = 0;
        
    double numDeparting = 0;
    double numArriving = 0;
    double departingWaitTime = 0;
    double arrivingWaitTime = 0;
        
    public Output(int numAgents, int numVans, int vanSize, double endTime) {
                
        this.numAgents = numAgents;
        this.numVans = numVans;
        this.vanSize = vanSize;
        this.endTime = endTime;
    }

    public double calcCosts(){
        double costs = 0.0;
        costs += 4.5 * (double) numVans * DRIVER_COST;
        costs += 4.5 * (double) numAgents * AGENT_COST;
        costs += Constants.COST_PER_MILE[vanSize] * totalTravelDistance;
        return costs;
    }
    //when comparing van number and van size, cost of agents doesn't matter
    public double calcCostsExcludingAgents () {
        double costs = 0.0;
        costs += 4.5 * (double) numVans * DRIVER_COST;
        costs += Constants.COST_PER_MILE[vanSize] * totalTravelDistance;
        return costs;
    }
    /**
     * debug output for the system
     * @param runNum
     * @return formatted string with entire debug output
     */
    public String getDebugOutput(int runNum) {
        return "Run " + (runNum + 1) + //debug
            ":\n customerSatisfaction: " + customerSatisfaction +
            ", satisfied customers: " + satisfiedCustomers +
            "\n total customers that exited system: " + totalCustomers +
            ", total customers that entered system: " + customersThatEnteredSystem +
            "\n avg time spent waiting in line: " + lineWaitTime/totalCustomers +
            ", avg time spent waiting in a van: "+ vanWaitTime/totalCustomers + 
            "\n avg time departing: " + departingWaitTime/numDeparting +
            ", avg time arriving: " + arrivingWaitTime/numArriving + 
            "\n totalTravelDistance: " + totalTravelDistance+
            ", avg time spent moving per van: " + timeMoving/numVans + 
            "\n avg time spent exiting per van: " + timeExiting/numVans + 
            ", avg time spent boarding per van: " + timeBoarding/numVans +
            "\n percent time spent moving: " +(timeMoving/(timeMoving + timeBoarding + timeExiting)) +
            "\n percent time spent boarding/exiting: " + ((timeBoarding + timeExiting)/(timeMoving + timeBoarding + timeExiting)) +
            "\n total cost: " + calcCosts() + "$";
    }

	public double getCustomerSatisfaction() {
		return customerSatisfaction;
	}

	public void setCustomerSatisfaction(double customerSatisfaction) {
		this.customerSatisfaction = customerSatisfaction;
	}
        

}
