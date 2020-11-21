package espryth.fancywarps.commands;

import espryth.fancywarps.FancyWarps;
import espryth.fancywarps.commands.subcommands.*;
import espryth.fancywarps.utils.ColorUtil;
import espryth.fancywarps.utils.storage.Files;
import espryth.fancywarps.utils.storage.FilesManager;
import espryth.fancywarps.utils.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.*;

public class FancyWarpsCommandManager implements CommandExecutor, TabExecutor {

    private String set;
    private String tp;
    private String del;
    private String help;
    private String reload;

    private ColorUtil color;
    private HashMap<String, SubCommand> subCommands = new HashMap<>();
    private FancyWarps plugin;
    private FilesManager filesManager;
    public FancyWarpsCommandManager(FancyWarps plugin, FilesManager filesManager) {
        this.plugin = plugin;
        this.filesManager = filesManager;
        this.set = "set";
        this.tp = "tp";
        this.del = "del";
        this.help = "help";
        this.reload = "reload";
    }
    public void setup() {
        TabExecutor tabExecutor = this;
        this.plugin.getCommand("fancywarps").setExecutor(this);
        this.plugin.getCommand("fancywarps").setTabCompleter(tabExecutor);
        getSubCommands().put(this.help, new HelpCommand(this.plugin, this.filesManager));
        getSubCommands().put(this.set, new SetWarpCommand(this.plugin, this.filesManager));
        getSubCommands().put(this.del, new DelWarpCommand(this.plugin, this.filesManager));
        getSubCommands().put(this.tp, new TpWarpCommand(this.plugin, this.filesManager));
        getSubCommands().put(this.reload, new ReloadConfigCommand(this.plugin, this.filesManager));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        color = new ColorUtil();
        Player player = (Player) sender;
        Files lang = this.filesManager.getLang();
        if(!(sender instanceof Player)) {
            sender.sendMessage("Only players!");
            return true;
        }
        if(args.length == 0) {
            if(player.hasPermission("fancywarps.help")) {
                player.sendMessage(color.apply("&d&lFancy&7&l&oWarps &fVersion &f&o" + this.plugin.getDescription().getVersion() + "&r&f by Espryth"));
                for (SubCommand sb : this.plugin.getCommandManager().getSubCommands().values())
                    player.sendMessage(color.apply(sb.info()));
                return true;
            }
            player.sendMessage(color.apply("&d&lFancy&7&l&oWarps &fVersion &f&o" + this.plugin.getDescription().getVersion() + "&r&f by Espryth"));
            return true;
        }
        SubCommand subCommand = getSubCommands().get(args[0].toLowerCase());
        if(subCommand == null) {
            player.sendMessage(color.apply("&cUnknown command, use /fancywarps help"));
            return true;
        }

        ArrayList<String> list = new ArrayList<>();
        list.addAll(Arrays.asList(args));
        list.remove(0);
        String[] str = new String[list.size()];
        for(int i = 0; i < list.size(); i++) {
            str[i] = list.get(i);
        }
        if(player.hasPermission("fancywarps." + subCommand.name())) {
            try{
                subCommand.execute(player, str);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        player.sendMessage(color.apply(lang.getString("Lang.NoPermission")));
        return true;
    }

    public HashMap<String, SubCommand> getSubCommands() {
        return subCommands;
    }

    public String getSet() {
        return this.set;
    }
    public String getTp() {
        return this.tp;
    }
    public String getDel() {
        return this.del;
    }
    public String getHelp() {
        return this.help;
    }
    public String getReload() {
        return this.reload ;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> commands = new ArrayList<>();
        Player player = (Player)sender;
        Files warps = this.filesManager.getWarps();
        switch (args.length) {
            case 1:
                for (SubCommand sb : this.plugin.getCommandManager().getSubCommands().values()) {
                    if(player.hasPermission("fancywarps." + sb.name())) {
                        commands.add(sb.name());
                    }
                }
                break;
            case 2:
                if(args[0].equals(getDel()) || args[0].equals(getTp())) {
                    for(String warp : warps.getConfigurationSection("Warps").getKeys(false)) {
                        commands.add(warp);
                    }
                }
                break;
            case 3:
                if(args[0].equals(getTp())) {
                    for(Player target : Bukkit.getOnlinePlayers()) {
                        commands.add(target.getName());
                    }
                }
                break;
        }
        Collections.sort(commands);
        return commands;
    }
}
