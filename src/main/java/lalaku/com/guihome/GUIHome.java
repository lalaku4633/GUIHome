package lalaku.com.guihome;

import lalaku.com.guihome.commands.HomeCommand;
import lalaku.com.guihome.commands.HomeTab;
import lalaku.com.guihome.listeners.HomeListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class GUIHome extends JavaPlugin {

    public static GUIHome instance;
    public static String homePl = ChatColor.YELLOW + "[" + ChatColor.RESET + "Home" + ChatColor.YELLOW + "]" + ChatColor.RESET;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        getCommand("home").setExecutor(new HomeCommand());
        getCommand("home").setTabCompleter(new HomeTab());
        Bukkit.getPluginManager().registerEvents(new HomeListener(), this);
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        try {
            initiateFile("homes.yml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initiateFile (String name) throws Exception {
        File file = new File(getDataFolder(), name);
        if (!file.exists()) {
            file.createNewFile();
        }
    }

    public static GUIHome getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        // インスタンスを解放する
        instance = null;
    }
}
