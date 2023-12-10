package dev.ethans.cscbattlegrounds.chests;

import lombok.Data;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.*;

@Data
public class InstancedChest {

    private final int id;

    private final Chest chest;

    @Getter
    private final Map<Player, Inventory> savedPlayerInventories = new HashMap<>();

    public InstancedChest(int id, Chest chest) {
        this.id = id;
        this.chest = chest;
    }

    public Inventory generateInventory(Player player, Inventory inventory) {
        inventory.addItem(getRandomItems());
        shuffleInventory(inventory);
        savedPlayerInventories.put(player, inventory);
        return inventory;
    }

    private List<LootTableEntry> getLootTable() {
        List<LootTableEntry> lootTable = new ArrayList<>();

        for (LootTable table : LootTable.values()) {
            for (ItemStack item : table.getItems()) {
                lootTable.add(new LootTableEntry(table.getWeight(), item));
            }
        }

        return lootTable;
    }

    private ItemStack[] getRandomItems() {
        double budget = 1.0;

        List<LootTableEntry> lootTable = getLootTable();
        Collections.shuffle(lootTable);
        List<ItemStack> items = new ArrayList<>();

        while (budget > 0) {
            List<LootTableEntry> entries = new ArrayList<>(lootTable);
            LootTableEntry entry = entries.get(new Random().nextInt(entries.size()));

            while (entry.weight() > budget) {
                entry = entries.get(new Random().nextInt(entries.size()));
                entries.remove(entry);
                if (entries.isEmpty()) break;
            }

            budget -= entry.weight();
            items.add(entry.item());
        }

        return items.toArray(new ItemStack[0]);
    }

    public static void shuffleInventory(Inventory inventory) {
        List<ItemStack> contents = Arrays.asList(inventory.getContents());
        Collections.shuffle(contents);
        inventory.setContents(contents.toArray(new ItemStack[0]));
    }

    private record LootTableEntry(double weight, ItemStack item) {}

    private enum LootTable {
        COMMON(0.15,
                createItem("Bow", Material.BOW, 1),
                createItem("Arrow", Material.ARROW, 15),
                createItem("Stone Sword", Material.STONE_SWORD, Enchantment.DAMAGE_ALL, 1, 1),
                createItem("Stone Sword", Material.STONE_SWORD, Enchantment.FIRE_ASPECT, 1, 1),
                createItem("Stone Sword", Material.STONE_SWORD, 1),
                createItem("Stone Axe", Material.STONE_AXE, Enchantment.DAMAGE_ALL, 1, 1),
                createItem("Stone Axe", Material.STONE_AXE, Enchantment.FIRE_ASPECT, 1, 1),
                createItem("Stone Axe", Material.STONE_AXE, 1),
                createItem("Bread", Material.BREAD, 5),
                createItem("Steak", Material.COOKED_BEEF, 3),
                createPotion("Potion of Healing", PotionType.INSTANT_HEAL, false, 1),
                createItem("Chainmail Helmet", Material.CHAINMAIL_HELMET, 1),
                createItem("Chainmail Chestplate", Material.CHAINMAIL_CHESTPLATE, 1),
                createItem("Chainmail Leggings", Material.CHAINMAIL_LEGGINGS, 1),
                createItem("Chainmail Boots", Material.CHAINMAIL_BOOTS, 1)
        ),

        UNCOMMON(0.25,
                createItem("Arrow", Material.ARROW, 30),
                createItem("Bow", Material.BOW, Enchantment.ARROW_DAMAGE, 1, 1),
                createItem("Stone Sword", Material.STONE_SWORD, Enchantment.DAMAGE_ALL, 2, 1),
                createItem("Stone Sword", Material.STONE_SWORD, Enchantment.FIRE_ASPECT, 2, 1),
                createItem("Stone Axe", Material.STONE_AXE, Enchantment.DAMAGE_ALL, 2, 1),
                createItem("Stone Axe", Material.STONE_AXE, Enchantment.FIRE_ASPECT, 2, 1),
                createItem("Iron Sword", Material.IRON_SWORD, Enchantment.DAMAGE_ALL, 1, 1),
                createItem("Iron Sword", Material.IRON_SWORD, Enchantment.FIRE_ASPECT, 1, 1),
                createItem("Iron Sword", Material.IRON_SWORD, 1),
                createItem("Iron Axe", Material.IRON_AXE, Enchantment.DAMAGE_ALL, 1, 1),
                createItem("Iron Axe", Material.IRON_AXE, Enchantment.FIRE_ASPECT, 1, 1),
                createItem("Iron Axe", Material.IRON_AXE, 1),
                createItem("Bread", Material.BREAD, 10),
                createItem("Steak", Material.COOKED_BEEF, 7),
                createPotion("Potion of Healing", PotionType.INSTANT_HEAL, false, 2),
                createItem("Iron Helmet", Material.IRON_HELMET, 1),
                createItem("Iron Chestplate", Material.IRON_CHESTPLATE, 1),
                createItem("Iron Leggings", Material.IRON_LEGGINGS, 1),
                createItem("Iron Boots", Material.IRON_BOOTS, 1)
        ),

        RARE(0.5,
                createItem("Iron Sword", Material.IRON_SWORD, Enchantment.DAMAGE_ALL, 2, 1),
                createItem("Iron Sword", Material.IRON_SWORD, Enchantment.FIRE_ASPECT, 2, 1),
                createItem("Iron Axe", Material.IRON_AXE, Enchantment.DAMAGE_ALL, 2, 1),
                createItem("Iron Axe", Material.IRON_AXE, Enchantment.FIRE_ASPECT, 2, 1),
                createItem("Diamond Sword", Material.DIAMOND_SWORD, Enchantment.DAMAGE_ALL, 1, 1),
                createItem("Diamond Sword", Material.DIAMOND_SWORD, Enchantment.FIRE_ASPECT, 1, 1),
                createItem("Diamond Sword", Material.DIAMOND_SWORD, 1),
                createItem("Diamond Axe", Material.DIAMOND_AXE, Enchantment.DAMAGE_ALL, 1, 1),
                createItem("Diamond Axe", Material.DIAMOND_AXE, Enchantment.FIRE_ASPECT, 1, 1),
                createItem("Diamond Axe", Material.DIAMOND_AXE, 1),
                createItem("Bread", Material.BREAD, 15),
                createItem("Steak", Material.COOKED_BEEF, 10),
                createPotion("Potion of Healing", PotionType.INSTANT_HEAL, false, 3),
                createPotion("Splash Potion of Long Regen", PotionType.LONG_REGENERATION, true, 1),
                createItem("Iron Helmet", Material.IRON_HELMET, Enchantment.PROTECTION_ENVIRONMENTAL, 1, 1),
                createItem("Iron Chestplate", Material.IRON_CHESTPLATE, Enchantment.PROTECTION_ENVIRONMENTAL, 1, 1),
                createItem("Iron Leggings", Material.IRON_LEGGINGS, Enchantment.PROTECTION_ENVIRONMENTAL, 1, 1),
                createItem("Iron Boots", Material.IRON_BOOTS, Enchantment.PROTECTION_ENVIRONMENTAL, 1, 1)
        ),

        MYTHIC(1.0,
                createItem("Diamond Sword", Material.DIAMOND_SWORD, Enchantment.DAMAGE_ALL, 2, 1),
                createItem("Diamond Sword", Material.DIAMOND_SWORD, Enchantment.FIRE_ASPECT, 2, 1),
                createItem("Diamond Axe", Material.DIAMOND_AXE, Enchantment.DAMAGE_ALL, 2, 1),
                createItem("Diamond Axe", Material.DIAMOND_AXE, Enchantment.FIRE_ASPECT, 2, 1),
                createItem("Bread", Material.BREAD, 20),
                createItem("Steak", Material.COOKED_BEEF, 15),
                createPotion("Splash Potion of Healing", PotionType.INSTANT_HEAL, true, 4),
                createPotion("Splash Potion of Long Regen", PotionType.LONG_REGENERATION, true, 2),
                createItem("Trident", Material.TRIDENT, Enchantment.LOYALTY, 2, 1),
                createItem("Diamond Helmet", Material.DIAMOND_HELMET, Enchantment.PROTECTION_ENVIRONMENTAL, 1, 1),
                createItem("Diamond Chestplate", Material.DIAMOND_CHESTPLATE, Enchantment.PROTECTION_ENVIRONMENTAL, 1, 1),
                createItem("Diamond Leggings", Material.DIAMOND_LEGGINGS, Enchantment.PROTECTION_ENVIRONMENTAL, 1, 1),
                createItem("Diamond Boots", Material.DIAMOND_BOOTS, Enchantment.PROTECTION_ENVIRONMENTAL, 1, 1)
        );

        @Getter
        private final double weight;

        @Getter
        private final ItemStack[] items;

        LootTable(double weight, ItemStack... items) {
            this.weight = weight;
            this.items = items;
        }

        private static ItemStack createPotion(String displayName, PotionType potionType, boolean splash, int amount) {
            ItemStack item = new ItemStack(splash ? Material.SPLASH_POTION : Material.POTION);
            item.setAmount(amount);
            PotionMeta meta = (PotionMeta) item.getItemMeta();
            if (meta == null) return item;
            meta.setDisplayName(displayName);
            meta.setBasePotionType(potionType);
            item.setItemMeta(meta);
            return item;
        }

        private static ItemStack createItem(String displayName, Material material, Enchantment enchantment, int level, int amount) {
            ItemStack item = new ItemStack(material);
            item.setAmount(amount);
            if (enchantment.canEnchantItem(item))
                item.addUnsafeEnchantment(enchantment, level);
            return setName(item, displayName);
        }

        private static ItemStack createItem(String displayName, Material material, int amount) {
            ItemStack item = new ItemStack(material);
            item.setAmount(amount);
            return setName(item, displayName);
        }

        private static ItemStack setName(ItemStack item, String displayName) {
            ItemMeta meta = item.getItemMeta();
            if (meta == null) return item;
            meta.setDisplayName(displayName);
            item.setItemMeta(meta);
            return item;
        }
    }
}
