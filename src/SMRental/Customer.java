package SMRental;

public class Customer {
    enum Direction {
        ARRIVING, DEPARTING;
    }
    protected double startTime;
    int numPassengers;
        
    double waitStartTime; //debugging
    double lineWaitTime = 0;
    double vanWaitTime = 0;
        
    protected Direction direction;//direction of travel, set based on their entry point

    public double getStartTime() {
        return startTime;
    }
    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }
    public int getNumPassengers() {
        return numPassengers;
    }
    public void setnPassengers(int nPassengers) {
        this.numPassengers = nPassengers;
    }
}
