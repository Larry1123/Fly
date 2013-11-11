package net.larry1123.fly;

import net.canarymod.tasks.TaskOwner;
import net.larry1123.fly.commands.CommandMan;
import net.larry1123.fly.config.ConfigMan;
import net.larry1123.fly.task.FlyTime;
import net.larry1123.util.api.plugin.UtilPlugin;

public class Fly extends UtilPlugin implements TaskOwner {

    private static Fly plugin;

    private final ConfigMan configMan;
    private CommandMan commands;

    public static Fly getPlugin() {
        return plugin;
    }

    public Fly() {
        configMan = new ConfigMan(this);
        plugin = this;
    }

    @Override
    public boolean enable() {
        commands = new CommandMan(this);
        FlyTime.startUpdater();
        getLogger().info("Plugin Enabled");
        return commands.didCommandLoad();
    }

    @Override
    public void disable() {
        getLogger().info("Plugin Disabled");
    }

    public CommandMan getCommandMan() {
        return this.commands;
    }

    public ConfigMan getConfigMan() {
        return configMan;
    }

}
