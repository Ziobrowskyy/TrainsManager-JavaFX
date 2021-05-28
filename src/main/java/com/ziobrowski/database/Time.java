package com.ziobrowski.database;

import com.ziobrowski.Pair;
import com.ziobrowski.Utils;

import java.io.Serializable;

public class Time implements Serializable {
    int hour;
    int minute;

    public Time() {
    }

    public Time(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public Time(String timeString) throws IllegalArgumentException {
        Pair<Integer, Integer> timePair = parseTimeString(timeString);
        this.hour = timePair.getFirst();
        this.minute = timePair.getSecond();

    }

    //String header = (String) obj.getClass().getMethod("getSCVHeader").invoke(obj);
    //        String[] lines = (List<String>) obj.getClass().getMethod("getCSVData").invoke(obj);
    public String getSCVHeader() {
        return "hour,minute";
    }

    public String[] getCSVData() {
        return new String[]{hour + "," + minute};
    }

    public void setFromCSVData(String[] lineSplit) {
        hour = Integer.parseInt(lineSplit[0]);
        minute = Integer.parseInt(lineSplit[1]);
    }

    public int toMinutes() {
        return hour * 60 + minute;
    }

    public int compareTo(Time other) {
        int hourCmp = Integer.compare(hour, other.hour);
        if (hourCmp != 0)
            return hourCmp;
        return Integer.compare(minute, other.minute);
    }

    @Override
    public String toString() {
        return padLeft(hour, 2, '0') + ':' + padLeft(minute, 2, '0');
    }

    String padLeft(int value, int n, char c) {
        String iString = String.valueOf(value);
        return String.valueOf(c).repeat(Math.max(0, n - iString.length())) +
                iString;

    }

    static Pair<Integer, Integer> parseTimeString(String s) throws IllegalArgumentException {
        String[] splitted = s.split(":");
        if (splitted.length != 2)
            throw new IllegalArgumentException("Given string is not a valid time string! Invalid string composition - probably missing colon char ':'.");

        try {
            int hour = Integer.parseInt(splitted[0]);
            int minute = Integer.parseInt(splitted[1]);

            if (minute < 0 || minute >= 60 || hour < 0 || hour >= 24)
                throw new IllegalArgumentException("Given string is not a valid time string! Invalid range of given minute and/or hour value." + s);
            return new Pair<>(hour, minute);

        } catch (NumberFormatException ignored) {
            throw new IllegalArgumentException("Given string is not a valid time string! Error caught while parsing string to Integer.");
        }
    }

    public Time add(Time other) {
        int hourSum = hour + other.hour;
        if (minute + other.minute >= 60)
            hourSum++;
        return new Time(hourSum % 24, (minute + other.minute) % 60);
    }

    public Time sub(Time other) {
        int newHour = hour + 24 - other.hour;
        if (other.minute > minute)
            newHour--;
        int newMinute = (minute + 60 - other.minute) % 60;
        return new Time(newHour % 24, newMinute);
    }

    public static Time getRandom() {
        return new Time(Utils.getInt(24), Utils.getInt(60));
    }

    public static Time getRandom(int minHour, int maxHour, int minMinute, int maxMinute) {
        return new Time(Utils.getInt(minHour, maxHour), Utils.getInt(minMinute, maxMinute));
    }

    public static Time getRandomMinutes(int min, int max) {
        int r = Utils.getInt(min, max);
        return new Time(r / 60, r % 60);
    }
}
