import SMRental.SMRental;
import SMRental.Seeds;
import cern.jet.random.engine.RandomSeedGenerator;
/**
 * This class is for verification and validations
 * 
 */
public class SMRentalDebug {

    public static void main(String[] args) {

        double startTime=0.0;
        double endTime=270.0; //4.5 hours = 270 minutes
       
        SMRental SM;  // Simulation object
       
        boolean trace = true; //set to true if you want to get a trace log of what's happening
        //good idea to redirect the output to a file because it gets pretty big
        boolean trueRandom = false; // set to true if you want the seeds to be randomized
 
        int maxAgents = 15;
        int numVans = 10;
        int vanCap = 0; //van capacity is either 0 : 12, 1 : 18, 2 : 30
        int run = 0;
           
        // Lets get a set of uncorrelated seeds
        RandomSeedGenerator rsg = new RandomSeedGenerator();               
        Seeds sds = new Seeds(rsg, trueRandom);
        SM = new SMRental(startTime, endTime, maxAgents, vanCap, numVans, sds, trace);
        SM.runSimulation();
        //debug output
        System.out.println(SM.output.getDebugOutput(run));
    }
}
