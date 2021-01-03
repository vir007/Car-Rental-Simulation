package SMRental;

import java.util.ArrayList;



public class CustomerLine {
	//Identifiers used to select an individual CustomerLine are 
	//Constants. (TERM1, TERM2, RENTAL, CHECKIO). 
	//The first three match the identifier used to select customer line.
	//This is because at those points the customerLine and the vanLine are in the
	//same location.
    /* List of customer waiting in line */
    protected ArrayList<Customer> CustLine = new ArrayList<>();
    protected boolean isBoarding = false; //make sure only one can board at a time
    protected int getN() { 
        return CustLine.size(); 
    }  
        
    // Attribute n
    protected void spInsertQue(Customer cust){ 
        CustLine.add(cust); 
    }       
    protected Customer spRemoveQue() {
        return CustLine.remove(0);
    }
}
