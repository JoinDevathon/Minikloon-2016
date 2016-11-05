package org.devathon.contest2016.mm;

public enum NotePitch {
    L_SOL(0.53333333333333),
    L_LA(0.6),
    L_SI(0.666666666666),
    M_DO(0.7),
    M_RE(0.8),
    M_MI(0.9),
    M_FA(0.95),
    M_SOL(1.05),
    M_LA(1.2),
    M_SI(1.3333333333333),
    H_DO(1.4),
    H_RE(1.6),
    H_MI(1.8),
    H_FA(1.9),
    ;

    private final float pitch;

    NotePitch(double pitch) {
        this.pitch = (float) pitch;
    }

    public float getPitch() {
        return pitch;
    }
}
