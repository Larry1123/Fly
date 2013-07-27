package net.larry1123.fly;

import net.larry1123.fly.commands.CommandMan;
import net.larry1123.fly.config.ConfigMan;
import net.larry1123.lib.plugin.UtilPlugin;

public class Fly extends UtilPlugin {

    private ConfigMan config;
    private CommandMan commands;

    @Override
    public boolean enable() {
        config = new ConfigMan(this);
        commands = new CommandMan(this);
        getLogger().info("Plugin Enabled");
        return true;
    }

    @Override
    public void disable() {
        getLogger().info("Plugin Disabled");
    }

    public ConfigMan getConfigMan() {
        return this.config;
    }

    public CommandMan getCommandMan() {
        return this.commands;
    }

}
