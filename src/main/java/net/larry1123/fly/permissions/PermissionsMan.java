package net.larry1123.fly.permissions;

import net.canarymod.api.Server;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.blocks.CommandBlock;
import net.canarymod.chat.MessageReceiver;
import net.canarymod.user.Group;
import net.larry1123.fly.config.ConfigMan;
import net.larry1123.util.logger.EELogger;

public class PermissionsMan {

    public static final String commandNode = "net.larry1123.fly.command";
    public static final String otherNode = "net.larry1123.fly.other";
    public static final String speedNode = "net.larry1123.fly.speed";
    public static final String speedPartNode = speedNode + ".0.";
    public static final String maxNode = "net.larry1123.fly.speed.max";

    private static float maxSpeed = ConfigMan.getConfig().getMainConfig().getMaxSpeed();

    public static boolean allowCommand(MessageReceiver caller) {
        return caller.hasPermission(commandNode);
    }

    public static boolean allowFlyOther(MessageReceiver caller) {
        return caller.hasPermission(otherNode);
    }

    public static boolean canFly(Player player) {
        if (hasMaxFlySpeed(player)) {
            return true;
        }
        if (player.hasPermission(speedNode)) {
            for (String perm : player.getPermissionProvider().getPermissionsAsStringList()) {
                if (perm.startsWith(speedPartNode)) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    public static boolean hasMaxFlySpeed(Player player) {
        return player.hasPermission(maxNode);
    }

    public static boolean hasMaxFlySpeed(Group group) {
        return group.hasPermission(maxNode);
    }

    public static float getMaxSpeedByCaller(MessageReceiver caller) {
        if (caller instanceof Server || caller instanceof CommandBlock) {
            return ConfigMan.getConfig().getMainConfig().getMaxSpeed();
        } else if (caller instanceof Player) {
            Player player = (Player) caller;
            return getMaxSpeedForPlayer(player);
        } else {
            // Something else is on the server will see about how to handle this if needed
            return 0;
        }
    }

    public static float getMaxSpeedByGroup(Group group) {
        return getMaxSpeedForGroup(group);
    }

    private static float getMaxSpeedForPlayer(Player player) {
        float speed = 0;
        if (hasMaxFlySpeed(player)) {
            speed = maxSpeed;
        } else {
            speed = getMaxSpeedForGroup(player.getGroup());
            if (player.hasPermission(speedNode)) {
                for (String perm : player.getPermissionProvider().getPermissionsAsStringList()) {
                    if (perm.startsWith(speedPartNode)) {
                        try {
                            speed = getSpeedFromPermStirng(perm);
                        } catch (NumberFormatException e) {
                            EELogger.getLogger("Fly").logDerp("Player " + player.getName() + "'s Permissions are Malformed!");
                            speed = (float) 0.05;
                            if (speed > maxSpeed) {
                                speed = maxSpeed;
                            }
                            break;
                        }
                    }
                }
            }
        }
        return speed;
    }

    private static float getMaxSpeedForGroup(Group group) {
        float speed = 0;
        if (hasMaxFlySpeed(group)) {
            speed = maxSpeed;
        } else {
            if (group.hasPermission(speedNode)) {
                for (String perm : group.getPermissionProvider().getPermissionsAsStringList()) {
                    if (perm.startsWith(speedPartNode)) {
                        try {
                            speed = getSpeedFromPermStirng(perm);
                        } catch (NumberFormatException e) {
                            // Defult to 0.05 because they can use command some what
                            EELogger.getLogger("Fly").logDerp("Group " + group.getName() + "'s Permissions are Malformed!");
                            speed = (float) 0.05;
                            if (speed > maxSpeed) {
                                speed = maxSpeed;
                            }
                            break;
                        }
                    }
                }
            }
        }
        return speed;
    }

    private static float getSpeedFromPermStirng(String perm) throws NumberFormatException {
        float speed = Float.parseFloat("0." + perm.substring(perm.lastIndexOf(".") + 1));
        if (speed > maxSpeed) {
            speed = maxSpeed;
        }
        return speed;
    }

}
