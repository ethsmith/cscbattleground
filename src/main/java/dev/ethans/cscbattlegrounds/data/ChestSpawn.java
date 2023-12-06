package dev.ethans.cscbattlegrounds.data;

import lombok.Data;

@Data
public class ChestSpawn {

    private int id;
    private Position position;

    public ChestSpawn(int id, Position position) {
        this.id = id;
        this.position = position;
    }

    public ChestSpawn() {}
}
