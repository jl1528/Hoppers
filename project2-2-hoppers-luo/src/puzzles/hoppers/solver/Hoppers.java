package puzzles.hoppers.solver;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.hoppers.model.HoppersConfig;

import java.io.IOException;
import java.util.List;

public class Hoppers {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Hoppers filename");
        }
        try{
            System.out.print("File: " + args[0]);
            HoppersConfig hoppers = new HoppersConfig(args[0]);
            System.out.println(hoppers);
            Solver solver = new Solver(hoppers);
            List<Configuration> path = solver.constructPath(hoppers, solver.getSolution());
            System.out.println("Total configs: " + solver.getTotal());
            System.out.println("Unique configs: " + solver.getUnique());
            if(path.isEmpty()){
                System.out.println("No solution");
            } else{
                for(int i=0; i<path.size();i++){
                    System.out.println("Step " + i + ": " + path.get(i));
                }
            }


        }catch (IOException ioe){
            System.err.println("Io exception");

        }
    }
}
