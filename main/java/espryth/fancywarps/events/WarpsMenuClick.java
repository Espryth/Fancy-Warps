package espryth.fancywarps.events;

import espryth.fancywarps.FancyWarps;
import espryth.fancywarps.menu.WarpPageMenu;
import espryth.fancywarps.utils.ColorUtil;
import espryth.fancywarps.utils.TeleportUtil;
import espryth.fancywarps.utils.storage.Files;
import espryth.fancywarps.utils.storage.FilesManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.io.IOException;

public class WarpsMenuClick implements Listener {

    private final FilesManager filesManager;
    private final FancyWarps plugin;

    private ColorUtil color;

    public WarpsMenuClick(FancyWarps plugin,FilesManager filesManager) {
        this.plugin = plugin;
        this.filesManager = filesManager;
    }

    @EventHandler
    public void WarpsMenuClickEvent(InventoryClickEvent event) {
        Files warps = this.filesManager.getWarps();
        Files menuConfig = this.filesManager.getMenu();
        Files lang = this.filesManager.getLang();
        Files config = this.filesManager.getConfig();
        color = new ColorUtil();
        Player player = (Player) event.getWhoClicked();
        for(String menu : menuConfig.getConfigurationSection("Menus").getKeys(false)) {
            if (event.getView().getTitle().equals(color.apply(menuConfig.getString("Menus."+ menu + ".title")))) {
                if (event.getCurrentItem() == null || event.getSlotType() == null || event
                        .getCurrentItem().getType() == Material.AIR || event.isShiftClick()) {
                    event.setCancelled(true);
                    return;
                }
                if (event.getCurrentItem().hasItemMeta()) {
                    event.setCancelled(true);
                    for(String item : menuConfig.getConfigurationSection("Menus."+menu+".items").getKeys(false)) {
                        if(event.getSlot() == menuConfig.getInt("Menus."+menu+".items."+item+".slot")) {
                            if(!(menuConfig.contains("Menus."+menu+".items." +item+".action"))) {
                                event.setCancelled(true);
                                return;
                            }
                            if(menuConfig.getString("Menus."+menu+".items."+item+".action").equals("CLOSE")) {
                                player.closeInventory();
                                return;
                            }
                            for(String menu2 : menuConfig.getConfigurationSection("Menus").getKeys(false)) {
                                try {
                                    if(menuConfig.getString("Menus."+menu+".items."+item+".action").equals("OPEN_MENU_" + menu2)) {
                                        WarpPageMenu pagemenu = new WarpPageMenu(this.filesManager);
                                        pagemenu.openPageMenu(player, menu2);
                                        return;
                                    }
                                } catch (Exception e) {
                                    String name = menuConfig.getString("Menus."+menu+".items."+item+".action").replace("OPEN_MENU_", "");
                                    this.plugin.getLogger().info("Error to open the menu " + name + ", it exists?");
                                }
                            }
                            for(String warp : warps.getConfigurationSection("Warps").getKeys(false)) {
                                if(menuConfig.getString("Menus."+menu+".items."+item+".action").equals("WARP_TO_" +warp)) {
                                        if(config.getBoolean("Config.Individual-permission")) {
                                            String name = menuConfig.getString("Menus."+menu+".items."+item+".action").replace("WARP_TO_", "");
                                            if(player.hasPermission("fancywarps.warp." + name)) {
                                                try {
                                                    executeTeleport(player,warps,warp,config);
                                                } catch (Exception e) {

                                                plugin.getLogger().info("Error to teleporting to the warp " + name + ", it exists?");
                                                }
                                                return;
                                            }
                                            String sound = config.getString("Sounds.No-warp-permission");
                                            String[] values = sound.split(";");
                                            try{
                                                player.playSound(player.getLocation(), Sound.valueOf(values[0]),Integer.parseInt(values[1]), Integer.parseInt(values[2]));
                                            } catch (Exception e) {
                                                plugin.getLogger().info("Error with sound " + config.getString("Sounds.No-warp-permission"));
                                            }
                                            player.sendMessage(color.apply(lang.getString("Lang.Warp.NoPermissionIndividual").replace("%warp%", name)));
                                            return;
                                        }
                                        try {
                                            executeTeleport(player,warps,warp,config);
                                        } catch (Exception e) {
                                         String name = menuConfig.getString("Menus."+menu+".items."+item+".action").replace("WARP_TO_", "");
                                         plugin.getLogger().info("Error to teleporting to the warp " + name + ", it exists?");
                                        }
                                        return;
                                }
                            }
                        }
                    }
                }else {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    private void executeTeleport(Player player,Files warps, String warp, Files config) {
        player.closeInventory();
        Location initial = new Location(player.getWorld(), player.getLocation().getBlockX(),player.getLocation().getBlockY(),player.getLocation().getBlockZ());
        TeleportUtil teleportUtil = new TeleportUtil(this.plugin, this.filesManager, player, initial,
                Bukkit.getWorld(warps.getString("Warps." + warp + ".world")),
                warps.getDouble("Warps." + warp + ".x"),warps.getDouble("Warps." + warp + ".y"), warps.getDouble("Warps." + warp + ".z"),
                warps.getInt("Warps." + warp + ".yaw"), warps.getInt("Warps." + warp + ".z"));
        teleportUtil.teleport();
        String sound = config.getString("Sounds.Warping");
        String[] values = sound.split(";");
        try{
            player.playSound(player.getLocation(), Sound.valueOf(values[0]),Integer.parseInt(values[1]), Integer.parseInt(values[2]));
        } catch (Exception e) {
            plugin.getLogger().info("Error with sound " + config.getString("Sounds.Warping"));
        }
    }
}
