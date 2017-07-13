package MWeather;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.postgresql.ds.PGSimpleDataSource;

import java.sql.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

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

    private static double temperatureMin;
    private static double temperatureMax;
    private static double temperatureAvg;

    private static double airPressureMin;
    private static double airPressureMax;
    private static double airPressureAvg;

    private static double illuminanceMin;
    private static double illuminanceMax;
    private static double illuminanceAvg;



    //Datenbank Verbindung
    static Connection connection;

    private static ArrayList<Double> id = new ArrayList<Double>();
    private static ArrayList<Double> illuminance = new ArrayList<Double>();
    private static ArrayList<Double> humidity = new ArrayList<Double>();
    private static ArrayList<Double> airPressure = new ArrayList<Double>();
    private static ArrayList<Double> temperature = new ArrayList<Double>();
    private static ArrayList<Timestamp> timestamp = new ArrayList<Timestamp>();

    private Timeline timeline = null;

    SimpleDateFormat dateformatter = new SimpleDateFormat("HH");

    DecimalFormat f = new DecimalFormat("#0.00");

    private ZonedDateTime startofDay;
    private ZonedDateTime endofDay;

    public void initialize() throws SQLException {

        try {
            getData(288);
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

        timeline = new Timeline(new KeyFrame(Duration.minutes(5), event ->{

            try {
                getData(288);
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

        temperatureAvg = getAvg(temperature);
        temperatureMax = getMax(temperature);
        temperatureMin = getMin(temperature);

        airPressureAvg = getAvg(airPressure);
        airPressureMax = getMax(airPressure);
        airPressureMin = getMin(airPressure);

        illuminanceAvg = getAvg(illuminance);
        illuminanceMax = getMax(illuminance);
        illuminanceMin = getMin(illuminance);


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

        Temp_Anzeige.setText(String.valueOf(STA) + " °C");
        Temp_Maximal.setText(temperatureMax/100.0 + " °C");
        Temp_Minimal.setText(temperatureMin/100 + " °C");
        Temp_Durchschnitt.setText(f.format(temperatureAvg/100.0) + " °C");

        Luftdruck_Anzeige.setText(String.valueOf(SLA) + " mBar");
        Luftdruck_Minimal.setText(airPressureMin/10 + " mbar");
        Luftdruck_Maximal.setText(airPressureMax/10 + " mbar");
        Luftdruck_Durchschnitt.setText(f.format(airPressureAvg/10) + " mbar");

        Licht_Anzeige.setText(String.valueOf(LA) + " lx");
        Licht_Maximal.setText(illuminanceMax/100 + "lx");
        Licht_Minimal.setText(illuminanceMin/100 + "lx");
        Licht_Durchschnitt.setText(f.format(illuminanceAvg/100) + "lx");

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

    public double getMin(ArrayList<Double> i){

        double min = i.get(0);

        for(double x : i){
            if(x < min){
                min = x;
            }
        }

        return min;
    }

    public double getMax(ArrayList<Double> i){

        double max = i.get(0);

        for(double x : i){
            if(x > max){
                max = x;
            }
        }

        return max;
    }

    public double getAvg(ArrayList<Double> i){

        double avg = 0;

        int h = 0;

        for(double x : i){

            avg = avg + x;

            h++;
        }

        avg = avg / h;

        return avg;
    }

}
