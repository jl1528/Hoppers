package puzzles.strings;

import puzzles.common.solver.Configuration;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class StringsConfig implements Configuration {
    /** the starting string */
    private final String start;
    /** the end string */
    private final String end;
    /** the starting string as a char array */
    private final char[] startCh;

    /**
     * the public configuration for generating a string config
     * @param start starting string
     * @param end ending string
     */
    public StringsConfig(String start, String end){
        this.start = start;
        this.end = end;
        startCh = this.start.toCharArray();
    }

    /**
     * private copy constructor for generating neighbors/ successors of current string config
     * @param other the other string configuration
     * @param ch the char that will replace the letter in current config's string
     * @param i the index of where the replacement char goes
     */
    private StringsConfig(StringsConfig other, char ch, int i){
        this.end = other.end;
        char[] tempCh = other.start.toCharArray();  // crete temp char array to change the letter int the strings
        tempCh[i] = ch;
        this.start = new String(tempCh);    // this start will be the changed string from char array
        this.startCh = this.start.toCharArray();
    }

    /**
     * check if the config is a solution
     * @return true if it is a solution, false otherwise
     */
    @Override
    public boolean isSolution() {
        return start.equals(end);
    }

    /**
     * gets the neighbors of the current configuration by generating new configs
     * by changing a letter in the startign string
     * @return a list of neighbors
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        List<Configuration> neighbors = new LinkedList<>();
        // loop through the index of the char array to change a specific letter
        // each letter has 2 neighbors, a letter forward, a letter backward
        for(int i = 0; i<startCh.length;i++) {
            if (startCh[i] == 'A') {
                neighbors.add(new StringsConfig(this, 'Z', i));
                neighbors.add(new StringsConfig(this, 'B', i));
            }else if (startCh[i] == 'Z') {
                neighbors.add(new StringsConfig(this, 'A', i));
                neighbors.add(new StringsConfig(this,'Y', i));
            }else {
                int ch = startCh[i];    // get the ascii value to be able to change any other characters
                neighbors.add(new StringsConfig(this, (char)(ch+1), i));
                neighbors.add(new StringsConfig(this, (char)(ch-1), i));
            }
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
        if( other instanceof StringsConfig){
            return this.start.equals(((StringsConfig) other).start);
        }
        return false;
    }

    /**
     * gets the hashcode of this configuration
     * @return an integer representing the config's hashcode
     */
    @Override
    public int hashCode() {
        return start.hashCode() + end.hashCode();
    }

    /**
     * the string representation of the configuration
     * @return a string
     */
    @Override
    public String toString() {
        return this.start;
    }
}
