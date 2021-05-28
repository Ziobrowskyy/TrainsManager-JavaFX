package com.ziobrowski.database;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class TrainStation implements Serializable {
   transient SimpleStringProperty name;
   transient SimpleIntegerProperty maxCapacity;
   transient SimpleIntegerProperty actualCapacity;

   public TrainStation(String name, int capacity) {
      this.name = new SimpleStringProperty(name);
      this.maxCapacity = new SimpleIntegerProperty(capacity);
      this.actualCapacity = new SimpleIntegerProperty(0);
   }

   private void writeObject(ObjectOutputStream stream) throws IOException {
      stream.defaultWriteObject();
      stream.writeObject(name.getValueSafe());
      stream.writeObject(maxCapacity.getValue());
      stream.writeObject(actualCapacity.getValue());
   }

   private void readObject(ObjectInputStream stream) throws ClassNotFoundException, IOException {
      stream.defaultReadObject();
      name = new SimpleStringProperty((String) stream.readObject());
      maxCapacity = new SimpleIntegerProperty((int) stream.readObject());
      actualCapacity = new SimpleIntegerProperty((int) stream.readObject());
   }

   @Override
   public String toString() {
      return getName();
   }

   public String getName() {
      return name.get();
   }

   public SimpleStringProperty nameProperty() {
      return name;
   }

   public int getMaxCapacity() {
      return maxCapacity.get();
   }

   public SimpleIntegerProperty maxCapacityProperty() {
      return maxCapacity;
   }

   public int getActualCapacity() {
      return actualCapacity.get();
   }

   public SimpleIntegerProperty actualCapacityProperty() {
      return actualCapacity;
   }
}
