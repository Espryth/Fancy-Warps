package espryth.fancywarps.events;

import espryth.fancywarps.FancyWarps;
import espryth.fancywarps.utils.storage.Files;
import espryth.fancywarps.utils.storage.FilesManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashSet;

public class BlockPlayerMove implements Listener {

    private FilesManager filesManager;
    private FancyWarps plugin;

    public BlockPlayerMove(FancyWarps plugin, FilesManager filesManager) {
        this.plugin = plugin;
        this.filesManager = filesManager;
    }

    @EventHandler
    public void blockMove(PlayerMoveEvent event) {
        HashSet <String> warping = this.plugin.getWarping();
        Files config = this.filesManager.getConfig();
        Player player = event.getPlayer();
        if(config.getBoolean("Config.Block-player-move")) {
            if(warping.contains(player.getUniqueId().toString())) {
                event.setCancelled(true);
            }
        }
    }
}
