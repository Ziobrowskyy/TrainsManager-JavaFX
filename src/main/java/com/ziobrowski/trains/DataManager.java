package com.ziobrowski.trains;

import com.ziobrowski.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DataManager {
    static ObservableList<Train> trains = FXCollections.observableArrayList();
    static ObservableList<TrainStation> stations = FXCollections.observableArrayList();

    static void init() {
        for(int i = 1; i < 10; i++) {
            trains.add(new Train("Train " + i, randomType(), randomState(), Utils.getRandInt(10,100)));
        }
        for(int i = 1; i < 4; i++) {
            stations.add(new TrainStation("Station " + i, Utils.getRandInt(5,20)));
        }
    }
    static private TrainState randomState() {
        TrainState[] values = TrainState.values();
        return values[Utils.getRandInt(values.length)];
    }
    static private TrainCargoType randomType() {
        TrainCargoType[] values = TrainCargoType.values();
        return values[Utils.getRandInt(values.length)];
    }
    static class IdManager {
        static int idsSize = 1024;
        static boolean[] idPool = new boolean[idsSize];
        static int takenIds = 0;

        static int getId() {
            if (takenIds >= 1024)
                return -1;
            int chosen;
            do {
                chosen = Utils.getRandInt(idsSize);
            } while (!idPool[chosen]);
            idPool[chosen] = true;
            takenIds++;
            return chosen;
        }

        static void freeId(int id) {
            idPool[id] = false;
            takenIds--;
        }
    }

}
