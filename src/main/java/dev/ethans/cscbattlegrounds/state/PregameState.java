package dev.ethans.cscbattlegrounds.state;

import dev.ethans.cscbattlegrounds.CSCBattlegroundsPlugin;
import dev.ethans.cscbattlegrounds.game.GameManager;
import dev.ethans.cscbattlegrounds.scoreboard.BattlegroundsScoreboard;
import dev.ethans.cscbattlegrounds.state.base.GameState;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public class PregameState extends GameState {

    private static final CSCBattlegroundsPlugin plugin = CSCBattlegroundsPlugin.getInstance();

    private final GameManager gameManager = plugin.getGameManager();

    @Getter @Setter
    private boolean allPlayersReady = false;

    @Getter @Setter
    private boolean forceStart = false;

    private int ticks = 0;

    public PregameState(JavaPlugin plugin) {
        super(plugin);
    }

    @NotNull
    @Override
    public Duration getDuration() {
        return Duration.ZERO;
    }

    @Override
    protected void onEnd() {
        plugin.getLogger().info("Pregame state ended");
    }

    @Override
    protected void onStart() {
        plugin.getLogger().info("Pregame state started");
    }

    @Override
    public void onUpdate() {
        if (allPlayersReady) return;
        ticks++;
        if (ticks != 20) return;

        plugin.getServer().getOnlinePlayers().forEach(player ->
        {
            player.sendActionBar(Component.text(
                    "(" + gameManager.playersOnline() + "/" + gameManager.maxPlayers() + ") Players ready!",
                    TextColor.fromHexString("#1dacf7")));
        });

        ticks = 0;
        allPlayersReady = gameManager.allPlayersOnline();
    }

    @Override
    public boolean isReadyToEnd() {
        return allPlayersReady || forceStart;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        new BattlegroundsScoreboard(player);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        event.setCancelled(true);
    }
}
