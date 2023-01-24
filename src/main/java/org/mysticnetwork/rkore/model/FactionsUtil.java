package org.mysticnetwork.rkore.model;

import com.massivecraft.factions.*;
import com.massivecraft.factions.struct.Role;
import com.massivecraft.factions.zcore.fperms.Access;
import com.massivecraft.factions.zcore.fperms.PermissableAction;
import com.massivecraft.factions.zcore.util.TL;
import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

@UtilityClass
public class FactionsUtil {

    public static boolean canSpawnSchematic(Player player, int rotation, Location location,Schematic schematic) {
        for (Block block : schematic.toRegion(location, rotation).getBlocks()) {

            if (!canSpawnSchematic(player, block.getLocation(), "build", false))
                return false;
        }
        return true;
    }

    public static boolean canSpawnSchematic(Player player, Location location,Schematic schematic) {
        for (Block block : schematic.toRegion(location, player).getBlocks()) {

            if (!canSpawnSchematic(player, block.getLocation(), "build", false))
                return false;
        }
        return true;
    }

    public static boolean canSpawnSchematic(Player player, Location location, String action, boolean justCheck) {
        if (Conf.playersWhoBypassAllProtection.contains(player.getName())) return true;

        FPlayer me = FPlayers.getInstance().getById(player.getUniqueId().toString());
        if (me.isAdminBypassing()) return true;

        FLocation loc = new FLocation(location);
        Faction otherFaction = Board.getInstance().getFactionAt(loc);
        Faction myFaction = me.getFaction();

        //If is a Wilderness can't spawn
        if (otherFaction.isWilderness()) {
            return false;
        }

        //If is a safe Zone
        if (otherFaction.isSafeZone()) {
            return false;
        }

        //If is a war zone
        if (otherFaction.isWarZone()) {
            return false;
        }

        // If the faction target is not my own
        if (!otherFaction.getId().equals(myFaction.getId())) {
            return false;
        }

        // If the faction target is my own
        if (otherFaction.getId().equals(myFaction.getId())) {
            boolean pain = !justCheck && myFaction.getAccess(me, PermissableAction.PAIN_BUILD) == Access.ALLOW;
            return CheckActionState(myFaction, loc, me, PermissableAction.fromString(action), pain);
        }
        return false;
    }

    private static boolean CheckActionState(Faction target, FLocation location, FPlayer me, PermissableAction action, boolean pain) {
        if (Conf.ownedAreasEnabled && target.doesLocationHaveOwnersSet(location) && !target.playerHasOwnershipRights(me, location)) {
            // If pain should be applied
            if (pain && Conf.ownedAreaPainBuild)
                me.msg(TL.ACTIONS_OWNEDTERRITORYPAINDENY.toString().replace("{action}", action.toString()).replace("{faction}", target.getOwnerListString(location)));
            if (Conf.ownedAreaDenyBuild && pain) return false;
            else if (Conf.ownedAreaDenyBuild) {
                me.msg(TL.ACTIONS_NOPERMISSION.toString().replace("{faction}", target.getTag(me.getFaction())).replace("{action}", action.toString()));
                return false;
            }
        }
        return CheckPlayerAccess(me.getPlayer(), me, location, target, target.getAccess(me, action), action, pain);
    }
    private static boolean CheckPlayerAccess(Player player, FPlayer me, FLocation loc, Faction myFaction, Access access, PermissableAction action, boolean shouldHurt) {
        boolean landOwned = (myFaction.doesLocationHaveOwnersSet(loc) && !myFaction.getOwnerList(loc).isEmpty());
        if ((landOwned && myFaction.getOwnerListString(loc).contains(player.getName())) || (me.getRole() == Role.LEADER && me.getFactionId().equals(myFaction.getId())))
            return true;
        else if (landOwned && !myFaction.getOwnerListString(loc).contains(player.getName())) {
            me.msg(TL.ACTIONS_OWNEDTERRITORYDENY.toString().replace("{owners}", myFaction.getOwnerListString(loc)));
            if (shouldHurt) {
                player.damage(Conf.actionDeniedPainAmount);
                me.msg(TL.ACTIONS_NOPERMISSIONPAIN.toString().replace("{action}", action.toString()).replace("{faction}", Board.getInstance().getFactionAt(loc).getTag(myFaction)));
            }
            return false;
        } else if (!landOwned && access == Access.DENY) { // If land is not owned but access is set to DENY anyway
            if (shouldHurt) {
                player.damage(Conf.actionDeniedPainAmount);
                if ((Board.getInstance().getFactionAt(loc).getTag(myFaction)) != null)
                    me.msg(TL.ACTIONS_NOPERMISSIONPAIN.toString().replace("{action}", action.toString()).replace("{faction}", Board.getInstance().getFactionAt(loc).getTag(myFaction)));
            }
            if (myFaction.getTag(me.getFaction()) != null && action != null)
                me.msg(TL.ACTIONS_NOPERMISSION.toString().replace("{faction}", myFaction.getTag(me.getFaction())).replace("{action}", action.toString()));
            return false;
        } else if (access == Access.ALLOW) return true;
        me.msg(TL.ACTIONS_NOPERMISSION.toString().replace("{faction}", myFaction.getTag(me.getFaction())).replace("{action}", action.toString()));
        return false;
    }
}
