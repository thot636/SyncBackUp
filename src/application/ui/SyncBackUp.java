package application.ui;

import javafx.application.Application;
//import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author kderezinski
 */
public class SyncBackUp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("SyncBackUp");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(25, 25, 25, 25));
        RowConstraints row1 = new RowConstraints();
        row1.setPrefHeight(20);
        RowConstraints row2 = new RowConstraints();
        row2.setPrefHeight(170);
        RowConstraints row3 = new RowConstraints();
        row3.setPrefHeight(20);
        RowConstraints row4 = new RowConstraints();
        row4.setPrefHeight(170);
        RowConstraints row5 = new RowConstraints();
        row5.setPrefHeight(20);
        RowConstraints row6 = new RowConstraints();
        row6.setPrefHeight(170);
        grid.getRowConstraints().addAll(row1, row2, row3, row4, row5, row6);
        Scene scene = new Scene(grid, 500, 475);
        primaryStage.setScene(scene);

        BackgroundImage myBI = new BackgroundImage(new Image("background.jpg", 520, 495, false, true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        //then you set to your node
        //myContainer.setBackground(new Background(myBI));
        Region region = (Region) scene.getRoot();
        region.setBackground(new Background(myBI));
        
        Text text1 = new Text("Perform new back-up:");
        text1.setStyle(
                "-fx-text-fill: red;"+
                "-fx-font: 20px Medhurst;"
        );
        grid.add(text1, 0, 0);
        
        Button btn1 = new Button("NEW");
        btn1.setStyle(
                "-fx-background-radius: 5em; " +
                "-fx-min-width: 100px; " +
                "-fx-min-height: 100px; " +
                "-fx-max-width: 100px; " +
                "-fx-max-height: 100px;" +
                "-fx-base: #E00000;"+
                "-fx-text-fill: yellow;"+
                "-fx-font: 18px Medhurst;"
                
        );
        //he HBox pane sets an alignment for the button that is different 
        //from the alignment applied to the other controls in the grid pane
        HBox hbBtn1 = new HBox(10);
        hbBtn1.setAlignment(Pos.CENTER);
        hbBtn1.getChildren().add(btn1);
        grid.add(hbBtn1, 0, 1);

        btn1.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                
                NewBackUpWIndow nw = new NewBackUpWIndow();
                nw.start();
                
            }
        });

        DropShadow shadow = new DropShadow();
        //Adding the shadow when the mouse cursor is on
        btn1.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            btn1.setEffect(shadow);
        });

        //Removing the shadow when the mouse cursor is off
        btn1.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            btn1.setEffect(null);
        });
        
        Text text2 = new Text("Sync existing back-up:");
        text2.setStyle(
                "-fx-text-fill: black;"+
                "-fx-font: 20px Medhurst;"
        );
        grid.add(text2, 0, 2);
       
        Button btn2 = new Button("SYNC");
        btn2.setStyle(
                "-fx-background-radius: 5em; " +
                "-fx-min-width: 100px; " +
                "-fx-min-height: 100px; " +
                "-fx-max-width: 100px; " +
                "-fx-max-height: 100px;" +
                "-fx-base: #E00000;"+
                "-fx-text-fill: yellow;"+
                "-fx-font: 18px Medhurst;"
        );
        HBox hbBtn2 = new HBox(10);
        hbBtn2.setAlignment(Pos.CENTER);
        hbBtn2.getChildren().add(btn2);
        grid.add(hbBtn2, 0, 3);

        btn2.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                // type an action here
            }
        });
        
        btn2.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            btn2.setEffect(shadow);
        });

        //Removing the shadow when the mouse cursor is off
        btn2.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            btn2.setEffect(null);
        });
        
        Text text3 = new Text("Restore from the back-up:");
        text3.setStyle(
                "-fx-text-fill: black;"+
                "-fx-font: 20px Medhurst;"
        );
        grid.add(text3, 0, 4);
      
        Button btn3 = new Button("RESTORE");
        btn3.setStyle(
                "-fx-background-radius: 5em; " +
                "-fx-min-width: 100px; " +
                "-fx-min-height: 100px; " +
                "-fx-max-width: 100px; " +
                "-fx-max-height: 100px;" +
                "-fx-base: #E00000;"+
                "-fx-text-fill: yellow;"+
                "-fx-font: 18px Medhurst;"
        );
        HBox hbBtn3 = new HBox(10);
        hbBtn3.setAlignment(Pos.CENTER);
        hbBtn3.getChildren().add(btn3);
        grid.add(hbBtn3, 0, 5);

        btn3.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                // type an action here
            }
        });
        
        btn3.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            btn3.setEffect(shadow);
        });

        //Removing the shadow when the mouse cursor is off
        btn3.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            btn3.setEffect(null);
        });
        
        
        //grid.setGridLinesVisible(true)
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        
        launch(args);
    }
    public static Stage primaryStage;
    
}
