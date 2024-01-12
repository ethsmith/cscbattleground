package dev.ethans.cscbattlegrounds.data;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BattlegroundSpawn {

    private int id;
    private Position position;

    public BattlegroundSpawn(int id, Position position) {
        this.id = id;
        this.position = position;
    }
}
