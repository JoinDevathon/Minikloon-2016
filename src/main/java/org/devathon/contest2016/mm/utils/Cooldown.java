package org.devathon.contest2016.mm.utils;

public class Cooldown {
    private long lastTriggerMs = 0;
    private final long cooldownMs;

    public Cooldown(long cooldownMs) {
        this.cooldownMs = cooldownMs;
    }

    public boolean isReady() {
        return (System.currentTimeMillis() - lastTriggerMs) >= cooldownMs;
    }

    public void use() {
        lastTriggerMs = System.currentTimeMillis();
    }
}
