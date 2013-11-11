package net.larry1123.fly.config;

import net.canarymod.plugin.Plugin;
import net.larry1123.util.config.ConfigBase;
import net.larry1123.util.config.ConfigField;
import net.larry1123.util.config.ConfigFile;
import net.larry1123.util.config.UtilConfigManager;

public class MainConfig implements ConfigBase {

    private static UtilConfigManager utilConfigManager = UtilConfigManager.getConfig();

    private ConfigFile configManager;

    @ConfigField(comments = "TODO", name = "Alias") // TODO
    private String alias = new String(""); // lol

    @ConfigField(comments = {"You can set this to any number but anything over 1 deadly to servers.",
            "Just an extra note not even admins are allowed pass this speed."}, name = "MaxSpeed")
    private final float maxSpeed = 1;

    public MainConfig(Plugin plugin) {
        configManager = utilConfigManager.getPluginConfig(this, plugin);
    }

    void reload() {
        configManager.reload();
    }

    public String getAlias() {
        return alias;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

}
