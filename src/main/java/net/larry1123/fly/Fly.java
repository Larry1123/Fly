package net.larry1123.fly;

import net.canarymod.tasks.TaskOwner;
import net.larry1123.fly.commands.CommandMan;
import net.larry1123.fly.task.FlyTime;
import net.larry1123.util.plugin.UtilPlugin;

public class Fly extends UtilPlugin implements TaskOwner {

    private CommandMan commands;

    @Override
    public boolean enable() {
        commands = new CommandMan(this);
        FlyTime.setPlugin(this);
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

}
