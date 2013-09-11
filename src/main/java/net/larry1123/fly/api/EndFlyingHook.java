package net.larry1123.fly.api;

import net.canarymod.Canary;
import net.canarymod.api.GameMode;
import net.canarymod.api.entity.living.humanoid.HumanCapabilities;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.position.Location;
import net.canarymod.chat.MessageReceiver;
import net.canarymod.hook.CancelableHook;
import net.canarymod.hook.player.TeleportHook.TeleportCause;
import net.larry1123.fly.permissions.PermissionsMan;
import net.larry1123.fly.task.FlyTime;
import net.larry1123.util.chat.FontTools;

public class EndFlyingHook extends CancelableHook {

    private final Player player;
    private final MessageReceiver caller;
    private String callerMessage = "";
    private String playerMessage = "";

    public EndFlyingHook(Player player) {
        this.player = player;
        this.caller = Canary.getServer();
    }

    public EndFlyingHook(Player player, MessageReceiver caller) {
        this.player = player;
        this.caller = caller;
    }

    /**
     * Get the target player
     *
     * @return Player to fly
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Who is setting the Player into flight
     *
     * @return Issuer
     */
    public MessageReceiver getCaller() {
        return this.caller;
    }

    /**
     * Set the message to send to the player
     *
     * @param string The message to send
     */
    public void setPlayerMessage(String string) {
        if (string != null) {
            this.playerMessage = string;
        }
    }

    /**
     * Set the message to send to the caller
     *
     * @param string The message to send
     */
    public void setCallerMessage(String string) {
        if (string != null) {
            this.callerMessage = string;
        }
    }

    /**
     * Get the currently stored message that should be sent to the Player
     *
     * @return Message for the Player
     */
    public String getPlayerMessage() {
        return this.playerMessage;
    }

    /**
     * Get the currently stored message that should be sent to the Caller
     *
     * @return Message for the Caller
     */
    public String getCallerMessage() {
        return this.callerMessage;
    }

    /**
     * Sends stored Messages to the Player and Caller
     * Will ensure there is something to send to and that there is something to send
     */
    public void sendMessages() {
        if (player != null) {
            if (!playerMessage.equals("")) {
                player.message(this.playerMessage);
            }
            if (caller != player && !callerMessage.equals("")) {
                caller.message(this.callerMessage);
            }
        } else {
            if (caller != null && !callerMessage.equals("")) {
                caller.message(callerMessage);
            }
        }
    }

    public void execute() {
        if (caller == null) {
            return;
        }
        if (player == null) {
            setCallerMessage(FontTools.RED + "Player missing!");
            return;
        }
        if (!PermissionsMan.allowFlyOther(caller)) {
            if (caller != player) {
                setCallerMessage("You do not have Permission to stop others from flying!");
                return;
            }
        }
        // SetUp Done
        HumanCapabilities capabilities = player.getCapabilities();
        if (capabilities.mayFly() && !player.getMode().equals(GameMode.CREATIVE)) {
            capabilities.setFlying(false);
            capabilities.setMayFly(false);
            capabilities.setFlySpeed((float) 0.05);
            player.updateCapabilities();
            Location loc = player.getLocation();
            loc.setY(player.getWorld().getHighestBlockAt(loc.getBlockX(), loc.getBlockZ()) + 1);
            player.teleportTo(loc, TeleportCause.MOVEMENT);
            String message = " may no longer fly";
            setPlayerMessage("You" + message);
            if (caller != player) {
                setCallerMessage(player.getName() + message);
            }
            FlyTime.removePlayer(getPlayer());
        }
    }

}
