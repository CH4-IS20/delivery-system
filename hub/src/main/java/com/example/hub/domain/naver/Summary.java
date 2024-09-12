package com.example.hub.domain.naver;

public class Summary {
    private Location start;
    private Location goal;
    private int distance;
    private long duration;

    // Getters and Setters

    public Location getStart() {
        return start;
    }

    public void setStart(Location start) {
        this.start = start;
    }

    public Location getGoal() {
        return goal;
    }

    public void setGoal(Location goal) {
        this.goal = goal;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
