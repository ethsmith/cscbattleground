package dev.ethans.cscbattlegrounds.data;

import lombok.Data;
import org.bukkit.util.Vector;

@Data
public class BattlegroundSpawn {

    private int id;
    private Position position;

    public BattlegroundSpawn(int id, Position position) {
        this.id = id;
        this.position = position;
    }

    public BattlegroundSpawn() {}
}
