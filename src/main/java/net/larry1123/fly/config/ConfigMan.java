package net.larry1123.fly.config;


import net.canarymod.plugin.Plugin;

public class ConfigMan {

    private final MainConfig mainconfig;

    public ConfigMan(Plugin plugin) {
        mainconfig = new MainConfig(plugin);
    }

    public MainConfig getMainConfig() {
        return this.mainconfig;
    }

    public void reloadConfig() {
        mainconfig.reload();
    }

}
