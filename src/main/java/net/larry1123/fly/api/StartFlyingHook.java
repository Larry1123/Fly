package net.larry1123.fly.api;

import net.canarymod.Canary;
import net.canarymod.api.GameMode;
import net.canarymod.api.entity.living.humanoid.HumanCapabilities;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.chat.MessageReceiver;
import net.canarymod.hook.CancelableHook;
import net.larry1123.fly.config.ConfigMan;
import net.larry1123.fly.permissions.PermissionsMan;
import net.larry1123.fly.task.FlyTime;
import net.larry1123.util.chat.FontTools;

public class StartFlyingHook extends CancelableHook {

    private static float maxSpeed = ConfigMan.getConfig().getMainConfig().getMaxSpeed();

    private final Player player;
    private final MessageReceiver caller;
    private float speed;
    private long time;
    private String callerMessage = "";
    private String playerMessage = "";

    public StartFlyingHook(Player player) {
        this.player = player;
        this.caller = Canary.getServer();
        this.speed = (float) 0.05;
        this.time = 0;
    }

    public StartFlyingHook(Player player, long time) {
        this.player = player;
        this.caller = Canary.getServer();
        this.speed = (float) 0.05;
        this.time = time;
    }

    public StartFlyingHook(Player player, MessageReceiver caller) {
        this.player = player;
        this.caller = caller;
        this.speed = (float) 0.05;
        this.time = 0;
    }

    public StartFlyingHook(Player player, MessageReceiver caller, long time) {
        this.player = player;
        this.caller = caller;
        this.speed = (float) 0.05;
        this.time = time;
    }

    public StartFlyingHook(Player player, float speed) {
        this.player = player;
        this.caller = Canary.getServer();
        this.speed = speed;
        this.time = 0;
    }

    public StartFlyingHook(Player player, float speed, long time) {
        this.player = player;
        this.caller = Canary.getServer();
        this.speed = speed;
        this.time = time;
    }

    public StartFlyingHook(Player player, float speed, MessageReceiver caller) {
        this.player = player;
        this.caller = caller;
        this.speed = speed;
        this.time = 0;
    }

    public StartFlyingHook(Player player, float speed, MessageReceiver caller, long time) {
        this.player = player;
        this.caller = caller;
        this.speed = speed;
        this.time = time;
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
     * Get the speed to be set on the Player
     *
     * @return How fast to set the user flying
     */
    public float getSpeed() {
        return this.speed;
    }

    /**
     * Set how fast for the Player to fly
     * This Method will keep in check the max fly speed!
     *
     * @param speed How fast to fly
     */
    public void setSpeed(float speed) {
        if (speed > maxSpeed) {
            speed = maxSpeed;
        }
        this.speed = speed;
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

    /**
     * TODO
     */
    public void execute() {
        if (caller == null) {
            return;
        }
        if (player == null) {
            setCallerMessage(FontTools.RED + "Player missing!");
            return;
        }
        if (caller != player) {
            if (!PermissionsMan.allowFlyOther(caller)) {
                setCallerMessage(FontTools.RED + "You do not have Permission to allow others to fly!");
                return;
            }
        }
        if (!PermissionsMan.canFly(player)) {
            String message = " not allowed to fly!";
            if (caller != player) {
                setCallerMessage(player.getName() + " is" + message);
            } else {
                setPlayerMessage("You are" + message);
            }
        }
        // Limit to at lest maxSpeed for Safety
        if (speed > maxSpeed) {
            speed = maxSpeed;
        }
        // Limit speed to the max the player can go.
        float pspeed = PermissionsMan.getMaxSpeedByCaller(player);
        if (speed > pspeed) {
            speed = pspeed;
        }
        // SetUp Done
        HumanCapabilities capabilities = player.getCapabilities();
        if (!capabilities.mayFly() && !player.getMode().equals(GameMode.CREATIVE)) {
            // Starts flying if not Flying and not in Creative
            capabilities.setMayFly(true);
            capabilities.setFlying(true);
            capabilities.setFlySpeed(speed);
            player.updateCapabilities();
            String message;
            if (speed > 0.5) {
                message = " may fly now, with the speed of " + FontTools.RED + capabilities.getFlySpeed();
            } else {
                message = " may fly now, with the speed of " + capabilities.getFlySpeed();
            }
            setPlayerMessage("You" + message);
            if (caller != player) {
                setCallerMessage(player.getName() + message);
            }
            FlyTime.addPlayerFor(getPlayer(), time);
        } else if (player.getMode().equals(GameMode.CREATIVE)) {
            // Sets the Speed a Creative Player is flying at
            capabilities.setFlySpeed(speed);
            player.updateCapabilities();
            String message;
            if (speed > 0.5) {
                message = " flight speed is now " + FontTools.RED + capabilities.getFlySpeed();
            } else {
                message = " flight speed is now " + capabilities.getFlySpeed();
            }
            setPlayerMessage("Your" + message);
            if (caller != player) {
                setCallerMessage(player.getName() + message);
            }
        }
    }

}
