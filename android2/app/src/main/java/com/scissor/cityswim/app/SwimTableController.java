package com.scissor.cityswim.app;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

class SwimTableController {

    private final TableLayout layout;
    private final Context context;
    private final SwimDataFragment data;

    public SwimTableController(Context context, TableLayout layout, SwimDataFragment data) {
        this.context = context;
        this.layout = layout;
        this.data = data;
    }

    public void updateContents() {
        if (!data.hasSwims()) return;
        layout.removeAllViews();
        for (Swim swim : data.getSwims()) {
            if (!swim.isOver()) {
                TableRow row = new TableRow(context);
                row.addView(newColumn(swim.getPoolShortName(), swim.isRunning()));
                row.addView(newColumn(swim.getDayLabel(), swim.isRunning()));
                row.addView(newColumn(swim.getStartLabel(), swim.isRunning()));
                row.addView(newColumn(swim.getEndLabel(), swim.isRunning()));
//                row.addView(newColumn(swim.getLocation().toString(), swim.isRunning()));
                layout.addView(row);
            }
        }
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
