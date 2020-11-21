package espryth.fancywarps.utils;

import espryth.fancywarps.FancyWarps;
import espryth.fancywarps.api.TitleAPI;
import espryth.fancywarps.utils.storage.Files;
import espryth.fancywarps.utils.storage.FilesManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashSet;

public class TeleportUtil {

    private World world;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private Player player;
    private Location initial;
    private Location warp;

    private FancyWarps plugin;
    private FilesManager filesManager;
    private ColorUtil color;

    public TeleportUtil(FancyWarps plugin, FilesManager filesManager, Player player, Location initial,World world, double x, double y, double z, float yaw, float pitch) {
        this.plugin = plugin;
        this.filesManager = filesManager;
        this.player = player;
        this.initial = initial;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.warp = new Location(this.world,this.x,this.y,this.z,this.yaw, this.pitch);
    }

    public void teleport() {
        Files config = this.filesManager.getConfig();
        Files lang = this.filesManager.getLang();
        color = new ColorUtil();
        HashSet<String> warping = this.plugin.getWarping();
        CountdownTimer timer = new CountdownTimer(this.plugin, config.getInt("Config.Teleport-delay"),
        () -> {
            warping.add(this.player.getUniqueId().toString());
            if(lang.contains("Lang.Warp.Title")) {
                TitleAPI api = new TitleAPI();
                String title = lang.getString("Lang.Warp.Title");
                String[] values = title.split(";");
                api.sendTitle(this.player,color.apply(values[0].replace("%time%", Integer.toString(config.getInt("Config.Teleport-delay")))), color.apply(values[1].replace("%time%", Integer.toString(config.getInt("Config.Teleport-delay")))), 1,25,1);
            } else if (lang.contains("Lang.Warp.Message")) {
                this.player.sendMessage(color.apply(lang.getString("Lang.Warp.Message").replace("%time%", Integer.toString(config.getInt("Config.Teleport-delay")))));
            }
        },() -> {
            Location playerlocation = new Location(this.player.getWorld(), this.player.getLocation().getBlockX(), this.player.getLocation().getBlockY(), this.player.getLocation().getBlockZ());
            if(this.initial.equals(playerlocation)) {
                player.teleport(this.warp);
                TitleAPI api = new TitleAPI();
                api.sendTitle(this.player, "", "", 0,0,0);
            } else {
                TitleAPI api = new TitleAPI();
                String title = lang.getString("Lang.Warp.CancelTeleport");
                String[] values = title.split(";");
                api.sendTitle(this.player,color.apply(values[0]), color.apply(values[1]),5,25,5);
            }
            warping.remove(player.getUniqueId().toString());
        }, t -> {
            if(lang.contains("Lang.Warp.Title")) {
                TitleAPI api = new TitleAPI();
                String title = lang.getString("Lang.Warp.Title");
                String[] values = title.split(";");
                api.sendTitle(this.player,color.apply(values[0].replace("%time%", Integer.toString(t.getSecondsLeft()))), color.apply(values[1].replace("%time%", Integer.toString(t.getSecondsLeft()))), 1,40,10);
            }
        });
        timer.scheduleTimer();
    }

}
