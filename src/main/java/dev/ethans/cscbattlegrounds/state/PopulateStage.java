package dev.ethans.cscbattlegrounds.state;

import dev.ethans.cscbattlegrounds.CSCBattlegroundsPlugin;
import dev.ethans.cscbattlegrounds.data.BattlegroundsSpawns;
import dev.ethans.cscbattlegrounds.state.base.GameState;
import dev.ethans.cscbattlegrounds.util.WorldScanner;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.List;

public class PopulateStage extends GameState {

    private static final CSCBattlegroundsPlugin plugin = CSCBattlegroundsPlugin.getInstance();

    public PopulateStage(JavaPlugin plugin) {
        super(plugin);
    }

    @NotNull
    @Override
    public Duration getDuration() {
//        return Duration.ofMinutes(5);
        return Duration.ZERO;
    }

    @Override
    protected void onEnd() {

    }

    @Override
    protected void onStart() {
//        List<Location> locations = WorldScanner.scanWorld(new Location(plugin.getServer().getWorld("world"), 0, 0, 0), 2, 1, 50);
//        locations.forEach(location -> BattlegroundsSpawns.addChestSpawn(location));
    }

    @Override
    public void onUpdate() {

    }
}
