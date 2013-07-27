package net.larry1123.fly.commands;

import net.canarymod.Canary;
import net.canarymod.Translator;
import net.canarymod.api.GameMode;
import net.canarymod.api.Server;
import net.canarymod.api.entity.living.humanoid.HumanCapabilities;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.blocks.CommandBlock;
import net.canarymod.chat.MessageReceiver;
import net.larry1123.lib.chat.FontTools;
import net.larry1123.lib.plugin.commands.Command;
import net.larry1123.lib.plugin.commands.CommandData;
import net.visualillusionsent.utils.LocaleHelper;

public class CFly implements Command {

    private final CommandData command;
    private final CommandMan commandman;
    private boolean loaded = false;
    // This is to save servers! Warning do not set speed too high in anyway!
    private final float maxspeed = 1;

    public CFly(CommandMan commandman, String[] aliases) {
        this.commandman = commandman;
        command = new CommandData( //
                aliases, //
                new String[]{ "net.larry1123.fly.command", }, //
                "Allows you or someone else to fly!", //
                "/fly <Player|Speed> <Speed>" //
                );
        command.setMax(3);
    }

    @Override
    public void execute(MessageReceiver caller, String[] parameters) {
        if ((caller instanceof Server || caller instanceof CommandBlock) && parameters.length == 1) {
            caller.message("Must Provide Player");
            return;
        }
        Player player;
        float speed = (float) 0.05;
        if (caller instanceof Player) {
            if (parameters.length == 2) {
                if (Canary.getServer().getPlayer(parameters[1]) != null) {
                    player = Canary.getServer().getPlayer(parameters[1]);
                    if (parameters.length == 2) {
                        try {
                            speed = Float.parseFloat(parameters[2].toString());
                        } catch (NumberFormatException e) {
                            caller.message(FontTools.RED + "Given Speed is NaN!");
                        }
                    }
                } else {
                    player = (Player) caller;
                    if (parameters.length == 2) {
                        try {
                            speed = Float.parseFloat(parameters[1].toString());
                        } catch (NumberFormatException e) {
                            caller.message(FontTools.RED + "Given Speed is NaN!");
                        }
                    }
                }
            } else {
                player = (Player) caller;
            }
        } else {
            player = Canary.getServer().getPlayer(parameters[1]);
            if (parameters.length == 3) {
                try {
                    speed = Float.parseFloat(parameters[2].toString());
                } catch (NumberFormatException e) {
                    caller.message(FontTools.RED + "Given Speed is NaN!");
                }
            }
        }
        // Limit to at lest Maxspeed for Safety
        if (speed > maxspeed) {
            speed = maxspeed;
        }
        if (player == null) {
            caller.message(FontTools.RED + "Player not found!");
            return;
        }
        if (!caller.hasPermission("net.larry1123.fly.other")) {
            if (caller != player) {
                caller.message("You do not have Permisstion to allow others to fly!");
            }
        }
        if (!player.hasPermission("net.larry1123.fly.speed.1")) {
            for (String perm : player.getPermissionProvider().getPermissionsAsStringList()) {
                if (perm.startsWith("net.larry1123.fly.speed.0.")) {
                    try {
                        float playermax = Float.parseFloat("0." + perm.substring(perm.lastIndexOf(".") + 1));
                        if (speed > playermax) {
                            speed = playermax;
                        }
                    } catch (NumberFormatException e) {
                        if (!(caller instanceof Server)) {
                            caller.message(FontTools.RED + "Player's Permissions are Malformed!");
                        }
                        commandman.getPlugin().getLogger().logDerp(player.getName() + "'s Permissions are Malformed!");
                        if (speed > 0.5) {
                            speed = (float) 0.05;
                        }
                    }
                } else {
                    if (speed > 0.5) {
                        speed = (float) 0.05;
                    }
                }
            }
        }
        HumanCapabilities capabilities = player.getCapabilities();
        if (capabilities.isFlying() && !player.getMode().equals(GameMode.CREATIVE)) {
            capabilities.setFlying(false);
            capabilities.setMayFly(false);
            capabilities.setFlySpeed((float) 0.05);
            player.updateCapabilities();
            player.message("You may no longer fly.");
        } else if (!capabilities.isFlying() && !player.getMode().equals(GameMode.CREATIVE)) {
            capabilities.setMayFly(true);
            capabilities.setFlying(true);
            capabilities.setFlySpeed(speed);
            player.updateCapabilities();
            String message;
            if (speed > 0.5) {
                message = "You may fly now, with the speed of " + FontTools.RED + capabilities.getFlySpeed();
            } else {
                message = "You may fly now, with the speed of " + capabilities.getFlySpeed();
            }
            player.message(message);
        } else if (player.getMode().equals(GameMode.CREATIVE)) {
            capabilities.setFlySpeed(speed);
            player.updateCapabilities();
            String message;
            if (speed > 0.5) {
                message = "Your flight speed is now " + FontTools.RED + capabilities.getFlySpeed();
            } else {
                message = "Your flight speed is now " + capabilities.getFlySpeed();
            }
            player.message(message);
        }
    }

    @Override
    public CommandData getCommandData() {
        return command;
    }

    @Override
    public LocaleHelper getTranslator() {
        return Translator.getInstance();
    }

    @Override
    public boolean isForced() {
        return false;
    }

    @Override
    public boolean isloaded() {
        return loaded;
    }

    @Override
    public void setloadded(boolean loadedness) {
        loaded = loadedness;
    }

}
