package espryth.fancywarps.commands.subcommands;

import espryth.fancywarps.FancyWarps;
import espryth.fancywarps.utils.ColorUtil;
import espryth.fancywarps.utils.storage.Files;
import espryth.fancywarps.utils.storage.FilesManager;
import espryth.fancywarps.utils.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class TpWarpCommand extends SubCommand {
    private ColorUtil color;

    public TpWarpCommand(FancyWarps plugin, FilesManager filesManager) {
        this.plugin = plugin;
        this.filesManager = filesManager;
    }

    @Override
    public boolean execute(Player player, String[] args) {
        color = new ColorUtil();
        Files warps = this.filesManager.getWarps();
        Files lang = this.filesManager.getLang();
            if(args.length == 0 || args.length > 2) {
                player.sendMessage(color.apply("&dCorrect Usage: &7/fancywarps tp <warp> (player)"));
                return true;
            }
            if(args.length == 1) {
                if(player.hasPermission(permission(args[0]))){
                    if(!(warps.contains("Warps." + args[0]))) {
                        player.sendMessage(color.apply(lang.getString("Lang.UnknownWarp")));
                        return true;
                    }
                    warpTeleport(player, args[0]);
                    player.sendMessage(color.apply(lang.getString("Lang.Tp.ForcedTeleport").replace(
                            "%warp%", args[0])));
                    return true;
                }
                player.sendMessage(color.apply(lang.getString("Lang.Tp.NoPermissionIndividual").replace("%warp%", args[0])));
                return true;
            }
            if(args.length == 2) {
                if(player.hasPermission("fancywarps.tp.others")){
                    Player target = Bukkit.getPlayer(args[1]);
                    if(target == null || target == player) {
                        return true;
                    }
                    if(!(warps.contains("Warps." + args[0]))) {
                        player.sendMessage(color.apply(lang.getString("Lang.UnknownWarp")));
                        return true;
                    }
                    warpTeleport(target, args[0]);
                    target.sendMessage(color.apply(lang.getString("Lang.Tp.ForcedTeleport").replace(
                            "%warp%", args[0])));
                    player.sendMessage(color.apply(lang.getString("Lang.Tp.ForcedTeleportOther").replace(
                            "%warp%", args[0]).replace("%player%", target.getName())));
                    return true;
                }
                player.sendMessage(color.apply(lang.getString("Lang.Tp.NoPermissionOthers")));
                return true;
            }
            return true;
    }

    @Override
    public String name() {
        return this.plugin.getCommandManager().getTp();
    }

    @Override
    public String info() {
        return "&d/fancywarps tp &8- &7Teleport to existing warp.";
    }

    private String permission(String warp) {
        Files config = this.filesManager.getConfig();
        if(config.getBoolean("Config.Individual-permission")) {
            return "fancywarps.tp." + warp;
        }
        return "fancywarps.tp";
    }

    private void warpTeleport(Player player, String warp) {
        Files warps = this.filesManager.getWarps();
        World world = Bukkit.getServer().getWorld(warps.getString("Warps." + warp + ".world"));
        double x = warps.getDouble("Warps." + warp + ".x");
        double y = warps.getDouble("Warps." + warp + ".y");
        double z = warps.getDouble("Warps." + warp + ".z");
        float yaw = warps.getInt("Warps." + warp + ".yaw");
        float pitch = warps.getInt("Warps." + warp + ".pitch");
        Location location = new Location(world, x, y, z, yaw, pitch);
        player.teleport(location);
    }
}
