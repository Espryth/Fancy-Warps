package espryth.fancywarps.commands.subcommands;

import espryth.fancywarps.FancyWarps;
import espryth.fancywarps.utils.ColorUtil;
import espryth.fancywarps.utils.storage.Files;
import espryth.fancywarps.utils.storage.FilesManager;
import espryth.fancywarps.utils.SubCommand;
import org.bukkit.entity.Player;

public class DelWarpCommand extends SubCommand {

    private ColorUtil color;

    public DelWarpCommand(FancyWarps plugin, FilesManager filesManager) {
        this.plugin = plugin;
        this.filesManager = filesManager;
    }

    @Override
    public boolean execute(Player player, String[] args) {
        color = new ColorUtil();
        Files lang = this.filesManager.getLang();
        Files warps = this.filesManager.getWarps();
        if(args.length > 1 || args.length == 0) {
            player.sendMessage(color.apply("&dCorrect Usage: &7/fancywarps del <warp>"));
            return true;
        }
        if(args.length == 1) {
                if(!(warps.contains("Warps." + args[0]))) {
                    player.sendMessage(color.apply(lang.getString("Lang.UnknownWarp")));
                    return true;
                }
                warps.set("Warps."+args[0], null);
                warps.save();
                player.sendMessage(color.apply(lang.getString("Lang.Del.Deleted").replace(
                        "%warp%", args[0])));
                return true;
        }
        return true;
    }

    @Override
    public String name() {
        return this.plugin.getCommandManager().getDel();
    }

    @Override
    public String info() {
        return "&d/fancywarps del &8- &7Del a warp.";

    }
}
