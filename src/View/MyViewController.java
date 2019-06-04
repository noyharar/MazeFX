package View;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import com.sun.xml.internal.bind.v2.TODO;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;


import javax.swing.event.MenuEvent;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.ResourceBundle;


public class MyViewController implements IView, Observer {
    @FXML
    public MenuButton menuCharacter;
    public MenuItem daveboy;
    public MenuItem davegirl;


    @FXML
    private TextField heightField;

    @FXML
    private TextField widthField;
    @FXML
    private ToggleGroup toggleLevel;

    @FXML
    private RadioButton levelEasy;
    @FXML
    private RadioButton levelHard;

    @FXML
    private ImageView solveMazeImageButton;
    @FXML
    private ImageView solveImageButton;

    @FXML
    private MenuItem solveMazeButton;

    private MediaPlayer mediaPlayer;

    @FXML
    private CheckBox BGM_checkBox;

    @FXML
    public MazeDisplayer mazeDisplayer;
    @FXML
    public SolutionDisplayer solDisplayer;
    @FXML
    public DaveDisplayer daveDisplayer;

    public GameDisplayer gameDisplayer;

    @FXML
    public Label lbl_characterRow;
    public Label lbl_characterColumn;


    @FXML
    public MenuButton menuStyle;
    public MenuItem redStyle;
    public MenuItem blueStyle;
    public MenuItem colorfulStyle;
    public MenuItem brownStyle;

    private MyViewModel myViewModel;

    public String name = "Dave";

    public StringProperty characterRow = new SimpleStringProperty();
    public StringProperty characterColumn = new SimpleStringProperty();

    public BorderPane mainPane;
    public Pane mazePane;
    private boolean finishedAlready;

    private double origDaveX,origDaveY, orgSceneX,orgSceneY;

    private boolean loadMaze;


    public void initialize(MyViewModel myViewModel) {
        finishedAlready = false;
        loadMaze = false;
        gameDisplayer = new GameDisplayer();
        gameDisplayer.setMazeDisplayer(mazeDisplayer);
        gameDisplayer.setSolDisplayer(solDisplayer);
        gameDisplayer.setDaveDisplayer(daveDisplayer);
        //Set Binding for Properties
        lbl_characterRow.textProperty().bind(characterRow);
        lbl_characterColumn.textProperty().bind(characterColumn);

        mazePane.prefHeightProperty().bind(mainPane.heightProperty());
        mazePane.prefWidthProperty().bind(mainPane.widthProperty());
        mazeDisplayer.heightProperty().bind(mazePane.heightProperty());
        mazeDisplayer.widthProperty().bind(mazePane.widthProperty());
        mazeDisplayer.heightProperty().addListener((obs,oldVal,newVal)->{
            redrawMazeOnZoom();
        });
        mazeDisplayer.widthProperty().addListener((obs,oldVal,newVal)->{
            redrawMazeOnZoom();
        });


        solDisplayer.heightProperty().bind(mazeDisplayer.heightProperty());
        solDisplayer.widthProperty().bind(mazeDisplayer.widthProperty());
        solDisplayer.widthProperty().addListener(((observable, oldValue, newValue) -> {
            redrawSolutionOnZoom();
        }));
        solDisplayer.heightProperty().addListener((obs,old,newV)->{
            redrawSolutionOnZoom();
        });


        daveDisplayer.heightProperty().bind(mazeDisplayer.heightProperty());
        daveDisplayer.widthProperty().bind(mazeDisplayer.widthProperty());
        daveDisplayer.heightProperty().addListener((obs,oldVal,newVal)->{
            redrawMazeOnZoom();
        });
        daveDisplayer.widthProperty().addListener((obs,oldVal,newVal)->{
            redrawMazeOnZoom();
        });

        setDisableSolveButtons(true);



    }

    private void redrawSolutionOnZoom() {
        gameDisplayer.drawSolution(myViewModel.getMaze());
    }

    private void setDisableSolveButtons(Boolean doDisabled)
    {
        solveImageButton.setDisable(doDisabled);
        solveMazeImageButton.setDisable(doDisabled);
        solveMazeButton.setDisable(doDisabled);
    }

    //    @FXML
//    private Label valueError;


    int[][] mazeData;
//            = { // a stub...
////        {0, 1, 1, 1,},
////        {0, 0, 0, 0},
////        {0, 0, 1, 1},
////        {1, 1, 1, 0}
//            {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
//            {0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 0, 0, 1},
//            {0, 0, 1, 1, 1, 1, 1, 0, 0, 1, 0, 1, 0, 1, 1},
//            {1, 1, 1, 0, 0, 0, 1, 0, 1, 1, 0, 1, 0, 0, 1},
//            {1, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 1, 1, 0, 1},
//            {1, 1, 0, 0, 0, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1},
//            {1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 1},
//            {1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 0, 0, 1, 1},
//            {1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 1},
//            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1}
//    };

    public void solveMaze()
    {
        gameDisplayer.solDisplayer.setVisibleMaze(true);
        myViewModel.solveGame();

        setDisableSolveButtons(true);


    }

    public void generateMaze() {
        //int rows = Integer.valueOf(txtfld_rowsNum.getText());
        //int columns = Integer.valueOf(txtfld_columnsNum.getText());
        //this.mazeDisplayer.setMaze(getRandomMaze(rows,columns));

        gameDisplayer.solDisplayer.setVisibleMaze(false);
        myViewModel.generateMaze(Integer.parseInt(heightField.getText()),Integer.parseInt(widthField.getText()));
        finishedAlready = false;
        setDisableSolveButtons(false);


//        this.mazeDisplayer.setMaze(mazeData);
    }

    public void createLevel() {
        toggleLevel = new ToggleGroup();
        levelEasy.setSelected(true);
        levelEasy.setUserData("Easy");
        levelHard.setUserData("Hard");

        levelEasy.setToggleGroup(toggleLevel);
        levelHard.setToggleGroup(toggleLevel);
        toggleLevel.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                RadioButton last = (RadioButton) oldValue;
                RadioButton next = (RadioButton) newValue;
                //TODO: Add a real function to RadioButtons
                System.out.println(last.getText() + " was changed to " + next.getText());
            }
        });

    }


    public void openInstructions() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Instructions");
        alert.setHeaderText("HOW TO PLAY?");
        alert.setContentText("דייב המסוכן גירסת המבוך\n מטרת המשחק: עיזרו לדייב להגיע לדלת הסתרים \n אופן יצירת מבוך: כדי ליצור מבוך עליך להכניס את מידות המבוך הרצוי עליך.\n נא בחר מספר שלם וחיובי עבור הגובה(height)ורוחב(width) המבוך.\n" +
                "נא ללחוץ על כפתור Generate Maze (או Ctrl + G לקיצור).\nבמידה ואתם נתקעים או סתם עצלנים, ניתן לקבל את מסלול המבוך על ידי לחיצה על Solve Maze (או Ctrl + F לקיצור) והפתרון יוצג על גבי המסך.\n\nתוכלו לשמור ולטעון מבוכים שמורים ע\"י לחיצה על File ובחירת Save Maze או Load Maze בהתאמה. (Ctrl + S לשמירה, Ctrl + L לטעינה)\n\nעל מנת לזוז יש להשתמש במקשי החיצים, או לחילופין בלחצני המספרים ב-NumPad (2,4,6,8 בהתאמה). במידה ונתקעתם בקיר, תיפסלו ותחזרו לתחילת המשחק!\nניתן לזוז באלכסון כדי לקצר (1,3,7,9 בהתאמה), אם הדרך חסומה משני הצדדים בקירות תיפסלו ותחזרו להתחלה.\n");
        alert.show();
    }


    private int[] getValues() {
        String rowSizeFromUser = heightField.getText();
        String colSizeFromUser = widthField.getText();
        int[] mazeSizes = new int[2];
        if (rowSizeFromUser.isEmpty() || colSizeFromUser.isEmpty() || Integer.parseInt(rowSizeFromUser) > 300 ||
                Integer.parseInt(rowSizeFromUser) < 0 || Integer.parseInt(colSizeFromUser) > 300 || Integer.parseInt(colSizeFromUser) < 0) {
//            valueError.setVisible(true);
            return null;
        } else {
//            valueError.setText("Well Done, Creating Your Maze");
//            valueError.setVisible(true);
            mazeSizes[0] = Integer.parseInt(rowSizeFromUser);
            mazeSizes[1] = Integer.parseInt(colSizeFromUser);
//            newWindow.close();
//            isUsed = true;

        }
        return mazeSizes;
    }

    public void playMusic() {
        String BGM = "resources/Audio/mazemusic.mp3";
        Media hit = new Media(new File(BGM).toURI().toString());
        mediaPlayer = new MediaPlayer(hit);
        mediaPlayer.play();
        //loop
        mediaPlayer.setOnEndOfMedia(this::playMusic);

    }



    public void backgroundMusicStatus() {
        if (BGM_checkBox.isSelected()) {
            mediaPlayer.play();
        } else {
            mediaPlayer.pause();
        }
    }

    public void exitFromTheGame() throws InterruptedException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("EXIT");
        mediaPlayer.stop();
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES) {
            myViewModel.playSound("resources/Audio/goodBye.mp3");
            myViewModel.closeGame();
            Thread.sleep(855);
            Platform.exit();
        }
    }



    public void mouseClicked(MouseEvent mouseEvent) {

        mazeDisplayer.requestFocus();
        orgSceneX = mouseEvent.getSceneX();
        orgSceneY = mouseEvent.getSceneY();
    }


    public void moveDaveMouse(MouseEvent mEvent)
    {
        if(mEvent.isDragDetect()) {
            System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$ Entered Drag Event");
            if (gameDisplayer.getMazeDisplayer().getMaze() != null)
            {
                double offsetX = mEvent.getSceneX() - orgSceneX;
                double offsetY = mEvent.getSceneY() - orgSceneY;



//                while()
                System.out.println(offsetX + "," + offsetY);
            }
//            EventHandler<MouseEvent> circleOnMousePressedEventHandler = new EventHandler<MouseEvent>() {
//                public void handle(MouseEvent t) {
//                    double orgSceneX = t.getSceneX();
//                    double orgSceneY = t.getSceneY();
//                    Object orgTranslateX = (t.getSource());
//                    Object orgTranslateY = (t.getSource());
//                }
//            };
//
//            EventHandler<MouseEvent> circleOnMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
//
//                public void handle(MouseEvent t) {
//                    double offsetX = t.getSceneX() - orgSceneX;
//                    double offsetY = t.getSceneY() - orgSceneY;
//                    double newTranslateX = orgTranslateX + offsetX;
//                    double newTranslateY = orgTranslateY + offsetY;
//
//                    ((t.getSource())).setTranslateX(newTranslateX);
//                    ((t.getSource())).setTranslateY(newTranslateY);
//                }
//            };
//        }
        }
        mEvent.consume();
    }


    public void KeyPressedEasy(KeyEvent keyEvent) {
        int level = 1;
        if (!(keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.UP
                || keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.NUMPAD8 || keyEvent.getCode() == KeyCode.NUMPAD2
                || keyEvent.getCode() == KeyCode.NUMPAD4 || keyEvent.getCode() == KeyCode.NUMPAD6
                || keyEvent.getCode() == KeyCode.NUMPAD1 || keyEvent.getCode() == KeyCode.NUMPAD3
                || keyEvent.getCode() == KeyCode.NUMPAD7 || keyEvent.getCode() == KeyCode.NUMPAD9)) {
            keyEvent.consume();
            Alert alert = new Alert(Alert.AlertType.WARNING, "You pressed on illegal button.\n Please read the instructions and try again. ", ButtonType.OK);
            alert.setTitle("WARNING");
            alert.showAndWait();
            return;
        }

        if (levelEasy.isSelected()) {
            level = 1;

            if(keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.NUMPAD6)
            {
                daveDisplayer.setImageFileNameCharacter("resources/Images/" + name + ".png");
            }
            else if(keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.NUMPAD4)
            {
                daveDisplayer.setImageFileNameCharacter("resources/Images/" + name + "Left.jpg");
            }


        } else if (levelHard.isSelected()){
            level = -1;
            if(keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.NUMPAD4)
            {
                daveDisplayer.setImageFileNameCharacter("resources/Images/" + name + "Left.jpg");
            }
            else if(keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.NUMPAD6)
            {
                daveDisplayer.setImageFileNameCharacter("resources/Images/" + name + ".png");
            }
        }
        myViewModel.moveCharacter(keyEvent,level);

        if(myViewModel.gameWon() && !finishedAlready)
        {
            mediaPlayer.stop();
            playSpecificSound("resources/Audio/woohoo.wav");
            Alert EndGame = new Alert(Alert.AlertType.INFORMATION,"Congratulations!!! You have Won the Game, Dave is Resuced =)");
            EndGame.setTitle("Congratulations");
            EndGame.showAndWait();
            finishedAlready = true;


        }
//        mazeDisplayer.setCharacterPosition(ch);


//        System.out.println(characterRowNewPosition + "," + characterColumnNewPosition);
        this.characterRow.setValue(String.valueOf(daveDisplayer.getCharacterPositionRow()));
        this.characterColumn.setValue(String.valueOf(daveDisplayer.getCharacterPositionColumn()));
        keyEvent.consume();

    }

    private void playSpecificSound(String musicPath) {
        AudioClip sound = new AudioClip(new File(musicPath).toURI().toString());
        sound.play();
    }



    public void changeStyleToBlue() {
        this.mazeDisplayer.setImageFileNameWall("resources/Images/blueWall.jpg");
        gameDisplayer.redrawMaze();
    }

    public void changeStyleToRed() {
        this.mazeDisplayer.setImageFileNameWall("resources/Images/redWall.jpg");
//        mazeDisplayer.redraw();
        gameDisplayer.redrawMaze();
    }

    public void changeStyleTobrown() {
        this.mazeDisplayer.setImageFileNameWall("resources/Images/brownWall.jpg");
//        mazeDisplayer.redraw();
        gameDisplayer.redrawMaze();
    }

    public void changeStyleToColorful() {
        this.mazeDisplayer.setImageFileNameWall("resources/Images/ColorfulWall.jpg");
//        mazeDisplayer.redraw();
        gameDisplayer.redrawMaze();
    }

    public void changeToDave(ActionEvent actionEvent) {
        name = "dave";
        daveDisplayer.setImageFileNameCharacter("resources/Images/dave.png");
//        mazeDisplayer.redraw();
        gameDisplayer.redrawMaze();
    }

    public void changeToLily(ActionEvent actionEvent) {
        name = "lily";
        daveDisplayer.setImageFileNameCharacter("resources/Images/lily.png");
//        mazeDisplayer.redraw();
        gameDisplayer.redrawMaze();
    }

    public void saveMazeView()
    {
        Stage saveWindow = new Stage();
        FileChooser fileChooser = new FileChooser();

        //Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Maze files (*.maze)", "*.maze");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(saveWindow);


        if (file != null ) {
            try {
                finishToSave(file.toString());

            }
            catch (FileNotFoundException fileNfound)
            {
                fileNfound.printStackTrace();
            }
        }

    }

    public void LoadMaze()
    {
        finishedAlready = false;

        Stage loadWindow = new Stage();
        FileChooser fileChooser = new FileChooser();

        //Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Maze files (*.maze)", "*.maze");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showOpenDialog(loadWindow);


        if(file != null)
            myViewModel.loadMazeFromDisk(file.toString());


    }

    private void finishToSave(String name) throws FileNotFoundException {
        myViewModel.viewModelSaveMazeToTheDisc(name);
    }

    public void setViewModel(MyViewModel viewModel) {
        myViewModel = viewModel;
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o == myViewModel)
        {
            Maze testMaze = myViewModel.getMaze();
            if(testMaze != null) {
                mazeDisplayer.setMaze(myViewModel.getMaze());
                gameDisplayer.setCharacterPosition(myViewModel.getPosition());
                System.out.println("New Row=" + myViewModel.getPosition().getRowIndex() + ", New Col=" + myViewModel.getPosition().getColumnIndex());
            }
            else
            {
                Alert mazeNotFoundAlert = new Alert(Alert.AlertType.ERROR);
                mazeNotFoundAlert.setTitle("Maze Not Found!");
                mazeNotFoundAlert.setContentText("Please Try Reloading or Regenerating your maze!");
                mazeNotFoundAlert.show();

            }

            Solution testSolution = myViewModel.getSolution();

            if(testSolution != null)
            {
                solDisplayer.setSol(myViewModel.getSolution());
                solDisplayer.drawSolution(myViewModel.getMaze());
            }


        }

    }

    public void redrawMazeOnZoom()
    {
        gameDisplayer.redrawMaze();
    }


}


