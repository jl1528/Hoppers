package puzzles.hoppers.model;

import puzzles.common.Coordinates;
import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * the model class of the hoppers
 * @author Jonathan Luo
 */
public class HoppersModel {
    /** the collection of observers of this model */
    private final List<Observer<HoppersModel, String>> observers = new LinkedList<>();

    /** the current configuration */
    private HoppersConfig currentConfig;
    /** the current file of the hopper */
    private String currentFile;
    /** a list of selected coordinates */
    private final List<Coordinates> selected = new ArrayList<>();

    /**
     * The view calls this to add itself as an observer.
     * @param observer the view
     */
    public void addObserver(Observer<HoppersModel, String> observer) {
        this.observers.add(observer);
    }

    /**
     * get the current config of the model
     * @return hopper config
     */
    public HoppersConfig getCurrentConfig(){
        return this.currentConfig;
    }

    /**
     * get the row of the hopper board/ config
     * @return amount of rows
     */
    public int getRow(){
        return this.currentConfig.getRow();
    }

    /**
     * get the columns of the hopper board/ config
     * @return amount of columns
     */
    public int getCol(){
        return this.currentConfig.getColumn();
    }

    /**
     * get the current file of the model
     * @return a string representation of the file path
     */
    public String getCurrentFile(){
        return this.currentFile;
    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */
    private void alertObservers(String msg) {
        for (var observer : observers) {
            observer.update(this, msg);
        }
    }

    /**
     * the select method that makes movements/ changes on the model
     * @param row current row
     * @param col current column
     */
    public void select(int row, int col){
        // add row and col as a coordinate to selected
        selected.add(new Coordinates(row,col));
        Coordinates initial = selected.get(0);  // first coordinate is the initial position of the frog
        char ch = currentConfig.getBoard()[initial.row()][initial.col()];   // get the char of said frog, can be G or R
        // if selected has 2 coordinates, user wants to make a frog jump
        if(selected.size() ==2){
            Coordinates move = selected.get(1); // second coordinate is where the frog is jumping to
            // make the move and get the coordinate of the jumped position
            Coordinates jumped  = currentConfig.makeMove(initial.row(),initial.col()).get(move);
            if(currentConfig.validJump(move,jumped)){
                // if the jump is valid then make a Hoppers config with that change
                currentConfig = currentConfig.makeJump(ch, move,jumped,initial);
                // alert the observer of a change
                alertObservers("Jumped from ("+initial.row()+", "+initial.col()+") " +
                        "to ("+move.row()+", "+move.col()+")");
                // remove everything in selected list
                selected.removeAll(selected);
            }else {
                // else it is not a valid move, alert the observer and remove everything in selected
                alertObservers("Can't jump from ("+initial.row()+", "+initial.col()+") " +
                        "to ("+move.row()+", "+move.col()+")");
                selected.removeAll(selected);
            }
        }else{
            // if selected doesn't have a length of 2, is the first selection
            if(ch == 'G' || ch == 'R'){
                // if it s a frog than it is a valid selection, alert observer of this selection
                alertObservers("Selected (" + row +", "+col+")");
            }else {
                // else this space is not a valid selection, remove the selection and alert observer
                selected.remove(0);
                alertObservers("No frog at  (" + row +", "+col+")");
            }
        }
    }

    /**
     * reset the board
     */
    public void reset(){
        try {
            // set the current config to a new hoppers config of the same file
            currentConfig = new HoppersConfig(currentFile);
            alertObservers("Puzzle reset!");
        }catch (IOException ioe){
            System.out.println(ioe.getMessage());
        }
    }

    /**
     * give user a hint by making the next move on the board
     */
    public void hint(){
        // construct a path of configuration
        List<Configuration> path = solve();
        // if the current config has a solution
        if(path.contains(currentConfig)){
            // if it's solution is not itself
            if(path.size() > 1){
                Configuration next = path.remove(1);
                currentConfig = (HoppersConfig) next;
                alertObservers("Next Step!");
            }else{
                // the config is already solution
                alertObservers("Already solved!");
            }
        }else{
            // the current config has no next move, no solution
            alertObservers("No Solution!");
        }


    }

    /**
     * load a file into the model
     * @param file the file as a File object
     */
    public void load(File file){
        try {
            // set the current file to the new file that is being loaded
            this.currentFile = file.toString();
            // create a new config with new file
            this.currentConfig = new HoppersConfig(currentFile);
            String[] loaded = file.toString().split("\\\\");
            // alert observer that a new file is loaded and the models config changes
            alertObservers("Loaded: " + loaded[loaded.length -1]);
        }catch (IOException ioe){
            // not a valid file, alert observer
            alertObservers("Failed ot load:" + file);
        }
    }

    /**
     * solve the current configuration
     * @return path of the solved configuation
     */
    public List<Configuration> solve(){
        Solver sol = new Solver(currentConfig);
        return sol.constructPath(currentConfig, sol.getSolution());
    }

    /**
     * hoppers model constructor
     * @param filename the file path as a string
     * @throws IOException exception
     */
    public HoppersModel(String filename) throws IOException {
        this.currentConfig = new HoppersConfig(filename);
        this.currentFile = filename;
    }
}
