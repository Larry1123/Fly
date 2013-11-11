package net.larry1123.fly.task;

import net.canarymod.Canary;
import net.canarymod.api.entity.living.humanoid.HumanCapabilities;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.position.Location;
import net.canarymod.hook.player.TeleportHook;
import net.canarymod.tasks.ServerTask;
import net.canarymod.tasks.ServerTaskManager;
import net.canarymod.tasks.TaskOwner;
import net.larry1123.util.api.time.StringTime;

import java.util.HashMap;

import static net.larry1123.fly.Fly.getPlugin;

public class FlyTime extends ServerTask {

    private static HashMap<String, Long> playerFlyTime = new HashMap<String, Long>();

    public static void addPlayerFor(Player player, long time) {
        if (time == 0) return;
        if (player != null) {
            playerFlyTime.put(player.getName(), System.currentTimeMillis() + time);
        }
    }

    public static void addPlayerFor(Player player, String stime) {
        long time = StringTime.millisecondsFromString(stime);
        addPlayerFor(player, time);
    }

    public static void removePlayer(Player player) {
        if (player != null) {
            playerFlyTime.remove(player.getName());
        }
    }

    /**
     * Current Updater
     */
    private static FlyTime tickSystem;

    /**
     * Starts the updater polling if the config will allow
     */
    public static void startUpdater() {
        if (tickSystem == null) {
            tickSystem = new FlyTime(getPlugin(), 0);
            ServerTaskManager.addTask(tickSystem);
        }
    }

    /**
     * Stops the updater polling
     */
    public static void endUpdater() {
        if (tickSystem != null) {
            ServerTaskManager.removeTask(tickSystem);
            tickSystem = null;
        }
    }

    /**
     * Will start the updater if the config allows or stops the updater if running and needed to be
     */
    public static void reloadUpdater() {
        endUpdater();
        startUpdater();
    }

    private FlyTime(TaskOwner owner, long delay) {
        this(owner, delay, true);
    }

    private FlyTime(TaskOwner owner, long delay, boolean continuous) {
        super(owner, delay, continuous);
    }

    @Override
    public void run() {
        for (String player : playerFlyTime.keySet()) {
            if (playerFlyTime.get(player) < System.currentTimeMillis()) {
                Player playerObject = Canary.getServer().getPlayer(player);
                if (playerObject == null) return;
                if (playerObject.isAdmin()) return;
                HumanCapabilities capabilities = playerObject.getCapabilities();
                capabilities.setFlying(false);
                capabilities.setMayFly(false);
                capabilities.setFlySpeed((float) 0.05);
                playerObject.updateCapabilities();
                Location loc = playerObject.getLocation();
                loc.setY(playerObject.getWorld().getHighestBlockAt(loc.getBlockX(), loc.getBlockZ()) + 1);
                playerObject.teleportTo(loc, TeleportHook.TeleportCause.MOVEMENT);
                playerObject.message("You may no longer fly");
                FlyTime.removePlayer(playerObject);
            }
        }
    }

}
