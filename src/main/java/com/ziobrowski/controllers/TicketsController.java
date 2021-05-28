package com.ziobrowski.controllers;

import com.ziobrowski.database.Ticket;
import com.ziobrowski.database.TicketClass;
import com.ziobrowski.database.TicketDiscount;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class TicketsController implements Initializable {
    @FXML
    TableView<Ticket> ticketTableView;
    @FXML
    TableColumn<Ticket, String> trainNameColumn;
    @FXML
    TableColumn<Ticket, String> firstNameColumn;
    @FXML
    TableColumn<Ticket, String> lastNameColumn;
    @FXML
    TableColumn<Ticket, TicketDiscount> discountColumn;
    @FXML
    TableColumn<Ticket, TicketClass> classColumn;
    @FXML
    TableColumn<Ticket, Number> priceColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ticketTableView.setItems(DataController.tickets);

        trainNameColumn.setCellValueFactory(it -> it.getValue().getTrain().nameProperty());
        firstNameColumn.setCellValueFactory(it -> it.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(it -> it.getValue().lastNameProperty());
        discountColumn.setCellValueFactory(it -> new ReadOnlyObjectWrapper<>(it.getValue().getTicketDiscount()));
        classColumn.setCellValueFactory(it -> new ReadOnlyObjectWrapper<>(it.getValue().getTicketClass()));
        priceColumn.setCellValueFactory(it -> it.getValue().priceProperty());

        ticketTableView.setRowFactory(tv -> {

            TableRow<Ticket> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Ticket rowData = row.getItem();
                    DataController.tickets.remove(rowData);
                    rowData.getTrain().removeReservation();
                }
            });
            return row;
        });
    }
}
