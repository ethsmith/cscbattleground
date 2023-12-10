package dev.ethans.cscbattlegrounds.notices;

import dev.ethans.cscbattlegrounds.CSCBattlegroundsPlugin;
import dev.ethans.cscbattlegrounds.team.BattlegroundsTeam;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;

public class Notices {

    private static final CSCBattlegroundsPlugin plugin = CSCBattlegroundsPlugin.getInstance();

    public static TextComponent teamEliminated(BattlegroundsTeam team) {
        return Component.text("Team ")
                .append(team.getNameComponent())
                .append(Component.text(" has been eliminated!"))
                .color(team.getTextColor());
    }

    public static TextComponent playerEliminated(Player player) {
        if (plugin.getGameManager().getPlayerTeam(player) == null) return Component.text("");
        BattlegroundsTeam team = plugin.getGameManager().getPlayerTeam(player);
        return Component.text(player.getName() + " has been eliminated!").color(team.getTextColor());
    }

    public static TextComponent worldBossSpawned() {
        return Component.text("The world boss has spawned!").color(TextColor.fromHexString("#EB5121"));
    }

    public static TextComponent worldBossKilled() {
        return Component.text("The world boss has been killed!").color(TextColor.fromHexString("#EB5121"));
    }

    public static TextComponent worldBorderShrinking() {
        return Component.text("The world border is shrinking!", TextColor.fromHexString("#EF6461"));
    }

    public static TextComponent worldBorderTime(long timeLeft) {
        String time = String.format("%02d:%02d", timeLeft / 60, timeLeft % 60);
        return Component.text("World Border Duration: " + time, TextColor.fromHexString("#1DACF7"));
    }
}
