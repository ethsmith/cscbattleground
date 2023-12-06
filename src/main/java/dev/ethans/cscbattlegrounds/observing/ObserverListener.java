package dev.ethans.cscbattlegrounds.observing;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ObserverListener implements Listener {

    @EventHandler
    public void onObserverJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("cscbattlegrounds.observer")) return;
        player.setGameMode(GameMode.SPECTATOR);
        player.setFlySpeed(0.2f);
    }
}
