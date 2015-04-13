package com.scissor.cityswim.app;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import java.util.*;

public class LocationCrazinessIsolator {
    public static final int MAX_AGE_MS = 5 * 60 * 1000;
    public static final int MAX_ERROR_METERS = 100;
    private final LocationManager locationManager;

    public LocationCrazinessIsolator(Context context) {
        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

    }

    public Location usefulRecentLocation() {
        Location best = bestRecentLocation();
        if ((best != null)  && (best.getAccuracy() < MAX_ERROR_METERS)) {
            return best;
        } else {
            return null;
        }
    }

    public Location bestRecentLocation() {
        List<String> matchingProviders = locationManager.getAllProviders();
        List<Location> recentLocations = new ArrayList<Location>();
        for (String provider: matchingProviders) {
            final Location location = locationManager.getLastKnownLocation(provider);
            if(location != null && location.hasAccuracy() && location.getTime() > System.currentTimeMillis() - MAX_AGE_MS) {
                recentLocations.add(location);
            }
        }
        Collections.sort(recentLocations, new BestLocationAccuracy());
        if (recentLocations.size()>0) {
            return recentLocations.get(0);
        } else {
            return null;
        }
    }


    private static class BestLocationAccuracy implements Comparator<Location> {
        @Override
        public int compare(Location a, Location b) {
            return (int) ( 100 * (a.getAccuracy() - b.getAccuracy()));
        }
    }
}
