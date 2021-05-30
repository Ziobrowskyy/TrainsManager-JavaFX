package com.ziobrowski.models;

import com.ziobrowski.Pair;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TrainRoute implements Serializable {
    transient public ObservableList<Pair<TrainStation, Time>> stations = FXCollections.observableArrayList();

    public TrainRoute() {

    }

    public TrainRoute(List<TrainStation> stations) {
        stations.forEach(this::addStation);
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
