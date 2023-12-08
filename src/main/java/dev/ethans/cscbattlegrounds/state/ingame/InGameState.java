package dev.ethans.cscbattlegrounds.state.ingame;

import dev.ethans.cscbattlegrounds.CSCBattlegroundsPlugin;
import dev.ethans.cscbattlegrounds.border.ShrinkingWorldBorder;
import dev.ethans.cscbattlegrounds.data.BattlegroundSpawn;
import dev.ethans.cscbattlegrounds.data.BattlegroundsSpawns;
import dev.ethans.cscbattlegrounds.state.base.GameState;
import dev.ethans.cscbattlegrounds.state.ingame.listener.PlayerListener;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class InGameState extends GameState {

    private final CSCBattlegroundsPlugin plugin = CSCBattlegroundsPlugin.getInstance();

    private final Duration TIME_UNTIL_SHRINK = Duration.ofSeconds(plugin.getConfig().getInt("WORLD_BORDER.TIME_UNTIL_SHRINK"));
    private final Duration SHRINK_INTERVAL = Duration.ofSeconds(plugin.getConfig().getInt("WORLD_BORDER.SHRINK_INTERVAL"));
    private final Duration SHRINK_TIME = Duration.ofSeconds(plugin.getConfig().getInt("WORLD_BORDER.SHRINK_TIME"));
    private final double SHRINK_AMOUNT = plugin.getConfig().getDouble("WORLD_BORDER.SHRINK_AMOUNT");
    private final double INITIAL_SIZE = plugin.getConfig().getDouble("WORLD_BORDER.INITIAL_SIZE");
    private final double MIN_SIZE = plugin.getConfig().getDouble("WORLD_BORDER.MINIMUM_SIZE");

    @Getter
    private ShrinkingWorldBorder shrinkingWorldBorder;

    public InGameState(JavaPlugin plugin) {
        super(plugin);
        register(new PlayerListener());
    }

    @NotNull
    @Override
    public Duration getDuration() {
        return Duration.ZERO;
    }

    @Override
    protected void onEnd() {

    }

    @Override
    protected void onStart() {
        plugin.getLogger().info("Creating shrinking world border...");
        shrinkingWorldBorder = new ShrinkingWorldBorder(TIME_UNTIL_SHRINK, SHRINK_INTERVAL, SHRINK_TIME,
                SHRINK_AMOUNT, INITIAL_SIZE, MIN_SIZE);

        Set<BattlegroundSpawn> spawns = new HashSet<>(BattlegroundsSpawns.getPlayerSpawns().values());

        plugin.getLogger().info("Teleporting players to their spawns...");
        plugin.getGameManager().getTeams().forEach(battlegroundsTeam ->
        {
            // Grab random spawn then remove it from the set
            BattlegroundSpawn spawn = spawns.stream().skip((int) (spawns.size() * Math.random())).findFirst().orElse(null);
            if (spawn == null) return;
            battlegroundsTeam.getAllPlayers().forEach(player -> player.teleport(spawn.getPosition().toLocation()));
            spawns.remove(spawn);
        });
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public boolean isReadyToEnd() {
        return false;
    }
}
