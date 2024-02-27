package lalaku.com.guihome.listeners;

import lalaku.com.guihome.utils.HomeUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;

import static lalaku.com.guihome.GUIHome.homePl;
import static lalaku.com.guihome.commands.HomeCommand.MAX_HOME;

public class HomeListener implements Listener {
    public static Map<String, Integer> isSettingLore = new HashMap<String, Integer>();
    HomeUtils homeUtils = new HomeUtils();

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (ChatColor.translateAlternateColorCodes('&', e.getView().getTitle()).equals(homeUtils.tile(player)) && e.getCurrentItem() != null) {
            e.setCancelled(true);
            int slot = e.getRawSlot();
            if (0 <= slot && slot <= MAX_HOME - 1) {
                if (e.getCurrentItem().getType() == Material.RED_BED) {
                    player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1.0F, 1.0F);
                    int n = slot + 1;
                    homeUtils.teleportHome(player, n);
                    player.closeInventory();
                }
                if (e.getCurrentItem().getType() == Material.WHITE_BED) {
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F);
                }
            } else if (9 <= slot && slot <= MAX_HOME + 8 && e.getCurrentItem().getType() == Material.NAME_TAG) {
                player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1.0F, 1.0F);
                player.sendMessage(homePl + "設定したい説明文をチャット欄に打ってください。deleteで説明文が消え、exitで何も変更せずに普通のチャットに戻ります。");
                player.closeInventory();
                isSettingLore.put(player.getName(), slot - 8);
            } else if (18 <= slot && slot <= MAX_HOME + 17) {
                if (e.getCurrentItem().getType() == Material.COMPASS) {
                    homeUtils.saveHome(player, slot - 17);
                    homeUtils.homeInv(player);
                    player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1.0F, 1.0F);
                }
                if (e.getCurrentItem().getType() == Material.CAULDRON) {
                    homeUtils.verificationInv(player, slot - 17);
                }
            }
        }
        if (ChatColor.translateAlternateColorCodes('&', e.getView().getTitle()).contains("削除の確認") && e.getCurrentItem() != null) {
            e.setCancelled(true);
            int slot = e.getRawSlot();
            if (slot == 1) {
                if (e.getCurrentItem().getType() == Material.LIME_WOOL) {
                    player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1.0F, 1.0F);
                    int n = Integer.parseInt(e.getInventory().getItem(1).getItemMeta().getLocalizedName());
                    homeUtils.deleteHome(player, n);
                    homeUtils.homeInv(player);
                }
            }
            if (slot == 3) {
                if (e.getCurrentItem().getType() == Material.RED_WOOL) {
                    player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1.0F, 1.0F);
                    homeUtils.homeInv(player);
                }
            }
        }
    }
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        if (isSettingLore.containsKey(player.getName())) {
            e.setCancelled(true);
            Integer n = isSettingLore.get(player.getName());
            if (n != null) {
                if (e.getMessage().equals("exit")) {
                    player.sendMessage(homePl + "説明文設定を中断します");
                    player.playSound(player.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1.0F, 2.0F);
                }
                else if (e.getMessage().equals("delete")) {
                    homeUtils.deleteLore(player, n);
                    player.sendMessage(homePl + "home" + n + "の説明文を削除しました");
                    player.playSound(player.getLocation(), Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 1.0F, 1.0F);
                } else {
                    homeUtils.setHomeLore(player, n, e.getMessage());
                    player.sendMessage(homePl + "home" + n + "の説明文を設定しました");
                    player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0F, 2.0F);
                }
                isSettingLore.remove(player.getName());
            } else {

            }
        }
    }
}
