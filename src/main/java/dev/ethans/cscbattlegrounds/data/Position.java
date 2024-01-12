package dev.ethans.cscbattlegrounds.data;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;

@Data
@NoArgsConstructor
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

    public Location toLocation() {
        return new Location(Bukkit.getWorld(world), x, y, z);
    }

    public static Position fromLocation(Location location) {
        return new Position(location.getWorld().getName(), location.getX(), location.getY(), location.getZ());
    }
}