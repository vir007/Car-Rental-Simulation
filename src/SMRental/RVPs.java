package SMRental;
import cern.jet.random.Empirical;
import cern.jet.random.EmpiricalWalker;
import cern.jet.random.Exponential;
import cern.jet.random.engine.MersenneTwister;
import cern.jet.random.Normal;
class RVPs 
{
    SMRental model; // for accessing the clock
    // Data Models - i.e. random veriate generators for distributions
    // are created using Colt classes, define 
    // reference variables here and create the objects in the
    // constructor with seeds
        
    public static final double EXIT_TIME = 0.1; //in minutes, 6 seconds / 60 seconds per minute
    public static final double BOARD_TIME = 0.2; //in minutes, 12 seconds / 60 seconds per minute
    
    static final double pOne = 0.60;
    static final double pTwo = 0.20;
    static final double pThree = 0.15;
    static final double pFour = 0.5;
    private final double [] numPDF = { pOne, pTwo, pThree, pFour }; // for creating discrete PDF
    private EmpiricalWalker numpassenger; 
        
    Exponential exitTime;
    Exponential boardTime;
        
    Normal rentalCheckIn1;
    Normal rentalCheckIn2;
        
    private double checkInMean1=2.185;
    private double checkInStdev1=0.325;
    private double checkInMean2=2.185;
    private double checkInStdev2=0.325;
        
    Normal rentalCheckOut1;
    Normal rentalCheckOut2;
        
    private double checkOutMean1=1.450;
    private double checkOutStdev1=0.261;
    private double checkOutMean2=3.986;
    private double checkOutStdev2=0.448;    
        
    Exponential custArrivalTime;
        
    protected RVPs(SMRental model, Seeds sd){ 
        this.model = model; 
        // Set up distribution functions
        numpassenger = new EmpiricalWalker(numPDF,
                                           Empirical.NO_INTERPOLATION,
                                           new MersenneTwister(sd.PassengerN));
            
        exitTime = new Exponential(1 / EXIT_TIME, new MersenneTwister(sd.exitT));
        boardTime = new Exponential(1 / BOARD_TIME, new MersenneTwister(sd.boardT));
            
        rentalCheckIn1 = new Normal(checkInMean1, checkInStdev1, new MersenneTwister(sd.checkIn1));
        rentalCheckIn2 = new Normal(checkInMean2, checkInStdev2, new MersenneTwister(sd.checkIn2));
            
        rentalCheckOut1 = new Normal(checkOutMean1, checkOutStdev1, new MersenneTwister(sd.checkOut1));
        rentalCheckOut2 = new Normal(checkOutMean2, checkOutStdev2, new MersenneTwister(sd.checkOut2));
            
        //random mean, the state will be modified anyway
        custArrivalTime = new Exponential(1/25., new MersenneTwister(sd.arrivalT1));
    }
    
    /**
     * 
     * @return number of passengers that the customer instance represents (1-4)
     */
	int uNumPassengers() {
        int n = numpassenger.nextInt(); 
        return n + 1;
    }
}

                
        
        

