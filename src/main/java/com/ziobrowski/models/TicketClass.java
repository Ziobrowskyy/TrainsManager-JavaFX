package com.ziobrowski.models;

public enum TicketClass {
    FIRST("1st class", 2.0f),
    SECOND("2nd class", 1.33f),
    THIRD("3rd class", 1.f),
    CORRIDOR("4rd class (on corridor)", .5f);

    public final String name;
    public final float priceMultiplier;

    TicketClass(String name, float priceMultiplier) {
        this.name = name;
        this.priceMultiplier = priceMultiplier;
    }

    @Override
    public String toString() {
        return name;
    }
}
