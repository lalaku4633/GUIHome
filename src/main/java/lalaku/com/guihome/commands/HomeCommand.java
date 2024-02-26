package lalaku.com.guihome.commands;

import lalaku.com.guihome.utils.HomeUtils;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static lalaku.com.guihome.GUIHome.homePl;


public class HomeCommand implements CommandExecutor {

    HomeUtils homeUtils = new HomeUtils();
    public static final int MAX_HOME = 9;

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (args.length == 0) {
                homeUtils.homeInv(player);
                player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1.0F, 1.0F);
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("help")) {
                    player.sendMessage(ChatColor.STRIKETHROUGH + ChatColor.GRAY.toString() + "================" + ChatColor.RESET + ChatColor.YELLOW + " [" + ChatColor.RESET+"GUIHome" + ChatColor.YELLOW + "] " + ChatColor.RESET + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "================");
                    player.sendMessage(ChatColor.DARK_GRAY +"- " + ChatColor.DARK_AQUA + "/home: " + ChatColor.GRAY + "HomeGUIを開く");
                    player.sendMessage(ChatColor.DARK_GRAY  +"- " + ChatColor.DARK_AQUA + "/home set <number>: " + ChatColor.GRAY + "任意の番号のホームポイントをセット");
                    player.sendMessage(ChatColor.DARK_GRAY  +"- " + ChatColor.DARK_AQUA + "/home delete <number>: " + ChatColor.GRAY + "任意の番号の任意のホームポイントを削除");
                }
                else {
                    player.sendMessage(homePl + "/home help");
                }
            }
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("set")) {
                    int n = homeUtils.getInt(args[1]);
                    if (0 < n && n <= MAX_HOME) {
                        homeUtils.saveHome(player, n);
                        player.sendMessage(homePl + ChatColor.GREEN + "Home<" + n + ">がセットされました！");
                    }
                    else {
                        player.sendMessage(homePl + "/home help");
                    }
                }
                else if (args[0].equalsIgnoreCase("delete")) {
                    int n = homeUtils.getInt(args[1]);
                    if (0 < n && n <= MAX_HOME) {
                        if (homeUtils.deleteHome(player, n)) {
                            player.sendMessage(homePl + ChatColor.RED + "Home<" + n + ">が削除されました！");
                        }
                    }
                    else {
                        player.sendMessage(homePl + "/home help");
                    }
                }
                else {
                    player.sendMessage(homePl + "/home help");
                }
            }
        }
        return false;
    }
}
