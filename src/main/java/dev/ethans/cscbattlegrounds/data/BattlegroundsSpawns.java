package dev.ethans.cscbattlegrounds.data;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import dev.ethans.cscbattlegrounds.CSCBattlegroundsPlugin;
import lombok.Getter;
import org.bukkit.Location;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class BattlegroundsSpawns {

    private final static CSCBattlegroundsPlugin plugin = CSCBattlegroundsPlugin.getInstance();

    private final static Gson gson = new Gson();

    @Getter
    private final static Map<Integer, BattlegroundSpawn> playerSpawns = new HashMap<>();

    @Getter
    private final static Map<Integer, ChestSpawn> chestSpawns = new HashMap<>();

    @Getter
    private final static Queue<Integer> chestSpawnDeleteQueue = new LinkedList<>();

    @Getter
    private final static Queue<Integer> playerSpawnDeleteQueue = new LinkedList<>();

    public static void addChestSpawn(Location location) {
        int lastId = BattlegroundsSpawns.getChestSpawns().keySet().stream().max(Integer::compareTo).orElse(0);
        double x = location.getBlockX() + 0.5;
        double y = location.getBlockY()  + 1;
        double z = location.getBlockZ() + 0.5;
        ChestSpawn spawn = new ChestSpawn(lastId + 1, new Position(location.getWorld().getName(), x, y, z));
        BattlegroundsSpawns.getChestSpawns().put(lastId + 1, spawn);
    }

    public static void deletePlayerSpawnFiles() {
        while (!playerSpawnDeleteQueue.isEmpty()) {
            int id = playerSpawnDeleteQueue.poll();
            BattlegroundSpawn spawn = playerSpawns.get(id);

            if (spawn == null) continue;

            // Delete file matching id in plugin data folder / spawns
            File file = new File(plugin.getDataFolder() + "/player-spawns/" + id + ".json");

            if (!file.exists()) continue;

            boolean deleted = file.delete();

            if (!deleted)
                plugin.getLogger().warning("Failed to delete file " + file.getName());
        }
    }

    public static void deleteChestSpawnFiles() {
        while (!chestSpawnDeleteQueue.isEmpty()) {
            int id = chestSpawnDeleteQueue.poll();
            ChestSpawn spawn = chestSpawns.get(id);

            if (spawn == null) continue;

            // Delete file matching id in plugin data folder / spawns
            File file = new File(plugin.getDataFolder() + "/chest-spawns/" + id + ".json");

            if (!file.exists()) continue;

            boolean deleted = file.delete();

            if (!deleted)
                plugin.getLogger().warning("Failed to delete file " + file.getName());
        }
    }

    public static void loadBattlegroundSpawns() throws IOException {
        File spawnsFolder = new File(plugin.getDataFolder() + "/player-spawns");

        boolean created = createNeededFolders(spawnsFolder);
        if (!created) return;

        File[] files = spawnsFolder.listFiles();
        if (files == null) return;

        for (File file : files) {
            plugin.getLogger().info("Loading player spawn " + file.getName());

            // use gson to load file into BattlegroundSpawn object
            JsonReader reader = getJsonReader(file);
            BattlegroundSpawn spawn = gson.fromJson(reader, BattlegroundSpawn.class);

            if (spawn == null) continue;

            // add to battlegroundSpawns map
            playerSpawns.put(spawn.getId(), spawn);
        }
    }

    public static void loadChestSpawns() throws IOException {
        File spawnsFolder = new File(plugin.getDataFolder() + "/chest-spawns");

        boolean created = createNeededFolders(spawnsFolder);
        if (!created) return;

        File[] files = spawnsFolder.listFiles();
        if (files == null) return;

        for (File file : files) {
            plugin.getLogger().info("Loading chest spawn " + file.getName());

            // use gson to load file into BattlegroundSpawn object
            JsonReader reader = getJsonReader(file);
            ChestSpawn spawn = gson.fromJson(reader, ChestSpawn.class);

            if (spawn == null) continue;

            // add to battlegroundSpawns map
            chestSpawns.put(spawn.getId(), spawn);
        }
    }

    private static JsonReader getJsonReader(File file) throws IOException {
        String contents = FileUtils.fileRead(file);
        JsonReader reader = new JsonReader(new StringReader(contents));
        reader.setLenient(true);
        return reader;
    }

    private static boolean createNeededFolders(File path) {
        if (!path.exists()) {
            boolean created = path.mkdir();
            if (!created) {
                plugin.getLogger().warning("Failed to create spawns folder");
                return false;
            }
        }

        return true;
    }

    public static void saveBattlegroundSpawns() {
        playerSpawns.forEach((id, spawn) -> {
            // use gson to save BattlegroundSpawn object to file
            String filePath = plugin.getDataFolder() + "/player-spawns/" + id + ".json";
            String json = gson.toJson(spawn);

            try (FileWriter writer = new FileWriter(filePath)) {
                writer.write(json);
            } catch (Exception e) {
                plugin.getLogger().warning("Failed to save spawn " + id);
            }
        });
    }

    public static void saveChestSpawns() {
        chestSpawns.forEach((id, spawn) -> {
            // use gson to save BattlegroundSpawn object to file
            String filePath = plugin.getDataFolder() + "/chest-spawns/" + id + ".json";
            String json = gson.toJson(spawn);

            try (FileWriter writer = new FileWriter(filePath)) {
                writer.write(json);
            } catch (Exception e) {
                plugin.getLogger().warning("Failed to save spawn " + id);
            }
        });
    }
}
