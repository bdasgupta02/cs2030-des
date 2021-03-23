package cs2030.simulator;

/**
 * Customer class to represent a Customer.
 *
 * @author Bikramjit Dasgupta
 * @version 3.4
 */
public class Customer {
    protected final int id;
    protected final double time;

    /**
     * Constructs a Customer.
     *
     * @param id Customer Id.
     * @param time Customer time-stamp.
     */
    protected Customer(int id, double time) {
        this.time = time;
        this.id = id;
    }

    /**
     * Creates a new Customer.
     *
     * @param id Customer Id.
     * @param time Customer time-stamp.
     * @return New Customer object based on the given Id and time.
     */
    public static Customer createCustomer(int id, double time) {
        return new Customer(id, time);
    }

    /**
     * Compares the Id numbers of this Customer and another,
     * to return an integer value to determine their ranking.
     *
     * @param another The second Customer in this comparison.
     * @return Value indicating the ranking between the two Customers.
     */
    public int compareId(Customer another) {
        if (this.id == another.id) {
            return 0;
        } else {
            return this.id > another.id ? 1 : -1;
        }
    }

    /**
     * Returns the time-stamp value of this Customer.
     *
     * @return Time-stamp of this Customer.
     */
    public double getTime() {
        return this.time;
    }

    /**
     * Returns a String based on this Customer.
     *
     * @return String based on this Customer.
     */
    @Override
    public String toString() {
        return "" + this.id;
    }
}
