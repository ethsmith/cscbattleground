package dev.ethans.cscbattlegrounds.data;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChestSpawn {

    private int id;
    private Position position;

    public ChestSpawn(int id, Position position) {
        this.id = id;
        this.position = position;
    }
}
