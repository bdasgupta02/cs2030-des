package cs2030.simulator;

import cs2030.simulator.Customer;

/**
 * GreedyCustomer class to represent greedy Customers.
 *
 * @author Bikramjit Dasgupta
 * @version 3.4
 */
public class GreedyCustomer extends Customer {

    /**
     * Constructs a GreedyCustomer.
     *
     * @param id GreedyCustomer Id.
     * @param time Time-stamp of the GreedyCustomer.
     */
    private GreedyCustomer(int id, double time) {
        super(id, time);
    }

    /**
     * Creates a GreedyCustomer.
     *
     * @param id GreedyCustomer Id.
     * @param time Time-stamp of the GreedyCustomer.
     * @return New GreedyCustomer with the given Id and time-stamp.
     */
    public static GreedyCustomer createGreedyCustomer(int id, double time) {
        return new GreedyCustomer(id, time);
    }

    /**
     * Returns a String based on this GreedyCustomer.
     *
     * @return String based on this GreedyCustomer.
     */
    @Override
    public String toString() {
        return this.id + "(greedy)";
    }
}
