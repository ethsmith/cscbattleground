package dev.ethans.cscbattlegrounds.notices;

import dev.ethans.cscbattlegrounds.CSCBattlegroundsPlugin;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class NoticeSounds {

    private final static CSCBattlegroundsPlugin plugin = CSCBattlegroundsPlugin.getInstance();

    public static void playEliminatedSound() {
        plugin.getServer().getOnlinePlayers().forEach(player ->
                player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1, 1));
    }
}
