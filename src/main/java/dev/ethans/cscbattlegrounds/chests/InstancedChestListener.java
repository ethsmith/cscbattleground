package dev.ethans.cscbattlegrounds.chests;

import dev.ethans.cscbattlegrounds.CSCBattlegroundsPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
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

    private final Map<Player, Map<Integer, InstancedChest>> playerChestInstances = new HashMap<>();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChestOpen(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player player = event.getPlayer();

        if (event.getClickedBlock() == null) return;

        Block block = event.getClickedBlock();

        if (block.getType() != Material.CHEST) return;
        if (!block.hasMetadata("chest-id")) return;

        int id = block.getMetadata("chest-id").get(0).asInt();

        if (playerChestInstances.containsKey(player) && playerChestInstances.get(player).containsKey(id)) {
            InstancedChest instancedChest = playerChestInstances.get(player).get(id);
            instancedChest.getChest().open();
            player.openInventory(InstancedChest.getSavedPlayerInventories().get(player));
            return;
        }

        event.setCancelled(true);

        Inventory inventory = plugin.getServer().createInventory(null, 27, Component.text("Instanced Chest #" + id));

        InstancedChest instancedChest = new InstancedChest(id, inventory, (Chest) block.getState());
        InstancedChest.getSavedPlayerInventories().put(player, player.getInventory());
        instancedChest.getChest().open();

        player.openInventory(inventory);

        if (!playerChestInstances.containsKey(player))
            playerChestInstances.put(player, new HashMap<>());

        playerChestInstances.get(player).put(id, instancedChest);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCloseInventory(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() != null) return;

        Player player = (Player) event.getPlayer();

        if (!playerChestInstances.containsKey(player)) return;

        Map<Integer, InstancedChest> playerChests = playerChestInstances.get(player);

        TextComponent title = (TextComponent) event.getView().title();

        if (!title.content().startsWith("Instanced Chest #")) return;

        int id = Integer.parseInt(title.content().split("#")[1]);

        if (!playerChests.containsKey(id)) return;

        InstancedChest instancedChest = playerChests.get(id);
        instancedChest.getChest().close();
    }
}
