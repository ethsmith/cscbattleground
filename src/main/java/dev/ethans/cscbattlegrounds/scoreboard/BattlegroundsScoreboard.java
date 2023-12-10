package dev.ethans.cscbattlegrounds.scoreboard;

import dev.ethans.cscbattlegrounds.CSCBattlegroundsPlugin;
import dev.ethans.cscbattlegrounds.team.BattlegroundsTeam;
import dev.ethans.cscbattlegrounds.util.StringUtil;
import fr.mrmicky.fastboard.adventure.FastBoard;
import lombok.Data;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Data
public class BattlegroundsScoreboard {

    private static final CSCBattlegroundsPlugin plugin = CSCBattlegroundsPlugin.getInstance();

    private final FastBoard board;

    private final static TextComponent title = Component.text("CSC", TextColor.fromHexString("#e3c913"), TextDecoration.BOLD)
            .append(Component.text(" Battleground", TextColor.fromHexString("#1dacf7"), TextDecoration.BOLD));


    public BattlegroundsScoreboard(Player player) {
        this.board = new FastBoard(player);
        board.updateTitle(title);
        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> updateLines(player), 0L, 20L);
    }

    private void updateLines(Player player) {
        BattlegroundsTeam team = plugin.getGameManager().getPlayerTeam(player);

        if (team == null) {
            board.updateLines(
                    Component.text(""),
                    Component.text("Waiting for players...").color(TextColor.fromHexString("#1dacf7"))
            );
            return;
        }

        List<Component> teamMembers = new ArrayList<>();
        teamMembers.add(Component.text(""));
        teamMembers.add(Component.text("Teammates:", TextColor.fromHexString("#3454D1")));

        team.getPlayers().forEach(uuid -> {
            OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(uuid);
            Player teamMember = offlinePlayer.getPlayer();

            String playerName = teamMember != null ? teamMember.getName() + " ✔" : offlinePlayer.getName() + " ✘";
            TextColor textColor = teamMember != null ? TextColor.fromHexString("#1dacf7") : TextColor.fromHexString("#EF6461");

            teamMembers.add(Component.text(playerName, textColor));
        });

        teamMembers.add(Component.text(""));
        teamMembers.add(Component.text("Team Color: ", TextColor.fromHexString("#3454D1")));
        teamMembers.add(Component.text(StringUtil.firstLetterUppercase(team.getColor()), team.getTextColor()));

        board.updateLines(teamMembers);
    }
}
