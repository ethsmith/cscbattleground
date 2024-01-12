package dev.ethans.cscbattlegrounds.state;

import dev.ethans.cscbattlegrounds.CSCBattlegroundsPlugin;
import dev.ethans.cscbattlegrounds.game.GameManager;
import dev.ethans.cscbattlegrounds.scoreboard.BattlegroundsScoreboard;
import dev.ethans.cscbattlegrounds.state.base.GameState;
import dev.ethans.cscbattlegrounds.util.Strings;
import dev.ethans.cscbattlegrounds.util.Styles;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

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
        sendCountdownMessage(5);
        plugin.getLogger().info("Pregame state ended");
    }

    private void sendCountdownMessage(int seconds) {
        AtomicInteger atomicSeconds = new AtomicInteger(seconds);

        new BukkitRunnable() {
            @Override
            public void run() {
                plugin.getServer().getOnlinePlayers().forEach(player -> {
                    TextColor timeColor;

                    if (atomicSeconds.get() > 3)
                        timeColor = Styles.SECONDARY_BLUE;
                    else if (atomicSeconds.get() <= 3 && atomicSeconds.get() >= 2)
                        timeColor = Styles.YELLOW;
                    else
                        timeColor = Styles.PRIMARY_RED;

                    Title title = Title.title(
                            Strings.STARTING_IN_TITLE(atomicSeconds.get(), timeColor),
                            Strings.STARTING_IN_SUB_TITLE,
                            Title.Times.times(Duration.ZERO, Duration.ofMillis(1200), Duration.ZERO)
                    );

                    player.showTitle(title);

                    if (atomicSeconds.get() <= 0)
                        cancel();
                });
            }
        }.runTaskTimer(plugin, 0, 20);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (atomicSeconds.get() == 0) {
                    cancel();
                    return;
                }

                atomicSeconds.getAndDecrement();
            }
        }.runTaskTimer(plugin, 0, 20);
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
            Title title = Title.title(
                    Strings.WAITING_FOR_PLAYERS_TITLE,
                    Strings.WAITING_FOR_PLAYERS_SUB_TITLE,
                    Title.Times.times(Duration.ZERO, Duration.ofMillis(1200), Duration.ZERO)
            );
            player.showTitle(title);
        });

        ticks = 0;
        allPlayersReady = gameManager.allPlayersOnline();
    }

    @Override
    public boolean isReadyToEnd() {
        if (allPlayersReady || forceStart) {
            plugin.getServer().getOnlinePlayers().forEach(Audience::clearTitle);
            return true;
        }
        return false;
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
