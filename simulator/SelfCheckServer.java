package cs2030.simulator;

import java.util.LinkedList;
import cs2030.simulator.Server;
import cs2030.simulator.Customer;

/**
 * SelfCheckServer class to store and manage self-checkout servers
 * to serve Customers.
 *
 * @author Bikramjit Dasgupta
 * @version 3.4
 */
public class SelfCheckServer extends Server {
    private static final LinkedList<Customer> selfCheckQueue = new LinkedList<>();

    /**
     * Constructs a SelfCheckServer.
     *
     * @param id SelfCheckServer Id.
     */
    private SelfCheckServer(int id) {
        super(id, SelfCheckServer.selfCheckQueue);
    }

    /**
     * Constructs a SelfCheckServer.
     *
     * @param id SelfCheckServer Id.
     * @param idle Indicator to determine if this SelfCheckServer will be idle.
     * @param resting Indicator to determine if this SelfCheckServer will be resting.
     */
    private SelfCheckServer(int id, boolean idle, boolean resting) {
        super(id, SelfCheckServer.selfCheckQueue, idle, resting);
    }

    public static SelfCheckServer createServer(int id) {
        return new SelfCheckServer(id);
    }

    /**
     * Adds a Customer to the common Customer queue
     * shared by SelfCheckServers.
     *
     * @param customer Customer to add.
     * @return This SelfCheckServer.
     */
    @Override
    public SelfCheckServer addCustomer(Customer customer) {
        SelfCheckServer.selfCheckQueue.add(customer);
        return this;
    }

    /**
     * Returns a boolean value indicating if this SelfCheckServer's
     * queue is full.
     *
     * @param limit Queue limit for the Server.
     * @return Boolean value indicating if this SelfCheckServer's queue is full.
     */
    @Override
    public boolean isFull(int limit) {
        return SelfCheckServer.selfCheckQueue.size() >= limit;
    }

    /**
     * Returns a boolean value indicating if this SelfCheckServer's
     * queue is empty.
     *
     * @return Boolean value indicating if this SelfCheckServer's queue is empty.
     */
    @Override
    public boolean isEmptyQueue() {
        return SelfCheckServer.selfCheckQueue.isEmpty();
    }

    /**
     * Removes and returns the first Customer on the
     * waiting queue, shared by the SelfCheckServers.
     *
     * @return First Customer waiting in the queue.
     */
    @Override
    public Customer removeHead() {
        return SelfCheckServer.selfCheckQueue.removeFirst();
    }

    /**
     * Returns the number of Customers waiting in the
     * SelfCheckServer queue.
     *
     * @return Number of Customers waiting.
     */
    @Override
    public int genQueueSize() {
        return SelfCheckServer.selfCheckQueue.size();
    }

    /**
     * Returns a new SelfCheckServer based on this SelfCheckServer,
     * but with an idle state.
     *
     * @return New SelfCheckServer with an idle state.
     */
    @Override
    public SelfCheckServer setFree() {
        return new SelfCheckServer(super.id, true, super.resting);
    }

    /**
     * Returns a new SelfCheckServer based on this SelfCheckServer,
     * but with a busy state.
     *
     * @return New SelfCheckServer with a busy state.
     */
    @Override
    public SelfCheckServer setBusy() {
        return new SelfCheckServer(super.id, false, super.resting);
    }

    /**
     * Returns a new SelfCheckServer based on this SelfCheckServer,
     * but with a resting state.
     *
     * @return New SelfCheckServer with a resting state.
     */
    @Override
    public SelfCheckServer startRest() {
        return new SelfCheckServer(super.id, super.idle, false);
    }

    /**
     * Returns this SelfCheckServer.
     *
     * @return this SelfCheckServer.
     */
    @Override
    public SelfCheckServer stopRest() {
        return this;
    }

    /**
     * Returns a String based on this SelfCheckServer.
     *
     * @return String based on this SelfCheckServer.
     */
    @Override
    public String toString() {
        return "self-check " + super.id;
    }
}
