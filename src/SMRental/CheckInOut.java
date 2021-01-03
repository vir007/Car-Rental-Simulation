package SMRental;
import SMRental.Customer.Direction;
import simulationModelling.ConditionalActivity;

public class CheckInOut extends ConditionalActivity {
        
    SMRental model;
    Customer icCustomer;
        
    public CheckInOut(SMRental model) { this.model=model;}
        
    public static boolean precondition(SMRental model) {
        return (model.rgRentalCounter.getN() < model.rgRentalCounter.numAgents) && 
            (model.qCustomerLine[Constants.CHECKIO].getN() > 0);
    }
    @Override
    protected double duration() {
        return uRentalCIOTime(icCustomer.direction);
    }

    @Override
    public void startingEvent() {
        icCustomer = model.qCustomerLine[Constants.CHECKIO].spRemoveQue();               
        model.rgRentalCounter.insertGrp(icCustomer);   
    }

    @Override
    protected void terminatingEvent() {
        model.rgRentalCounter.removeGrp(icCustomer);
        if(icCustomer.direction == Direction.ARRIVING){
            model.udp.UpdateOutputs(icCustomer);
            // No need to implement SP.Leave - Java has a garbage collector 
        }
        else {
            model.qCustomerLine[Constants.RENTAL].spInsertQue(icCustomer);
        }
    }
    //embedded RVPs
    public double uRentalCIOTime(Direction direction) {
        if(direction == Direction.ARRIVING) {
            double a=0.768;
            double g1=model.rvp.rentalCheckIn1.nextDouble();
            double g2=model.rvp.rentalCheckIn2.nextDouble();
            return( (a*g1) + ((1-a)*g2) );
        }
        else {
            double a=0.866;
            double g1=model.rvp.rentalCheckOut1.nextDouble();
            double g2=model.rvp.rentalCheckOut2.nextDouble();
            return( (a*g1) + ((1-a)*g2) );
        }
    }
}
