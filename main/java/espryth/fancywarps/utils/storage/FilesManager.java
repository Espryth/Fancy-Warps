package espryth.fancywarps.utils.storage;

import espryth.fancywarps.FancyWarps;

public class FilesManager {

    private Files config;
    private Files warps;
    private Files lang;
    private Files menu;

    private FancyWarps plugin;

    public FilesManager(FancyWarps plugin) {
        this.plugin = plugin;
    }

    public void setup() {
        this.config = new Files(this.plugin, "config.yml");
        this.warps = new Files(this.plugin, "warps.yml");
        this.lang = new Files(this.plugin, "lang.yml");
        this.menu = new Files(this.plugin, "menu.yml");
    }

    public Files getConfig() {
        return this.config;
    }
    public Files getWarps() {
        return this.warps;
    }
    public Files getLang() {
        return this.lang;
    }
    public Files getMenu() { return this.menu; }
}