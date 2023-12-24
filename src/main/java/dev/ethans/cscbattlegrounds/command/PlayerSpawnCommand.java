package dev.ethans.cscbattlegrounds.command;

import dev.ethans.cscbattlegrounds.CSCBattlegroundsPlugin;
import dev.ethans.cscbattlegrounds.data.BattlegroundSpawn;
import dev.ethans.cscbattlegrounds.data.BattlegroundsSpawns;
import dev.ethans.cscbattlegrounds.data.ChestSpawn;
import dev.ethans.cscbattlegrounds.data.Position;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class PlayerSpawnCommand implements CommandExecutor {

    private boolean showingMarkers = false;

    public PlayerSpawnCommand() {
        createMarkerRunnable();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (!commandSender.hasPermission("cscbattlegrounds.admin")) return true;
        if (!(commandSender instanceof Player player)) return true;

        switch (command.getName().toLowerCase()) {
            case "playerspawn":
            case "ps":
            case "pspawn":
                if (strings.length == 0) {
                    // Add player spawn
                    addPlayerSpawn(player);
                    return true;
                }

                int id = -1;
                if (strings.length > 1) {
                    try { id = Integer.parseInt(strings[1]); }

                    catch (NumberFormatException e) {
                        player.sendMessage("§cInvalid ID.");
                        return true;
                    }
                }

                switch (strings[0].toLowerCase()) {
                    case "add":
                        // Add player spawn
                        addPlayerSpawn(player);
                        break;

                    case "remove":
                        // Remove player spawn
                        if (id == -1) {
                            player.sendMessage("§cYou must specify an ID.");
                            return true;
                        }

                        if (!BattlegroundsSpawns.getPlayerSpawns().containsKey(id)) {
                            player.sendMessage("§cThat ID does not exist.");
                            return true;
                        }

                        BattlegroundsSpawns.getPlayerSpawns().remove(id);
                        BattlegroundsSpawns.getPlayerSpawnDeleteQueue().offer(id);
                        break;

                    case "markers":
                        if (showingMarkers) {
                            showingMarkers = false;
                            player.sendMessage("§cDisabled chest spawn markers.");
                        } else {
                            showingMarkers = true;
                            player.sendMessage("§aEnabled chest spawn markers.");
                        }
                        break;

                    default:
                        player.sendMessage("§cInvalid subcommand.");
                        break;
                }
                break;

            default:
                break;

        }
        return true;
    }

    private void addPlayerSpawn(Player player) {
        int lastId = BattlegroundsSpawns.getPlayerSpawns().keySet().stream().max(Integer::compareTo).orElse(0);
        double x = player.getLocation().getBlockX() + 0.5;
        double y = player.getLocation().getBlockY();
        double z = player.getLocation().getBlockZ() + 0.5;
        BattlegroundSpawn spawn = new BattlegroundSpawn(lastId + 1, new Position(player.getWorld().getName(), x, y, z));
        BattlegroundsSpawns.getPlayerSpawns().put(lastId + 1, spawn);
        player.sendMessage("§aAdded player spawn with ID " + (lastId + 1) + ".");
    }

    private void createMarkerRunnable() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!showingMarkers) return;

                for (ChestSpawn spawn : BattlegroundsSpawns.getChestSpawns().values()) {
                    Position position = spawn.getPosition();
                    Location location = new Location(CSCBattlegroundsPlugin.getInstance().getServer().getWorld(position.getWorld()), position.getX(), position.getY(), position.getZ());
                    location.getWorld().spawnParticle(org.bukkit.Particle.VILLAGER_HAPPY, location, 10);
                }
            }
        }.runTaskTimer(CSCBattlegroundsPlugin.getInstance(), 0, 1);
    }
}
