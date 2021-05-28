package com.ziobrowski.controllers;

import com.ziobrowski.database.Time;
import com.ziobrowski.database.Train;
import com.ziobrowski.database.TrainStation;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TrainsController implements Initializable {
    @FXML
    public ComboBox<TrainStation> originStationComboBox;
    @FXML
    public ComboBox<TrainStation> destinationStationComboBox;
    @FXML
    public ComboBox<Integer> hourComboBox;
    @FXML
    public ComboBox<Integer> minuteComboBox;

    @FXML
    public TextField nameTextField;
    @FXML
    Button clearFiltersButton;

    @FXML
    public TableView<Train> trainTableView;
    @FXML
    public TableColumn<Train, String> nameColumn;
    @FXML
    public TableColumn<Train, String> originColumn;
    @FXML
    public TableColumn<Train, String> destinationColumn;
    @FXML
    public TableColumn<Train, String> departureTimeColumn;
    @FXML
    public TableColumn<Train, String> travelTimeColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FilteredList<TrainStation> destinationStationFiltered = new FilteredList<>(DataController.stations);
        FilteredList<Train> trainsFiltered = new FilteredList<>(DataController.trains, Train::checkCapacity);
        SortedList<Train> trainsSortable = new SortedList<>(trainsFiltered);

        ObjectProperty<Predicate<Train>> originFilter = new SimpleObjectProperty<>();
        ObjectProperty<Predicate<Train>> destinationFilter = new SimpleObjectProperty<>();
        ObjectProperty<Predicate<Train>> nameFilter = new SimpleObjectProperty<>();
        ObjectProperty<Predicate<Train>> timeFilter = new SimpleObjectProperty<>();
        ObjectProperty<Predicate<Train>> capacityFilter = new SimpleObjectProperty<>();

        originFilter.bind(Bindings.createObjectBinding(() -> (train -> {
                    TrainStation value = originStationComboBox.getValue();
                    if (value == null)
                        return true;
                    return train.getFirstStation() == value;
                }),
                originStationComboBox.valueProperty())
        );

        destinationFilter.bind(Bindings.createObjectBinding(() -> (train -> {
                    TrainStation value = destinationStationComboBox.getValue();
                    if (value == null)
                        return true;
                    return train.getDestinationStation() == value;
                }),
                destinationStationComboBox.valueProperty())
        );

        nameFilter.bind(Bindings.createObjectBinding(() -> (train -> {
                    String value = nameTextField.getText();
                    if (value == null)
                        return true;
                    return train.getName().contains(value);
                }),
                nameTextField.textProperty())
        );

        timeFilter.bind(Bindings.createObjectBinding(() -> (train -> {
                    try {

                        int valueHours = hourComboBox.getValue();
                        int valueMinutes = minuteComboBox.getValue();
                        return train.getDepartureTime().compareTo(new Time(valueHours, valueMinutes)) >= 0;
                    } catch (NullPointerException ignored) {
                    }
                    return true;
                }),
                hourComboBox.valueProperty(), minuteComboBox.valueProperty())
        );

        capacityFilter.bind(Bindings.createObjectBinding(() -> ((Predicate<Train>) Train::checkCapacity),
                trainTableView.focusedProperty())
        );

        trainsFiltered.predicateProperty().bind(Bindings.createObjectBinding(
                () -> originFilter.get()
                        .and(destinationFilter.get())
                        .and(nameFilter.get())
                        .and(timeFilter.get())
                        .and(capacityFilter.get()),
                originFilter, destinationFilter, nameFilter, timeFilter, capacityFilter)
        );

        clearFiltersButton.setOnAction(e -> {
            originStationComboBox.setValue(null);
            destinationStationComboBox.setValue(null);
            nameTextField.setText(null);
            hourComboBox.setValue(null);
            minuteComboBox.setValue(null);
        });

        originStationComboBox.setItems(DataController.stations);
        originStationComboBox.setOnAction(e -> {
            TrainStation value = originStationComboBox.getValue();
            destinationStationFiltered.setPredicate(it -> it != value);
        });

        destinationStationComboBox.setItems(destinationStationFiltered);

        hourComboBox.setItems(FXCollections.observableArrayList(
                IntStream.range(0, 24).boxed().collect(Collectors.toList()))
        );

        hourComboBox.setOnAction(e -> {
            if (minuteComboBox.getValue() == null) {
                minuteComboBox.setValue(0);
            }
        });

        minuteComboBox.setItems(FXCollections.observableArrayList(
                IntStream.range(0, 60).boxed().filter(it -> it % 5 == 0).collect(Collectors.toList()))
        );

        trainsSortable.comparatorProperty().bind(trainTableView.comparatorProperty());
        trainTableView.setItems(trainsSortable);

        nameColumn.setCellValueFactory(it -> it.getValue().nameProperty());
        originColumn.setCellValueFactory(it -> it.getValue().getFirstStation().nameProperty());
        destinationColumn.setCellValueFactory(it -> it.getValue().getDestinationStation().nameProperty());
        departureTimeColumn.setCellValueFactory(it -> new ReadOnlyStringWrapper(it.getValue().getDepartureTime().toString()));
        travelTimeColumn.setCellValueFactory(it -> new ReadOnlyStringWrapper(it.getValue().getTravelTime().toString()));


        trainTableView.setRowFactory(tv -> {

            TableRow<Train> row = new TableRow<>() {
                private final Tooltip tooltip = new Tooltip();

                @Override
                public void updateItem(Train train, boolean empty) {
                    super.updateItem(train, empty);
                    if (train == null) {
                        setTooltip(null);
                    } else {
                        String tooltipText = "Name: " + train.nameProperty().get() + '\n' +
                                "Type: " + train.getType() + '\n' +
                                "State: " + train.getState() + '\n' +
                                "Cur cap: " + train.getCurrentCapacity() + '\n' +
                                "Max cap: " + train.getCapacity() + '\n' +
                                "-----------------\n" +
                                train.getRouteString();
                        tooltip.setText(tooltipText);
                        setTooltip(tooltip);
                    }
                }
            };
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Train rowData = row.getItem();

                    try {
                        TicketBuyController.selectedTrain = rowData;
                        Parent root = FXMLLoader.load(getClass().getResource("/BuyPromptLayout.fxml"));
                        Scene scene = new Scene(new StackPane(root));
                        Stage stage = new Stage();
                        stage.setScene(scene);
                        stage.show();
                        stage.setOnHidden(e -> trainTableView.refresh());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });
            return row;
        });
    }
}
