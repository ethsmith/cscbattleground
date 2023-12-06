package dev.ethans.cscbattlegrounds.listener;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.PortalCreateEvent;

public class PortalListener implements Listener {

    @EventHandler
    public void onPortalEnter(PortalCreateEvent event) {
        if (event.getEntity() instanceof Player player) {
            player.sendMessage(Component.text("Portals are disabled in this game!").color(NamedTextColor.RED));
        }
        event.setCancelled(true);
    }
}
