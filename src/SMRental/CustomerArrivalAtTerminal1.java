package SMRental;

import SMRental.Customer.Direction;
import simulationModelling.ScheduledAction;

public class CustomerArrivalAtTerminal1 extends ScheduledAction {
        
    SMRental model; // For accessing the complete model
    CustomerArrivalAtTerminal1(SMRental model) { this.model = model; }
        
        
    public double timeSequence() {
        return(DuCTerm1());
    }

    public void actionEvent() {
        // CustomerArrivalAtTerminal1 Action Sequence SCS
        model.output.customersThatEnteredSystem++;
        Customer icCustomer = new Customer();
        icCustomer.startTime = model.getClock();
        icCustomer.numPassengers = model.rvp.uNumPassengers();
        icCustomer.direction = Direction.ARRIVING;
                
        icCustomer.waitStartTime = model.getClock();
                
        model.qCustomerLine[Constants.TERM1].spInsertQue(icCustomer);
    }
    public double DuCTerm1() {
        double time = model.getClock();
        double poisson_mean = calculatePoissonMeanT1(time);
        return model.rvp.custArrivalTime.nextDouble(1/poisson_mean) + time;
    }
    public double calculatePoissonMeanT1(double t){                   
        if(t >= 0 && t < 15)
            return 15;
        if(t >= 15 && t < 30)
            return 17.5;
        if(t >= 30 && t < 45)
            return 5;
        if(t >= 45 && t < 60)
            return 4;
        if(t >= 60 && t < 75)
            return 3.333333333;
        if(t >= 75 && t < 90)
            return 4.285714286;
        if(t >= 90 && t < 105)
            return 4.615384615;
        if(t >= 105 && t < 120)
            return 6;
        if(t >= 120 && t < 135)
            return 15;
        if(t >= 135 && t < 150)
            return 2.0;
        if(t >= 150 && t < 165)
            return 6;
        if(t >= 165 && t < 180)
            return 4.285714286;
        if(t >= 180 && t < 195)
            return 3.75;
        if(t >= 195 && t < 210)
            return 4;
        if(t >= 210 && t < 225)
            return 8.571428571;
        if(t >= 225 && t < 240)
            return 20;
        if(t >= 240 && t < 255)
            return 15;
        if(t >= 255 && t < 270)
            return 30;
        throw new Error("didn't catch mean calc Term 1");
    }
}
