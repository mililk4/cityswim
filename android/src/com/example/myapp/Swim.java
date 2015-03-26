package com.example.myapp;

import java.util.Date;

public class Swim {
    private final String poolName;
    private final String dayLabel;
    private final String startLabel;
    private final String endLabel;
    private final Date start;
    private final Date end;

    public Swim(String pool_name, String day_label, String start_label, String end_label, long start, long end) {
        poolName = pool_name;
        dayLabel = day_label;
        startLabel = start_label;
        endLabel = end_label;
        this.start = new Date(start);
        this.end = new Date(end);
    }


    public String getPoolName() {
        return poolName;
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


}
