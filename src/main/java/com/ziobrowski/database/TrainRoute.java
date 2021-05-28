package com.ziobrowski.database;

import com.ziobrowski.Pair;
import com.ziobrowski.controllers.DataController;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TrainRoute implements Serializable {
    transient public ObservableList<Pair<TrainStation, Time>> stations = FXCollections.observableArrayList();

    public TrainRoute() {

    }

    public TrainRoute(List<TrainStation> stations) {
        stations.forEach(this::addStation);
    }

    public String getSCVHeader() {
        return "routeId,station,time";
    }

    public static String[] getCSVData() {
        ObservableList<TrainRoute> routes = DataController.getRoutes();
        int size = 0;
        for (TrainRoute t : routes)
            size += t.stations.size();

        String[] returnArray = new String[size];

        for (int i = 0, j = 0; i < routes.size() && j < size; i++) {
            TrainRoute t = routes.get(i);
            for (Pair<TrainStation, Time> p : t.stations) {
                returnArray[j++] = String.format(Locale.US,
                        "%d,%s,%s",
                        i,
                        p.getFirst().name.getValueSafe(),
                        p.getSecond()
                );
            }
        }
        return returnArray;
    }
    public static void setFromCSVData(String[] lineSplit) {
        firstName = new SimpleStringProperty(lineSplit[0]);
        lastName = new SimpleStringProperty(lineSplit[1]);
        ticketDiscount = TicketDiscount.valueOf(lineSplit[2]);
        ticketClass = TicketClass.valueOf(lineSplit[3]);
        price = new SimpleFloatProperty(Float.parseFloat(lineSplit[4]));
        train = DataController.getTrain(lineSplit[5]);
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeObject(new ArrayList<>(stations));
    }

    private void readObject(ObjectInputStream stream) throws ClassNotFoundException, IOException {
        stream.defaultReadObject();
        stations = FXCollections.observableArrayList((ArrayList<Pair<TrainStation, Time>>) stream.readObject());
    }

    public Pair<TrainStation, Time> getFirst() {
        assert (stations.size() >= 1);
        return stations.get(0);
    }

    public Pair<TrainStation, Time> getLast() {
        assert (stations.size() >= 1);
        return stations.get(stations.size() - 1);
    }

    public void add(TrainStation station, Time time) {
        stations.add(new Pair<>(station, time));
    }

    public void addStation(TrainStation station) {
        Time time;
        if (!stations.isEmpty()) {
            Time lastTime = stations.get(stations.size() - 1).getSecond();
            time = lastTime.add(Time.getRandomMinutes(5, 30));
        } else {
            time = Time.getRandom();
        }
        add(station, time);
    }
}
