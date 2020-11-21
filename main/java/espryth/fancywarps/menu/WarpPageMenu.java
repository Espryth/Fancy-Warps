package espryth.fancywarps.menu;

import espryth.fancywarps.utils.ColorUtil;
import espryth.fancywarps.utils.storage.Files;
import espryth.fancywarps.utils.storage.FilesManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WarpPageMenu {
    private ColorUtil color;
    private final FilesManager filesManager;

    public WarpPageMenu(FilesManager filesManager) {
        this.filesManager = filesManager;
    }

    public void openPageMenu(Player player, String menu) {
        color = new ColorUtil();
        Files menuConfig = this.filesManager.getMenu();
        Methods methods = new Methods(this.filesManager);
        Inventory inv = Bukkit.createInventory(null, menuConfig.getInt("Menus."+menu+".rows") * 9, color.apply(menuConfig.getString("Menus."+menu+".title")));
        for (String item : menuConfig.getConfigurationSection("Menus."+menu+".items").getKeys(false)) {
            ItemStack itemStack = new ItemStack(Material.valueOf(menuConfig.getString("Menus."+menu+".items." + item + ".material")), 1);
            ItemMeta meta = itemStack.getItemMeta();
            meta.setDisplayName(color.apply(menuConfig.getString("Menus."+menu+".items." + item + ".name")));
            methods.setLore(menu,item, meta);
            itemStack.setItemMeta(meta);
            inv.setItem(menuConfig.getInt("Menus."+menu+".items."+item +".slot"), itemStack);
        }
        if(menuConfig.getBoolean("Menus."+menu+".fillSlots")){
            ItemStack panel = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            ItemMeta panelmeta = panel.getItemMeta();
            panelmeta.setDisplayName(" ");
            panel.setItemMeta(panelmeta);
            for (int slot = 0; slot < inv.getSize(); slot++) {
                if (inv.getItem(slot) == null)
                    inv.setItem(slot, panel);
            }
        }
        player.openInventory(inv);
    }
}
