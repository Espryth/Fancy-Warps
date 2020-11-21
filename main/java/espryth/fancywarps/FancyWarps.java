package espryth.fancywarps;

import espryth.fancywarps.commands.FancyWarpsCommandManager;
import espryth.fancywarps.commands.WarpListCommandManager;
import espryth.fancywarps.events.BlockPlayerMove;
import espryth.fancywarps.events.WarpsMenuClick;
import espryth.fancywarps.utils.storage.FilesManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;

public class FancyWarps extends JavaPlugin {

    private HashSet<String> warping = new HashSet<String>();

    private FilesManager filesManager;
    private FancyWarpsCommandManager commandManager;
    private WarpListCommandManager warpListManager;
    @Override
    public void onEnable() {
        this.filesManager = new FilesManager(this);
        this.filesManager.setup();
        this.commandManager = new FancyWarpsCommandManager(this, this.filesManager);
        this.commandManager.setup();
        this.warpListManager = new WarpListCommandManager(this, this.filesManager);
        this.warpListManager.setup();
        registerEvents();
    }

    private void registerEvents() {
        Bukkit.getServer().getPluginManager().registerEvents(new WarpsMenuClick(this, this.filesManager), this);
        Bukkit.getServer().getPluginManager().registerEvents(new BlockPlayerMove(this, this.filesManager), this);
    }

    public FancyWarpsCommandManager getCommandManager() {
        return this.commandManager;
    }

    public HashSet<String> getWarping() {
        return this.warping;
    }
}
