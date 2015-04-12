package com.example.myapp;

import android.location.Location;

public class Pool {

    private final String name;
    private final Location location;

    public Pool(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    public Pool(String name, double latitude, double longitude) {
        this(name, latLongToLocation(latitude, longitude));
    }

    private static Location latLongToLocation(double latitude, double longitude) {
        Location location = new Location("ignored, I hope");
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return location;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "Pool{" +
                "name='" + name + '\'' +
                ", location=" + location +
                '}';
    }
}
