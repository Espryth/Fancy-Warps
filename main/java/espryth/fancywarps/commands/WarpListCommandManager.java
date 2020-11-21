package espryth.fancywarps.commands;

import espryth.fancywarps.FancyWarps;
import espryth.fancywarps.menu.WarpsMenu;
import espryth.fancywarps.utils.ColorUtil;
import espryth.fancywarps.utils.storage.Files;
import espryth.fancywarps.utils.storage.FilesManager;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class WarpListCommandManager implements CommandExecutor, TabExecutor {

    private FilesManager filesManager;
    private FancyWarps plugin;

    private ColorUtil color;

    public WarpListCommandManager(FancyWarps plugin, FilesManager filesManager) {
        this.plugin = plugin;
        this.filesManager = filesManager;
    }

    public void setup() {
        TabExecutor tabExecutor = this;
        this.plugin.getCommand("warps").setExecutor(this);
        this.plugin.getCommand("warps").setTabCompleter(tabExecutor);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        HashSet<String> warping = this.plugin.getWarping();
        color = new ColorUtil();
        if(!(sender instanceof Player)) {
            sender.sendMessage("Only players!");
            return true;
        }
        Player player = (Player) sender;
        Files lang = this.filesManager.getLang();
        Files config = this.filesManager.getConfig();
        if(player.hasPermission("fancywarps.warp")) {
            if(warping.contains(player.getUniqueId().toString())) {
                player.sendMessage(color.apply(lang.getString("Lang.AlreadyTeleporting")));
                return true;
            }
            WarpsMenu menu = new WarpsMenu(this.filesManager);
            menu.openWarpsMenu(player);
            String sound = config.getString("Sounds.Open-menu");
            String[] values = sound.split(";");
            try{
                player.playSound(player.getLocation(), Sound.valueOf(values[0]),Integer.parseInt(values[1]), Integer.parseInt(values[2]));
            } catch (Exception e) {
                plugin.getLogger().info("Error with sound " + config.getString("Sounds.Open-menu"));
            }
            return true;
        }
        player.sendMessage(color.apply(lang.getString("Lang.NoPermission")));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> commands = new ArrayList<>();
        if(args.length >= 0) {
            return commands;
        }
        return null;
    }
}
