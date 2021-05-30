package com.ziobrowski.controllers;

import com.ziobrowski.models.Ticket;
import com.ziobrowski.models.TicketClass;
import com.ziobrowski.models.TicketDiscount;
import com.ziobrowski.models.Train;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class TicketBuyController implements Initializable {

    @FXML
    public GridPane gridPane;
    @FXML
    public TextField firstNameField;
    @FXML
    public TextField lastNameField;
    @FXML
    public ComboBox<TicketDiscount> discountComboBox;
    @FXML
    public ComboBox<TicketClass> ticketClassComboBox;
    @FXML
    public Label totalPriceLabel;
    @FXML
    public Button buyButton;

    static Train selectedTrain = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (selectedTrain == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Selected train is null!");
            alert.showAndWait();
        }

        discountComboBox.getItems().addAll(TicketDiscount.values());
        discountComboBox.setValue(TicketDiscount.NORMAL);

        ticketClassComboBox.getItems().addAll(TicketClass.values());
        ticketClassComboBox.setValue(TicketClass.THIRD);

        discountComboBox.setOnAction(e -> setTicketPrice());
        ticketClassComboBox.setOnAction(e -> setTicketPrice());

        setTicketPrice();

        buyButton.setOnAction(e -> {
            if (selectedTrain.makeReservation())
                DataController.tickets.add(new Ticket(
                        firstNameField.getText(),
                        lastNameField.getText(),
                        discountComboBox.getValue(),
                        ticketClassComboBox.getValue(),
                        getTicketPrice(),
                        selectedTrain)
                );
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Could not make reservation :^(");
                alert.showAndWait();
            }
            ((Stage) gridPane.getScene().getWindow()).close();
        });
    }

    void setTicketPrice() {
        totalPriceLabel.setText(String.format("%.2f", getTicketPrice()));
    }

    float getTicketPrice() {
        return selectedTrain.getPrice() * ticketClassComboBox.getValue().priceMultiplier * discountComboBox.getValue().discount;
    }
}
