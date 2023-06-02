package puzzles.clock;

import puzzles.common.solver.Configuration;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ClockConfig implements Configuration {
    /** the total hours the clock has*/
    private final int hours;
    /** the starting hour*/
    private final int start;
    /** the ending hour*/
    private final int end;

    /**
     * clock configuration
     * @param hours total hours of clock
     * @param start starting hour
     * @param end end hour
     */
    public ClockConfig(int hours, int start, int end ){
        this.hours = hours;
        this.start = start;
        this.end = end;
    }

    /**
     * private copy constructor for generating neighbors/ successors of current clock config
     * @param other the other clock config
     * @param current current hour to be set
     */
    private ClockConfig(ClockConfig other, int current){
        this.hours = other.hours;
        this.end = other.end;
        this.start = current;
    }

    /**
     * checks if the config is a solution by checking whether the solution's start is equal to the end
     * @return ture if the configuration is a solution, false otherwise
     */
    @Override
    public boolean isSolution() {
        return start == end;
    }

    /**
     * gets the neighbors of the current configuration by generating new configs with differing starts
     * @return a linked list that are neighbors of the config
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        List<Configuration> neighbors = new LinkedList<>();
        // start will have 2 neighbors, an hour forwards and an hour backwards
        if(this.start == 1){
            neighbors.add(new ClockConfig(this, hours));
            neighbors.add(new ClockConfig(this, start+1));
        } else if (this.start == hours) {
            neighbors.add(new ClockConfig(this, start-1));
            neighbors.add(new ClockConfig(this, 1));
        }
        else {
            neighbors.add(new ClockConfig(this, start-1));
            neighbors.add(new ClockConfig(this, start+1));
        }
        return neighbors;
    }

    /**
     * compares and checks whether this configuration equals another
     * @param other the other configuration/ object
     * @return true if this config's start is equal to the other's, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if(other instanceof ClockConfig){
            return this.start == ((ClockConfig) other).start;
        }
        return false;
    }

    /**
     * gets the hashcode of this configuration
     * @return an integer representing the config's hashcode
     */
    @Override
    public int hashCode() {
        return start + end + hours;
    }

    /**
     * the string representation of the configuration
     * @return a string
     */
    @Override
    public String toString() {
        return String.valueOf(this.start);
    }
}
