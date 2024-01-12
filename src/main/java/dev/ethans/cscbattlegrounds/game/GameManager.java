package dev.ethans.cscbattlegrounds.game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.ethans.cscbattlegrounds.CSCBattlegroundsPlugin;
import dev.ethans.cscbattlegrounds.team.BattlegroundsTeam;
import lombok.Data;
import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class GameManager {

    private final static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private final static CSCBattlegroundsPlugin plugin = CSCBattlegroundsPlugin.getInstance();

    private final static String TEAM_FILE_PATH = plugin.getDataFolder().getAbsolutePath() + "/teams/";

    private final Set<BattlegroundsTeam> teams = new HashSet<>();

    @Getter
    private BattlegroundsTeam winningTeam = null;

    public BattlegroundsTeam getPlayerTeam(Player player) {
        return teams.stream().filter(team -> team.getPlayers().contains(player.getUniqueId())).findFirst().orElse(null);
    }

    public int getAlivePlayerCount() {
        return (int) teams.stream().flatMap(team -> team.getPlayers().stream()).filter(uuid -> {
            if (plugin.getServer().getPlayer(uuid) == null) return false;
            return Objects.requireNonNull(plugin.getServer().getPlayer(uuid)).isOnline();
        }).count();
    }

    public boolean allPlayersOnline() {
        return teams.stream().allMatch(team -> team.getPlayers().stream().allMatch(uuid -> {
            if (plugin.getServer().getPlayer(uuid) == null) return false;
            return Objects.requireNonNull(plugin.getServer().getPlayer(uuid)).isOnline();
        }));
    }

    public int playersOnline() {
        return (int) teams.stream().flatMap(team -> team.getPlayers().stream()).filter(uuid -> {
            if (plugin.getServer().getPlayer(uuid) == null) return false;
            return Objects.requireNonNull(plugin.getServer().getPlayer(uuid)).isOnline();
        }).count();
    }

    public int maxPlayers() {
        return teams.stream().mapToInt(team -> team.getPlayers().size()).sum();
    }

    public boolean isOneTeamLeft() {
        // stream teams and return a count of teams that have at least one player that is online and in survival mode
        Set<BattlegroundsTeam> aliveTeams = teams.stream().filter(team -> team.getPlayers().stream().anyMatch(uuid -> {
            if (plugin.getServer().getPlayer(uuid) == null) return false;
            Player player = plugin.getServer().getPlayer(uuid);
            if (player == null) return false;
            return player.isOnline() && !player.isDead() && player.getGameMode() == GameMode.SURVIVAL;
        })).collect(Collectors.toSet());

        if (aliveTeams.size() == 1)
            winningTeam = aliveTeams.iterator().next();

        return aliveTeams.size() == 1;
    }

    public void saveTeams() {
        teams.forEach(team -> {
            String json = gson.toJson(team);
            String filePath = TEAM_FILE_PATH + team.getName() + ".json";

            // Get the directory path without the file name
            String directoryPath = TEAM_FILE_PATH;

            // Create the directory if it doesn't exist
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                if (directory.mkdirs()) {
                    plugin.getLogger().info("Directory created: " + directoryPath);
                } else {
                    plugin.getLogger().severe("Failed to create directory: " + directoryPath);
                    return; // Stop saving teams if directory creation fails
                }
            }

            try (FileWriter writer = new FileWriter(filePath)) {
                writer.write(json);
                plugin.getLogger().info("Saved team " + team.getName() + " to " + filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void loadTeams() {
        try {
            List<Path> teamFiles = getTeamFiles();

            for (Path filePath : teamFiles) {
                String json = Files.readString(filePath);
                BattlegroundsTeam team = gson.fromJson(json, BattlegroundsTeam.class);
                team.setTransientFields();
                teams.add(team);
                plugin.getLogger().info("Loaded team " + team.getName() + " from " + filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Path> getTeamFiles() throws IOException {
        List<Path> teamFiles = new ArrayList<>();

        Files.walkFileTree(Paths.get(TEAM_FILE_PATH), EnumSet.noneOf(FileVisitOption.class), Integer.MAX_VALUE,
                new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                        if (file.toString().endsWith(".json")) {
                            teamFiles.add(file);
                        }
                        return FileVisitResult.CONTINUE;
                    }
                });

        return teamFiles;
    }
}