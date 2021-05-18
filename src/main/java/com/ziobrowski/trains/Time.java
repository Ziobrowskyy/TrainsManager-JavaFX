package com.ziobrowski.trains;

public class Time implements Comparable<Time> {
    final int hour;
    final int minute;

    public Time(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }
    public Time(String timeString) {
        //hh:mm
        String[] split = timeString.split(":");
        this.hour = Integer.parseInt(split[0]);
        this.minute = Integer.parseInt(split[1]);
    }

    @Override
    public String toString() {
        return "" + padLeft(hour) + ":" + padLeft(minute);
    }

    @Override
    public int compareTo(Time o) {
        if (this.hour == o.hour) {
            return Integer.compare(this.minute, o.minute);
        }
        return Integer.compare(this.hour, o.hour);
    }

    public boolean equals(Time o) {
        return this.hour == o.hour && this.minute == o.minute;
    }

    private String padLeft(int n) {
        return String.format("%02d", n);
    }
}