package espryth.fancywarps.commands.subcommands;

import espryth.fancywarps.FancyWarps;
import espryth.fancywarps.utils.ColorUtil;
import espryth.fancywarps.utils.SubCommand;
import espryth.fancywarps.utils.storage.FilesManager;
import org.bukkit.entity.Player;

public class ReloadConfigCommand extends SubCommand {
    private ColorUtil color;

    public ReloadConfigCommand(FancyWarps plugin, FilesManager filesManager) {
        this.plugin = plugin;
        this.filesManager = filesManager;
    }

    @Override
    public boolean execute(Player player, String[] args) {
        color = new ColorUtil();
        player.sendMessage(color.apply("&dFancyWarps configuration reloaded!"));
        this.filesManager.getConfig().reload();
        this.filesManager.getLang().reload();
        this.filesManager.getWarps().reload();
        this.filesManager.getMenu().reload();
        return true;
    }

    @Override
    public String name() {
        return this.plugin.getCommandManager().getReload();
    }

    @Override
    public String info() {
        return "&d/fancywarps reload &8- &7Reload the configuration.";
    }

}
