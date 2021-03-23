import java.util.Scanner;
import cs2030.simulator.Shop;
import cs2030.simulator.Event;

/**
 * Main class for a program to simulate discrete events
 * with different types of Servers and Events.
 *
 * @author Bikramjit Dasgupta
 * @version 3.4
 */
public class Main {

    /**
     * Uses a Scanner and calls another method to read
     * user input.
     *
     * @param args String array of arguments.
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        inputAndRun(sc);
        sc.close();
    }

    /**
     * Reads user input through a given Scanner and
     * (i) configures public Suppliers for Events, and
     * (ii) calls the Shop class to run and print the simulation.
     *
     * @param sc Scanner to read input.
     */
    private static void inputAndRun(Scanner sc) {
        final int seed = sc.nextInt();
        final int serverNum = sc.nextInt();
        final int selfCheckNum = sc.nextInt();
        final int qLength = sc.nextInt();
        final int customerNum = sc.nextInt();
        final double arrRate = sc.nextDouble();
        final double serveRate = sc.nextDouble();
        final double restRate = sc.nextDouble();
        final double restProb = sc.nextDouble();
        final double greedyCustomers = sc.nextDouble();
        Event.initSuppliers(seed, arrRate, serveRate, restRate);
        Shop.execute(serverNum, qLength, customerNum, restProb, selfCheckNum, greedyCustomers);
    }
}
