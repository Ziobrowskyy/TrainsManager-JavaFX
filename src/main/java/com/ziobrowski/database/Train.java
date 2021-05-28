package com.ziobrowski.database;

import com.ziobrowski.DoNotExport;
import com.ziobrowski.Pair;
import com.ziobrowski.Utils;
import com.ziobrowski.controllers.DataController;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Locale;
import java.util.stream.IntStream;


public class Train implements Serializable {
    transient SimpleStringProperty name;

    TrainType type;
    TrainState state;
    transient SimpleIntegerProperty currentCapacity;
    transient SimpleIntegerProperty capacity;
    TrainRoute route;

    transient SimpleIntegerProperty baseTicketPrice;

    public Train() {
    }

    public Train(String name, TrainType type, TrainState state, int capacity, TrainRoute route) {
        this.name = new SimpleStringProperty(name);
        this.type = type;
        this.state = state;
        this.currentCapacity = new SimpleIntegerProperty(0);
        this.capacity = new SimpleIntegerProperty(capacity);
        this.route = route;
        this.baseTicketPrice = new SimpleIntegerProperty(calculateTicketPrice());
    }

    private void writeObject(ObjectOutputStream stream) throws IOException, NoSuchFieldException {
        stream.defaultWriteObject();
        stream.writeObject(state.name());
        stream.writeObject(name.getValueSafe());
        stream.writeInt(currentCapacity.getValue());
        stream.writeInt(capacity.getValue());
        stream.writeInt(baseTicketPrice.getValue());
    }

    private void readObject(ObjectInputStream stream) throws ClassNotFoundException, IOException, NoSuchFieldException {
        stream.defaultReadObject();
        state = TrainState.valueOf((String) stream.readObject());
        name = new SimpleStringProperty((String) stream.readObject());
        currentCapacity = new SimpleIntegerProperty(stream.readInt());
        capacity = new SimpleIntegerProperty(stream.readInt());
        baseTicketPrice = new SimpleIntegerProperty(stream.readInt());
    }

    public static String getSCVHeader() {
        return "name,type,state,currentCapacity,maxCapacity,firstStation,departureTime,baseTicketPrice";
    }

    public static String[] getCSVData() {
        ObservableList<Train> trains = DataController.getTrains();
        String[] returnArray = new String[trains.size()];
        for (int i = 0; i < trains.size(); i++) {
            Train t = trains.get(i);
            returnArray[i] = String.format(Locale.US,
                    "%s,%s,%s,%d,%d,%s,%s,%d",
                    t.name.getValueSafe(),
                    t.type.name(),
                    t.state.name(),
                    t.getCurrentCapacity(),
                    t.capacity.getValue(),
                    t.route.getFirst().getFirst().name.getValueSafe(),
                    t.route.getFirst().getSecond(),
                    t.baseTicketPrice.getValue()
            );
        }
        return returnArray;
    }

    public void setFromCSVData(String[] lineSplit) {
        name = new SimpleStringProperty(lineSplit[0]);
        type = TrainType.valueOf(lineSplit[1]);
        state = TrainState.valueOf(lineSplit[2]);
        currentCapacity = new SimpleIntegerProperty(Integer.parseInt(lineSplit[3]));
        capacity = new SimpleIntegerProperty(Integer.parseInt(lineSplit[4]));
        route = new TrainRoute();
        route.add(DataController.getStation(lineSplit[5]), new Time(lineSplit[6]));
        baseTicketPrice = new SimpleIntegerProperty(Integer.parseInt(lineSplit[7]));
    }

    public boolean checkCapacity() {
        return getCurrentCapacity() < getCapacity();
    }

    public boolean removeReservation() {
        if (getCurrentCapacity() <= 0)
            return false;
        currentCapacity.set(getCurrentCapacity() - 1);
        return true;
    }

    public boolean makeReservation() {
        if (getCurrentCapacity() + 1 > getCapacity())
            return false;
        currentCapacity.set(getCurrentCapacity() + 1);
        return true;
    }

    int calculateTicketPrice() {
        return (int) (getTravelTime().toMinutes() * Utils.getDouble(1, 3));
    }

    public int getPrice() {
        return baseTicketPrice.get();
    }

    public Time getTravelTime() {
        assert (route.stations.size() >= 2);
        Time firstStationTime = route.stations.get(0).getSecond();
        Time lastStationTime = route.stations.get(route.stations.size() - 1).getSecond();
        return lastStationTime.sub(firstStationTime);
    }

    public Time getDepartureTime() {
        assert (route.stations.size() >= 1);
        return route.stations.get(0).getSecond();
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public TrainType getType() {
        return type;
    }

    public TrainState getState() {
        return state;
    }

    public int getCurrentCapacity() {
        return currentCapacity.get();
    }

    public int getCapacity() {
        return capacity.get();
    }

    public SimpleIntegerProperty capacityProperty() {
        return capacity;
    }

    public TrainRoute getRoute() {
        return route;
    }

    public String getRouteString() {
        StringBuilder builder = new StringBuilder();
        IntStream.range(0, route.stations.size()).forEachOrdered(it -> {
                    Pair<TrainStation, Time> value = route.stations.get(it);
                    builder.append("Stop ").append(it).append(": ");
                    builder.append(value.getFirst().nameProperty().get());
                    builder.append(" at ");
                    builder.append(value.getSecond());
                    builder.append('\n');
                }
        );
        return builder.toString();
    }

    public TrainStation getFirstStation() {
        return route.stations.get(0).getFirst();
    }

    public TrainStation getDestinationStation() {
        return route.stations.get(route.stations.size() - 1).getFirst();
    }
}
