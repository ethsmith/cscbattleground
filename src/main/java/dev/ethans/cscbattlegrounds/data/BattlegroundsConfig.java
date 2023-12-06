package dev.ethans.cscbattlegrounds.data;

import dev.ethans.cscbattlegrounds.CSCBattlegroundsPlugin;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

public class BattlegroundsConfig {

    private static final CSCBattlegroundsPlugin plugin = CSCBattlegroundsPlugin.getInstance();

    @Getter
    private final FileConfiguration config;

    public BattlegroundsConfig() {
        plugin.saveDefaultConfig();
        this.config = plugin.getConfig();
    }
}
