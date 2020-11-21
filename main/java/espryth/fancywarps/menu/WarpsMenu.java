package espryth.fancywarps.menu;

import espryth.fancywarps.utils.ColorUtil;
import espryth.fancywarps.utils.storage.Files;
import espryth.fancywarps.utils.storage.FilesManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WarpsMenu {

    private ColorUtil color;
    private final FilesManager filesManager;

    public WarpsMenu(FilesManager filesManager) {
        this.filesManager = filesManager;
    }

    public void openWarpsMenu(Player player) {
        color = new ColorUtil();
        Files menuConfig = this.filesManager.getMenu();
        Methods methods = new Methods(this.filesManager);
        Inventory inv = Bukkit.createInventory(null, menuConfig.getInt("Menus.1.rows") * 9, color.apply(menuConfig.getString("Menus.1.title")));
        for (String item : menuConfig.getConfigurationSection("Menus.1.items").getKeys(false)) {
            ItemStack itemStack = new ItemStack(Material.valueOf(menuConfig.getString("Menus.1.items." + item + ".material")), 1);
            ItemMeta meta = itemStack.getItemMeta();
            meta.setDisplayName(color.apply(menuConfig.getString("Menus.1.items." + item + ".name")));
            methods.setLore("1",item, meta);
            itemStack.setItemMeta(meta);
            inv.setItem(menuConfig.getInt("Menus.1.items."+item +".slot"), itemStack);
        }
        if(menuConfig.getBoolean("Menus.1.fillSlots")){
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
