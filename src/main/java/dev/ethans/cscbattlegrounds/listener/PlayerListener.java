package dev.ethans.cscbattlegrounds.listener;

import dev.ethans.cscbattlegrounds.CSCBattlegroundsPlugin;
import dev.ethans.cscbattlegrounds.notices.NoticeSounds;
import dev.ethans.cscbattlegrounds.notices.Notices;
import dev.ethans.cscbattlegrounds.scoreboard.BattlegroundsScoreboard;
import dev.ethans.cscbattlegrounds.team.BattlegroundsTeam;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {

    private static final CSCBattlegroundsPlugin plugin = CSCBattlegroundsPlugin.getInstance();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        if (plugin.getGameManager().getPlayerTeam(player) == null) return;
        BattlegroundsTeam team = plugin.getGameManager().getPlayerTeam(player);

        if (team.getDeadPlayers().size() == team.getPlayers().size()) {
            NoticeSounds.playEliminatedSound();
            plugin.getServer().broadcast(Notices.teamEliminated(team));
            return;
        }

        NoticeSounds.playEliminatedSound();
        plugin.getServer().broadcast(Notices.playerEliminated(player));
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
//        new BattlegroundsScoreboard(player);
    }
}
