package espryth.fancywarps.utils;

import espryth.fancywarps.FancyWarps;
import espryth.fancywarps.utils.storage.FilesManager;
import org.bukkit.entity.Player;

public abstract class SubCommand {

    protected FancyWarps plugin;
    protected FilesManager filesManager;
    public abstract boolean execute(Player player, String[] args);
    public abstract String name();
    public abstract String info();


}
