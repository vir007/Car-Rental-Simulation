import SMRental.Output;
import SMRental.SMRental;
import SMRental.Seeds;
import cern.jet.random.engine.RandomSeedGenerator;
import outputAnalysis.ConfidenceInterval;
/**
 * This class is for experimentation with the number of agents needed
 * 
 */
public class SMRentalAgents {
    static final String CI_FORMAT = "NumAgents: %d\t PE: %f\t zeta: %f\t Cf Min: %f\t Cf Max: %f\t stdev: %f\t zeta/PE %4f\n";
    public static void main(String[] args) {
        
        double startTime=0.0;
        double endTime=270.0; //4.5 hours = 270 minutes
        final int NUMRUNS = 10; //runs for each options
        final double CNF_LEVEL = 0.9;
                   
        SMRental SM;  // Simulation object
               
        boolean trace = false; //set to true if you want to get a trace log of what's happening
        //good idea to redirect the output to a file because it gets pretty big
        boolean trueRandom = false; // set to true if you want the seeds to be randomized
         
        int maxAgents = 15;
        int numVans = 3;
        int vanCap = 0; //van capacity is either 0 : 12, 1 : 18, 2 : 30
                   
        // Lets get a set of uncorrelated seeds
        RandomSeedGenerator rsg = new RandomSeedGenerator();            
        Seeds []sds = new Seeds[NUMRUNS];
        for(int i=0 ; i<NUMRUNS ; i++) {
            sds[i] = new Seeds(rsg, trueRandom);
        }
        
        System.out.println("-----------------------");
        System.out.println("rental agents cost experimentation: ");
        Output [][] agentsExperimentOutput = new Output[maxAgents][NUMRUNS];
        double [][]agentRunCost = new double[maxAgents][NUMRUNS];
        double [][]agentRunSatis = new double[maxAgents][NUMRUNS];
        ConfidenceInterval c;
        for (int numAgents = 0; numAgents < maxAgents; numAgents++) {
            for (int run = 0; run < NUMRUNS; run++) {
                SM = new SMRental(startTime, endTime, numAgents+1, vanCap, numVans, sds[run], trace);
                SM.runSimulation();
                agentsExperimentOutput[numAgents][run] = SM.getOutput();
                agentRunCost[numAgents][run] += agentsExperimentOutput[numAgents][run].calcCosts();
                agentRunSatis[numAgents][run] += agentsExperimentOutput[numAgents][run].getCustomerSatisfaction();
            }
            c = new ConfidenceInterval(agentRunCost[numAgents], CNF_LEVEL);
            System.out.print(outputVanStats(c, numAgents + 1));
        }
        System.out.println("-----------------------");
        System.out.println("rental agents satisfaction experimentation: ");
        for (int numAgents = 0; numAgents < maxAgents; numAgents++) {
            c = new ConfidenceInterval(agentRunSatis[numAgents], CNF_LEVEL);
            System.out.print(outputVanStats(c, numAgents + 1));
        }
        outputComparisonTable(agentRunSatis);
    }
    static String outputVanStats(ConfidenceInterval c, int numAgents) {
        return String.format(CI_FORMAT, 
                             numAgents, 
                             c.getPointEstimate(), 
                             c.getZeta(), 
                             c.getCfMin(), 
                             c.getCfMax(), 
                             c.getStdDev(),
                             c.getZeta()/c.getPointEstimate());
    }
    static void outputComparisonTable(double [][]satisfactionRate) {
    	System.out.print("\t");
    	for (int i = 0; i < satisfactionRate[0].length; i++) {
			System.out.print("run " + (i+1) + "\t ");
		}
    	System.out.println();
    	for (int i = 0; i < satisfactionRate.length; i++) {
    		System.out.print((i+1) + " agents\t ");
			for (int j = 0; j < satisfactionRate[i].length; j++) {
				System.out.print(((int)satisfactionRate[i][j]) + "\t");
			}
			System.out.println();
		}
    }
}
