package puzzles.clock;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.util.List;

public class Clock {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(("Usage: java Clock hours stop end"));
        } else {
            // get information from the program arguments
            int hours = Integer.parseInt(args[0]);
            int start = Integer.parseInt(args[1]);
            int end = Integer.parseInt(args[2]);
            // create start and end clock config
            ClockConfig clockStart = new ClockConfig(hours,start,end);
            ClockConfig clockEnd = new ClockConfig(hours,end,end);
            // solve and create path
            Solver solver = new Solver(clockStart);
            List<Configuration> path = solver.constructPath(clockStart,clockEnd);
            // print out the results and steps
            System.out.println("Hours: " + hours + ", Start: " + start + ", End: " + end);
            System.out.println("Total configs: " + solver.getTotal());
            System.out.println("Unique configs: "+ solver.getUnique());
            if(path.isEmpty()){
                System.out.println("No solution");
            } else if (path.get(0).toString().equals(args[2])) {
                System.out.println("Step 0: "+ path.get(0));
            } else{
                for(int i=0; i<path.size();i++){
                    System.out.println("Step " + i + ": " + path.get(i));
                }
            }
        }
    }
}
