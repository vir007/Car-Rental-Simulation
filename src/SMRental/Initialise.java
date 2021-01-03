package SMRental;

import simulationModelling.ScheduledAction;

class Initialise extends ScheduledAction
{
    SMRental model;
        
    double [] ts = { 0.0, -1.0 }; // -1.0 ends scheduling
    int tsix = 0;  // set index to first entry.
        
    public Initialise(SMRental smRental) {
        model=smRental;
    }

    public double timeSequence() 
        {
            return ts[tsix++];  // only invoked at t=0
        }
        
    public void actionEvent() 
        {
            int id; 
            model.rgVans = new Vans[model.numVans];
            for(int i=0;i<model.numVans;i++) {
                model.rgVans[i] = new Vans();
            }
        
            for(id = Constants.TERM1 ; id <= Constants.CHECKIO ; id++)
            {
                model.qCustomerLine[id] = new CustomerLine();
                        
            }
            for(id = Constants.TERM1 ; id <= Constants.DROPOFF ; id++)
            {
                model.qVanLines[id] = new VanLines(); 
                
            }
            
            int location = Constants.TERM1;
            
            for(int i=0 ; i < model.numVans ; i++){
                model.rgVans[i].numPassengers = 0;
                model.qVanLines[location].spInsertQue(i);
                model.rgVans[i].location = location; //debug
                model.rgVans[i].destination = location;//debug
                model.rgVans[i].isMoving = false;//debug
                location= (location+1) % (Constants.DROPOFF + 1);
            }
                 
            model.output.totalTravelDistance=0;
            model.output.totalCustomers=0;
            model.output.satisfiedCustomers=0;             
            model.output.customersThatEnteredSystem = 0;//debug
        }
}

