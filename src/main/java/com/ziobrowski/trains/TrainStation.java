package com.ziobrowski.trains;

import java.util.ArrayList;
import java.util.List;

public class TrainStation {
    String name;
    int capacity;
    final int id = DataManager.IdManager.getId();
    List<Pair<Train, Time>> trains = new ArrayList<>();

    public TrainStation(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return name + "@" + id;
    }
}
