package com.ziobrowski.trains;

public class Train {
    String name;
    TrainCargoType type;
    TrainState state;
    int capacity;
    final int id = DataManager.IdManager.getId();

    Train(String name, TrainCargoType type, TrainState state, int capacity) {
        this.name = name;
        this.type = type;
        this.state = state;
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return name + "@" + id;
    }
}
