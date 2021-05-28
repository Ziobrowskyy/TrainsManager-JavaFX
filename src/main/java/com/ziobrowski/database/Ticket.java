package com.ziobrowski.database;

import com.ziobrowski.controllers.DataController;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Locale;

public class Ticket implements Serializable {
    transient SimpleStringProperty firstName;
    transient SimpleStringProperty lastName;
    TicketDiscount ticketDiscount;
    TicketClass ticketClass;
    transient SimpleFloatProperty price;
    Train train;
    public Ticket() {

    }
    public Ticket(String firstName, String lastName, TicketDiscount ticketDiscount, TicketClass ticketClass, float price, Train train) {
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.ticketDiscount = ticketDiscount;
        this.ticketClass = ticketClass;
        this.price = new SimpleFloatProperty(price);
        this.train = train;
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeObject(firstName.getValueSafe());
        stream.writeObject(lastName.getValueSafe());
        stream.writeFloat(price.getValue());
    }

    private void readObject(ObjectInputStream stream) throws ClassNotFoundException, IOException {
        stream.defaultReadObject();
        firstName = new SimpleStringProperty((String) stream.readObject());
        lastName = new SimpleStringProperty((String) stream.readObject());
        price = new SimpleFloatProperty(stream.readFloat());
    }

    public static String getSCVHeader() {
        return "firstName,lastName,discount,class,price,trainName";
    }

    public static String[] getCSVData() {
        ObservableList<Ticket> tickets = DataController.getTickets();
        String[] returnArray = new String[tickets.size()];
        for (int i = 0; i < tickets.size(); i++) {
            Ticket t = tickets.get(i);
            returnArray[i] = String.format(Locale.US,
                    "%s,%s,%s,%s,%.4f,%s",
                    t.firstName.getValueSafe(),
                    t.lastName.getValue(),
                    t.ticketDiscount.name(),
                    t.ticketClass.name(),
                    t.price.getValue(),
                    t.train.name.getValueSafe()
            );
        }
        return returnArray;
    }

    public void setFromCSVData(String[] lineSplit) {
        firstName = new SimpleStringProperty(lineSplit[0]);
        lastName = new SimpleStringProperty(lineSplit[1]);
        ticketDiscount = TicketDiscount.valueOf(lineSplit[2]);
        ticketClass = TicketClass.valueOf(lineSplit[3]);
        price = new SimpleFloatProperty(Float.parseFloat(lineSplit[4]));
        train = DataController.getTrain(lineSplit[5]);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public SimpleStringProperty firstNameProperty() {
        return firstName;
    }

    public String getLastName() {
        return lastName.get();
    }

    public SimpleStringProperty lastNameProperty() {
        return lastName;
    }

    public TicketDiscount getTicketDiscount() {
        return ticketDiscount;
    }

    public TicketClass getTicketClass() {
        return ticketClass;
    }

    public float getPrice() {
        return price.get();
    }

    public SimpleFloatProperty priceProperty() {
        return price;
    }

    public Train getTrain() {
        return train;
    }
}
