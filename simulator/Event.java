package cs2030.simulator;

import java.util.Optional;
import java.util.function.Supplier;
import cs2030.simulator.Customer;
import cs2030.simulator.Server;
import cs2030.simulator.RandomGenerator;

/**
 * Event class to represent Event with
 * different states.
 *
 * @author Bikramjit Dasgupta
 * @version 3.4
 */
public class Event {
    private final Customer customer;
    private final double time;
    private final Optional<Server> server;
    private final int state;
    public static final int ARRIVES = 1;
    public static final int SERVED = 2;
    public static final int LEAVES = 3;
    public static final int DONE = 4;
    public static final int WAITS = 5;
    public static final int SERVER_REST = 6;
    public static final int SERVER_BACK = 7;
    public static final int DOES_NOT_EXIST = -1;
    public static Supplier<Double> customerTypeSupp;
    public static Supplier<Double> interArrivalSupp;
    public static Supplier<Double> randRestSupp;
    public static Supplier<Double> restPeriodSupp;
    public static Supplier<Double> serveTimeSupp;

    /**
     * Constructs an Event.
     *
     * @param customer Customer for this Event.
     * @param time Time-stamp of this Event.
     * @param server Optional Server for this Event.
     * @param state State of this Event.
     */
    private Event(Customer customer, double time, Optional<Server> server, int state) {
        this.customer = customer;
        this.time = time;
        this.server = server;
        this.state = state;
    }

    /**
     * Creates an Event.
     *
     * @param customer Customer for the Event.
     * @param time Time-stamp for the Event.
     * @return New Event based on the given Customer and time-stamp.
     */
    public static Event createEvent(Customer customer, double time) {
        return new Event(customer, time, Optional.empty(), Event.ARRIVES);
    }

    /**
     * Initiates the Event static Suppliers based on
     * RandomGenerator methods.
     *
     * @param seed The RandomGenerator seed.
     * @param lambda The arrival rate.
     * @param mu The service rate.
     * @param rho The resting rate.
     */
    public static void initSuppliers(int seed, double lambda, double mu, double rho) {
        RandomGenerator random = new RandomGenerator(seed, lambda, mu, rho);
        Event.customerTypeSupp = () -> random.genCustomerType();
        Event.interArrivalSupp = () -> random.genInterArrivalTime();
        Event.randRestSupp = () -> random.genRandomRest();
        Event.restPeriodSupp = () -> random.genRestPeriod();
        Event.serveTimeSupp = () -> random.genServiceTime();
    }

    /**
     * Changes the Event state to SERVED and returns
     * a new Event.
     *
     * @param server Server used to serve this Event.
     * @return New Event with a SERVED state.
     */
    public Event serve(Server server) {
        return new Event(this.customer, this.time, Optional.ofNullable(server), Event.SERVED);
    }

    /**
     * Changes the Event state to DONE and returns
     * a new Event.
     *
     * @return New Event with a DONE state.
     */
    public Event done() {
        return new Event(this.customer, this.time, this.server, Event.DONE);
    }

    /**
     * Changes the Event state to LEAVES and returns
     * a new Event.
     *
     * @return New Event with a LEAVES state.
     */
    public Event leaves() {
        return new Event(this.customer, this.time, Optional.empty(), Event.LEAVES);
    }

    /**
     * Changes the Event state to WAITS and returns
     * a new Event.
     *
     * @param server Server used to wait this Event.
     * @return New Event with a WAITS state.
     */
    public Event waits(Server server) {
        return new Event(this.customer, this.time, Optional.ofNullable(server), Event.WAITS);
    }

    /**
     * Changes the Event state to SERVER_REST and returns
     * a new Event.
     *
     * @param server Server for the new SERVER_REST Event.
     * @return New Event with a SERVER_REST state.
     */
    public Event serverRests(Server server) {
        return new Event(this.customer, this.time, Optional.of(server), Event.SERVER_REST);
    }

    /**
     * Changes the Event state to SERVER_BACK and returns
     * a new Event.
     *
     * @param restPeriod Return time of the Server in this Event.
     * @return New Event with a SERVER_BACK state.
     */
    public Event serverBack(double restPeriod) {
        return new Event(this.customer, this.time + restPeriod, this.server, Event.SERVER_BACK);
    }

    /**
     * Returns the Customer in this Event.
     *
     * @return Customer from this Event.
     */
    public Customer getCustomer() {
        return this.customer;
    }

    /**
     * Adds time to this Event's time-stamp.
     *
     * @param time Time to add.
     * @return New Event with the added time.
     */
    public Event addTime(double time) {
        return new Event(this.customer, this.time + time, this.server, this.state);
    }

    /**
     * Returns the time-stamp of this Event.
     *
     * @return Time-stamp of this Event.
     */
    public double getTime() {
        return this.time;
    }

    /**
     * Checks the state of this Event.
     *
     * @param checkState State to check this Event with.
     * @return Value to indicate it is the given state.
     */
    public boolean isState(int checkState) {
        return this.state == checkState;
    }

    /**
     * Returns the Server index for a chronological
     * List from this Event's Server.
     *
     * @return Server index.
     */
    public int serverListIndex() {
        Server s = this.server.orElse(null);
        if (s != null) {
            return s.makeListIndex();
        } else {
            return Event.DOES_NOT_EXIST;
        }
    }

    /**
     * Compares the time-stamp of this Event with
     * another Event.
     *
     * @param another The second Event in this comparison.
     * @return Value indicating the rank between the two Events.
     */
    public int compareTime(Event another) {
        if (this.time == another.time) {
            return 0;
        } else {
            return this.time > another.time ? 1 : -1;
        }
    }

    /**
     * Compares the Customer Id of this Event with another
     * Event's Customer Id.
     *
     * @param another The second Event in this comparison.
     * @return Value indicating the rank between the two Events.
     */
    public int compareId(Event another) {
        return this.customer.compareId(another.customer);
    }

    /**
     * Compares the state of this Event with another
     * Event.
     *
     * @param another The second Event in this comparison.
     * @return Value indicating the rank between the two Events.
     */
    public int compareState(Event another) {
        if (this.state == another.state) {
            return 0;
        } else {
            return this.state < another.state ? 1 : -1;
        }
    }

    /**
     * Returns a String based on this Event.
     *
     * @return String based on this Event.
     */
    @Override
    public String toString() {
        String stateString;
        if (this.state == Event.LEAVES) {
            stateString = " leaves";
        } else if (this.state == Event.SERVED) {
            stateString = " served by " + this.server.get();
        } else if (this.state == Event.WAITS) {
            stateString = " waits to be served by " + this.server.get();
        } else if (this.state == Event.DONE) {
            stateString = " done serving by " + this.server.get();
        } else if (this.state == Event.SERVER_BACK) {
            stateString = "";
        } else if (this.state == Event.SERVER_REST) {
            stateString = "";
        } else {
            stateString = " arrives";
        }
        return String.format("%.3f " + this.customer + stateString, this.time);
    }
}
