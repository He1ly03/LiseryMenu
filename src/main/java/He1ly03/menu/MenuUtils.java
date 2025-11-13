package He1ly03.menu;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MenuUtils {
    
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final Map<Character, String> LEGACY_TO_MINI = new HashMap<>();
    
    static {
        LEGACY_TO_MINI.put('0', "<black>");
        LEGACY_TO_MINI.put('1', "<dark_blue>");
        LEGACY_TO_MINI.put('2', "<dark_green>");
        LEGACY_TO_MINI.put('3', "<dark_aqua>");
        LEGACY_TO_MINI.put('4', "<dark_red>");
        LEGACY_TO_MINI.put('5', "<dark_purple>");
        LEGACY_TO_MINI.put('6', "<gold>");
        LEGACY_TO_MINI.put('7', "<gray>");
        LEGACY_TO_MINI.put('8', "<dark_gray>");
        LEGACY_TO_MINI.put('9', "<blue>");
        LEGACY_TO_MINI.put('a', "<green>");
        LEGACY_TO_MINI.put('b', "<aqua>");
        LEGACY_TO_MINI.put('c', "<red>");
        LEGACY_TO_MINI.put('d', "<light_purple>");
        LEGACY_TO_MINI.put('e', "<yellow>");
        LEGACY_TO_MINI.put('f', "<white>");
        LEGACY_TO_MINI.put('k', "<obfuscated>");
        LEGACY_TO_MINI.put('l', "<bold>");
        LEGACY_TO_MINI.put('m', "<strikethrough>");
        LEGACY_TO_MINI.put('n', "<underlined>");
        LEGACY_TO_MINI.put('o', "<italic>");
        LEGACY_TO_MINI.put('r', "<reset>");
    }
    
    private static boolean placeholderAPIEnabled = false;
    
    public static void initialize() {
        placeholderAPIEnabled = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
        if (placeholderAPIEnabled) {
            Bukkit.getLogger().info("[LiseryMenu] PlaceholderAPI найден! Поддержка плейсхолдеров включена.");
        }
    }
    
    public static String replacePlaceholders(String text, Player player) {
        if (text == null) {
            return "";
        }
        
        if (player != null) {
            text = text.replace("%player%", player.getName())
                    .replace("%player_name%", player.getName())
                    .replace("%player_uuid%", player.getUniqueId().toString())
                    .replace("%player_level%", String.valueOf(player.getLevel()))
                    .replace("%player_health%", String.valueOf((int) player.getHealth()))
                    .replace("%player_food%", String.valueOf(player.getFoodLevel()))
                    .replace("%player_world%", player.getWorld().getName())
                    .replace("%player_gamemode%", player.getGameMode().name())
                    .replace("%player_exp%", String.valueOf(player.getExp()));
        }

        if (placeholderAPIEnabled && player != null) {
            try {
                Class<?> papiClass = Class.forName("me.clip.placeholderapi.PlaceholderAPI");
                text = (String) papiClass.getMethod("setPlaceholders", org.bukkit.OfflinePlayer.class, String.class)
                        .invoke(null, player, text);
            } catch (Exception e) {
                Bukkit.getLogger().warning("[LiseryMenu] PlaceholderAPI не работает: " + e.getMessage());
            }
        }

        return text;
    }

    public static boolean isPlaceholderAPIEnabled() {
        return placeholderAPIEnabled;
    }

    public static Component parseComponent(String text, Player player) {
        if (text == null || text.isEmpty()) {
            return Component.empty();
        }

        String processed = text;
        if (player != null) {
            processed = replacePlaceholders(processed, player);
        }

        processed = convertLegacyToMiniMessage(processed);

        try {
            return MINI_MESSAGE.deserialize(processed);
        } catch (Exception e) {
            return LegacyComponentSerializer.legacyAmpersand().deserialize(processed);
        }
    }

    public static Component parseComponent(String text) {
        return parseComponent(text, null);
    }

    private static String convertLegacyToMiniMessage(String input) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char current = input.charAt(i);
            if (current == '&' && i + 1 < input.length()) {
                if (i + 7 <= input.length()) {
                    String hex = input.substring(i + 1, i + 7);
                    if (hex.matches("(?i)[0-9a-f]{6}")) {
                        result.append("<#").append(hex.toLowerCase(Locale.ROOT)).append(">");
                        i += 6;
                        continue;
                    }
                }

                char code = Character.toLowerCase(input.charAt(i + 1));
                String replacement = LEGACY_TO_MINI.get(code);
                if (replacement != null) {
                    result.append(replacement);
                    i++;
                    continue;
                }
            }

            result.append(current);
        }

        return result.toString();
    }
    
    public static boolean evaluateCondition(String condition, Player player) {
        if (condition == null || condition.trim().isEmpty()) {
            return true;
        }
        
        condition = replacePlaceholders(condition.trim(), player);
        
        if (condition.startsWith("permission:")) {
            String permission = condition.substring("permission:".length()).trim();
            return player.hasPermission(permission);
        }
        
        if (condition.startsWith("has_item:")) {
            String itemCheck = condition.substring("has_item:".length()).trim();
            return checkHasItem(player, itemCheck);
        }
        
        try {
            if (condition.contains(">=")) {
                String[] parts = condition.split(">=");
                double left = parseNumber(parts[0].trim());
                double right = parseNumber(parts[1].trim());
                return left >= right;
            } else if (condition.contains("<=")) {
                String[] parts = condition.split("<=");
                double left = parseNumber(parts[0].trim());
                double right = parseNumber(parts[1].trim());
                return left <= right;
            } else if (condition.contains(">")) {
                String[] parts = condition.split(">");
                double left = parseNumber(parts[0].trim());
                double right = parseNumber(parts[1].trim());
                return left > right;
            } else if (condition.contains("<")) {
                String[] parts = condition.split("<");
                double left = parseNumber(parts[0].trim());
                double right = parseNumber(parts[1].trim());
                return left < right;
            } else if (condition.contains("==")) {
                String[] parts = condition.split("==");
                String left = parts[0].trim();
                String right = parts[1].trim();
                return left.equals(right);
            } else if (condition.contains("!=")) {
                String[] parts = condition.split("!=");
                String left = parts[0].trim();
                String right = parts[1].trim();
                return !left.equals(right);
            }
        } catch (Exception e) {
            return false;
        }
        
        return true;
    }
    
    private static boolean checkHasItem(Player player, String itemCheck) {
        String[] parts = itemCheck.split(" ");
        if (parts.length < 2) {
            return false;
        }
        
        String materialStr = parts[0].trim();
        int requiredAmount;
        try {
            requiredAmount = Integer.parseInt(parts[1].trim());
        } catch (NumberFormatException e) {
            return false;
        }
        
        if (materialStr.startsWith("ia:")) {
            return checkHasCustomItem(player, materialStr, requiredAmount, "ItemsAdder");
        }
        
        if (materialStr.startsWith("oraxen-")) {
            return checkHasCustomItem(player, materialStr, requiredAmount, "Oraxen");
        }
        
        if (materialStr.startsWith("nexo-")) {
            return checkHasCustomItem(player, materialStr, requiredAmount, "Nexo");
        }
        
        try {
            org.bukkit.Material material = org.bukkit.Material.valueOf(materialStr.toUpperCase());
            int count = 0;
            for (org.bukkit.inventory.ItemStack item : player.getInventory().getContents()) {
                if (item != null && item.getType() == material) {
                    count += item.getAmount();
                }
            }
            return count >= requiredAmount;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    private static boolean checkHasCustomItem(Player player, String materialStr, int requiredAmount, String pluginType) {
        try {
            if (pluginType.equals("ItemsAdder") && materialStr.startsWith("ia:")) {
                if (Bukkit.getPluginManager().getPlugin("ItemsAdder") == null) return false;
                
                String itemId = materialStr.substring("ia:".length()).replace("-", ":");
                Class<?> customStackClass = Class.forName("dev.lone.itemsadder.api.CustomStack");
                
                int count = 0;
                for (org.bukkit.inventory.ItemStack item : player.getInventory().getContents()) {
                    if (item != null) {
                        try {
                            Object customStack = customStackClass.getMethod("byItemStack", org.bukkit.inventory.ItemStack.class)
                                    .invoke(null, item);
                            if (customStack != null) {
                                String namespacedId = (String) customStackClass.getMethod("getNamespacedID")
                                        .invoke(customStack);
                                if (namespacedId != null && namespacedId.equals(itemId)) {
                                    count += item.getAmount();
                                }
                            }
                        } catch (Exception ignored) {}
                    }
                }
                return count >= requiredAmount;
                
            } else if (pluginType.equals("Oraxen") && materialStr.startsWith("oraxen-")) {
                if (Bukkit.getPluginManager().getPlugin("Oraxen") == null) return false;
                
                String itemId = materialStr.substring("oraxen-".length());
                Class<?> oraxenItemsClass = Class.forName("io.th0rgal.oraxen.api.OraxenItems");
                
                int count = 0;
                for (org.bukkit.inventory.ItemStack item : player.getInventory().getContents()) {
                    if (item != null) {
                        try {
                            String id = (String) oraxenItemsClass.getMethod("getIdByItem", org.bukkit.inventory.ItemStack.class)
                                    .invoke(null, item);
                            if (id != null && id.equals(itemId)) {
                                count += item.getAmount();
                            }
                        } catch (Exception ignored) {}
                    }
                }
                return count >= requiredAmount;
                
            } else if (pluginType.equals("Nexo") && materialStr.startsWith("nexo-")) {
                if (Bukkit.getPluginManager().getPlugin("Nexo") == null) return false;
                
                String itemId = materialStr.substring("nexo-".length());
                Class<?> nexoItemsClass = Class.forName("com.nexomc.nexo.api.NexoItems");
                
                int count = 0;
                for (org.bukkit.inventory.ItemStack item : player.getInventory().getContents()) {
                    if (item != null) {
                        try {
                            String id = (String) nexoItemsClass.getMethod("idFromItem", org.bukkit.inventory.ItemStack.class)
                                    .invoke(null, item);
                            if (id != null && id.equals(itemId)) {
                                count += item.getAmount();
                            }
                        } catch (Exception ignored) {}
                    }
                }
                return count >= requiredAmount;
            }
        } catch (Exception e) {
            Bukkit.getLogger().warning("[LiseryMenu] Ошибка проверки " + pluginType + " предмета: " + e.getMessage());
        }
        
        return false;
    }
    
    private static double parseNumber(String str) {
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}

