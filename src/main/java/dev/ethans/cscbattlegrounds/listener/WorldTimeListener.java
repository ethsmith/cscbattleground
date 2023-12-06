package dev.ethans.cscbattlegrounds.listener;

import dev.ethans.cscbattlegrounds.CSCBattlegroundsPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.TimeSkipEvent;

public class WorldTimeListener implements Listener {

    private static final CSCBattlegroundsPlugin plugin = CSCBattlegroundsPlugin.getInstance();

    private boolean broadcastedAtNight = false;
    private boolean broadcastedAtDay = false;

    private final TextComponent dayTitle = Component.text("The sun is rising...")
            .color(TextColor.fromHexString("#f2f27a"));

    private final TextComponent daySubTitle = Component.text("Monsters are now weaker!")
            .color(TextColor.fromHexString("#f2f27a"));

    private final TextComponent nightTitle = Component.text("The sun is setting...")
            .color(TextColor.fromHexString("#2e4482"));

    private final TextComponent nightSubTitle = Component.text("Monsters are now stronger!")
            .color(TextColor.fromHexString("#2e4482"));

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWorldTimeChange(TimeSkipEvent event) {
        if (event.getSkipReason() != TimeSkipEvent.SkipReason.CUSTOM) return;

        if (event.getWorld().getTime() < 13000) {
            if (broadcastedAtDay) return;
            broadcastedAtNight = false;
//            plugin.getServer().broadcast(dayMessage);
            plugin.getServer().getOnlinePlayers().forEach(player -> {
                player.sendTitlePart(TitlePart.TITLE, dayTitle);
                player.sendTitlePart(TitlePart.SUBTITLE, daySubTitle);
            });
            broadcastedAtDay = true;
            return;
        }

        if (broadcastedAtNight) return;

//        plugin.getServer().broadcast(nightMessage);
        plugin.getServer().getOnlinePlayers().forEach(player -> {
            player.sendTitlePart(TitlePart.TITLE, nightTitle);
            player.sendTitlePart(TitlePart.SUBTITLE, nightSubTitle);
        });
        broadcastedAtDay = false;
        broadcastedAtNight = true;
    }
}
