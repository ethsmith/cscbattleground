package dev.ethans.cscbattlegrounds.border;

import dev.ethans.cscbattlegrounds.CSCBattlegroundsPlugin;
import dev.ethans.cscbattlegrounds.notices.Notices;
import dev.ethans.cscbattlegrounds.util.StringUtil;
import dev.ethans.cscbattlegrounds.util.Strings;
import dev.ethans.cscbattlegrounds.util.Styles;
import lombok.Data;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Data
public class ShrinkingWorldBorder {

    private static final CSCBattlegroundsPlugin plugin = CSCBattlegroundsPlugin.getInstance();

    private final Duration timeUntilShrink;

    private final Duration shrinkInterval;

    private final Duration shrinkTime;

    private final double shrinkAmount;

    private final double initialSize;
    private final double minimumSize;

    private final WorldBorder worldBorder;

    private final Map<Player, BossBar> bossBars = new HashMap<>();

    private boolean timerEnded = false;

    public ShrinkingWorldBorder(Duration timeUntilShrink, Duration shrinkInterval, Duration shrinkTime, double shrinkAmount, double initialSize, double minimumSize) {
        this.timeUntilShrink = timeUntilShrink;
        this.shrinkInterval = shrinkInterval;
        this.shrinkTime = shrinkTime;
        this.shrinkAmount = shrinkAmount;
        this.initialSize = initialSize;
        this.minimumSize = minimumSize;
        this.worldBorder = plugin.getServer().getWorld("world").getWorldBorder();

        worldBorder.setSize(initialSize);
        worldBorder.setCenter(0, 0);
        worldBorder.setDamageAmount(1);
        scheduleShrink();
    }

    private void scheduleShrink() {
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (worldBorder.getSize() <= minimumSize) {
                        cancel();
                        return;
                    }

                    plugin.getServer().broadcast(Strings.WORLD_BORDER_SHRINKING);
                    plugin.getServer().getLogger().info("Current world border size: " + worldBorder.getSize());
                    worldBorder.setSize(worldBorder.getSize() - shrinkAmount, shrinkTime.toSeconds());
                }
            }.runTaskTimer(plugin, 0, shrinkInterval.toSeconds() * 20);
            sendTimerActionBar();
        }, timeUntilShrink.toSeconds() * 20);
    }

    private void sendTimerActionBar() {
        AtomicLong secondsToShrink = new AtomicLong(shrinkInterval.getSeconds());
        new BukkitRunnable() {
            @Override
            public void run() {
                if (worldBorder.getSize() <= minimumSize || timerEnded) {
                    timerEnded = true;
                    cancel();
                    return;
                }

                long secondsLeft = secondsToShrink.getAndDecrement();
                float progress = (float) secondsLeft / shrinkInterval.getSeconds();

                String gameInfoStr = StringUtil.evenlySpaced(Strings.WORLD_BORDER_TIME_STRING(secondsLeft),
                        "Alive: " + plugin.getGameManager().getAlivePlayerCount(),
                        "Players: " + plugin.getGameManager().maxPlayers());

                TextComponent gameInfo = Component.text(gameInfoStr, Styles.PRIMARY_BLUE);

                plugin.getServer().getOnlinePlayers().forEach(player -> {
                    if (bossBars.containsKey(player)) {
                        bossBars.get(player).progress(progress);
                        bossBars.get(player).name(gameInfo);
                        return;
                    }
                    BossBar bossBar = BossBar.bossBar(gameInfo, progress, BossBar.Color.BLUE, BossBar.Overlay.PROGRESS);
                    player.showBossBar(bossBar);
                    bossBars.put(player, bossBar);
                });

                if (secondsToShrink.get() == 0)
                    secondsToShrink.set(shrinkInterval.getSeconds());
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    public void end() {
        timerEnded = true;
        bossBars.forEach(Audience::hideBossBar);
    }
}