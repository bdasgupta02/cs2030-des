package cs2030.simulator;

import java.util.LinkedList;
import cs2030.simulator.Customer;

/**
 * Server class to store and manage Servers to serve
 * Customers.
 *
 * @author Bikramjit Dasgupta
 * @version 3.4
 */
public class Server {
    protected final int id;
    private final LinkedList<Customer> customerQueue; //
    protected final boolean idle;
    protected final boolean resting;

    /**
     * Constructs a Server.
     *
     * @param id Server Id.
     * @param customerQueue Customer queue for this Server.
     */
    protected Server(int id, LinkedList<Customer> customerQueue) {
        this.id = id;
        this.customerQueue = customerQueue;
        this.idle = true;
        this.resting = false;
    }

    /**
     * Constructs a Server.
     *
     * @param id Server Id.
     * @param customerQueue Customer queue for this Server.
     * @param idle Indicator to determine if this Server is waiting.
     * @param resting Indicator to determine if this Server is resting.
     */
    protected Server(int id, LinkedList<Customer> customerQueue, boolean idle, boolean resting) {
        this.id = id;
        this.customerQueue = customerQueue;
        this.idle = idle;
        this.resting = resting;
    }

    /**
     * Creates a new Server with a given Id number.
     *
     * @param id Server Id.
     * @return New Server with the given Id.
     */
    public static Server createServer(int id) {
        return new Server(id, new LinkedList<>());
    }

    /**
     * Adds a Customer to this Server's Customer queue.
     *
     * @param customer Customer to add.
     * @return New server with the updated queue.
     */
    public Server addCustomer(Customer customer) { //
        this.customerQueue.add(customer);
        return new Server(this.id, this.customerQueue, false, this.resting);
    }

    /**
     * Returns a boolean value indicating if this Server's
     * queue is full.
     *
     * @param limit Queue limit for the Server.
     * @return Boolean value indicating if this Server's queue is full.
     */
    public boolean isFull(int limit) {
        return this.customerQueue.size() >= limit;
    }

    /**
     * Returns a boolean value indicating if this Server's
     * queue is empty.
     *
     * @return Boolean value indicating if this Server's queue is empty.
     */
    public boolean isEmptyQueue() {
        return this.customerQueue.size() == 0;
    }

    /**
     * Removes and returns the first Customer in this Server's
     * Queue.
     *
     * @return The first Customer in this Server's queue.
     */
    public Customer removeHead() {
        return this.customerQueue.removeFirst();
    }

    /**
     * Creates a List index for this Server based on
     * its Id number.
     *
     * @return Integer value indicating this Server's index in a List.
     */
    public int makeListIndex() {
        return this.id - 1;
    }

    /**
     * Returns a new Server based on this Server, but with an
     * idle state.
     *
     * @return New Server with an idle state.
     */
    public Server setFree() {
        return new Server(this.id, this.customerQueue, true, this.resting);
    }

    /**
     * Returns a new Server based on this Server, but with a
     * busy state.
     *
     * @return New Server with a busy state.
     */
    public Server setBusy() {
        return new Server(this.id, this.customerQueue, false, this.resting);
    }

    /**
     * Returns a boolean value indicating if this Server is resting.
     *
     * @return Boolean value indicating if this Server is resting.
     */
    public boolean isResting() {
        return this.resting;
    }

    /**
     * Returns a new Server based on this Server, but with a
     * resting state.
     *
     * @return New Server with a resting state.
     */
    public Server startRest() {
        return new Server(this.id, this.customerQueue, this.idle, true);
    }

    /**
     * Returns a new Server based on this Server, but with a
     * non-resting state.
     *
     * @return New Server with a non-resting state.
     */
    public Server stopRest() {
        return new Server(this.id, this.customerQueue, this.idle, false);
    }

    /**
     * Returns a boolean value indicating if this Server is idle.
     *
     * @return Boolean value indicating if this Server is idle.
     */
    public boolean isIdle() {
        return this.idle;
    }

    /**
     * Returns the number of Customers waiting in this Server's
     * queue.
     *
     * @return Number of customers waiting in this Server's queue.
     */
    public int genQueueSize() {
        return this.customerQueue.size();
    }

    /**
     * Returns a String based on this Server.
     *
     * @return String based on this Server.
     */
    @Override
    public String toString() {
        return "server " + this.id;
    }
}
