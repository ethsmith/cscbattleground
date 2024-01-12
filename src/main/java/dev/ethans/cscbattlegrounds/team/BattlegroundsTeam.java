package dev.ethans.cscbattlegrounds.team;

import dev.ethans.cscbattlegrounds.CSCBattlegroundsPlugin;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
public class BattlegroundsTeam {

    private final static CSCBattlegroundsPlugin plugin = CSCBattlegroundsPlugin.getInstance();

    private String name;

    private String color;

    private transient TextComponent nameComponent;

    private transient TextColor textColor;

    private Set<UUID> players;

    public BattlegroundsTeam(String name, String color) {
        new BattlegroundsTeam(name, color, new HashSet<>());
    }

    public BattlegroundsTeam(String name, String color, Set<UUID> players) {
        this.name = name;
        this.color = color;
        this.textColor = color.startsWith("#") ? TextColor.fromHexString(color) : NamedTextColor.NAMES.value(color.toLowerCase());
        this.nameComponent = Component.text(name, textColor);
        this.players = players;
    }

    public void setTransientFields() {
        this.textColor = color.startsWith("#") ? TextColor.fromHexString(color) : NamedTextColor.NAMES.value(color.toLowerCase());
        this.nameComponent = Component.text(name, textColor);
    }

    public Set<Player> getAllPlayers() {
        Set<Player> players = new HashSet<>();
        this.players.forEach(uuid -> {
            Player player = plugin.getServer().getPlayer(uuid);
            if (player == null) return;
            if (!player.isOnline()) return;
            players.add(player);
        });
        return players;
    }

    public Set<Player> getAlivePlayers() {
        Set<Player> players = new HashSet<>();
        this.players.forEach(uuid -> {
            Player player = plugin.getServer().getPlayer(uuid);
            if (player == null) return;
            if (!player.isOnline()) return;
            if (player.getGameMode() != GameMode.SURVIVAL) return;
            players.add(player);
        });
        return players;
    }

    public Set<Player> getDeadPlayers() {
        Set<Player> players = new HashSet<>();
        this.players.forEach(uuid -> {
            Player player = plugin.getServer().getPlayer(uuid);
            if (player == null) return;
            if (!player.isOnline()) return;
            if (player.getGameMode() != GameMode.SPECTATOR) return;
            players.add(player);
        });
        return players;
    }
}
