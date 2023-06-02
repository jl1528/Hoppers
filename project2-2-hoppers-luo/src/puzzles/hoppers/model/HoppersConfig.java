package puzzles.hoppers.model;

import puzzles.common.Coordinates;
import puzzles.common.solver.Configuration;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * the config class of the constructor
 * @author Jonathan Luo
 */
public class HoppersConfig implements Configuration{
    /** 2 d array representing the hoppers board */
    private final char[][] board;
    /** amount of row the board has */
    private final int row;
    /** amount of columns the board has */
    private final int column;
    /** list of coordinates of frogs */
    private final List<Coordinates> frogCoordinates;

    /**
     * public hoppers config that takes in a string file, reads and creates the hoppers board
     * @param filename name of the file path
     * @throws IOException exception for no file found
     */
    public HoppersConfig(String filename) throws IOException {
        try (BufferedReader in = new BufferedReader(new FileReader(filename))){
            String[] line = in.readLine().split("\\s++");
            this.row = Integer.parseInt(line[0]);
            this.column = Integer.parseInt(line[1]);
            this.board = new char[row][column];
            this.frogCoordinates = new ArrayList<>();
            for (int r = 0; r<row; r++){
                line = in.readLine().split("\\s++");
                StringBuilder ch = new StringBuilder();
                for(String s:line) {
                    ch.append(s);}
                char[] cells = ch.toString().toCharArray();
                int colCount =0;
                for (char c : cells){
                    board[r][colCount] = c;
                    // go through to check for frogs to create coordinates and add them to a list
                    if(c == 'G' || c == 'R'){
                        this.frogCoordinates.add(new Coordinates(r, colCount));
                    }
                    colCount++;
                }
            }
        }
    }

    /**
     * private config for generating a hopper config for frogs
     * @param other other config
     * @param ch the char, red frog or green frog
     * @param now coordinate of the frog after it has jumped
     * @param jumped coordinate of the space it has jumped
     * @param initial coordinate of the initial position of the frog
     */
    private HoppersConfig(HoppersConfig other, char ch, Coordinates now, Coordinates jumped, Coordinates initial){
        this.row = other.row;
        this.column = other.column;
        this.frogCoordinates = new ArrayList<>();
        this.board = new char[row][column];
        for(int r = 0; r< row; r++){
            System.arraycopy(other.board[r], 0, this.board[r], 0 , column);
        }
        this.board[initial.row()][initial.col()] = '.';        // set previous to empty space
        this.board[now.row()][now.col()] = ch;           // the jumped frogs position now
        this.board[jumped.row()][jumped.col()] = '.';   // change the frog that got jumped to empty space
        // loop through board to create and add frog coordinates
        for(int r = 0; r< row; r++){
            for (int c = 0;c<column; c++) {
                if(this.board[r][c] == 'R'||this.board[r][c] == 'G'){
                    this.frogCoordinates.add(new Coordinates(r, c));
                }
            }
        }
    }

    /**
     * make a jump/ create a hopper config, used in hoppers model.
     * @param ch other config
     * @param now coordinate of the frog after it has jumped
     * @param jumped coordinate of the space it has jumped
     * @param initial coordinate of the initial position of the frog
     * @return hopper config with changed position of frogs
     */
    public HoppersConfig makeJump(char ch, Coordinates now, Coordinates jumped, Coordinates initial){
        return new HoppersConfig(this, ch, now, jumped, initial);
    }

    /**
     * check if this is a valid jump
     * @param move space the frog is jumping to
     * @param jumped the space the frog has jumped
     * @return true if is a valid jump, false otherwise
     */
    public  boolean validJump(Coordinates move, Coordinates jumped){
        if(jumped != null && insideBounds(jumped.row(), jumped.col())){
            return emptySpace(move) && greenFrog(jumped);
        }
        return false;
    }

    /**
     * checks to see if a coordinate is inside bounds before moving a piece.
     * @return true if coordinate inside bounds else false
     */
    public boolean insideBounds(int row, int column){
        if(row >= this.row || row < 0){
            return false;
        }
        return (column < this.column && column >= 0);
    }

    /**
     * create a map for frog movement, key is the new position of the frog after it jumps and
     * value is the position of the space the frog as jumped over
     * @return map of coordinates for movement of a frog
     */
    public Map<Coordinates, Coordinates> makeMove(int row, int col){
        Map<Coordinates,Coordinates> moves = new HashMap<>();
        // diagonal coordinates
        moves.put(new Coordinates(row+2,col+2),new Coordinates(row+1,col+1));
        moves.put(new Coordinates(row-2,col-2),new Coordinates(row-1,col-1));
        moves.put(new Coordinates(row-2,col+2),new Coordinates(row-1,col+1));
        moves.put(new Coordinates(row+2,col-2),new Coordinates(row+1,col-1));
        // horizontal and vertical coordinates
        moves.put(new Coordinates(row,col+4), new Coordinates(row,col+2));
        moves.put(new Coordinates(row,col-4), new Coordinates(row,col-2));
        moves.put(new Coordinates(row-4,col),new Coordinates(row-2,col));
        moves.put(new Coordinates(row+4,col), new Coordinates(row+2,col));
        return moves;
    }

    /**
     * checks if this coordinate contains an empty space in the board
     * @param coordinate coordinate of the space
     * @return true if space is empty, false otherwise
     */
    public boolean emptySpace(Coordinates coordinate){
        return board[coordinate.row()][coordinate.col()] == '.';
    }

    /**
     * checks if this coordinate contains a green frog in the board
     * @param coordinate coordinate of the space
     * @return true if there is a green frog, false otherwise
     */
    public boolean greenFrog(Coordinates coordinate){
        return board[coordinate.row()][coordinate.col()] == 'G';
    }

    /**
     * get amount of rows of the config
     * @return amount of rows
     */
    public int getRow(){
        return row;
    }

    /**
     * get amount of columns of the config
     * @return amount of coloumns
     */
    public int getColumn(){
        return column;
    }

    /**
     * get the 2d array representation of the hoppers board/ config
     * @return the board
     */
    public char[][] getBoard(){
        return board;
    }
    /**
     * gets the hashcode of this configuration
     * @return an integer representing the hashcode
     */
    @Override
    public int hashCode(){
        int hash = 0;
        for (Coordinates coord: frogCoordinates) {
            hash+=coord.hashCode();
        }
        return hash;
    }

    /**
     * compares and checks whether this configuration equals another
     * @param other the other configuration/ object
     * @return true if the configurations are equal flase otherwise
     */
    @Override
    public boolean equals(Object other){
        if (other instanceof HoppersConfig){
            return Arrays.deepEquals(this.board, ((HoppersConfig) other).board);
        }
        return false;
    }

    /**
     * is solution if red frog is last frog
     * @return true if is solution false otherwise
     */
    @Override
    public boolean isSolution() {
        for(int r = 0; r<row; r++){
            for(int c = 0; c<column; c++){
                if(this.board[r][c] == 'G'){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * get the neighbors of the current config
     * @return list of neighbors
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        List<Configuration> neighbor = new ArrayList<>();
        for (Coordinates frog:frogCoordinates) {
            char ch = this.board[frog.row()][frog.col()];
            // create map of all possible movements a frog can make
            Map<Coordinates,Coordinates> coordinates = makeMove(frog.row(), frog.col());
            for (Coordinates coord : coordinates.keySet()) {
                if(insideBounds(coord.row(),coord.col()) && emptySpace(coord) && greenFrog(coordinates.get(coord))){
                    neighbor.add(new HoppersConfig(this, ch, coord, coordinates.get(coord), frog));
                }
            }
        }
        return neighbor;
    }
    @Override
    public String toString(){
        StringBuilder string  = new StringBuilder();
        for(int r =0; r<row; r++){
            string.append(System.lineSeparator());
            for(int c = 0; c<column;c++){
                string.append(this.board[r][c]);
                string.append(" ");
            }
        }
        string.append(System.lineSeparator());
        return string.toString();
    }
}
