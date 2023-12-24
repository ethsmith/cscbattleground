package dev.ethans.cscbattlegrounds.util;

import dev.ethans.cscbattlegrounds.CSCBattlegroundsPlugin;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class WorldScanner {

    public static List<Location> scanWorld(Location center, int spacing, int count, int length) {
        World world = center.getWorld();
        List<Location> locations = new ArrayList<>();

        // Ensure positive values for spacing, count, and length
        spacing = Math.max(1, spacing);
        count = Math.max(1, count);
        length = Math.max(1, length);

        int centerX = center.getChunk().getX();
        int centerZ = center.getChunk().getZ();

        for (int x = -length; x <= length; x += spacing) {
            for (int z = -length; z <= length; z += spacing) {
                int chunkX = centerX + x;
                int chunkZ = centerZ + z;

                int blockX = chunkX << 4; // Multiply by 16 to get block coordinates
                int blockZ = chunkZ << 4;

                for (int i = 0; i < count; i++) {
                    int blockY = world.getHighestBlockYAt(blockX, blockZ);
                    Location location = new Location(world, blockX, blockY, blockZ);
                    if (location.getBlock().getType() == Material.WATER) continue;
                    CSCBattlegroundsPlugin.getInstance().getLogger().info("Scanned location at " + location.toString());
                    locations.add(location);

                    // Move to the next block in the same chunk
                    blockX++;
                }
            }
        }

        return locations;
    }
}
