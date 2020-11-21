package espryth.fancywarps.commands.subcommands;

import espryth.fancywarps.FancyWarps;
import espryth.fancywarps.utils.ColorUtil;
import espryth.fancywarps.utils.storage.Files;
import espryth.fancywarps.utils.storage.FilesManager;
import espryth.fancywarps.utils.SubCommand;
import org.bukkit.entity.Player;

public class SetWarpCommand extends SubCommand {

    private ColorUtil color;

    public SetWarpCommand(FancyWarps plugin, FilesManager filesManager) {
        this.plugin = plugin;
        this.filesManager = filesManager;
    }

    @Override
    public boolean execute(Player player, String[] args) {
        color = new ColorUtil();
        Files lang = this.filesManager.getLang();
        if(args.length > 1 || args.length == 0) {
            player.sendMessage(color.apply("&dCorrect Usage: &7/fancywarps set <warp>"));
            return true;
        }
        if(args.length == 1) {
            setWarpCoords(player, args[0]);
            player.sendMessage(color.apply(lang.getString("Lang.Set.Setted").replace(
                    "%warp%", args[0])));
        }
        return true;
    }

    @Override
    public String name() {
        return this.plugin.getCommandManager().getSet();
    }

    @Override
    public String info() {
        return "&d/fancywarps set &8- &7Set a new warp.";
    }

    private void setWarpCoords(Player player, String warp) {
        Files warps = this.filesManager.getWarps();
        String world = player.getWorld().getName();
        double x = player.getLocation().getX();
        double y = player.getLocation().getY();
        double z = player.getLocation().getZ();
        float yaw = player.getLocation().getYaw();
        float pitch = player.getLocation().getPitch();
        warps.set("Warps."+ warp +".world", world);
        warps.set("Warps."+ warp +".x", Double.valueOf(x));
        warps.set("Warps."+ warp +".y",  Double.valueOf(y));
        warps.set("Warps."+ warp +".z",  Double.valueOf(z));
        warps.set("Warps."+ warp +".yaw", Float.valueOf(yaw));
        warps.set("Warps."+ warp +".pitch", Float.valueOf(pitch));
        warps.save();
    }
}
