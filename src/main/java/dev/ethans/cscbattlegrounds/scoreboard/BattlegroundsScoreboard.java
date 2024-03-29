package dev.ethans.cscbattlegrounds.scoreboard;

import dev.ethans.cscbattlegrounds.CSCBattlegroundsPlugin;
import dev.ethans.cscbattlegrounds.team.BattlegroundsTeam;
import dev.ethans.cscbattlegrounds.util.StringUtil;
import dev.ethans.cscbattlegrounds.util.Styles;
import fr.mrmicky.fastboard.adventure.FastBoard;
import lombok.Data;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
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

    private final static TextComponent title = Component.text("CSC", Styles.YELLOW, TextDecoration.BOLD)
            .append(Component.text(" Battleground", Styles.PRIMARY_BLUE, TextDecoration.BOLD));


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
                    Component.text("Waiting for players...").color(Styles.PRIMARY_BLUE)
            );
            return;
        }

        List<Component> infoLines = new ArrayList<>();
        infoLines.add(Component.text(""));
        infoLines.add(Component.text("Teammates:", Styles.SECONDARY_BLUE));

        team.getPlayers().forEach(uuid -> {
            OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(uuid);
            Player teamMember = offlinePlayer.getPlayer();

            String playerName = teamMember != null ? teamMember.getName() + " ✔" : offlinePlayer.getName() + " ✘";
            TextColor textColor = teamMember != null ? Styles.PRIMARY_BLUE : Styles.PRIMARY_RED;

            infoLines.add(Component.text(playerName, textColor));
        });

        infoLines.add(Component.text(""));
        infoLines.add(Component.text("Team Color: ", Styles.SECONDARY_BLUE));
        infoLines.add(Component.text(StringUtil.firstLetterUppercase(team.getColor()), team.getTextColor()));

        board.updateLines(infoLines);
    }
}
