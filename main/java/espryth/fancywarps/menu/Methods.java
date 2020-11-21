package espryth.fancywarps.menu;

import espryth.fancywarps.utils.ColorUtil;
import espryth.fancywarps.utils.storage.Files;
import espryth.fancywarps.utils.storage.FilesManager;
import org.bukkit.ChatColor;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Methods {
    private final FilesManager filesManager;
    private ColorUtil color;
    public Methods(FilesManager filesManager) {
        this.filesManager = filesManager;
    }
    public void setLore(String menu,String item,  ItemMeta meta) {
        color = new ColorUtil();
        Files menuConfig = this.filesManager.getMenu();
        if(menuConfig.contains("Menus."+menu+".items." + item + ".lore")) {
            List<String> list = new ArrayList<>();
            for (String line : menuConfig.getStringList("Menus."+menu+".items." + item + ".lore")){
                list.add(color.apply(line));
            }
            meta.setLore(list);
        }
    }

}
