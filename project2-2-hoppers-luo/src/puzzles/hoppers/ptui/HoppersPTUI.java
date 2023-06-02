package puzzles.hoppers.ptui;

import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersConfig;
import puzzles.hoppers.model.HoppersModel;


import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * the PTUI of the hoppers puzzle
 * @author Jonathan Luo
 */
public class HoppersPTUI implements Observer<HoppersModel, String> {
    /** the hoppers model */
    private HoppersModel model;

    /**
     * initialize the pt-ui, calls itself ass the observer
     * @param filename the file as a string
     * @throws IOException no file exception
     */
    public void init(String filename) throws IOException {
        this.model = new HoppersModel(filename);
        this.model.addObserver(this);
        load(filename);
        displayHelp();
    }

    /** hint function that calls model's hint */
    public void hint(){
        model.hint();
    }

    /**
     * load function that calls model load and passes the filename as a file object
     * @param filename the file as a string
     */
    public void load(String filename){
        model.load(new File(filename));
    }

    /**
     * select function that calls model's select function
     * @param row the current row
     * @param col the current col
     */
    public void select(int row, int col){
        model.select(row,col);
    }

    /**
     * reset tha calls the model's reset method
     */
    public void reset(){
        model.reset();
    }

    /**
     * update the model and prints the data and display the board
     * @param model the object that wishes to inform this object
     *                about something that has happened.
     * @param data optional data the server.model can send to the observer
     *
     */
    @Override
    public void update(HoppersModel model, String data) {
        this.model = model;
        System.out.println(data);
        displayBoard();
    }

    /**
     * display board displays the hoppers board with more readability, numbered cols and rows
     */
    public void displayBoard(){
        StringBuilder board = new StringBuilder();
        HoppersConfig hopper = this.model.getCurrentConfig();
        board.append("  ");
        // number the columns
        for (int i = 0; i<hopper.getColumn(); i++){
            board.append(" ").append(i);
        }
        board.append(System.lineSeparator());
        board.append("  ").append("-".repeat(Math.max(0, hopper.getColumn() * 2)));
        // number the rows
        for(int r =0; r<hopper.getRow(); r++){
            board.append(System.lineSeparator()).append(r).append("| ");
            for(int c = 0; c<hopper.getColumn();c++){
                board.append(hopper.getBoard()[r][c]);
                board.append(" ");
            }
        }
        board.append(System.lineSeparator());
        System.out.println(board);
    }

    /**
     * displays what the user can do
     */
    private void displayHelp() {
        System.out.println( "h(int)              -- hint next move" );
        System.out.println( "l(oad) filename     -- load new puzzle file" );
        System.out.println( "s(elect) r c        -- select cell at r, c" );
        System.out.println( "q(uit)              -- quit the game" );
        System.out.println( "r(eset)             -- reset the current game" );
    }

    /**
     * run the program. takes in user input and calls methods based on the first letter
     */
    public void run() {
        Scanner in = new Scanner( System.in );
        for ( ; ; ) {
            System.out.print( "> " );
            String line = in.nextLine();
            String[] words = line.split( "\\s+" );
            if (words.length > 0) {
                if (words[0].startsWith( "q" )) {
                    break;
                }if ( words[0].startsWith("s")){
                    select(Integer.parseInt(words[1]),Integer.parseInt(words[2]));
                }if ( words[0].startsWith("h")){
                    hint();
                }if ( words[0].startsWith("r")){
                    reset();
                }if(words[0].startsWith("l")){
                    load(words[1]);
                }
            }
        }
    }

    /**
     * main method to initialize and run the pt-ui program for hoppers
     * @param args the arguments, should be a file path
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java HoppersPTUI filename");
        } else {
            try {
                HoppersPTUI ptui = new HoppersPTUI();
                ptui.init(args[0]);
                ptui.run();
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        }
    }
}
