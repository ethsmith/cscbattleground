package dev.ethans.cscbattlegrounds.state.ingame.listener;

import dev.ethans.cscbattlegrounds.CSCBattlegroundsPlugin;
import dev.ethans.cscbattlegrounds.notices.NoticeSounds;
import dev.ethans.cscbattlegrounds.notices.Notices;
import dev.ethans.cscbattlegrounds.team.BattlegroundsTeam;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
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
            plugin.getServer().broadcast(Notices.teamEliminated(team));
            return;
        }

        NoticeSounds.playEliminatedSound();
        plugin.getServer().broadcast(Notices.playerEliminated(player));
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!event.hasExplicitlyChangedPosition()) return;
        Player player = event.getPlayer();
        player.sendActionBar(Component.text("Your Coords: ", TextColor.fromHexString("#1DACF7"))
                .append(Component.text("X: " + String.format("%.2f", player.getLocation().getX())
                                + " Y: " + String.format("%.2f", player.getLocation().getY())
                                + " Z: " + String.format("%.2f", player.getLocation().getZ()),
                        TextColor.fromHexString("#EFE9F4"))));
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
            damager.sendMessage(Component.text("You cannot damage your own team!", TextColor.fromHexString("#EF6461")));
        }
    }
}
