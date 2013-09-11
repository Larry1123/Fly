package net.larry1123.fly.commands;

import net.canarymod.Canary;
import net.canarymod.Translator;
import net.canarymod.api.Server;
import net.canarymod.api.entity.living.humanoid.HumanCapabilities;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.blocks.CommandBlock;
import net.canarymod.chat.MessageReceiver;
import net.larry1123.fly.api.EndFlyingHook;
import net.larry1123.fly.api.StartFlyingHook;
import net.larry1123.fly.config.ConfigMan;
import net.larry1123.fly.permissions.PermissionsMan;
import net.larry1123.util.chat.FontTools;
import net.larry1123.util.plugin.commands.Command;
import net.larry1123.util.plugin.commands.CommandData;
import net.larry1123.util.time.StringTime;
import net.visualillusionsent.utils.LocaleHelper;

public class CFly implements Command {

    private final CommandData command;
    private final CommandMan commandman;
    private boolean loaded = false;
    // This is to save servers! Warning do not set speed too high in anyway!
    private static float maxSpeed = ConfigMan.getConfig().getMainConfig().getMaxSpeed();

    public CFly(CommandMan commandman, String[] aliases) {
        this.commandman = commandman;
        command = new CommandData( //
                aliases, //
                new String[] {PermissionsMan.commandNode,}, //
                "Allows you or someone else to fly!", //
                /**
                 * fly
                 * fly Speed
                 * fly Player
                 * fly Player Time
                 * fly Player Speed
                 * fly Player Speed Time
                 */
                "/fly [Player|Speed] [Speed|Time] [Time]" //
        );
        command.setMax(4);
    }

    @Override
    public void execute(MessageReceiver caller, String[] parameters) {
        if ((caller instanceof Server || caller instanceof CommandBlock) && parameters.length == 1) {
            // Makes sure Server commands and CommandBlock commands at lest have one parameter
            caller.message("Must Provide Player");
            return;
        }
        Player player;
        float speed = (float) 0.05;
        long time = 0;
        if (caller instanceof Player) {
            if (parameters.length == 1) {
                // fly
                player = (Player) caller;
            } else if (parameters.length == 2) {
                if (Canary.getServer().getPlayer(parameters[1]) != null) {
                    // fly Player
                    player = Canary.getServer().getPlayer(parameters[1]);
                } else {
                    // fly Speed
                    player = (Player) caller;
                    try {
                        speed = Float.parseFloat(parameters[1]);
                    } catch (NumberFormatException e) {
                        caller.message(FontTools.RED + "Given Speed is NaN!");
                    }
                }
            } else {
                if (Canary.getServer().getPlayer(parameters[1]) != null) {
                    player = Canary.getServer().getPlayer(parameters[1]);
                    try {
                        speed = Float.parseFloat(parameters[2]);
                    } catch (NumberFormatException e) {
                        //
                    }
                } else {
                    player = (Player) caller;
                    try {
                        speed = Float.parseFloat(parameters[1]);
                    } catch (NumberFormatException e) {
                        //
                    }
                }
                boolean speeded = false;
                try {
                    // fly Player Speed
                    speed = Float.parseFloat(parameters[2]);
                    speeded = true;
                } catch (NumberFormatException e) {
                    // fly Player Time
                    // We know that it is only Time stuff left, or well we can hope
                    String[] parts = new String[parameters.length - 3];
                    int y = 0;
                    for (int i = 2; i >= parameters.length; i++) {
                        parts[y++] = parameters[i];
                    }
                    time = StringTime.millisecondsFromString(parts);
                }
                if (speeded) {
                    // fly Player Speed Time
                    String[] parts = new String[parameters.length - 4];
                    int y = 0;
                    for (int i = 3; i >= parameters.length; i++) {
                        parts[y++] = parameters[i];
                    }
                    time = StringTime.millisecondsFromString(parts);
                }
            }
        } else {
            player = Canary.getServer().getPlayer(parameters[1]);
            if (parameters.length == 3) {
                try {
                    speed = Float.parseFloat(parameters[2]);
                } catch (NumberFormatException e) {
                    caller.message(FontTools.RED + "Given Speed is NaN!");
                }
            }
        }
        if (player == null) {
            caller.message(FontTools.RED + "Player not found!");
            return;
        }
        if (!PermissionsMan.allowFlyOther(caller)) {
            if (caller != player) {
                caller.message("You do not have Permission to allow others to fly!");
                return;
            }
        }
        // Limit to at lest Maxspeed for Safety
        if (speed > maxSpeed) {
            speed = maxSpeed;
        }
        // Limit speed to the max the player can go.
        if (speed > PermissionsMan.getMaxSpeedByCaller(player)) {
            speed = PermissionsMan.getMaxSpeedByCaller(player);
            if (caller != player) {
                caller.message(player.getName() + " can not fly that fast. Setting Speed to " + speed);
            } else {
                caller.message("You can not fly that fast. Setting Speed to " + speed);
            }
        }
        // SetUp Done
        HumanCapabilities capabilities = player.getCapabilities();
        if (!capabilities.mayFly()) {
            StartFlyingHook hook = (StartFlyingHook) new StartFlyingHook(player, speed, caller, time).call();
            if (!hook.isCanceled()) {
                hook.execute();
                hook.sendMessages();
            }
        } else {
            EndFlyingHook hook = (EndFlyingHook) new EndFlyingHook(player, caller).call();
            if (!hook.isCanceled()) {
                hook.execute();
                hook.sendMessages();
            }
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
