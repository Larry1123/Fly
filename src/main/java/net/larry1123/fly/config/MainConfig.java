package net.larry1123.fly.config;

import net.larry1123.fly.Fly;
import net.larry1123.lib.config.ConfigBase;
import net.larry1123.lib.config.ConfigFeild;
import net.larry1123.lib.config.ConfigFile;

public class MainConfig implements ConfigBase {

    private ConfigFile configManager;

    @ConfigFeild( comments = "TODO", name = "Alias" ) // TODO
    private String alias = new String(""); // lol

    public MainConfig(Fly plugin) {
        configManager = new ConfigFile(this, plugin.getName());
    }

    void reload() {
        configManager.reload();
    }

    public String getAlias() {
        return alias;
    }

}
