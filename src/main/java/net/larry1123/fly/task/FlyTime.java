package net.larry1123.fly.task;

import net.canarymod.Canary;
import net.canarymod.api.OfflinePlayer;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.tasks.ServerTask;
import net.canarymod.tasks.ServerTaskManager;
import net.canarymod.tasks.TaskOwner;
import net.larry1123.fly.Fly;
import net.larry1123.util.api.time.StringTime;
import org.apache.commons.lang3.time.DateUtils;

import java.util.HashMap;

public class FlyTime extends ServerTask {

    private static HashMap<OfflinePlayer, Long> playerFlyTime = new HashMap<OfflinePlayer, Long>();

    public static void addPlayerFor(Player player, long time) {
        if (time < DateUtils.MILLIS_PER_SECOND) {
            if (player != null) {
                playerFlyTime.put(Canary.getServer().getOfflinePlayer(player.getName()), System.currentTimeMillis() + time);
            }
        }
    }

    public static void addPlayerFor(Player player, String stime) {
        long time = StringTime.millisecondsFromString(stime);
        if (time < DateUtils.MILLIS_PER_SECOND) {
            if (player != null) {
                playerFlyTime.put(Canary.getServer().getOfflinePlayer(player.getName()), System.currentTimeMillis() + time);
            }
        }
    }

    public static void removePlayer(Player player) {
        if (player != null) {
            OfflinePlayer offplay = Canary.getServer().getOfflinePlayer(player.getName());
            if (playerFlyTime.containsKey(offplay)) {
                playerFlyTime.remove(offplay);
            }
        }
    }

    /**
     * Current Updater
     */
    private static FlyTime ticksystem;
    /**
     * The plugin that will own the updater
     */
    private static TaskOwner plugin = null;

    /**
     * This is to be only used for internal uses
     *
     * @param plugin
     */
    public static void setPlugin(Fly plugin) {
        FlyTime.plugin = plugin;
    }

    /**
     * Starts the updater polling if the config will allow
     */
    public static void startUpdater() {
        if (plugin != null) {
            if (ticksystem == null) {
                ticksystem = new FlyTime(plugin, DateUtils.MILLIS_PER_SECOND);
                ServerTaskManager.addTask(ticksystem);
            }
        }
    }

    /**
     * Stops the updater polling
     */
    public static void endUpdater() {
        if (ticksystem != null) {
            ServerTaskManager.removeTask(ticksystem);
            ticksystem = null;
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
        //
    }

}
