package com.ziobrowski.controllers;

import com.ziobrowski.*;
import com.ziobrowski.database.Ticket;
import com.ziobrowski.database.Train;
import com.ziobrowski.database.TrainRoute;
import com.ziobrowski.database.TrainStation;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ExportController implements Initializable {
    @FXML
    Button ticketImportButton;
    @FXML
    Button ticketExportButton;
    @FXML
    Button trainsImportButton;
    @FXML
    Button trainsExportButton;
    @FXML
    Button routesImportButton;
    @FXML
    Button routesExportButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ticketImportButton.setOnAction(event -> {
            try {
                DataController.getTickets().clear();
                readFromCSV(Ticket.class, "tickets");

            } catch (Exception exception) {
                exception.printStackTrace();

                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Import/Export Data");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Could not import tickets from file");
                errorAlert.showAndWait();
            }
        });

        ticketExportButton.setOnAction(event -> {
            try {
                writeToCSV(Ticket.class, "tickets");
            } catch (Exception exception) {
                exception.printStackTrace();

                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Import/Export Data");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Could not export tickets to file");
                errorAlert.showAndWait();
            }
        });

        trainsImportButton.setOnAction(event -> {
            try {
                DataController.getTrains().clear();
                readFromCSV(Train.class, "trains");
            } catch (Exception exception) {
                exception.printStackTrace();

                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Import/Export Data");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Could not import trains from file");
                errorAlert.showAndWait();
            }
        });

        trainsExportButton.setOnAction(event -> {
            try {
                writeToCSV(Train.class, "trains");
            } catch (Exception exception) {
                exception.printStackTrace();

                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Import/Export Data");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Could not export trains to file");
                errorAlert.showAndWait();
            }
        });
        routesImportButton.setOnAction(event -> {
            try {
                DataController.getRoutes().clear();
                readFromCSV(TrainRoute.class, "routes");
            } catch (Exception exception) {
                exception.printStackTrace();

                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Import/Export Data");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Could not import trains from file");
                errorAlert.showAndWait();
            }
        });

        routesExportButton.setOnAction(event -> {
            try {
                writeToCSV(TrainRoute.class, "trains");
            } catch (Exception exception) {
                exception.printStackTrace();

                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Import/Export Data");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Could not export trains to file");
                errorAlert.showAndWait();
            }
        });
    }

    static final String dumpFileName = "full_data.dat";

    public static void serialize() throws IOException {
        ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(dumpFileName));
        stream.writeObject(new ArrayList<>(DataController.stations));
        stream.writeObject(new ArrayList<>(DataController.trains));
        stream.writeObject(new ArrayList<>(DataController.tickets));
        stream.writeObject(new ArrayList<>(DataController.routes));
        stream.flush();
        stream.close();
    }

    public static void deserialize() throws IOException, ClassNotFoundException {
        ObjectInputStream stream = new ObjectInputStream(new FileInputStream(dumpFileName));
        DataController.stations = FXCollections.observableArrayList((ArrayList<TrainStation>) stream.readObject());
        DataController.trains = FXCollections.observableArrayList((ArrayList<Train>) stream.readObject());
        DataController.tickets = FXCollections.observableArrayList((ArrayList<Ticket>) stream.readObject());
        DataController.routes = FXCollections.observableArrayList((ArrayList<TrainRoute>) stream.readObject());
        stream.close();

        System.out.println("Stations size:");
        System.out.println(DataController.stations.size());
        System.out.println("Trains size:");
        System.out.println(DataController.trains.size());
        System.out.println("Tickets size:");
        System.out.println(DataController.tickets.size());
        System.out.println("Routes size:");
        System.out.println(DataController.routes.size());
    }

    public static void writeToCSV(Class c, String fileName) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        BufferedWriter stream = new BufferedWriter(
                new OutputStreamWriter(
//                        new FileOutputStream(obj.getClass().getSimpleName() + ".csv")
                        new FileOutputStream(fileName + ".csv")
                )
        );

        String header = (String) c.getMethod("getSCVHeader").invoke(null);
        String[] lines = (String[]) c.getMethod("getCSVData").invoke(null);

        stream.write(header);
        for (String line : lines) {
            stream.newLine();
            stream.write(line);
        }

        stream.close();
    }

    public static void readFromCSV(Class c, String fileName) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        BufferedReader stream = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(fileName + ".csv")
                )
        );
        //consume header
        stream.readLine();

        String line = stream.readLine();
        while (line != null && !line.isEmpty()) {
            String[] lineSplit = line.split(",");

            c.getMethod("setFromCSVData", String[].class).invoke(null, (Object) lineSplit);

            line = stream.readLine();
        }
        stream.close();
    }
}
