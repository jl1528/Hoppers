package puzzles.hoppers.gui;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersConfig;
import puzzles.hoppers.model.HoppersModel;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * the GUI portion of hoppers
 * @author Jonthan Luo
 */
public class HoppersGUI extends Application implements Observer<HoppersModel, String> {
    /** The size of all icons, in square dimension */
    private final static int ICON_SIZE = 75;
    /** the font size for labels and buttons */
    private final static int FONT_SIZE = 12;

    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";

    /** the image of the red frog*/
    private final Image redFrog = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"red_frog.png"));
    /** the image of the green frog*/
    private final Image greenFrog = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"green_frog.png"));
    /** the image of the lily pad*/
    private final Image lilyPad = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"lily_pad.png"));
    /** the image of the water*/
    private final Image water = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"water.png"));
    /** the stage for the scene and gui */
    private Stage stage;
    /** the hoppers model */
    private HoppersModel model;
    /** the label displaying alert observer messages */
    private Label label;
    /** a 2d array of buttons that will represent the spaces on the board, allows for selection and button manipulation*/
    private Button[][] buttons;
    /** the 2d array of characters represents the hopper board */
    private char[][] board;
    /** the borderpane that will contain elements to the hopper GUI */
    private BorderPane borderPane;

    /**
     * initialize the Hoppers Gui, adds itself as observer
     * @throws Exception exception
     */
    public void init() throws Exception {
        String filename = getParameters().getRaw().get(0);
        model = new HoppersModel(filename); // create new hoppers model with file from system args
        model.addObserver(this);
        board = model.getCurrentConfig().getBoard();
        // create 2d array based on the row and colour of the board
        buttons = new Button[model.getRow()][model.getCol()];

    }

    /**
     * create the center element of the borderpane.
     * @return grid pane of buttons
     */
    public GridPane createHoppersBoard(){
        GridPane hoppers = new GridPane();
        for(int r =0; r< model.getRow(); r++){
            for(int c = 0; c< model.getCol(); c++){
                // create a button for each space on hopper board
                Button button = new Button();
                button.setMinSize(ICON_SIZE, ICON_SIZE);
                button.setMaxSize(ICON_SIZE, ICON_SIZE);
                int finalR = r;
                int finalC = c;
                // set each buttons action to be select when user clicks on them
                button.setOnAction(event -> {model.select(finalR, finalC);});
                // set eh graphic for each button based on the character at that location
                if (board[r][c] == 'R'){
                    button.setGraphic(new ImageView(redFrog));
                } else if (board[r][c]=='G') {
                    button.setGraphic(new ImageView(greenFrog));
                }else if(board[r][c] == '.'){
                    button.setGraphic(new ImageView(lilyPad));
                }else {
                    button.setGraphic(new ImageView(water));

                }
                // set the position at a row and column to this particular button and add it to the grid pane
                buttons[r][c] = button;
                hoppers.add(button, c, r);
            }
        }
        return hoppers;

    }

    /**
     * starts the GUI application, displays the stage, labels and buttons
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.setTitle("Hoppers GUI");
        borderPane = new BorderPane();
        Scene scene = new Scene( borderPane);

        // top of border pane is the label with a text from alert observer method
        label= new Label("Loaded: " + model.getCurrentFile().split("/")[2]);
        label.setFont(Font.font("arial", FontWeight.BOLD, FONT_SIZE));
        BorderPane.setAlignment(label, Pos.CENTER);
        borderPane.setTop(label);

        // create the grid pane that will be at the center of the borderpane, representing the hopper board
        GridPane hoppers = createHoppersBoard();
        hoppers.setAlignment(Pos.CENTER);
        borderPane.setCenter(hoppers);

        // create the bottom of the border pane which will be an hbox of action buttons, load, reset and hint
        HBox actionButtons = new HBox();
        Button load = new Button("Load");
        Button reset = new Button("Reset");
        Button hint = new Button("Hint");
        load.setPrefSize(50, 25);
        reset.setPrefSize(50, 25);
        hint.setPrefSize(50,25);
        load.setFont(Font.font("arial", FontWeight.BOLD, FONT_SIZE));
        reset.setFont(Font.font("arial", FontWeight.BOLD, FONT_SIZE));
        hint.setFont(Font.font("arial", FontWeight.BOLD, FONT_SIZE));
        hint.setOnAction(event -> {model.hint();});
        reset.setOnAction(event -> {model.reset();});

        // the action for pressing load button
        final FileChooser chooser = new FileChooser();
        load.setOnAction(event -> {

            String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
            currentPath += File.separator + "data" + File.separator + "hoppers";
            chooser.setInitialDirectory(new File(currentPath));

            File file = chooser.showOpenDialog(stage);
            if(file!= null){
                model.load(file);
            }
        });
        actionButtons.getChildren().addAll(load, reset, hint);
        actionButtons.setAlignment(Pos.CENTER);
        borderPane.setBottom(actionButtons);

        stage.setScene(scene);
        stage.show();
    }

    /**
     * update the model and make changes on the GUI to show the current configuration
     * @param hoppersModel the object that wishes to inform this object
     *                about something that has happened.
     * @param msg optional data the server.model can send to the observer
     *
     */
    @Override
    public void update(HoppersModel hoppersModel, String msg) {
        label.setText(msg);
        board = hoppersModel.getCurrentConfig().getBoard();
        model = hoppersModel;
        buttons = new Button[hoppersModel.getRow()][hoppersModel.getCol()];

        //change board when new file is loaded
        GridPane hoppers = createHoppersBoard();
        hoppers.setAlignment(Pos.CENTER);
        borderPane.setCenter(hoppers);

        // update the button graphic
        for(int r =0; r< hoppersModel.getRow(); r++) {
            for (int c = 0; c < hoppersModel.getCol(); c++) {
                if (board[r][c] == 'R'){
                    buttons[r][c].setGraphic(new ImageView(redFrog));
                } else if (board[r][c]=='G') {
                    buttons[r][c].setGraphic(new ImageView(greenFrog));
                }else if(board[r][c] == '.'){
                    buttons[r][c].setGraphic(new ImageView(lilyPad));
                }else {
                    buttons[r][c].setGraphic(new ImageView(water));
                }
            }
        }
        this.stage.sizeToScene();  // when a different sized puzzle is loaded
    }

    /**
     * main method to run the program
     * @param args list of strings
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java HoppersPTUI filename");
        } else {
            Application.launch(args);
        }
    }
}
