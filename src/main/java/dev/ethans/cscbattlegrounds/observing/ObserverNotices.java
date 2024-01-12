package dev.ethans.cscbattlegrounds.observing;

import dev.ethans.cscbattlegrounds.CSCBattlegroundsPlugin;
import dev.ethans.cscbattlegrounds.team.BattlegroundsTeam;
import dev.ethans.cscbattlegrounds.util.Strings;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;

public class ObserverNotices {

    private final static CSCBattlegroundsPlugin plugin = CSCBattlegroundsPlugin.getInstance();

    public static TextComponent twoTeamsClose(BattlegroundsTeam teamOne, BattlegroundsTeam teamTwo) {
        Player player = plugin.getServer().getPlayer(Objects.requireNonNull(teamOne.getPlayers().stream().findFirst().orElse(UUID.randomUUID())));

        if (player == null) return Component.text("");

        Location location = player.getLocation();

        return Strings.TWO_TEAMS_CLOSE(teamOne, teamTwo, location);
    }

    public static TextComponent playerNearManyMobs(Player player) {
        return Component.text(player.getName() + " is near many mobs!")
                .color(NamedTextColor.GREEN)
                .clickEvent(ClickEvent.runCommand("tp " + player.getLocation().getX() + " " + player.getLocation().getY() + 5 + " " + player.getLocation().getZ()));
    }
}
