package dev.ethans.cscbattlegrounds;

import dev.ethans.cscbattlegrounds.border.ShrinkingWorldBorder;
import dev.ethans.cscbattlegrounds.chests.InstancedChestListener;
import dev.ethans.cscbattlegrounds.command.ChestSpawnCommand;
import dev.ethans.cscbattlegrounds.command.PlayerSpawnCommand;
import dev.ethans.cscbattlegrounds.data.BattlegroundsConfig;
import dev.ethans.cscbattlegrounds.data.BattlegroundsSpawns;
import dev.ethans.cscbattlegrounds.game.GameManager;
import dev.ethans.cscbattlegrounds.listener.MobListener;
import dev.ethans.cscbattlegrounds.listener.WorldTimeListener;
import dev.ethans.cscbattlegrounds.state.PopulateStage;
import dev.ethans.cscbattlegrounds.state.ingame.InGameState;
import dev.ethans.cscbattlegrounds.state.PregameState;
import dev.ethans.cscbattlegrounds.state.base.ScheduledStateSeries;
import dev.ethans.cscbattlegrounds.time.DayNightCycle;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class CSCBattlegroundsPlugin extends JavaPlugin {

    @Getter
    private static CSCBattlegroundsPlugin instance;

    @Getter
    private GameManager gameManager;

    @Getter
    private BattlegroundsConfig battlegroundsConfig;

    @Getter
    private DayNightCycle dayNightCycle;

    @Getter
    private ShrinkingWorldBorder shrinkingWorldBorder;

    private ScheduledStateSeries stateSeries;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        battlegroundsConfig = new BattlegroundsConfig();

        gameManager = new GameManager();
        gameManager.loadTeams();

        try { BattlegroundsSpawns.loadBattlegroundSpawns(); }

        catch (IOException e) {
            e.printStackTrace();
        }

        try { BattlegroundsSpawns.loadChestSpawns(); }

        catch (IOException e) {
            e.printStackTrace();
        }

        getCommand("playerspawn").setExecutor(new PlayerSpawnCommand());
        getCommand("chestspawn").setExecutor(new ChestSpawnCommand());

        getServer().getPluginManager().registerEvents(new WorldTimeListener(), this);
        getServer().getPluginManager().registerEvents(new MobListener(), this);
        getServer().getPluginManager().registerEvents(new InstancedChestListener(), this);
//        getServer().getScheduler().runTaskLater(this, () -> dayNightCycle = new DayNightCycle(), 15 * 20);

        dayNightCycle = new DayNightCycle();

        stateSeries = new ScheduledStateSeries(this);
        stateSeries.add(new PopulateStage(this));
        stateSeries.add(new PregameState(this));
        stateSeries.add(new InGameState(this));
        stateSeries.start();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        gameManager.saveTeams();

        stateSeries.end();

        BattlegroundsSpawns.deletePlayerSpawnFiles();
        BattlegroundsSpawns.saveBattlegroundSpawns();
        BattlegroundsSpawns.deleteChestSpawnFiles();
        BattlegroundsSpawns.saveChestSpawns();
    }
}
