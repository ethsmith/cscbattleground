package dev.ethans.cscbattlegrounds.time;

import dev.ethans.cscbattlegrounds.CSCBattlegroundsPlugin;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class DayNightCycle {

    private final static CSCBattlegroundsPlugin plugin = CSCBattlegroundsPlugin.getInstance();
    private final static int MINUTES_PER_DAY = plugin.getConfig().getInt("DAY_NIGHT_CYCLE.MINUTES_PER_DAY") * 60 * 20; // 45 minutes converted to ticks
    private final static int MINUTES_PER_NIGHT = plugin.getConfig().getInt("DAY_NIGHT_CYCLE.MINUTES_PER_NIGHT") * 60 * 20; // 15 minutes converted to ticks

    private final static int DEFAULT_MINUTES_PER_DAY = 20 * 60 * 20;
    private final static int DEFAULT_MINUTES_PER_NIGHT = 7 * 60 * 20;

    private double currentTime = 0;

    private final long tickIntervalDay = MINUTES_PER_DAY / DEFAULT_MINUTES_PER_DAY;

    private final long tickIntervalNight = MINUTES_PER_NIGHT / DEFAULT_MINUTES_PER_NIGHT;

    private final World world;

    public DayNightCycle() {
        this.world = plugin.getServer().getWorld("world");
        if (world == null) return;
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        world.setTime(0);
        scheduleDayTask();
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () ->
                world.setTime((long) currentTime), 1, 1);
    }

    private void scheduleDayTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                currentTime += 0.6;
                if (currentTime >= 13000) {
                    scheduleNightTask();
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0, tickIntervalDay);
    }

    private void scheduleNightTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                currentTime += 0.6;
                if (currentTime >= 24000) {
                    cancel();
                    currentTime = 0;
                    scheduleDayTask();
                }
            }
        }.runTaskTimer(plugin, 0, tickIntervalNight);
    }

    public void reset() {
        World world = plugin.getServer().getWorld("world");
        if (world == null) return;
        world.setTime(0);
    }
}
