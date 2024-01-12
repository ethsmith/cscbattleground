package dev.ethans.cscbattlegrounds.listener;

import dev.ethans.cscbattlegrounds.CSCBattlegroundsPlugin;
import dev.ethans.cscbattlegrounds.util.Strings;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.TimeSkipEvent;

public class WorldTimeListener implements Listener {

    private static final CSCBattlegroundsPlugin plugin = CSCBattlegroundsPlugin.getInstance();

    private boolean broadcastedAtNight = false;
    private boolean broadcastedAtDay = false;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWorldTimeChange(TimeSkipEvent event) {
        if (event.getSkipReason() != TimeSkipEvent.SkipReason.CUSTOM) return;

        if (event.getWorld().getTime() < 13000) {
            if (broadcastedAtDay) return;
            broadcastedAtNight = false;

            plugin.getServer().getOnlinePlayers().forEach(player -> {
                player.sendTitlePart(TitlePart.TITLE, Strings.DAY_TITLE);
                player.sendTitlePart(TitlePart.SUBTITLE, Strings.DAY_SUB_TITLE);
            });

            broadcastedAtDay = true;
            return;
        }

        if (broadcastedAtNight) return;

        plugin.getServer().getOnlinePlayers().forEach(player -> {
            player.sendTitlePart(TitlePart.TITLE, Strings.NIGHT_TITLE);
            player.sendTitlePart(TitlePart.SUBTITLE, Strings.NIGHT_SUB_TITLE);
        });

        broadcastedAtDay = false;
        broadcastedAtNight = true;
    }
}
