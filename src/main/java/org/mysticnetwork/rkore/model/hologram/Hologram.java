package org.mysticnetwork.rkore.model.hologram;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.model.Tuple;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.CompParticle;
import org.mineacademy.fo.remain.Remain;

import lombok.Getter;
import lombok.Setter;


public class Hologram {

    @Getter
    @Setter
    private static double loreLineHeight = 0.26D;

    @Getter
    private static Set<Hologram> registeredItems = new HashSet<>();

    private boolean isDeSpawned = false;

    private static volatile BukkitTask tickingTask = null;

    @Getter
    private final List<ArmorStand> loreEntities = new ArrayList<>();

    private final Location lastTeleportLocation;

    @Getter
    private final List<String> loreLines = new ArrayList<>();

    @Getter
    private final List<Tuple<CompParticle, Object>> particles = new ArrayList<>();

    @Getter
    private Entity entity;

    private Location pendingTeleport = null;

    public Hologram(Location spawnLocation) {
        this.lastTeleportLocation = spawnLocation.clone();

        registeredItems.add(this);

        onReload();
    }

    @Deprecated
    public static void onReload() {
        if (tickingTask != null)
            tickingTask.cancel();

        tickingTask = scheduleTickingTask();
    }

    private static BukkitTask scheduleTickingTask() {
        return Common.runTimer(1, () -> {

            for (final Iterator<Hologram> it = registeredItems.iterator(); it.hasNext(); ) {
                final Hologram model = it.next();

                if (model.isSpawned())
                    if (!model.getEntity().isValid() || model.getEntity().isDead()) {
                        model.removeLore();
                        model.getEntity().remove();

                        it.remove();
                    } else
                        model.tick();
            }
        });
    }

    public Hologram spawn(Location location) {
        Valid.checkBoolean(!this.isSpawned(), this + " is already spawned!");

        this.entity = this.createEntity(location.clone());
        Valid.checkNotNull(this.entity, "Failed to spawn entity from " + this);

        this.drawLore(this.lastTeleportLocation);

        return this;
    }

    protected Entity createEntity(Location location) {
        final Bat bat = (Bat) location.getWorld().spawnEntity(location, EntityType.BAT);
        bat.setOp(true);
        bat.setAwake(true);
        bat.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10000, 10000, true, false));
        bat.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 10000, 1, true, false));
        bat.remove();
        return bat;
    }

    public static void temporaryDeSpawn(List<Hologram> holograms) {
        for (Hologram hologram : holograms) hologram.temporaryDeSpawn(hologram);
    }

    public void temporaryDeSpawn(Hologram hologram) {
        hologram.removeLore();
        hologram.isDeSpawned = true;
    }

    public static void respawn(List<Hologram> holograms) {
        for (Hologram hologram : holograms)
            hologram.respawn(hologram);
    }


    public void respawn(Hologram hologram) {
        drawLore(getLocation());
        hologram.isDeSpawned = false;
    }

    public void drawLore(Location location) {
        removeLore();
        if (this.loreLines.isEmpty())
            return;

        if (this.entity instanceof ArmorStand && ((ArmorStand) this.entity).isSmall())
            location = location.clone();

        for (final String loreLine : this.loreLines) {
            final ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
            location = location.add(0.0, -0.15, 0);
            armorStand.setGravity(false);
            armorStand.setSmall(false);
            armorStand.setVisible(false);
            armorStand.setRemoveWhenFarAway(true);
            Remain.setCustomName(armorStand, loreLine);

            location = location.subtract(0, loreLineHeight, 0);

            this.loreEntities.add(armorStand);
        }
    }

    public void updateLore(List<String> lore) {
        if (isDeSpawned) return;

        if (loreEntities.isEmpty()) drawLore(getLocation());
        for (final ArmorStand loreEntity : this.loreEntities) {
            Remain.setCustomName(loreEntity, lore.get(loreEntities.indexOf(loreEntity)));
        }
    }

    private void tick() {
        if (this.pendingTeleport != null) {
            this.entity.teleport(this.pendingTeleport);

            for (final ArmorStand loreEntity : this.loreEntities)
                loreEntity.teleport(this.pendingTeleport);

            this.pendingTeleport = null;
            return;
        }

        this.onTick();

        for (final Tuple<CompParticle, Object> tuple : this.particles) {
            final CompParticle particle = tuple.getKey();
            final Object extra = tuple.getValue();

            if (extra instanceof CompMaterial)
                particle.spawn(this.getLocation(), (CompMaterial) extra);

            else if (extra instanceof Double)
                particle.spawn(this.getLocation(), (double) extra);
        }
    }

    protected void onTick() {
    }

    public final boolean isSpawned() {
        return this.entity != null;
    }

    public final void removeLore() {
        this.loreEntities.forEach(ArmorStand::remove);
    }

    public final Hologram setLore(List<String> lore) {
        this.loreLines.clear();
        this.loreLines.addAll(lore);

        return this;
    }

    public final void addParticleEffect(CompParticle particle) {
        this.addParticleEffect(particle, null);
    }

    public final void addParticleEffect(CompParticle particle, CompMaterial data) {
        this.particles.add(new Tuple<>(particle, data));
    }

    public final Location getLocation() {
        this.checkSpawned("getLocation");

        return this.entity.getLocation();
    }

    public final Location getLastTeleportLocation() {
        return this.lastTeleportLocation.clone();
    }

    public final void teleport(Location location) {
        Valid.checkBoolean(this.pendingTeleport == null, this + " is already pending teleport to " + this.pendingTeleport);
        this.checkSpawned("teleport");

        this.lastTeleportLocation.setX(location.getY());
        this.lastTeleportLocation.setY(location.getY());
        this.lastTeleportLocation.setZ(location.getZ());

        this.pendingTeleport = location;
    }

    public final void remove() {
        this.removeLore();

        if (this.entity != null)
            this.entity.remove();

        registeredItems.remove(this);
    }

    private void checkSpawned(String method) {
        Valid.checkBoolean(this.isSpawned(), this + " is not spawned, cannot call " + method + "!");
    }

    @Override
    public String toString() {
        return "ArmorStandItem{spawnLocation=" + Common.shortLocation(this.lastTeleportLocation) + ", spawned=" + this.isSpawned() + "}";
    }

    public static void deleteAll() {

        for (final Iterator<Hologram> it = registeredItems.iterator(); it.hasNext(); ) {
            final Hologram item = it.next();

            if (item.isSpawned())
                item.getEntity().remove();

            item.removeLore();
            it.remove();
        }
    }
}