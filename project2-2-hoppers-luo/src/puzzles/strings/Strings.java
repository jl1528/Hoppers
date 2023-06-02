package puzzles.strings;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.util.List;

public class Strings {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(("Usage: java Strings start finish"));
        } else {
            // get information from the program arguments
            String start = args[0];
            String end = args[1];
            // create start and end string config
            StringsConfig stringsConfigStart = new StringsConfig(start,end);
            StringsConfig stringsConfigEnd = new StringsConfig(end,end);
            // solve and create path
            Solver solver = new Solver(stringsConfigStart);
            List<Configuration> path = solver.constructPath(stringsConfigStart,stringsConfigEnd);
            // print out the results and steps
            System.out.println("Start: " + args[0] + ", End: " + args[1]);
            System.out.println("Total configs: " + solver.getTotal());
            System.out.println("Unique configs: "+ solver.getUnique());
            if(path.isEmpty()){
                System.out.println("No solution");
            }else if (path.get(0).toString().equals(args[1])) {
                System.out.println("Step 0: "+ path.get(0));
            }else {
                for(int i = 0 ; i <path.size(); i++){
                    System.out.println("Step " + i + ": " + path.get(i));
                }
            }

        }
    }
}
