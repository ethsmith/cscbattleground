package dev.ethans.cscbattlegrounds.util;

import dev.ethans.cscbattlegrounds.CSCBattlegroundsPlugin;
import dev.ethans.cscbattlegrounds.team.BattlegroundsTeam;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Strings {

    private static final CSCBattlegroundsPlugin plugin = CSCBattlegroundsPlugin.getInstance();

    public static TextComponent TEAM_ELIMINATED(BattlegroundsTeam team) {
        return Component.text("Team ")
                .append(team.getNameComponent())
                .append(Component.text(" has been eliminated!"))
                .color(team.getTextColor());
    }

    public static TextComponent PLAYER_ELIMINATED(Player player) {
        if (plugin.getGameManager().getPlayerTeam(player) == null) return Component.text("");
        BattlegroundsTeam team = plugin.getGameManager().getPlayerTeam(player);
        return Component.text(player.getName() + " has been eliminated!").color(team.getTextColor());
    }

    public static TextComponent WORLD_BOSS_SPAWNED = Component.text("The world boss has spawned!").color(Styles.SECONDARY_RED);

    public static TextComponent WORLD_BOSS_KILLED = Component.text("The world boss has been killed!").color(Styles.SECONDARY_RED);

    public static TextComponent WORLD_BORDER_SHRINKING = Component.text("The world border is shrinking!", Styles.PRIMARY_RED);

    public static final TextComponent DAY_TITLE = Component.text("The sun is rising...")
            .color(Styles.SUNNY);

    public static final TextComponent DAY_SUB_TITLE = Component.text("Monsters are now weaker!")
            .color(Styles.SUNNY);

    public static final TextComponent NIGHT_TITLE = Component.text("The sun is setting...")
            .color(Styles.NIGHT);

    public static final TextComponent NIGHT_SUB_TITLE = Component.text("Monsters are now stronger!")
            .color(Styles.NIGHT);

    public static TextComponent WORLD_BORDER_TIME(long timeLeft) {
        String time = String.format("%02d:%02d", timeLeft / 60, timeLeft % 60);
        return Component.text("World Border Duration: " + time, Styles.PRIMARY_BLUE);
    }

    public static TextComponent WINNING_TEAM_TITLE(BattlegroundsTeam team) {
        return team.getNameComponent()
                .append(Component.text(" Won!"))
                .color(team.getTextColor());
    }

    public static TextComponent WINNING_TEAM_SUBTITLE(BattlegroundsTeam team) {
        return Component.text("Thanks for playing!", NamedTextColor.GRAY);
    }

    public static String WORLD_BORDER_TIME_STRING(long timeLeft) {
        String time = String.format("%02d:%02d", timeLeft / 60, timeLeft % 60);
        return "Border: " + time;
    }

    public static TextComponent TWO_TEAMS_CLOSE(BattlegroundsTeam teamOne, BattlegroundsTeam teamTwo, Location location) {
        return Component.text("Team ")
                .append(teamOne.getNameComponent())
                .color(teamOne.getTextColor())
                .append(Component.text(" and "))
                .append(Component.text(" Team "))
                .append(teamTwo.getNameComponent())
                .color(teamTwo.getTextColor())
                .append(Component.text(" are within 10 blocks of each other!"))
                .color(NamedTextColor.GRAY)
                .clickEvent(ClickEvent.runCommand("tp " + location.getX() + " " + location.getY() + 5 + " " + location.getZ()));
    }

    public static TextComponent STARTING_IN_TITLE(long seconds, TextColor timeColor) {
        return Component.text("Starting in ", Styles.PRIMARY_BLUE)
                .append(Component.text(String.valueOf(seconds), timeColor))
                .append(Component.text(" seconds...", Styles.PRIMARY_BLUE));
    }

    public static final TextComponent STARTING_IN_SUB_TITLE = Component.text("Good luck!", Styles.PRIMARY_BLUE);

    public static final TextComponent WAITING_FOR_PLAYERS_TITLE = Component.text("Waiting for players...", Styles.SECONDARY_BLUE);

    public static final TextComponent WAITING_FOR_PLAYERS_SUB_TITLE = Component.text("(" + plugin.getGameManager().playersOnline() + "/" + plugin.getGameManager().maxPlayers() + ") Players ready!", Styles.PRIMARY_BLUE);

    public static TextComponent YOUR_COORDS(Player player) {
        return Component.text("Your Coords: ", Styles.PRIMARY_BLUE)
                .append(Component.text("X: " + String.format("%.2f", player.getLocation().getX())
                                + " Y: " + String.format("%.2f", player.getLocation().getY())
                                + " Z: " + String.format("%.2f", player.getLocation().getZ()),
                        Styles.WHITE));
    }

    public static final TextComponent YOU_CANT_DAMAGE_YOUR_OWN_TEAM = Component.text("You cannot damage your own team!", Styles.PRIMARY_RED);
}
