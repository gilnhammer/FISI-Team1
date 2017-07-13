package MWeather;

import MWeather.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;

public class Main extends Application {

    static Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemClassLoader().getResource("Frontend.fxml"));
        Parent parent = loader.load();
        controller = loader.getController();
        primaryStage.setTitle("M-Wetter");
        primaryStage.setScene(new Scene(parent, 600, 400));
        primaryStage.show();
    }



    public static void main(String[] args) throws SQLException {
            launch(args);
    }



}