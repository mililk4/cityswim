package com.scissor.cityswim.app;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LocationCrazinessIsolator {
    public static final int MAX_AGE_MS = 10 * 60 * 1000;
    public static final int MAX_ERROR_METERS = 250;
    private final LocationManager locationManager;
    private Location lastUsefulLocation;

    public LocationCrazinessIsolator(Context context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        final Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        locationManager.requestLocationUpdates(30 * 1000, 100, criteria, new MyListener(), Looper.myLooper());
    }

    public @Nullable Location usefulRecentLocation() {
        Location best = bestRecentLocation();
        if ((best != null) && (best.getAccuracy() < MAX_ERROR_METERS)) {
            lastUsefulLocation = best;
            return best;
        }
        if (lastUsefulLocation != null) return lastUsefulLocation;
        return null;
    }

    public @Nullable Location bestRecentLocation() {
        List<String> matchingProviders = locationManager.getAllProviders();
        List<Location> recentLocations = new ArrayList<Location>();
        for (String provider : matchingProviders) {
            final Location location = locationManager.getLastKnownLocation(provider);
            if (location != null && location.hasAccuracy() && location.getTime() > System.currentTimeMillis() - MAX_AGE_MS) {
                recentLocations.add(location);
                Log.i("location", "found location " + location);
            }
        }
        Collections.sort(recentLocations, new BestLocationAccuracy());
        if (recentLocations.size() > 0) {
            return recentLocations.get(0);
        } else {
            return null;
        }
    }


    private static class BestLocationAccuracy implements Comparator<Location> {
        @Override
        public int compare(Location a, Location b) {
            return (int) (100 * (a.getAccuracy() - b.getAccuracy()));
        }
    }

    /**
     * Doesn't need to do anything because we don't need to respond actively to updates.
     * The UI refreshes every 10 seconds and uses the best available location; that's plenty.
     */
    private class MyListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }
}
