package com.scissor.cityswim.app;

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

    public String getPoolShortName() {
        return shorten(pool.getName());
    }

    private String shorten(String longName) {
        return longName.replaceAll("\\s*Pool$", "").replaceAll("\\s*Community","").replaceAll("Martin Luther King Jr", "MLK").replaceAll("North", "N");
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
