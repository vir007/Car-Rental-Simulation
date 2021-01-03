import SMRental.Output;
import SMRental.SMRental;
import SMRental.Seeds;
import cern.jet.random.engine.RandomSeedGenerator;
import outputAnalysis.ConfidenceInterval;

/**
 * This class is for the experimentation with the number and capacity of vans
 * 
 */
public class SME2 {
    static final String CI_FORMAT = "NumVans: %d\t PE: %f\t zeta: %f\t Cf Min: %f\t Cf Max: %f\t stdev: %f\t zeta/PE %4f\n";
    public static void main(String[] args) {
        
        final int NUMRUNS = 10; //runs for each options
        final double CNF_LEVEL = 0.9;
                   
        double startTime=0.0;
        double endTime=270.0; //4.5 hours = 270 minutes
                   
        Seeds[] sds = new Seeds[NUMRUNS];
        SMRental SM;  // Simulation object
                   
        boolean trace = false; //set to true if you want to get a trace log of what's happening
        //good idea to redirect the output to a file because it gets pretty big
        boolean trueRandom = false; // set to true if you want the seeds to be randomized
                 
        int maxAgents = 15;
        int maxVans = 15;
        int maxVanCap = 3;//van capacity is either 0 : 12, 1 : 18, 2 : 30
                   
        // Lets get a set of uncorrelated seeds
        RandomSeedGenerator rsg = new RandomSeedGenerator();               
        for(int i=0 ; i<NUMRUNS ; i++) 
            sds[i] = new Seeds(rsg, trueRandom);
                   
        // Loop for NUMRUN simulation runs for each case
        // try each configuration of van capacity[0-2] and number of vans[1-10]
        Output [][][]outputs      = new Output[maxVanCap][maxVans][NUMRUNS];
        double [][][]costResults  = new double[maxVanCap][maxVans][NUMRUNS];
        double [][][]satisResults = new double[maxVanCap][maxVans][NUMRUNS];
                  
        for (int vanCap = 0; vanCap < maxVanCap; vanCap++) {
            for (int numVans = 0; numVans < maxVans; numVans++) {
                for(int run = 0 ; run < NUMRUNS ; run++) {
                    SM = new SMRental(startTime, endTime, maxAgents, vanCap, numVans+1, sds[run], trace);
                    SM.runSimulation();
                    outputs[vanCap][numVans][run] = SM.getOutput();
                }
            }
        }
                
        ConfidenceInterval c;
                
        System.out.println("Running a test with numruns = " + NUMRUNS);
        System.out.println("Vans Costs: ");
        for (int vanCap = 0; vanCap < maxVanCap; vanCap++) {
            System.out.println("------ Van Size "+ (vanCap) + " ------");
            for (int numVans = 0; numVans < maxVans; numVans++) {
                for (int j = 0; j < NUMRUNS; j++) {
                        
                    costResults[vanCap][numVans][j] = outputs[vanCap][numVans][j].calcCostsExcludingAgents();
                    satisResults[vanCap][numVans][j] += outputs[vanCap][numVans][j].getCustomerSatisfaction();
                }
                c = new ConfidenceInterval(costResults[vanCap][numVans], CNF_LEVEL);
                System.out.print(outputVanStats(c, numVans + 1));
            }
        }
        System.out.println("Vans Satisfaction: ");
        for (int vanCap = 0; vanCap < maxVanCap; vanCap++) {
            System.out.println("------ Van Size "+ (vanCap) + " ------");
            for (int numVans = 0; numVans < maxVans; numVans++) {
                c = new ConfidenceInterval(satisResults[vanCap][numVans], CNF_LEVEL);
                System.out.print(outputVanStats(c, numVans + 1));
            }
        }
        System.out.println("Printing tables of each of the runs satisfaction rates");
        for (int i = 0; i < satisResults.length; i++) {
        	System.out.println("van size: " + i);
        	outputComparisonTable(satisResults[i]);
		}
        
    }
    /**
     * returns a string with the van stats formated
     * @param c 
     * @param numVans
     * @return formatted string
     */
    static String outputVanStats(ConfidenceInterval c, int numVans) {
        return String.format(CI_FORMAT, 
                             numVans, 
                             c.getPointEstimate(), 
                             c.getZeta(), 
                             c.getCfMin(), 
                             c.getCfMax(), 
                             c.getStdDev(),
                             c.getZeta()/c.getPointEstimate());
    }
    
    /**
     * prints a table with the runs as columns and num vans as rows
     * @param satisfactionRate
     */
    static void outputComparisonTable(double [][]satisfactionRate) {
    	System.out.print("\t");
    	for (int i = 0; i < satisfactionRate[0].length; i++) {
			System.out.print("run " + (i+1) + "\t ");
		}
    	System.out.println();
    	for (int i = 0; i < satisfactionRate.length; i++) {
    		System.out.print((i+1) + " vans\t ");
			for (int j = 0; j < satisfactionRate[i].length; j++) {
				System.out.print(((int)satisfactionRate[i][j]) + "\t");
			}
			System.out.println();
		}
    }
}
