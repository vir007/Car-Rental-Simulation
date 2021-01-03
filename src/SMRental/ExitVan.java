package SMRental;

import simulationModelling.ConditionalActivity;

public class ExitVan extends  ConditionalActivity {
        
    SMRental model;
    Customer icCustomer;
    int vanLineid;
    int vanId;
    double startTime; //debug
        
    public ExitVan(SMRental model) {
        this.model=model;
    }

    public static boolean precondition(SMRental model) {
        int[] id = CanExit(model);
        return (id != null);
    }
        
    @Override
    public void startingEvent(){
        int[] id = CanExit(model);
        try {
            vanLineid=id[0];
            vanId = id[1];				
        } catch (NullPointerException npe) {
        	npe.printStackTrace();
        	throw new Error("null pointer at starting event exitvan" );
        }
        model.rgVans[vanId].isExiting = true;
        startTime = model.getClock(); //debug
        name = "exiting van " + vanId + " at location " + vanLineid + "; " + model.rgVans[vanId].getN() + " customers left";
    }
        
    @Override
    protected double duration() {
        return uExitTime();
    }
        
    @Override
    protected void terminatingEvent() {
        model.rgVans[vanId].isExiting = false;

        icCustomer = remove(model.rgVans[vanId]);
                
        model.output.timeExiting += (model.getClock() - startTime); //debug
        icCustomer.vanWaitTime += model.getClock() - icCustomer.waitStartTime; //debug
        icCustomer.waitStartTime = model.getClock(); //debug
                
        if(vanLineid==Constants.DROPOFF)
        {
            model.udp.UpdateOutputs(icCustomer);
        }
        else {
            model.qCustomerLine[Constants.CHECKIO].spInsertQue(icCustomer);
        }
                
        //set done exiting once the customers that were riding the bus have finished exiting
        //now the bus can be boarded if at a vanline when that can happen
        if(model.rgVans[vanId].getN()==0)
        {
            model.rgVans[vanId].finishedExiting = true;
        }               
    }
    //embedded UDPs
    /**
     * goes through each van line and van enqueued in the van line to find a van that can exit
     * @param model
     * @return a tuple of the vanline that the van can exit from and the van identifier or NULL
     */
    public static int[] CanExit(SMRental model) {
        int[] exitIds= null;
        int vanId;
            
        for(int vanlineid = Constants.RENTAL; vanlineid <= Constants.DROPOFF && exitIds == null; vanlineid++)
        {
            if (model.qVanLines[vanlineid].getN() > 0) {
                for (int i = 0; i < model.qVanLines[vanlineid].getN() && exitIds == null; i++) {
                    vanId = model.qVanLines[vanlineid].qVans.get(i);
                    if(model.rgVans[vanId].numPassengers == 0 && model.rgVans[vanId].getN() > 0) {
                        //DEBUG
                        throw new Error("Van has non zero passengers with zero customers aboard.");
                    }
                    if(model.rgVans[vanId].numPassengers>0 && 
                    		!model.rgVans[vanId].finishedExiting && 
                    		!model.rgVans[vanId].isExiting){
                        exitIds = new int[2];
                        exitIds[0]=vanlineid;
                        exitIds[1]=vanId;
                    }
                }
            }
        }
        return (exitIds);
    }
    protected Customer remove(Vans v) {
        v.numPassengers -= v.group.get(0).numPassengers;
        return(v.group.remove(0));
    }
    //embedded RVPs
    public double uExitTime() {
        return model.rvp.exitTime.nextDouble();
    }
}
