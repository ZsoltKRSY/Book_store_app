package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CustomerView {

    private Label customerLabel;

    public CustomerView(Stage primaryStage){
        primaryStage.setTitle("Bookstore");

        GridPane gridPane = new GridPane();
        initializeGridPane(gridPane);

        Scene scene = new Scene(gridPane, 720, 480);
        primaryStage.setScene(scene);

        initializeUserScreen(gridPane);

        primaryStage.show();
    }

    private void initializeGridPane(GridPane gridPane){
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));
    }

    private void initializeUserScreen(GridPane gridPane){
        Text sceneTitle = new Text("Unfortunately, you're only a customer.");
        sceneTitle.setFont(Font.font("Tahome", FontWeight.BOLD, 24));
        gridPane.add(sceneTitle, 0, 0, 2, 2);

        Text sceneDescription1 = new Text("Not much for you to do here!");
        sceneDescription1.setFont(Font.font(("Tahome"), FontWeight.NORMAL, 20));
        gridPane.add(sceneDescription1, 1, 2, 1, 1);
        Text sceneDescription2 = new Text("(at least at the moment)");
        sceneDescription2.setFont(Font.font(("Tahome"), FontWeight.NORMAL, 20));
        gridPane.add(sceneDescription2, 1, 3, 1, 1);
    }
}
