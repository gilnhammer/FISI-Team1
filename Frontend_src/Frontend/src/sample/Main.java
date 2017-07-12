package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends Application {

    static Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Frontend.fxml"));
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