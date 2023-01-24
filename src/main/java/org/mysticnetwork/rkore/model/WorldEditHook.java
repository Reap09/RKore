package org.mysticnetwork.rkore.model;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.registry.WorldData;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.mysticnetwork.rkore.RKore;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.visual.VisualizedRegion;

import java.io.File;
import java.io.FileInputStream;

import static com.google.common.base.Preconditions.checkNotNull;

@UtilityClass
public class WorldEditHook {

    public static BukkitWorld adapt(org.bukkit.World world) {
        checkNotNull(world);
        return new BukkitWorld(world);
    }

    @Nullable
    @SneakyThrows
    public Clipboard loadClipboard(@NotNull File schematicFile) {
        BukkitWorld weWorld = adapt(RKore.getInstance().getServer().getWorlds().get(0));
        WorldData worldData = weWorld.getWorldData();

        return ClipboardFormat.SCHEMATIC.getReader(new FileInputStream(schematicFile)).read(worldData);
    }

    @SneakyThrows
    public void pasteClipboard(Clipboard clipboard, Location location, int rotation, boolean withAir) {
        BukkitWorld weWorld = new BukkitWorld(location.getWorld());
        ClipboardHolder holder = new ClipboardHolder(clipboard, weWorld.getWorldData());

        Vector to = new Vector(location.getX(), location.getY(), location.getZ());

        AffineTransform transform = new AffineTransform();
        transform = transform.rotateY(rotation);
        holder.setTransform(transform);

        final EditSession session = WorldEdit.getInstance().getEditSessionFactory().getEditSession(weWorld, -1);
        session.enableQueue();
        Operation op = holder.createPaste(session, session.getWorld().getWorldData())
                .ignoreAirBlocks(!withAir)
                .to(to)
                .build();
        Operations.completeLegacy(op);
        session.flushQueue();
    }

    public Location getMinimumLocation(Clipboard clipboard, Location pasteLocation, double rotation) {
        Vector originalOrigin = clipboard.getOrigin();
        Vector originalMinimumPoint = clipboard.getRegion().getMinimumPoint();

        Vector originalMaximumOffset = originalOrigin.subtract(originalMinimumPoint);

        Vector newOrigin = new Vector(pasteLocation.getBlockX(), pasteLocation.getBlockY(), pasteLocation.getBlockZ());
        Vector newMinimumPoint = newOrigin.subtract(originalMaximumOffset);

        Vector newRotatedMinimumPoint = rotateAround(newMinimumPoint, newOrigin, rotation);

        return new Location(pasteLocation.getWorld(), newRotatedMinimumPoint.getBlockX(), newRotatedMinimumPoint.getBlockY(), newRotatedMinimumPoint.getBlockZ());
    }

    public Location getMaximumLocation(Clipboard clipboard, Location pasteLocation, double rotation) {
        Vector originalOrigin = clipboard.getOrigin();
        Vector originalMaximumPoint = clipboard.getRegion().getMaximumPoint();

        Vector originalMaximumOffset = originalOrigin.subtract(originalMaximumPoint);

        Vector newOrigin = new Vector(pasteLocation.getBlockX(), pasteLocation.getBlockY(), pasteLocation.getBlockZ());
        Vector newMaximumPoint = newOrigin.subtract(originalMaximumOffset);

        Vector newRotatedMaximumPoint = rotateAround(newMaximumPoint, newOrigin, rotation);

        return new Location(pasteLocation.getWorld(), newRotatedMaximumPoint.getBlockX(), newRotatedMaximumPoint.getBlockY(), newRotatedMaximumPoint.getBlockZ());
    }

    private Vector rotateAround(Vector point, Vector center, double angle) {
        angle = Math.toRadians(angle * -1);
        double rotatedX = Math.cos(angle) * (point.getBlockX() - center.getBlockX()) - Math.sin(angle) * (point.getBlockZ() - center.getBlockZ()) + center.getBlockX();
        double rotatedZ = Math.sin(angle) * (point.getBlockX() - center.getBlockX()) + Math.cos(angle) * (point.getBlockZ() - center.getBlockZ()) + center.getBlockZ();

        return new Vector(rotatedX, point.getY(), rotatedZ);
    }

    public VisualizedRegion getClipboardRegion(Clipboard clipboard, Location pasteLocation, double rotation) {
        return new VisualizedRegion(getMinimumLocation(clipboard, pasteLocation, rotation), getMaximumLocation(clipboard, pasteLocation, rotation));
    }


}