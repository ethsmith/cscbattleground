package dev.ethans.cscbattlegrounds.chests;

import dev.ethans.cscbattlegrounds.CSCBattlegroundsPlugin;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

public class InstancedChestListener implements Listener {

    private static final CSCBattlegroundsPlugin plugin = CSCBattlegroundsPlugin.getInstance();

    @Getter
    private static final Map<Integer, InstancedChest> instancedChests = new HashMap<>();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChestOpen(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player player = event.getPlayer();

        if (event.getClickedBlock() == null) return;

        Block block = event.getClickedBlock();

        if (block.getType() != Material.CHEST) return;
        if (!block.hasMetadata("chest-id")) return;

        int id = block.getMetadata("chest-id").get(0).asInt();

        if (!instancedChests.containsKey(id)) return;

        event.setCancelled(true);

        InstancedChest instancedChest = instancedChests.get(id);

        if (instancedChest.getSavedPlayerInventories().containsKey(player)) {
            Inventory inventory = instancedChest.getSavedPlayerInventories().get(player);
            instancedChest.getChest().open();
            player.openInventory(inventory);
            return;
        }

        Inventory inventory = instancedChest.generateInventory(player, plugin.getServer()
                .createInventory(null, 27, Component.text("Instanced Chest #" + id)));

        instancedChest.getChest().open();
        player.openInventory(inventory);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCloseInventory(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() != null) return;

        Player player = (Player) event.getPlayer();
        TextComponent title = (TextComponent) event.getView().title();

        if (!title.content().startsWith("Instanced Chest #")) return;

        int id = Integer.parseInt(title.content().split("#")[1]);

        InstancedChest instancedChest = instancedChests.get(id);

        if (!instancedChest.getSavedPlayerInventories().containsKey(player)) return;

        instancedChest.getChest().close();
    }
}
