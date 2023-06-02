package puzzles.common.solver;

import java.util.*;

public class Solver {
    /** the total configs generated*/
    private int total;
    /** the predecessor map for bfs*/
    Map<Configuration ,Configuration> predecessors = new HashMap<>();
    /** the solution configuration*/
    private Configuration solution;

    /**
     * the constructor of solver which is the bfs algorithm
     * @param start the starting configuration
     */
    public Solver(Configuration start){
        List<Configuration> queue = new LinkedList<>();
        queue.add(start);
        predecessors.put(start,start);
        total+=1;
        while (!queue.isEmpty()) {
            // process next node at front of queue
            Configuration current = queue.remove(0);
            if (current.isSolution()) {
                solution = current;
                break;
            }
            // loop over all neighbors of current
            for (Configuration nbr : current.getNeighbors()) {
                total+=1;
                // process unvisited neighbors
                if(!predecessors.containsKey(nbr)) {
                    predecessors.put(nbr, current);
                    queue.add(nbr);
                }
            }
        }
    }

    /**
     * get the solution after solving the configuration
     * @return solution configuration
     */
    public Configuration getSolution() {
        return solution;
    }

    /**
     * construct the shortest path from predecessor map
     * @param startNode the start configuration
     * @param endNode the end configuration
     * @return a list of strings representing the path
     */
    public List<Configuration> constructPath(Configuration startNode, Configuration endNode) {
        List<Configuration> path = new LinkedList<>();
        if(predecessors.containsKey(endNode)) {
            Configuration currNode = endNode;
            while (currNode != startNode) {
                path.add(0, currNode);
                currNode = predecessors.get(currNode);
            }
            path.add(0, startNode);
        }
        return path;
    }

    /**
     * get total configs generated
     * @return the number of total configs
     */
    public int getTotal(){
        return total;
    }

    /**
     * get the number of unique configs
     * @return number of configs
     */
    public int getUnique(){return predecessors.size();}
}
