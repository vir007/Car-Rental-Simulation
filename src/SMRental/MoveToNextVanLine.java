package SMRental;
import simulationModelling.ConditionalActivity;

public class MoveToNextVanLine extends ConditionalActivity
{

    SMRental model; 
    VanLines vanLine;
    int currentLocation;
    int vanId;
    int destination;
    double startTime;

                
    public MoveToNextVanLine(SMRental model){
        this.model = model;
    }
    
    public static boolean precondition(SMRental model){
        int []ids = CanMove(model);
        return (ids != null);
    }
        
    @Override
    public void startingEvent(){
        int []ids = CanMove(model);
        try {
            currentLocation = ids[0];
            vanId = remove(currentLocation, ids[1]);
        } catch (NullPointerException npe) {
        	npe.printStackTrace();
        	throw new Error("null pointer at starting event MTNVL" );
        }
        destination = GetNextVanDestination(vanId, currentLocation);
        model.rgVans[vanId].destination = destination; //debug
        model.rgVans[vanId].isMoving = true; //debug
        startTime = model.getClock(); //debug
        name =  "Van " + (vanId+1) + " moving from " + currentLocation + " to " + destination;
    }
        
    @Override
    public double duration() {
        return uTravelTime(GetDistance(currentLocation, destination));
    }
        
    @Override
    public void terminatingEvent(){
            
        model.qVanLines[destination].spInsertQue(vanId);
        model.output.totalTravelDistance+=GetDistance(currentLocation, destination);
        model.rgVans[vanId].finishedExiting=false;
            
        model.rgVans[vanId].location = destination; //debug
        model.rgVans[vanId].isMoving = false; //debug
        model.output.timeMoving += (model.getClock() - startTime);//debug
            
    }
    //embedded UDPs
    public static int[] CanMove(SMRental model) {       
        //in a perfect world I would have written unit tests for all the udps
        //and made them more functional
        int []ids = null; //line, van ids
        Vans van;
            
        int vanId = -1;
        for(int lineId = 0; lineId < model.qVanLines.length  && ids == null; lineId++) {
                    
            boolean doneExiting = false;
            boolean lineEmpty = false;
            boolean noSpace = false;
            if (model.qVanLines[lineId].qVans.isEmpty()) { // if there are no vans, then exit without checking
                continue;
            }
            for (int i = 0; i < model.qVanLines[lineId].qVans.size() && ids == null; i++) {
                            
                vanId = model.qVanLines[lineId].qVans.get(i);
                van = model.rgVans[vanId];
                            
                if(van.isBoarding)
                    continue;
                            
                //check if need to wait until finished exiting
                if(lineId == Constants.RENTAL || lineId == Constants.DROPOFF) {
                    doneExiting = van.finishedExiting;
                } else { // no exit needed
                    doneExiting = true;
                }
                            
                //check to see if line is empty or if there is enough space in front 
                //to accommodate passengers
                if(model.qCustomerLine[lineId].getN()==0 || spaceInFront(model, lineId, vanId)) {
                    lineEmpty = true;
                }
                            
                noSpace = (model.udp.customerCanBoard(lineId, vanId)  == -1);
                if ((doneExiting && (lineEmpty || noSpace)) ||
                    ((van.getN()==0) && (lineEmpty || noSpace))) { // if there is no exit that can be done
                    ids = new int[2];
                    ids[0] = lineId;
                    ids[1] = i;
                }
            }
        }
        return ids;
    }
    protected static int getNumPassengersInLine(CustomerLine c){
        int acc = 0;
        for (int i = 0; i < c.CustLine.size(); i++) {
            acc += c.CustLine.get(i).getNumPassengers();
        }
        return acc;
    }
    /**
     * this function will determine if there is enough space in the vans in front of it to accomodate 
     * the passengers in the the line that have not yet boarded.
     * @return
     */
    public static boolean spaceInFront(SMRental model, int vanLineId, int vanId) {
        int passengersWaiting = getNumPassengersInLine(model.qCustomerLine[vanLineId]);
        int totalVanCapacity = 0;
        for (int i = 0; i < vanId; i++) {
            totalVanCapacity += getRemainingSpace(model.rgVans[vanId],model.vanCapacity);
        }
        return (totalVanCapacity >= passengersWaiting);
    }
    
    protected static int getRemainingSpace(Vans v, int vanCapacity) {
        return vanCapacity - v.numPassengers;
    }
        
    public double GetDistance(int currentLocation, int destination){
        if (currentLocation==Constants.TERM1) {
            if(destination==Constants.TERM2)
                return 0.3;
            else
                return 1.5; //rental
        } else if (currentLocation==Constants.TERM2) {
            return 2.0;
        } else if (currentLocation==Constants.RENTAL){
            if (destination==Constants.DROPOFF)
                return 1.7;
            else
                return 1.5;//term1
        } else if (currentLocation == Constants.DROPOFF) {
            return 0.5;
        }
        else {
            //debug
            throw new Error("get distance failed with loc: " + currentLocation + " dest: " + destination);
        }
    }
    /**
     * gets next van line destination based on how filled the van is
     * and the current location
     * @param vanId
     * @param currentLocation
     * @return destination
     */
    public int GetNextVanDestination(int vanId, int currentLocation){
        int nextLocation = -1;
        if(currentLocation==Constants.TERM1)
        {
            if(model.rgVans[vanId].numPassengers==model.vanCapacity) {
                nextLocation=Constants.RENTAL;
            }
            else {
                nextLocation=Constants.TERM2;
            }
        }
        if(currentLocation==Constants.TERM2)
        {
            nextLocation=Constants.RENTAL;  
        }
        if(currentLocation==Constants.RENTAL)
        {
            if(model.rgVans[vanId].numPassengers==0) {
                            
                nextLocation=Constants.TERM1;
            }
            else {
                nextLocation=Constants.DROPOFF;
            }
        }
        if(currentLocation==Constants.DROPOFF)
        {
            nextLocation=Constants.TERM1;   
        }
        if (nextLocation == -1) //debug
            throw new Error("get destination failed with loc: " + currentLocation + " vanid: " + vanId);
        return nextLocation;
    }
    
    int remove(int vanLineId, int n) {
        return model.qVanLines[vanLineId].qVans.remove(n);
    }
    
    //embedded DVPs
    public double uTravelTime(double distance){
        return (distance / Constants.VAN_SPEED) * 60.; //changed to calculations so that in the future
        //it would be possible to modify van_speed and have that change actually effect the system
    }
}
