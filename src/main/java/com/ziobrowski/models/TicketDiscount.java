package com.ziobrowski.models;

import java.util.Locale;

public enum TicketDiscount {
    NORMAL("Normal", 1.0f),
    STUDENT("Student", .51f),
    DISABLED("Disabled", .25f),
    FAMILY_FRIENDS("Family & friends", .75f),
    SENIOR("Senior", .49f),
    VETERAN("Veteran", .35f);

    public final String name;
    public final float discount;

    TicketDiscount(String name, float discount) {
        this.name = name;
        this.discount = discount;
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "%s (%d%%)", name, (int) (discount * 100));
    }
}
