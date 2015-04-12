package com.example.myapp;

import android.location.Location;

import java.util.Date;

public class Swim {
    private final Pool pool;
    private final String dayLabel;
    private final String startLabel;
    private final String endLabel;
    private final Date start;
    private final Date end;

    public Swim(Pool pool, String dayLabel, String startLabel, String endLabel, long start, long end) {
        this.pool = pool;
        this.dayLabel = dayLabel;
        this.startLabel = startLabel;
        this.endLabel = endLabel;
        this.start = new Date(start);
        this.end = new Date(end);
    }

    public Pool getPool() {
        return pool;
    }

    public String getPoolName() {
        return pool.getName();
    }
//    pool.name.replaceAll("\\s*Pool$", "").replaceAll("Mission Community","Mission").replaceAll("Martin Luther King Jr", "MLK")

    public String getPoolShortName() {
        return pool.getName().replaceAll("\\s*Pool$", "").replaceAll("\\s*Community","").replaceAll("Martin Luther King Jr", "MLK");
    }

    public String getDayLabel() {
        return dayLabel;
    }

    public String getStartLabel() {
        return startLabel;
    }

    public String getEndLabel() {
        return endLabel;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public boolean isRunning() {
        return now().after(start) && now().before(end);
    }

    public boolean isOver() {
        return end.before(now());
    }

    private Date now() {
        return new Date();
    }

    public Location getLocation() {
        return pool.getLocation();
    }

}
