package cs2030.simulator;

import java.util.Comparator;
import cs2030.simulator.Event;

/**
 * EventComparator class to compare and rank Event objects.
 *
 * @author Bikramjit Dasgupta
 * @version 3.4
 */
public class EventComparator implements Comparator<Event> {

    /**
     * Compares two Event objects by comparing their (i) times,
     * (ii) Id numbers and (iii) states, and returns an int value
     * to determine the ranking between the two Events.
     *
     * @param a The first Event used in this comparator.
     * @param b The second Event used in this comparator.
     * @return Integer value indicating the ranking between the two Events.
     */
    @Override
    public int compare(Event a, Event b) {
        if (a.compareTime(b) == 0) {
            if (a.compareState(b) == 0) {
                if (a.compareId(b) == 0) {
                    return 0;
                } else {
                    return a.compareId(b);
                }   
            } else {
                return a.compareState(b);
            }
        } else {
            return a.compareTime(b);
        }        
    }
}
