package com.ziobrowski;

import com.ziobrowski.controllers.DataController;
import com.ziobrowski.controllers.ExportController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Import data");
            alert.setHeaderText(null);
            alert.setContentText("Do you want to import data from file?");
            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK) {
                try {
                    ExportController.deserialize();
                } catch (Exception ignored) {
                    ignored.printStackTrace();
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Import data");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Could not import data from file");
                    errorAlert.showAndWait();
                    DataController.init();
                }
            } else {
                DataController.init();
            }
        }
        Parent root = FXMLLoader.load(getClass().getResource("/MainLayout.fxml"));
        Scene scene = new Scene(new StackPane(root));
        stage.setScene(scene);
        stage.show();

        stage.setOnHidden(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Export data");
            alert.setHeaderText(null);
            alert.setContentText("Do you want to save data to file?");
            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK) {
                try {
                    ExportController.serialize();
                } catch (IOException ignored) {
                    ignored.printStackTrace();
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Import data");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Could not export data to file");
                    errorAlert.showAndWait();
                }
            }
        });
    }

    public static void main(String[] args) {
        launch();
    }

}