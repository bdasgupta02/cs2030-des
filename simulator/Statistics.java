package cs2030.simulator;

/**
 * Statistics class to store, calculate and print
 * statistics.
 *
 * @author Bikramjit Dasgupta
 * @version 3.4
 */
public class Statistics {
    private final double totalWaitTime;
    private final int totalServed;
    private final int totalSize;

    /**
     * Constructs a Statistics object.
     *
     * @param totalSize Number of Customers at the start of the simulation.
     */
    private Statistics(int totalSize) {
        this.totalWaitTime = 0;
        this.totalServed = 0;
        this.totalSize = totalSize;
    }

    /**
     * Constructs a Statistics object.
     *
     * @param totalWaitTime Total wait time from every waiting Customer.
     * @param totalServed Total Customers served.
     * @param totalSize Number of Customers at the start of the simulation.
     */
    private Statistics(double totalWaitTime, int totalServed, int totalSize) {
        this.totalWaitTime = totalWaitTime;
        this.totalServed = totalServed;
        this.totalSize = totalSize;
    }

    /**
     * Creates a new Statistics object with the total 
     * number of Customers at the start of a Discrete Event
     * Simulation.
     *
     * @param totalSize Number of Customers at the start of the simulation.
     * @return New Statistics object.
     */
    public static Statistics createStatistics(int totalSize) {
        return new Statistics(totalSize);
    }

    /**
     * Increments the totalServed attribute of this 
     * Statistics object and returns a new Statistics object.
     *
     * @return New Statistics object with an incremented totalServed attribute.
     */
    public Statistics incrementServed() {
        return new Statistics(this.totalWaitTime, this.totalServed + 1, this.totalSize);
    }

    /**
     * Adds the totalWaitTime attribute of this 
     * Statistics object with a given double value.
     *
     * @param addition Number to be added to this Statistics object's tatalWaitTime attribute.
     * @return New Statistics object with an added totalWaitTime attribute.
     */
    public Statistics addWaitTime(double addition) {
        return new Statistics(this.totalWaitTime + addition, this.totalServed, this.totalSize);
    }

    /**
     * Calculates the average wait time faced by 
     * Customers.
     *
     * @return Average waiting time of Customers who have waited to be served.
     */
    private double avgWaitTime() {
        return this.totalWaitTime / (double) totalServed;
    }

    /**
     * Calculates the number of Customers who left 
     * without getting served.
     *
     * @return Numbers of Customers who left without getting served.
     */
    private int leftCustomers() {
        return this.totalSize - this.totalServed;
    }

    /**
     * Creates a String based on this Statistics object's 
     * attributes and methods.
     *
     * @return String based on this Statistics object's attributes and methods.
     */
    @Override
    public String toString() {
        if (totalServed == 0) {
            return String.format("[%.3f " + totalServed + " " +
                    this.leftCustomers() + "]", 0.0);
        } else {
            return String.format("[%.3f " + totalServed + " " +
                    this.leftCustomers() + "]", this.avgWaitTime());
        }
    }
}
