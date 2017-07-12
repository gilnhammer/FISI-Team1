package sample;


import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.postgresql.ds.PGSimpleDataSource;

import java.sql.*;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.String.valueOf;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.sun.javafx.charts.ChartLayoutAnimator;

import javax.swing.text.DateFormatter;

public class Controller {

    //Temperatur Graph - Deklarationen
    @FXML
    private NumberAxis xAchse;
    @FXML
    private NumberAxis yAchse;
    @FXML
    private LineChart<?, ?> Temp_Graph;

    //Luftfeuchtigkeit Graph - Deklarationen
    @FXML
    private NumberAxis x_Achse;
    @FXML
    private NumberAxis y_Achse;
    @FXML
    private LineChart<?, ?> Luftfeuchte_Graph;

    XYChart.Series temperatur;
    XYChart.Series luftfeuchte;

    //LabelText - Start

    @FXML
    private Label Start_Temp_Anzeige;
    @FXML
    private Label Start_Luftdruck_Anzeige;
    @FXML
    private Label Start_Luftfeuchtigkeit_Anzeige;
    @FXML
    private Label Start_Zustand_Anzeige;

    //LabelText - Temperatur

    @FXML
    private Label Temp_Anzeige;
    @FXML
    private Label Temp_Minimal;
    @FXML
    private Label Temp_Maximal;
    @FXML
    private Label Temp_Durchschnitt;

    //LabelText - Luftdruck

    @FXML
    private Label Luftdruck_Anzeige;
    @FXML
    private Label Luftdruck_Minimal;
    @FXML
    private Label Luftdruck_Maximal;
    @FXML
    private Label Luftdruck_Durchschnitt;

    //LabelText - Licht

    @FXML
    private Label Licht_Anzeige;
    @FXML
    private Label Licht_Minimal;
    @FXML
    private Label Licht_Maximal;
    @FXML
    private Label Licht_Durchschnitt;

    private int v = 0;


    //Datenbank Verbindung
    static Connection connection;

    private static ArrayList<Double> id = new ArrayList<Double>();
    private static ArrayList<Double> illuminance = new ArrayList<Double>();
    private static ArrayList<Double> humidity = new ArrayList<Double>();
    private static ArrayList<Double> airPressure = new ArrayList<Double>();
    private static ArrayList<Double> temperature = new ArrayList<Double>();
    private static ArrayList<Timestamp> timestamp = new ArrayList<Timestamp>();

    private Timeline timeline = null;

    SimpleDateFormat dateformatter = new SimpleDateFormat("HH:mm");

    private ZonedDateTime startofDay;
    private ZonedDateTime endofDay;

    public void initialize() throws SQLException {

        try {
            getData(1500);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        setDaten();

        startofDay = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS);
        endofDay = startofDay.plusDays(1);

        System.out.println(startofDay);
        System.out.println(endofDay);

        xAchse.setLowerBound(startofDay.toEpochSecond() * 1000);
        xAchse.setUpperBound(endofDay.toEpochSecond() * 1000);

        x_Achse.setLowerBound(startofDay.toEpochSecond() * 1000);
        x_Achse.setUpperBound(endofDay.toEpochSecond() * 1000);

        xAchse.setTickUnit(7200000);
        x_Achse.setTickUnit(7200000);

        temperatur = new XYChart.Series();
        luftfeuchte = new XYChart.Series();

        Temp_Graph.setCreateSymbols(false);
        Luftfeuchte_Graph.setCreateSymbols(false);

        Temp_Graph.getData().addAll(temperatur); //Werte auf dem Graphen anzeigen lassen
        Luftfeuchte_Graph.getData().addAll(luftfeuchte);

        //xAchse.setLabel("Zeit in h");
        //x_Achse.setLabel("Zeit in h");

        xAchse.setTickLabelFormatter(new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                String x = String.valueOf(dateformatter.format(object));

                return x;
            }

            @Override
            public Number fromString(String string) {


                return null;
            }
        });

        x_Achse.setTickLabelFormatter(new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                String x = String.valueOf(dateformatter.format(object));

                return x;
            }

            @Override
            public Number fromString(String string) {


                return null;
            }
        });

        setGraph();

        timeline = new Timeline(new KeyFrame(Duration.minutes(0.1), event ->{

            try {
                getData(1500);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            setDaten();

            setGraph();


        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

    }

    public void setGraph() {

        temperatur.getData().clear();


        /*
        long lowBound = timestamp.get(287).getTime();
        long upBound = timestamp.get(0).getTime();

        xAchse.setLowerBound(lowBound);
        xAchse.setUpperBound(upBound);
        xAchse.setTickUnit(600000);
        */

        //System.out.println(x);

        int x = 0;

        for (double i : temperature) {



            temperatur.getData().add(new XYChart.Data<>(timestamp.get(x).getTime(), i/100)); //Chart Werte übergeben

            //System.out.println(i);
            x++;

        }


        luftfeuchte.getData().clear();

        int y = 0;

        for (double i : humidity) {

            luftfeuchte.getData().add(new XYChart.Data<>(timestamp.get(y).getTime(), i/10)); //Chart Werte übergebe

            y += 1;

            //System.out.println(i);

        }

        System.out.println("Low " + xAchse.getLowerBound());

    }



    public void setDaten(){

        double STA = (temperature.get(0)/100);

        System.out.println("Aktuelle Temperatur: " + STA);

        Start_Temp_Anzeige.setText(String.valueOf(STA) + " °C");

        double SLA = (airPressure.get(0)/1000);
        Start_Luftdruck_Anzeige.setText(String.valueOf(SLA) + " mBar");

        double SLFA = (humidity.get(0)/10);
        Start_Luftfeuchtigkeit_Anzeige.setText(String.valueOf(SLFA) + " %");

        double LA = (illuminance.get(0)/100);

        if (STA > 33) {
            Start_Zustand_Anzeige.setText("ES IST ZU WARM!!");
        }else if (STA < 0 ) {
            Start_Zustand_Anzeige.setText("ES IST ZU KALT!!");
        }else{
            Start_Zustand_Anzeige.setText("ok");
        }

        Temp_Anzeige.setText(String.valueOf(STA) + "° C");
        Temp_Maximal.setText("40" + "°C");
        Temp_Minimal.setText("10" + "°C");
        Temp_Durchschnitt.setText("30" + "°C");

        Luftdruck_Anzeige.setText(String.valueOf(SLA) + " mBar");
        Luftdruck_Minimal.setText("850" + " mbar");
        Luftdruck_Maximal.setText("910" + " mbar");
        Luftdruck_Durchschnitt.setText("860" + " mbar");

        Licht_Anzeige.setText(String.valueOf(LA) + " lx");
        Licht_Maximal.setText("30" + "lx");
        Licht_Minimal.setText("10" + "lx");
        Licht_Durchschnitt.setText("20" + "lx");

    }

    public static void getData(int i) throws SQLException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setDatabaseName("WetterStation");
        dataSource.setServerName("192.168.178.142");
        dataSource.setPortNumber(5432);
        dataSource.setUser("postgres");
        dataSource.setPassword("Pa$$w0rd");
        connection = dataSource.getConnection();
        String simpleQueryStr = "SELECT * from wetterdaten ORDER BY id DESC LIMIT ?";
        PreparedStatement simpleQuery = connection.prepareStatement(simpleQueryStr);
        simpleQuery.setInt(1, i);
        ResultSet resultSet = simpleQuery.executeQuery();

        int x = 0;

        id.clear();
        temperature.clear();
        airPressure.clear();
        humidity.clear();
        illuminance.clear();
        timestamp.clear();

        while (resultSet.next()){

            id.add(resultSet.getDouble(1));
            temperature.add(resultSet.getDouble(2));
            airPressure.add(resultSet.getDouble(3));
            humidity.add(resultSet.getDouble(4));
            illuminance.add(resultSet.getDouble(5));
            timestamp.add(resultSet.getTimestamp(6));

            //System.out.println("ID: " + id + " Temperatur: " + temperature + " Luftdruck: " + airPressure + " Luftfeuchtigkeit: " + humidity + " Helligkeit: " + illuminance);

        }

        System.out.println(temperature.size());

        connection.close();

    }

}
