package dev.ethans.cscbattlegrounds.state.ingame.listener;

import dev.ethans.cscbattlegrounds.CSCBattlegroundsPlugin;
import dev.ethans.cscbattlegrounds.notices.NoticeSounds;
import dev.ethans.cscbattlegrounds.notices.Notices;
import dev.ethans.cscbattlegrounds.team.BattlegroundsTeam;
import dev.ethans.cscbattlegrounds.util.Strings;
import dev.ethans.cscbattlegrounds.util.Styles;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerListener implements Listener {

    private static final CSCBattlegroundsPlugin plugin = CSCBattlegroundsPlugin.getInstance();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        if (plugin.getGameManager().getPlayerTeam(player) == null) return;
        BattlegroundsTeam team = plugin.getGameManager().getPlayerTeam(player);

        if (team.getDeadPlayers().size() == team.getPlayers().size()) {
            NoticeSounds.playEliminatedSound();
            plugin.getServer().broadcast(Strings.TEAM_ELIMINATED(team));
            return;
        }

        NoticeSounds.playEliminatedSound();
        plugin.getServer().broadcast(Strings.PLAYER_ELIMINATED(player));
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!event.hasExplicitlyChangedPosition()) return;
        Player player = event.getPlayer();
        player.sendActionBar(Strings.YOUR_COORDS(player));
    }

    @EventHandler
    public void onPlayerDamagePlayer(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player damager)) return;
        if (!(event.getEntity() instanceof Player damaged)) return;

        BattlegroundsTeam damagerTeam = plugin.getGameManager().getPlayerTeam(damager);
        BattlegroundsTeam damagedTeam = plugin.getGameManager().getPlayerTeam(damaged);

        if (damagedTeam == null) return;
        if (damagerTeam == null) return;

        if (damagerTeam.equals(damagedTeam)) {
            event.setCancelled(true);
            damager.sendMessage(Strings.YOU_CANT_DAMAGE_YOUR_OWN_TEAM);
        }
    }
}
