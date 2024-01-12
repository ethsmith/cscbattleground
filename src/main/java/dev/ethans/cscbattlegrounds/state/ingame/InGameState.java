package dev.ethans.cscbattlegrounds.state.ingame;

import com.nametagedit.plugin.NametagEdit;
import dev.ethans.cscbattlegrounds.CSCBattlegroundsPlugin;
import dev.ethans.cscbattlegrounds.border.ShrinkingWorldBorder;
import dev.ethans.cscbattlegrounds.chests.InstancedChest;
import dev.ethans.cscbattlegrounds.chests.InstancedChestListener;
import dev.ethans.cscbattlegrounds.data.BattlegroundSpawn;
import dev.ethans.cscbattlegrounds.data.BattlegroundsSpawns;
import dev.ethans.cscbattlegrounds.notices.Notices;
import dev.ethans.cscbattlegrounds.state.base.GameState;
import dev.ethans.cscbattlegrounds.state.ingame.listener.PlayerListener;
import dev.ethans.cscbattlegrounds.team.BattlegroundsTeam;
import dev.ethans.cscbattlegrounds.util.Strings;
import dev.ethans.cscbattlegrounds.util.Styles;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    private final List<ArmorStand> instancedChestDisplays = new ArrayList<>();
    private final List<Chest> instancedChests = new ArrayList<>();

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
        instancedChests.forEach(chest -> chest.setType(Material.AIR));
        instancedChestDisplays.forEach(Entity::remove);

        BattlegroundsTeam winningTeam = plugin.getGameManager().getWinningTeam();
        Title teamWonTitle = Title.title(Strings.WINNING_TEAM_TITLE(winningTeam), Strings.WINNING_TEAM_SUBTITLE(winningTeam), Title.Times.times(Duration.ofSeconds(2), Duration.ofSeconds(5), Duration.ofSeconds(2)));
        plugin.getServer().getOnlinePlayers().forEach(player -> player.showTitle(teamWonTitle));

        shrinkingWorldBorder.end();
    }

    @Override
    protected void onStart() {
        plugin.getLogger().info("Creating shrinking world border...");
        shrinkingWorldBorder = new ShrinkingWorldBorder(TIME_UNTIL_SHRINK, SHRINK_INTERVAL, SHRINK_TIME,
                SHRINK_AMOUNT, INITIAL_SIZE, MIN_SIZE);

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            Set<BattlegroundSpawn> spawns = new HashSet<>(BattlegroundsSpawns.getPlayerSpawns().values());

            plugin.getLogger().info("Teleporting players to their spawns...");
            plugin.getGameManager().getTeams().forEach(battlegroundsTeam ->
            {
                // Grab random spawn then remove it from the set
                BattlegroundSpawn spawn = spawns.stream().skip((int) (spawns.size() * Math.random())).findFirst().orElse(null);
                if (spawn == null) return;
                battlegroundsTeam.getAllPlayers().forEach(player -> {
                    NametagEdit.getApi().setPrefix(player, ChatColor.valueOf(battlegroundsTeam.getColor().toUpperCase()) + "");
                    player.teleport(spawn.getPosition().toLocation());
                });
                spawns.remove(spawn);
            });
        }, 20 * 6);

        BattlegroundsSpawns.getChestSpawns().forEach((id, chestSpawn) ->
        {
            Location location = chestSpawn.getPosition().toLocation();
            location.getBlock().setType(Material.CHEST);

            Chest chest = (Chest) location.getBlock().getState();
            chest.setMetadata("chest-id", new FixedMetadataValue(plugin, id));
            instancedChests.add(chest);
            InstancedChestListener.getInstancedChests().put(id, new InstancedChest(id, chest));

            ArmorStand armorStand = location.getWorld().spawn(location.clone().subtract(0, 0.75, 0), ArmorStand.class);
            armorStand.customName(Component.text("Instanced Chest", Styles.PRIMARY_BLUE));
            armorStand.setCustomNameVisible(true);
            armorStand.setGravity(false);
            armorStand.setVisible(false);
            instancedChestDisplays.add(armorStand);
        });
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public boolean isReadyToEnd() {
        return plugin.getGameManager().isOneTeamLeft();
    }
}
