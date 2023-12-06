package dev.ethans.cscbattlegrounds.data;

import lombok.Data;

@Data
public class Position {

    private String world;
    private double x;
    private double y;
    private double z;

    public Position(String world, double x, double y, double z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Position() {}
}