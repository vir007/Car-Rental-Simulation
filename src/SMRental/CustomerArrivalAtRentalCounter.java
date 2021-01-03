package SMRental;

import SMRental.Customer.Direction;
import simulationModelling.ScheduledAction;

public class CustomerArrivalAtRentalCounter extends ScheduledAction {

    SMRental model; 
    CustomerArrivalAtRentalCounter(SMRental model) {
            this.model = model;
    }
    @Override
    protected double timeSequence() {
        return DuCRental();
    }

    @Override
    protected void actionEvent() {
        model.output.customersThatEnteredSystem++;
        Customer icCustomer = new Customer();
        icCustomer.startTime = model.getClock();
        icCustomer.numPassengers = model.rvp.uNumPassengers();
        icCustomer.direction = Direction.DEPARTING;
                
        icCustomer.waitStartTime = model.getClock();
                
        model.qCustomerLine[Constants.CHECKIO].spInsertQue(icCustomer);
    }
    //embedded RVPs
    public double DuCRental(){
        double time = model.getClock();
        double poisson_mean = calculatePoissonMeanRC(time);
        return model.rvp.custArrivalTime.nextDouble(1/poisson_mean) + time;
    }       
    public double calculatePoissonMeanRC(double time){
        if(time >= 0 && time < 15)
            return 5;
        if(time >= 15 && time < 30)
            return 6.666666667;
        if(time >= 30 && time < 45)
            return 3.333333333;
        if(time >= 45 && time < 60)
            return 2.142857143;
        if(time >= 60 && time < 75)
            return 2.608695652;
        if(time >= 75 && time < 90)
            return 2.857142857;
        if(time >= 90 && time < 105)
            return 3.75;
        if(time >= 105 && time < 120)
            return 5.454545455;
        if(time >= 120 && time < 135)
            return 3.529411765;
        if(time >= 135 && time < 150)
            return 2.0;
        if(time >= 150 && time < 165)
            return 1.666666667;
        if(time >= 165 && time < 180)
            return 2.5;
        if(time >= 180 && time < 195)
            return 1.875;
        if(time >= 195 && time < 210)
            return 3.75;
        if(time >= 210 && time < 225)
            return 4.615384615;
        if(time >= 225 && time < 240)
            return 4.615384615;
        if(time >= 240 && time < 255)
            return 12;
        if(time >= 255 && time <= 270)
            return 15;
        throw new Error("didn't catch mean calc RC");
    }
}
