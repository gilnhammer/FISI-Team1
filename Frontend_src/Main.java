package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent parent = FXMLLoader.load(getClass().getResource("Frontend.fxml"));
        primaryStage.setTitle("M-Wetter");
        primaryStage.setScene(new Scene(parent, 600, 400));
        primaryStage.show();




    }

    public static void main(String[] args) {
        launch(args);
    }
}