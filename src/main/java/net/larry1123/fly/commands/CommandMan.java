package net.larry1123.fly.commands;

import net.canarymod.commandsys.CommandDependencyException;
import net.larry1123.fly.Fly;
import net.larry1123.lib.CanaryUtil;
import net.larry1123.lib.plugin.commands.Command;

public class CommandMan {

    private final Fly plugin;

    private Command fly;

    public CommandMan(Fly plugin) {
        this.plugin = plugin;
        String[] aliases;
        if (!plugin.getConfigMan().getConfig().getAlias().equals("")) {
            aliases = new String[2];
            aliases[1] = "fly";
            aliases[2] = plugin.getConfigMan().getConfig().getAlias();
        } else {
            aliases = new String[]{ "fly" };
        }

        fly = new CFly(this, aliases);

        if (!regCommand(fly)) {
            fly = new CFly(this, new String[] { "fly" });
            if (!regCommand(fly) && !plugin.getConfigMan().getConfig().getAlias().equals("")) {
                fly = new CFly(this, new String[] { plugin.getConfigMan().getConfig().getAlias() });
                regCommand(fly);
            }
        }
        if (!fly.isloaded()) {
            getPlugin().getLogger().logCustom("Commands", "Failed to add command: " + fly.getCommandData().getAliases()[0]);
        } else {
            getPlugin().getLogger().logCustom("Commands", "Added command: " + fly.getCommandData().getAliases()[0]);
        }
    }

    public Fly getPlugin() {
        return plugin;
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
