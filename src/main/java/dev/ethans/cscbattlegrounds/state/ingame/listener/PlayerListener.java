package dev.ethans.cscbattlegrounds.state.ingame.listener;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!event.hasExplicitlyChangedPosition()) return;
        Player player = event.getPlayer();
        player.sendActionBar(Component.text("Your Coords: ", TextColor.fromHexString("#1DACF7"))
                .append(Component.text("X: " + String.format("%.2f", player.getLocation().getX())
                                + " Y: " + String.format("%.2f", player.getLocation().getY())
                                + " Z: " + String.format("%.2f", player.getLocation().getZ()),
                        TextColor.fromHexString("#EFE9F4"))));
    }
}
