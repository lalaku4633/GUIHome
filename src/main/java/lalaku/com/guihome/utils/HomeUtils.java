package lalaku.com.guihome.utils;

import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

import static lalaku.com.guihome.GUIHome.getInstance;
import static lalaku.com.guihome.GUIHome.homePl;


public class HomeUtils {


    private File getFile() {
        return new File("plugins/GUIHome/homes.yml");
    }

    private YamlConfiguration loadConfig() {
        return YamlConfiguration.loadConfiguration(getFile());
    }

    private void saveConfig(YamlConfiguration config) {
        try {
            config.save(getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String tile(Player player) {
        String invTitle = ChatColor.GREEN + player.getName() + "のホーム一覧";
        return invTitle;
    }
    public void saveHome(Player player, int n) {
        UUID playerUUID = player.getUniqueId();
        Location playerLocation = player.getLocation();
        YamlConfiguration config = loadConfig();
        String playerUUIDString = playerUUID.toString();
        String locationKey = playerUUIDString + ".location." + n;
        config.set(locationKey + ".x", playerLocation.getX());
        config.set(locationKey + ".y", playerLocation.getY());
        config.set(locationKey + ".z", playerLocation.getZ());
        config.set(locationKey + ".yaw", playerLocation.getYaw());
        config.set(locationKey + ".pitch", playerLocation.getPitch());
        config.set(locationKey + ".world", playerLocation.getWorld().getName());
        config.set(locationKey + ".isSet", true);
        saveConfig(config);
    }

    public boolean deleteHome(Player player, int n) {
        UUID playerUUID = player.getUniqueId();
        YamlConfiguration config = loadConfig();
        String playerUUIDString = playerUUID.toString();
        String locationKey = playerUUIDString + ".location." + n;

        if (config.contains(locationKey)) {
            config.set(locationKey, null);
            saveConfig(config);
            return true;
        } else {
            return false;
        }
    }
    public void setHomeLore (Player player, int n, String string) {
        UUID playerUUID = player.getUniqueId();
        YamlConfiguration config = loadConfig();
        String playerUUIDString = playerUUID.toString();
        String locationKey = playerUUIDString + ".location." + n;
        config.set(locationKey + ".Lore", string);
        saveConfig(config);
    }
    public void summonCircle(Location location, int size) {
        for (int d = 0; d <= 90; d += 1) {
            Location particleLoc = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
            particleLoc.setX(location.getX() + Math.cos(d) * size);
            particleLoc.setZ(location.getZ() + Math.sin(d) * size);
            location.getWorld().spawnParticle(Particle.REDSTONE, particleLoc, 1, new Particle.DustOptions(Color.PURPLE, 1));
        }
    }
    public void teleportHome(Player player, int n) {
        UUID playerUUID = player.getUniqueId();
        YamlConfiguration config = loadConfig();
        String playerUUIDString = playerUUID.toString();
        String locationKey = playerUUIDString + ".location." + n;

        if (config.contains(locationKey)) {
            ConfigurationSection locationSection = config.getConfigurationSection(locationKey);
            double x = locationSection.getDouble("x");
            double y = locationSection.getDouble("y");
            double z = locationSection.getDouble("z");
            float yaw = (float) locationSection.getDouble("yaw");
            float pitch = (float) locationSection.getDouble("pitch");
            String worldName = locationSection.getString("world");

            // Check if the world exists in the server
            World world = Bukkit.getWorld(worldName);
            if (world != null) {
                Location teleportLocation = new Location(world, x, y, z, yaw, pitch);
                player.teleport(teleportLocation);
                player.sendMessage(homePl + ChatColor.DARK_PURPLE + "Home<" + n + ">にテレポートしました！");
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 0.0F);
                Bukkit.getScheduler().runTaskLater(getInstance(), () -> {
                    summonCircle(player.getLocation().add(0, 1, 0), 1);
                }, 3L); // 3 tick 後に実行
            } else {
                player.sendMessage(homePl + "指定されたワールドが見つかりませんでした。");
            }
        } else {
            player.sendMessage(homePl + "指定されたホームが見つかりませんでした。");
        }
    }

    public boolean homeExists(Player player, int n) {
        UUID playerUUID = player.getUniqueId();
        YamlConfiguration config = loadConfig();
        String playerUUIDString = playerUUID.toString();
        String locationKey = playerUUIDString + ".location." + n;

        if (config.contains(locationKey)) {
            ConfigurationSection locationSection = config.getConfigurationSection(locationKey);
            if (locationSection != null && locationSection.getBoolean("isSet")) {
                return true;
            }
        }
        return false;
    }
    public boolean deleteLore(Player player, int n) {
        UUID playerUUID = player.getUniqueId();
        YamlConfiguration config = loadConfig();
        String playerUUIDString = playerUUID.toString();
        String locationKey = playerUUIDString + ".location." + n + ".Lore";

        if (config.contains(locationKey)) {
            config.set(locationKey, null);
            saveConfig(config);
            return true;
        } else {
            return false;
        }
    }
    public int getInt (String string) {
        try {
            int n = Integer.parseInt(string);
            return n;
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    public void verificationInv(Player player, int n) {
        Inventory inv = Bukkit.createInventory(player, InventoryType.HOPPER, ChatColor.GRAY + "HOME " + n + " 削除の確認");

        ItemStack yes = new ItemStack(Material.LIME_WOOL);
        ItemMeta yesMeta = yes.getItemMeta();
        yesMeta.setDisplayName(ChatColor.BOLD + ChatColor.GREEN.toString() + "削除する");
        yesMeta.setLore(Arrays.asList(ChatColor.GRAY + "クリックで削除してHomeGUIに戻る"));
        yesMeta.setLocalizedName(n + "");
        yes.setItemMeta(yesMeta);

        ItemStack no = new ItemStack(Material.RED_WOOL);
        ItemMeta noMeta = no.getItemMeta();
        noMeta.setDisplayName(ChatColor.BOLD + ChatColor.RED.toString() + "削除しない");
        noMeta.setLore(Arrays.asList(ChatColor.GRAY + "クリックでHomeGUIに戻る"));
        no.setItemMeta(noMeta);

        inv.setItem(1, yes);
        inv.setItem(3, no);

        player.openInventory(inv);
        player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1.0F, 1.0F);
    }
    public void homeInv(Player player) {
        UUID playerUUID = player.getUniqueId();
        YamlConfiguration config = loadConfig();
        String playerUUIDString = playerUUID.toString();
        Inventory inv = Bukkit.createInventory(player, 27, tile(player));

        for (int i = 1; i <= 9; i++) {
            if (homeExists(player, i)) {
                String locationKey = playerUUIDString + ".location." + i + ".Lore";
                ItemStack redBed = new ItemStack(Material.RED_BED);
                ItemMeta redBedMeta = redBed.getItemMeta();
                redBedMeta.setDisplayName(ChatColor.GREEN.toString() + ChatColor.BOLD + "HOME " + i);
                if (config.contains(locationKey)) {
                    String homeLore = config.getString(locationKey);
                    redBedMeta.setLore(Arrays.asList(ChatColor.GRAY + "クリックで home " + i + " にテレポート", ChatColor.GOLD + ChatColor.STRIKETHROUGH.toString() + "============" + ChatColor.RESET + " " + ChatColor.GOLD + "Lore" + ChatColor.RESET + " " + ChatColor.GOLD + ChatColor.STRIKETHROUGH + "============", ChatColor.RESET + ChatColor.WHITE.toString() + homeLore));
                }
                else {
                    redBedMeta.setLore(Arrays.asList(ChatColor.GRAY + "クリックで home " + i + " にテレポート"));
                }
                redBed.setItemMeta(redBedMeta);

                ItemStack nameTag = new ItemStack(Material.NAME_TAG);
                ItemMeta nameTagMeta = nameTag.getItemMeta();
                nameTagMeta.setDisplayName(ChatColor.BOLD + ChatColor.DARK_PURPLE.toString() + "説明文 " + i);
                nameTagMeta.setLore(Arrays.asList(ChatColor.GRAY + "クリックで説明文を追加 home " + i));
                nameTag.setItemMeta(nameTagMeta);

                ItemStack homeRemove = new ItemStack(Material.CAULDRON);
                ItemMeta homeRemoveMeta = homeRemove.getItemMeta();
                homeRemoveMeta.setDisplayName(ChatColor.BOLD + ChatColor.RED.toString() + "削除 " + i);
                homeRemoveMeta.setLore(Arrays.asList(ChatColor.GRAY + "クリックで削除 home " + i));
                homeRemove.setItemMeta(homeRemoveMeta);


                inv.setItem(i - 1, redBed);
                inv.setItem(i + 8, nameTag);
                inv.setItem(i + 17, homeRemove);
            }
            else {
                ItemStack whiteBed = new ItemStack(Material.WHITE_BED);
                ItemMeta whiteBedMeta = whiteBed.getItemMeta();
                whiteBedMeta.setDisplayName(ChatColor.RED.toString() + ChatColor.BOLD + "HOME " + i);
                whiteBedMeta.setLore(Arrays.asList(ChatColor.GRAY + "設定可能 home " + i));
                whiteBed.setItemMeta(whiteBedMeta);

                ItemStack homeSet = new ItemStack(Material.COMPASS);
                ItemMeta homeSetMeta = homeSet.getItemMeta();
                homeSetMeta.setDisplayName(ChatColor.BOLD + ChatColor.GREEN.toString() + "セット " + i);
                homeSetMeta.setLore(Arrays.asList(ChatColor.GRAY + "クリックでセット home " + i));
                homeSet.setItemMeta(homeSetMeta);

                inv.setItem(i - 1, whiteBed);
                inv.setItem(i + 17, homeSet);
            }
        }
        player.openInventory(inv);
    }
}
