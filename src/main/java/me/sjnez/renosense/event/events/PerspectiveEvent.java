package me.sjnez.renosense.event.events;

import me.sjnez.renosense.event.EventStage;

public class PerspectiveEvent extends EventStage {
    private float aspect;

    public PerspectiveEvent(float aspect) {
        this.aspect = aspect;
    }

    public float getAspect() {
        return aspect;
    }

    public void setAspect(float aspect) {
        this.aspect = aspect;
    }
}