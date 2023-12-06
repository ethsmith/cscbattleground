package dev.ethans.cscbattlegrounds.chests;

import dev.ethans.cscbattlegrounds.CSCBattlegroundsPlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InstancedChestListener implements Listener {

    private static final CSCBattlegroundsPlugin plugin = CSCBattlegroundsPlugin.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChestOpen(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getClickedBlock() == null) return;
        Block block = event.getClickedBlock();
        if (block.getType() != Material.CHEST) return;

        event.setCancelled(true);

        Inventory inventory = plugin.getServer().createInventory(null, 27, Component.text("Chest"));
        new InstancedChest(inventory);

        player.openInventory(inventory);
    }
}