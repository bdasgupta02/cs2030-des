package cs2030.simulator;

import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;
import cs2030.simulator.Server;
import cs2030.simulator.Statistics;
import cs2030.simulator.Event;
import cs2030.simulator.Customer;
import cs2030.simulator.EventComparator;
import cs2030.simulator.SelfCheckServer;
import cs2030.simulator.GreedyCustomer;

/**
 * A shop class to store and manage a list of Server objects
 * to simulate events with Customer objects.
 *
 * @author Bikramjit Dasgupta
 * @version 3.4
 */
public class Shop {
    private final List<Server> serverList;
    private final double restProb;
    private static final int DOES_NOT_EXIST = -1;

    /**
     * Constructs a Shop.
     *
     * @param serverList List of servers created by user input.
     * @param restProb The probability of server resting for this Shop.
     */
    private Shop(List<Server> serverList, double restProb) {
        this.serverList = serverList;
        this.restProb = restProb;
    }

    /**
     * Creates a combination of human and self-checkout Server
     * objects based on their specified numbers.
     *
     * @param humanNum Number of human Servers.
     * @param selfCheckNum Number of self-checkout Servers.
     * @return A List of Servers combining the two types of Servers.
     */
    private static List<Server> createServers(int humanNum, int selfCheckNum) {
        List<Server> serverList = new ArrayList<>();
        for (int i = 0; i < humanNum; i++) {
            serverList.add(Server.createServer(i + 1));
        }
        int k = serverList.size();
        for (int i = k; i < k + selfCheckNum; i++) {
            serverList.add(SelfCheckServer.createServer(i + 1));
        }
        return serverList;
    }

    /**
     * Creates a PriorityQueue of Events based on the specified
     * number of arriving typical and greedy Customers.
     *
     * @param customerNum Number of typical Customers.
     * @param greedyCustomers Number of greedy Customers.
     * @return PriorityQueue of the combined new Events.
     */
    private static PriorityQueue<Event> makeCustomers(int customerNum, double greedyCustomers) {
        PriorityQueue<Event> events = new PriorityQueue<>(new EventComparator());
        double time = 0.0;
        for (int i = 1; i <= customerNum; i++) {
            if (Event.customerTypeSupp.get() < greedyCustomers) {
                events.offer(Event.createEvent(GreedyCustomer.createGreedyCustomer(i, time), time));
            } else {
                events.offer(Event.createEvent(Customer.createCustomer(i, time), time));
            }
            time += Event.interArrivalSupp.get();
        }
        return events;
    }

    /**
     * Creates a new Shop object and executes the simulation
     * of discrete Events.
     *
     * @param humanNum Number of human Servers.
     * @param qMax Maximum number of waiting Customers in a Server queue.
     * @param customerNum Number of typical Customers.
     * @param restProb Probability of a Server resting.
     * @param selfCheckNum Number of self-checkout Servers.
     * @param greedyCustomers Number of greedy customers.
     */
    public static void execute(int humanNum, int qMax, int customerNum, double restProb,
                               int selfCheckNum, double greedyCustomers) {
        List<Server> servers = createServers(humanNum, selfCheckNum);
        Shop shop = new Shop(servers, restProb);
        shop.simulate(makeCustomers(customerNum, greedyCustomers), qMax);
    }

    /**
     * Simulates the discrete Events based on this Shop's
     * List of Servers and a given PriorityQueue of Events,
     * and prints (i) state changes with timings, and (ii)
     * Statistics for the average waiting time, number of Customers
     * served, and the number of Customers who left without being served.
     *
     * @param eventQueue PriorityQueue of Events.
     * @param qMax Maximum number of waiting Customers in a Server queue.
     */
    public void simulate(PriorityQueue<Event> eventQueue, int qMax) {
        Statistics statistics = Statistics.createStatistics(eventQueue.size());
        int idleIdx;
        int emptyIdx;
        int greedyIdx;
        while (!eventQueue.isEmpty()) {
            Event event = eventQueue.poll();
            if (!event.isState(Event.SERVER_BACK) && !event.isState(Event.SERVER_REST)) {
                System.out.println(event);
            }
            boolean keep = true;
            if (event.isState(Event.ARRIVES)) {
                if (event.getCustomer() instanceof GreedyCustomer) {
                    greedyIdx = this.greedyServerSearch(qMax);
                    if (greedyIdx != Shop.DOES_NOT_EXIST &&
                            this.serverList.get(greedyIdx).isIdle()) {
                        this.serverList.set(greedyIdx, this.serverList.get(greedyIdx).setBusy());
                        event = event.serve(this.serverList.get(greedyIdx));
                        statistics = statistics.incrementServed();
                    } else if (greedyIdx > Shop.DOES_NOT_EXIST) {
                        Customer toQueue = event.getCustomer();
                        this.serverList.set(greedyIdx,
                                this.serverList.get(greedyIdx).addCustomer(toQueue).setBusy());
                        event = event.waits(this.serverList.get(greedyIdx));
                    } else {
                        event = event.leaves();
                    }
                } else {
                    idleIdx = this.findIdleServer();
                    emptyIdx = this.findFirstWithRoom(qMax);
                    if (idleIdx != Shop.DOES_NOT_EXIST) {
                        this.serverList.set(idleIdx, this.serverList.get(idleIdx).setBusy());
                        event = event.serve(this.serverList.get(idleIdx));
                        statistics = statistics.incrementServed();
                    } else if (emptyIdx != Shop.DOES_NOT_EXIST) {
                        Customer toQueue = event.getCustomer();
                        this.serverList.set(emptyIdx,
                                this.serverList.get(emptyIdx).addCustomer(toQueue).setBusy());
                        event = event.waits(this.serverList.get(emptyIdx));
                    } else {
                        event = event.leaves();
                    }
                }
            } else if (event.isState(Event.SERVED)) {
                event = event.done().addTime(Event.serveTimeSupp.get());
            } else if (event.isState(Event.DONE)) {
                int serverIdx = event.serverListIndex();
                double doneTime = event.getTime();
                if (!(this.serverList.get(serverIdx) instanceof SelfCheckServer) &&
                        Event.randRestSupp.get() < this.restProb) {
                    double restPeriod = Event.restPeriodSupp.get();
                    Event restEvent = Event.createEvent(null, doneTime)
                            .serverRests(this.serverList.get(serverIdx)).serverBack(restPeriod);
                    this.serverList.set(serverIdx, this.serverList.get(serverIdx).startRest());
                    eventQueue.offer(restEvent);
                } else if (this.serverList.get(serverIdx).isEmptyQueue()) {
                    this.serverList.set(serverIdx, this.serverList.get(serverIdx).setFree());
                } else {
                    Customer newCustomer = this.serverList.get(serverIdx).removeHead();
                    Event newEvent = Event.createEvent(newCustomer, doneTime)
                            .serve(this.serverList.get(serverIdx));
                    this.serverList.set(serverIdx, this.serverList.get(serverIdx).setBusy());
                    eventQueue.offer(newEvent);
                    statistics = statistics.incrementServed()
                            .addWaitTime(doneTime - newCustomer.getTime());
                }
                keep = false;
            } else if (event.isState(Event.SERVER_BACK)) {
                int serverIdx = event.serverListIndex();
                double serveTime = event.getTime();
                this.serverList.set(serverIdx, this.serverList.get(serverIdx).stopRest());
                if (!this.serverList.get(serverIdx).isEmptyQueue()) {
                    Customer newCustomer = this.serverList.get(serverIdx).removeHead();
                    Event newEvent = Event.createEvent(newCustomer, serveTime)
                            .serve(this.serverList.get(serverIdx));
                    this.serverList.set(serverIdx, this.serverList.get(serverIdx).setBusy());
                    eventQueue.offer(newEvent);
                    statistics = statistics.incrementServed()
                            .addWaitTime(serveTime - newCustomer.getTime());
                } else {
                    this.serverList.set(serverIdx, this.serverList.get(serverIdx).setFree());
                }
                keep = false;
            } else {
                keep = false;
            }
            if (keep) {
                eventQueue.offer(event);
            }
        }
        System.out.println(statistics);
    }

    /**
     * Finds the List index of the first Server with room
     * in their waiting queue.
     *
     * @param limit Maximum queue limit for a Server.
     * @return List index of the first Server with room in their waiting queue.
     */
    private int findFirstWithRoom(int limit) {
        for (int i = 0; i < this.serverList.size(); i++) {
            if (!this.serverList.get(i).isFull(limit)) {
                return i;
            }
        }
        return Shop.DOES_NOT_EXIST;
    }

    /**
     * Finds the List index of the first idle Server.
     *
     * @return List index of the first idle Server.
     */
    private int findIdleServer() {
        for (int i = 0; i < this.serverList.size(); i++) {
            if (this.serverList.get(i).isIdle() && this.serverList.get(i).isEmptyQueue() &&
                    !this.serverList.get(i).isResting()) {
                return i;
            }
        }
        return Shop.DOES_NOT_EXIST;
    }

    /**
     * Finds the List index of the Server with the
     * shortest waiting queue.
     *
     * @param qMax Maximum number of waiting Customers in a Server queue.
     * @return List index of the Server with the shortest waiting queue.
     */
    private int greedyServerSearch(int qMax) {
        int idleIdx = this.findIdleServer();
        if (idleIdx == Shop.DOES_NOT_EXIST) {
            int lowest = qMax - 1;
            for (Server server : this.serverList) {
                if (server.genQueueSize() < lowest) {
                    lowest = server.genQueueSize();
                }
            }
            for (int i = 0; i < this.serverList.size(); i++) {
                if (this.serverList.get(i).genQueueSize() == lowest) {
                    return i;
                }
            }
            return Shop.DOES_NOT_EXIST;
        } else {
            return idleIdx;
        }
    }
}
