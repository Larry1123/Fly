package net.larry1123.fly.commands;

import net.canarymod.commandsys.CommandDependencyException;
import net.larry1123.fly.Fly;
import net.larry1123.fly.config.ConfigMan;
import net.larry1123.fly.config.MainConfig;
import net.larry1123.util.CanaryUtil;
import net.larry1123.util.plugin.commands.Command;

public class CommandMan {

    private static final MainConfig config = ConfigMan.getConfig().getMainConfig();
    private final Fly plugin;
    private Command fly;
    private boolean loaded = false;

    public CommandMan(Fly plugin) {
        this.plugin = plugin;
        String[] aliases;
        if (!config.getAlias().equals("")) {
            aliases = new String[2];
            aliases[1] = "fly";
            aliases[2] = config.getAlias();
        } else {
            aliases = new String[] {"fly"};
        }
        fly = new CFly(this, aliases);
        if (!regCommand(fly)) {
            fly = new CFly(this, new String[] {"fly"});
            if (!regCommand(fly) && !config.getAlias().equals("")) {
                fly = new CFly(this, new String[] {config.getAlias()});
                regCommand(fly);
            }
        }
        if (!fly.isloaded()) {
            getPlugin().getLogger().logCustom("Commands", "Failed to add command: " + fly.getCommandData().getAliases()[0]);
            this.loaded = false;
        } else {
            getPlugin().getLogger().logCustom("Commands", "Added command: " + fly.getCommandData().getAliases()[0]);
            this.loaded = true;
        }
    }

    public Fly getPlugin() {
        return this.plugin;
    }

    public boolean didCommandLoad() {
        return this.loaded;
    }

    private boolean regCommand(Command command) {
        try {
            CanaryUtil.commands().registerCommand(command, getPlugin());
            return true;
        } catch (CommandDependencyException e) {
            // getPlugin().getLogger().logCustom("Commands", "Failed to add command: " + command.getCommandData().getAliases()[0], e);
            command.setloadded(false);
            return false;
        }
    }

}
