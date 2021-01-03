package SMRental;

import simulationModelling.ConditionalActivity;

public class BoardVan extends ConditionalActivity
{

    SMRental model;
    Customer icCustomer;
    UDPs udp;
    int lineId;
    int vanId;
    int custPos;
    int []ids;
        
    double startTime;
    public BoardVan(SMRental model){
        this.model=model;               
    }
        
    public static boolean precondition(SMRental model){
        int []id = CanBoard(model);
                
        return (id != null);

    }
        
    @Override
    public double duration(){
        return uBoardTime();
    }
    /**
     * 
     */
    @Override
    public void startingEvent(){
        ids = CanBoard(model);
        try {
	        lineId = ids[0];
	        custPos = ids[1];
	    } catch (NullPointerException npe) {
	    	npe.printStackTrace();
	    	throw new Error("null pointer at starting event boardvan" );
	    }
        vanId = model.qVanLines[lineId].qVans.get(0);
        model.qCustomerLine[lineId].isBoarding = true;
                
        icCustomer = remove(lineId, custPos);
        model.rgVans[vanId].isBoarding = true;
                
        icCustomer.lineWaitTime += model.getClock() - icCustomer.waitStartTime;//debug
        startTime = model.getClock();//debug
                
        name = "Van " + vanId + " boarded at " + lineId;
    }
        
    @Override
    protected void terminatingEvent(){
                
        icCustomer.waitStartTime = model.getClock();//debug
        model.output.timeBoarding += (model.getClock() - startTime);//debug
                
        model.rgVans[vanId].isBoarding = false;
        model.qCustomerLine[lineId].isBoarding = false; 
        insert(model.rgVans[vanId], icCustomer);
    }
    //embdedded UDPs
    /**
     * checks each van line and the customers inside the associated customer line
     * to determine if there is a van that can board a customer
     * @param model
     * @return a tuple with the vanLine/customerline id and the index of the customer, or NULL
     */
    public static int[] CanBoard(SMRental model){
        int custId;
        int []ids = null;
        boolean canBeBoarded = false;
        int vanId;
        for (int vanLineId = 0; vanLineId <= Constants.RENTAL && ids == null; vanLineId++) {
            if (model.qVanLines[vanLineId].getN() > 0 && !model.qCustomerLine[vanLineId].isBoarding) {
                vanId = model.qVanLines[vanLineId].qVans.get(0);
                
                canBeBoarded = (model.rgVans[vanId].finishedExiting || 
                				model.rgVans[vanId].getN() == 0 	|| 
                				vanLineId < Constants.RENTAL); 
                
                custId = model.udp.customerCanBoard(vanLineId, vanId);
                
                if (canBeBoarded && custId != -1) {
                	model.rgVans[vanId].finishedExiting = true; //no disembarkers = no need to try to exit
                    ids = new int[2];
                    ids[0] = vanLineId;
                    ids[1] = custId;
                }
                                
            }
        }
        return ids;
    }
    protected void insert(Vans v, Customer icCustomer) {
        v.numPassengers += icCustomer.numPassengers;
        v.group.add(icCustomer);
    }
        
    Customer remove(int lineId, int custPos) {
        return model.qCustomerLine[lineId].CustLine.remove(custPos);
    }
    //embedded RVPs
    public double uBoardTime() {
        return model.rvp.boardTime.nextDouble();
    }
}
