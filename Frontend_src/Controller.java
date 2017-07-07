package sample;


import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

public class Controller {

    //Temperatur Graph - Deklarationen
    @FXML
    private CategoryAxis xAchse;
    @FXML
    private NumberAxis yAchse;
    @FXML
    private LineChart<?, ?> Temp_Graph;

    //Luftfeuchtigkeit Graph - Deklarationen
    @FXML
    private CategoryAxis x_Achse;
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




    public void initialize() {

        //TEMPERATUR GRAPH
        XYChart.Series temperatur = new XYChart.Series();    //Chart-Objekt erstellen

        temperatur.getData().add(new XYChart.Data<>("1", 33)); //Chart Werte übergeben
        temperatur.getData().add(new XYChart.Data<>("2", 30)); //xAchse: String, yAchse: Number
        temperatur.getData().add(new XYChart.Data<>("4", 30));
        temperatur.getData().add(new XYChart.Data<>("5", 32));
        temperatur.getData().add(new XYChart.Data<>("6", 29));
        temperatur.getData().add(new XYChart.Data<>("7", 28));
        temperatur.getData().add(new XYChart.Data<>("8", 28));
        temperatur.getData().add(new XYChart.Data<>("9", 27));
        temperatur.getData().add(new XYChart.Data<>("10", 27));
        temperatur.getData().add(new XYChart.Data<>("11", 24));
        temperatur.getData().add(new XYChart.Data<>("12", -15));
        temperatur.getData().add(new XYChart.Data<>("13", 22));
        temperatur.getData().add(new XYChart.Data<>("14", 0));
        temperatur.getData().add(new XYChart.Data<>("15", 19));
        temperatur.getData().add(new XYChart.Data<>("16", 18));
        temperatur.getData().add(new XYChart.Data<>("17", 16));
        temperatur.getData().add(new XYChart.Data<>("18", 17));
        temperatur.getData().add(new XYChart.Data<>("19", 19));
        temperatur.getData().add(new XYChart.Data<>("20", 21));
        temperatur.getData().add(new XYChart.Data<>("21", 22));
        temperatur.getData().add(new XYChart.Data<>("22", 23));
        temperatur.getData().add(new XYChart.Data<>("23", 24));
        temperatur.getData().add(new XYChart.Data<>("24", 27));

        Temp_Graph.getData().addAll(temperatur); //Werte auf dem Graphen anzeigen lassen


        //Luftfeuchtigkeit Graph
        XYChart.Series luftfeuchte = new XYChart.Series(); //Chart-Objekt erstellen

        luftfeuchte.getData().add(new XYChart.Data<>("1",40));//Chart Werte übergeben
        luftfeuchte.getData().add(new XYChart.Data<>("2",34));//xAchse: String, yAchse: Number
        luftfeuchte.getData().add(new XYChart.Data<>("3",39));
        luftfeuchte.getData().add(new XYChart.Data<>("4",37));
        luftfeuchte.getData().add(new XYChart.Data<>("5",35));
        luftfeuchte.getData().add(new XYChart.Data<>("6",34));
        luftfeuchte.getData().add(new XYChart.Data<>("7",34));
        luftfeuchte.getData().add(new XYChart.Data<>("8",33));
        luftfeuchte.getData().add(new XYChart.Data<>("9",32));
        luftfeuchte.getData().add(new XYChart.Data<>("10",35));
        luftfeuchte.getData().add(new XYChart.Data<>("11",36));
        luftfeuchte.getData().add(new XYChart.Data<>("12",35));
        luftfeuchte.getData().add(new XYChart.Data<>("14",33));
        luftfeuchte.getData().add(new XYChart.Data<>("15",31));
        luftfeuchte.getData().add(new XYChart.Data<>("16",30));
        luftfeuchte.getData().add(new XYChart.Data<>("17",29));
        luftfeuchte.getData().add(new XYChart.Data<>("18",41));
        luftfeuchte.getData().add(new XYChart.Data<>("19",42));
        luftfeuchte.getData().add(new XYChart.Data<>("20",38));
        luftfeuchte.getData().add(new XYChart.Data<>("21",37));
        luftfeuchte.getData().add(new XYChart.Data<>("22",37));
        luftfeuchte.getData().add(new XYChart.Data<>("23",36));
        luftfeuchte.getData().add(new XYChart.Data<>("24",24));

        Luftfeuchte_Graph.getData().addAll(luftfeuchte);

        Start_Temp_Anzeige.setText("20" + "°C");
        Start_Luftdruck_Anzeige.setText("900" + " mbar");
        Start_Luftfeuchtigkeit_Anzeige.setText("40" + "%");
        Start_Zustand_Anzeige.setText("OK");

        Temp_Anzeige.setText("20" + "°C");
        Temp_Maximal.setText("40" + "°C");
        Temp_Minimal.setText("10" + "°C");
        Temp_Durchschnitt.setText("30" + "°C");

        Luftdruck_Anzeige.setText("900" + " mbar");
        Luftdruck_Minimal.setText("850" + " mbar");
        Luftdruck_Maximal.setText("910" + " mbar");
        Luftdruck_Durchschnitt.setText("860" + " mbar");

        Licht_Anzeige.setText("20" + "lx");
        Licht_Maximal.setText("30" + "lx");
        Licht_Minimal.setText("10" + "lx");
        Licht_Durchschnitt.setText("20" + "lx");


    }

}
