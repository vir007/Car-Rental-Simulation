package SMRental;
  
public class Constants {

    private Constants() {
        throw new IllegalStateException("Utility class");
    }
    public static int [] getVanCapacity() {
        return vanCapacity;
    }
    /* Constants */
    protected static final double []PATH_DISTANCE = {1.5, 1.7, 0.5, 0.3, 2.0}; //in miles
    public static final double VAN_SPEED = 20; //in mph
    public static final double CUSTOMER_SATISFIED_ARRIVING = 20; //in minutes
    public static final double CUSTOMER_SATISFIED_DEPARTING = 18; // in minutes
    protected static final double []COST_PER_MILE = {0.43, 0.73, 0.92}; 
    
    /**
     * The following constants aren't defined in the CM, but are related to the 
     * entities.
     */
    private static final int []vanCapacity = {12, 18, 30};
    public static final int TERM1 = 0; //represents the vanline and customerline identifiers
    public static final int TERM2 = 1;
    public static final int RENTAL = 2;
    public static final int CHECKIO = 3;
    public static final int DROPOFF = 3;
    
    protected static final String[] VANLINE_LOCATIONS = {"Term1", "Term2", "Rental", "Dropoff"}; 
    public static final int NUM_CUST_LINES = 4;
           
}
