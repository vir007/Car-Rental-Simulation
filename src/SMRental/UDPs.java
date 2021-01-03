package SMRental;

import SMRental.Customer.Direction;

public class UDPs {

    SMRental model;
        
    public UDPs(SMRental model) {
        this.model = model;
    }
    /**
     * calculates the satisfaction rate
     * @param totalCustomers
     * @param satisfiedCustomers
     * @return satisfaction rate in percentage (0-100)
     */
	public double calcCustomerSatisfaction(double totalCustomers, double satisfiedCustomers) {
	    if (totalCustomers == 0)
	        return 0;
	    return ((double)satisfiedCustomers/(double)totalCustomers)*100;
	}
	/**
	 * updates the outputs 
	 * @param icCustomer the customer that is leaving the system
	 */
    public void UpdateOutputs(Customer icCustomer){
        model.output.totalCustomers+=1;
        double Totaltime = model.getClock()-icCustomer.startTime;
            
        if(icCustomer.direction == Direction.ARRIVING)
        {
            model.output.arrivingWaitTime += Totaltime;
            model.output.numArriving++;
            if(Totaltime<Constants.CUSTOMER_SATISFIED_ARRIVING)
            {
                model.output.satisfiedCustomers+=1;
            }
        }
        if(icCustomer.direction==Direction.DEPARTING)
        {
            model.output.departingWaitTime += Totaltime;
            model.output.numDeparting++;
            if(Totaltime<Constants.CUSTOMER_SATISFIED_DEPARTING)
            {
                model.output.satisfiedCustomers+=1;
            }
        }
        model.output.customerSatisfaction = calcCustomerSatisfaction(model.output.totalCustomers, model.output.satisfiedCustomers);
        model.output.lineWaitTime += icCustomer.lineWaitTime; //debug
        model.output.vanWaitTime += icCustomer.vanWaitTime;                     
    }
     
    /**
     * finds the index of the customer a customer that can board the van at the line
     * @param lineId the customer line
     * @param vanId the van
     * @return the index of the customer, or -1 if not found
     */
    public int customerCanBoard(int lineId, int vanId) {
        int custThatCanBoard = -1;
        Vans van = model.rgVans[vanId];
        CustomerLine line = model.qCustomerLine[lineId];
        Customer c;
        for (int i = 0; i < line.getN() && custThatCanBoard == -1; i++) {
            c = line.CustLine.get(i);
            if(model.vanCapacity >= van.numPassengers + c.numPassengers) {
                custThatCanBoard = i;
            }                  
        }
        return custThatCanBoard;
    }

}
