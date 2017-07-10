package sample;


import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static java.lang.String.valueOf;

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


    //Datenbank Verbindung


    static Connection connection;

    private static ArrayList<Double> id = new ArrayList<Double>();
    private static ArrayList<Double> illuminance = new ArrayList<Double>();
    private static ArrayList<Double> humidity = new ArrayList<Double>();
    private static ArrayList<Double> airPressure = new ArrayList<Double>();
    private static ArrayList<Double> temperature = new ArrayList<Double>();

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

        while (resultSet.next()){

            id.add(resultSet.getDouble(1));
            temperature.add(resultSet.getDouble(2));
            airPressure.add(resultSet.getDouble(3));
            humidity.add(resultSet.getDouble(4));
            illuminance.add(resultSet.getDouble(5));



            //System.out.println(temperature.get(x));

            System.out.println("ID: " + id + " Temperatur: " + temperature + " Luftdruck: " + airPressure + " Luftfeuchtigkeit: " + humidity + " Helligkeit: " + illuminance);

        }




    }



    public void initialize() throws SQLException {


        //TEMPERATUR GRAPH
        XYChart.Series temperatur = new XYChart.Series();    //Chart-Objekt erstellen
        Temp_Graph.setCreateSymbols(false);

        temperatur.getData().add(new XYChart.Data<>(1, 33)); //Chart Werte übergeben
        temperatur.getData().add(new XYChart.Data<>(2, 30)); //xAchse: String, yAchse: Number


        Temp_Graph.getData().addAll(temperatur); //Werte auf dem Graphen anzeigen lassen


        //Luftfeuchtigkeit Graph
        XYChart.Series luftfeuchte = new XYChart.Series(); //Chart-Objekt erstellen
        Luftfeuchte_Graph.setCreateSymbols(false);

        luftfeuchte.getData().add(new XYChart.Data<>(1,40));//Chart Werte übergeben
        luftfeuchte.getData().add(new XYChart.Data<>(2,34));//xAchse: String, yAchse: Number


        Luftfeuchte_Graph.getData().addAll(luftfeuchte);

        double STA = (temperature.get(0)/100);
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

}
