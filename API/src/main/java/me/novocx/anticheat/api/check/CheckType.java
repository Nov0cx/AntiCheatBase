package me.novocx.anticheat.api.check;

public enum CheckType {
    COMBAT, PLAYER, MOVEMENT, MOTION, KILLAURA, BADPACKET, HAND, AUTOCLICKER, ALL, INVALID_MOVEMENT;

    public String getName() {
        return name().toLowerCase();
    }
}
