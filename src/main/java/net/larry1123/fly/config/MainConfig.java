package net.larry1123.fly.config;

import net.larry1123.util.config.ConfigBase;
import net.larry1123.util.config.ConfigFeild;
import net.larry1123.util.config.ConfigFile;

public class MainConfig implements ConfigBase {

    private ConfigFile configManager;

    @ConfigFeild (comments = "TODO", name = "Alias") // TODO
    private String alias = new String(""); // lol

    private final float maxspeed = 1;

    public MainConfig(String plugin) {
        configManager = new ConfigFile(this, plugin);
    }

    void reload() {
        configManager.reload();
    }

    public String getAlias() {
        return alias;
    }

    public float getMaxSpeed() {
        return maxspeed;
    }

}
