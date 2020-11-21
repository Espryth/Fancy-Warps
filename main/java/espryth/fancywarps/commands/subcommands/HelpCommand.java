package espryth.fancywarps.commands.subcommands;

import espryth.fancywarps.FancyWarps;
import espryth.fancywarps.utils.ColorUtil;
import espryth.fancywarps.utils.storage.FilesManager;
import espryth.fancywarps.utils.SubCommand;
import org.bukkit.entity.Player;

public class HelpCommand extends SubCommand {
    private ColorUtil color;

    public HelpCommand(FancyWarps plugin, FilesManager filesManager) {
        this.plugin = plugin;
        this.filesManager = filesManager;
    }

    @Override
    public boolean execute(Player player, String[] args) {
        color = new ColorUtil();
        player.sendMessage(color.apply("&d&lFancy&7&l&oWarps &fVersion &f&o" + this.plugin.getDescription().getVersion() + "&r&f by Espryth"));
        for (SubCommand sb : this.plugin.getCommandManager().getSubCommands().values())
            player.sendMessage(color.apply(sb.info()));
        return true;
    }

    @Override
    public String name() {
        return this.plugin.getCommandManager().getHelp();
    }

    @Override
    public String info() {
        return "&d/fancywarps help &8- &7Show this.";
    }
}
