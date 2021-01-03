package SMRental;

import java.util.HashSet;

class RentalCounter {
        
    // Attributes
    protected int numAgents; // Number of Agents at RentalCounter
    // For implementing the group, use a HashSet object.
    protected HashSet<Customer> group = new HashSet<>(); // change 
        
    public RentalCounter(int nagents) {
        this.numAgents = nagents;
    }
    // Required methods to manipulate the group
	protected void insertGrp(Customer icCustomer){
        group.add(icCustomer);
    }
    protected boolean removeGrp(Customer icCustomer) {
        return(group.remove(icCustomer));
    }        
    protected int getN()  {
        return group.size(); 
    }  // Attribute n
        

}
