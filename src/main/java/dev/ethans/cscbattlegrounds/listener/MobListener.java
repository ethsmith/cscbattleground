package dev.ethans.cscbattlegrounds.listener;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;

public class MobListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void removeSunlightDamage(EntitySpawnEvent event) {
        if (event.getEntity().getType() == EntityType.ZOMBIE) {
            Zombie zombie = (Zombie) event.getEntity();
            zombie.setShouldBurnInDay(false);
            return;
        }

        if (event.getEntity().getType() == EntityType.SKELETON) {
            Skeleton skeleton = (Skeleton) event.getEntity();
            skeleton.setShouldBurnInDay(false);
            return;
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void modifyDamageAtNight(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) return;

        if (event.getDamager() instanceof Player damager && !(event.getEntity() instanceof Player)) {
            World world = damager.getWorld();

            if (world.getTime() < 13000) return;

            double damage = event.getDamage() * 0.75;
            event.setDamage(damage);
            return;
        }

        if (!(event.getDamager() instanceof Player) && event.getEntity() instanceof Player) {
            Entity damager = event.getDamager();
            World world = damager.getWorld();

            if (world.getTime() < 13000) return;

            double damage = event.getDamage() * 1.5;
            event.setDamage(damage);
            return;
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMobDeath(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Monster)) return;
        Entity entity = event.getEntity();
        event.getDrops().clear();

        // 50% chance to spawn a chest on mob death
        if (Math.random() < 0.5)
            entity.getLocation().add(0, 1, 0).getBlock().setType(Material.CHEST);
    }
}
