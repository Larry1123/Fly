package net.larry1123.fly.config;

import net.larry1123.fly.Fly;

public class ConfigMan {

    private MainConfig mainconfig;

    public ConfigMan(Fly plugin) {
        mainconfig = new MainConfig(plugin);
    }

    public MainConfig getConfig() {
        return this.mainconfig;
    }

    public void reloadConfig() {
        mainconfig.reload();
    }

}
