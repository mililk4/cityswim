package com.scissor.cityswim.app;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

class SwimTableController {

    private final TableLayout layout;
    private final Context context;
    private final SwimDataFragment data;
    private final LocationCrazinessIsolator locationProvider;

    public SwimTableController(Context context, TableLayout layout,
                               SwimDataFragment data, LocationCrazinessIsolator location) {
        this.context = context;
        this.layout = layout;
        this.data = data;
        this.locationProvider = location;
    }

    public void updateContents() {
        if (!data.hasSwims()) return;
        Location location = locationProvider.usefulRecentLocation();
        layout.removeAllViews();
        for (Swim swim : data.getSwims()) {
            if (!swim.isOver()) {
                TableRow row = new TableRow(context);
                row.addView(newColumn(swim.getPoolShortName(), swim.isRunning()));
                row.addView(newColumn(swim.getDayLabel(), swim.isRunning()));
                row.addView(newColumn(swim.getStartLabel(), swim.isRunning()));
                row.addView(newColumn(swim.getEndLabel(), swim.isRunning()));
                if (location != null) {
                    row.addView(newColumn(distanceAsString(location, swim.getLocation()), swim.isRunning()));
                }
                layout.addView(row);
            }
        }
    }

    private String distanceAsString(Location us, Location pool) {
        float miles = us.distanceTo(pool) / 1609.344f;
        return String.format("%1.1f mi", miles);
    }

    private TextView newColumn(String poolName, boolean bold) {
        final TextView column = new TextView(context);
        column.setText(poolName);
        column.setTextColor(Color.BLACK);
        column.setTextSize(18);
        column.setPadding(5, 5, 5, 5);
        if (bold) {
            column.setTypeface(null, Typeface.BOLD);
        }
        return column;
    }
}
