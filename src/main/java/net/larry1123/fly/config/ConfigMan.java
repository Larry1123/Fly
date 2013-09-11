package net.larry1123.fly.config;


public class ConfigMan {

    static ConfigMan config = new ConfigMan("Fly");

    private MainConfig mainconfig;

    public ConfigMan(String plugin) {
        mainconfig = new MainConfig(plugin);
    }

    public static ConfigMan getConfig() {
        return config;
    }

    public MainConfig getMainConfig() {
        return this.mainconfig;
    }

    public void reloadConfig() {
        mainconfig.reload();
    }

}
