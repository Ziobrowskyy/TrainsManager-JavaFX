package com.ziobrowski.controllers;

import com.ziobrowski.*;
import com.ziobrowski.database.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Comparator;
import java.util.stream.Collectors;

public class DataController {
    static transient ObservableList<TrainStation> stations = FXCollections.observableArrayList();
    static transient ObservableList<Train> trains = FXCollections.observableArrayList();
    static transient ObservableList<Ticket> tickets = FXCollections.observableArrayList();
    static transient ObservableList<TrainRoute> routes = FXCollections.observableArrayList();

    DataController() {
//        if (stations.isEmpty() || trains.isEmpty())
//            init();
    }


    public static void init() {
        stations.add(new TrainStation("station 1", 10));
        stations.add(new TrainStation("station 2", 10));
        stations.add(new TrainStation("station 3", 10));
        stations.add(new TrainStation("station 4", 10));
        stations.add(new TrainStation("station 5", 10));
        stations.add(new TrainStation("station 6", 10));
        stations.add(new TrainStation("station 7", 10));
        stations.add(new TrainStation("station 8", 10));

        routes.add(makeRandomRoute());
        routes.add(makeRandomRoute());
        routes.add(makeRandomRoute());
        routes.add(makeRandomRoute());
        routes.add(makeRandomRoute());

        trains.add(new Train("train 1", TrainType.CARGO, TrainState.NEW, Utils.getInt(1, 5), getRandomRoute()));
        trains.add(new Train("train 2", TrainType.CARGO, TrainState.NEW, Utils.getInt(1, 5), getRandomRoute()));
        trains.add(new Train("train 3", TrainType.CARGO, TrainState.NEW, Utils.getInt(1, 5), getRandomRoute()));
        trains.add(new Train("train 4", TrainType.CARGO, TrainState.NEW, Utils.getInt(1, 5), getRandomRoute()));
        trains.add(new Train("train 5", TrainType.CARGO, TrainState.NEW, Utils.getInt(1, 5), getRandomRoute()));
    }

    private static TrainRoute getRandomRoute() {
        if (routes.size() == 0)
            return null;
        return routes.get(Utils.getInt(routes.size()));
    }

    private static TrainRoute makeRandomRoute() {
        assert (stations.size() >= 2);
        return new TrainRoute(
                stations.stream()
                        .sorted(Comparator.comparingInt(ts -> Utils.getInt()))
                        .limit(Utils.getInt(2, stations.size()))
                        .collect(Collectors.toList())
        );
    }

    public static ObservableList<TrainStation> getStations() {
        return stations;
    }

    public static ObservableList<Train> getTrains() {
        return trains;
    }

    public static ObservableList<Ticket> getTickets() {
        return tickets;
    }

    public static ObservableList<TrainRoute> getRoutes() {
        return routes;
    }

    public static TrainRoute getRoute(Pair<TrainStation, Time> firstStop, Pair<TrainStation, Time> lastStop) {
        for (TrainRoute t : routes) {
            if (t.getFirst() == firstStop && t.getLast() == lastStop)
                return t;
        }
        return null;
    }

    public void addRoute(TrainRoute route) {
        routes.add(route);
    }

    public void removeRoute(TrainRoute route) {
        routes.remove(route);
    }

    public static TrainStation getStation(String name) {
        for (TrainStation t : stations) {
            if (t.getName().equals(name))
                return t;
        }
        return null;
    }

    public void addStation(TrainStation station) {
        stations.add(station);
    }

    public void removeStation(TrainStation station) {
        stations.remove(station);
    }

    public static Train getTrain(String name) {
        for (Train t : trains) {
            if (t.getName().equals(name))
                return t;
        }
        return null;
    }

    public void addTrain(Train train) {
        trains.add(train);
    }

    public void removeTrain(Train train) {
        trains.remove(train);
    }
}
