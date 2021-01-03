package SMRental;

import java.util.Random;

import cern.jet.random.engine.RandomSeedGenerator;

public class Seeds 
{
    int PassengerN;   // Number of passengers
    int exitT;
    int boardT;
        
    int checkIn1;
    int checkIn2;
    int checkOut1;
    int checkOut2;
        
    int arrivalT1;
    int arrivalT2;
    int arrivalRC;
    Random rand = new Random();
    public Seeds(RandomSeedGenerator rsg, boolean trueRandom){
        if (trueRandom) {
            for (int i = 0; i < rand.nextInt(30); i++) {
                rsg.nextSeed();//get a random value each time
            }
        }
        PassengerN = rsg.nextSeed();
        exitT = rsg.nextSeed();
        boardT = rsg.nextSeed();
            
        checkIn1 = rsg.nextSeed();
        checkIn2 = rsg.nextSeed();
        checkOut1 = rsg.nextSeed();
        checkOut2 = rsg.nextSeed();

        arrivalT1 = rsg.nextSeed(); 
        arrivalT2 = rsg.nextSeed();
        arrivalRC = rsg.nextSeed();
    }
}
