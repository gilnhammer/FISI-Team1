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


    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent parent = FXMLLoader.load(getClass().getResource("Frontend.fxml"));
        primaryStage.setTitle("M-Wetter");
        primaryStage.setScene(new Scene(parent, 600, 400));
        primaryStage.show();





    }



    public static void main(String[] args) throws SQLException {

        Controller controller = new Controller();

        Timer t = new Timer();

        t.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    controller.initialize();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 5000);

        controller.getData(288);


        launch(args);
    }



}