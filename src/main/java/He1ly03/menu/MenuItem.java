package He1ly03.menu;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MenuItem {
    private final String materialString;
    private final Material material;
    private final int amount;
    private final String displayName;
    private final List<String> lore;
    private final int customModelData;
    private final boolean glowing;
    private final String skullOwner;
    private final String leatherColor;
    private final int durability;
    private final List<String> leftClickActions;
    private final List<String> rightClickActions;
    private final List<String> middleClickActions;
    private final List<String> shiftLeftClickActions;
    private final List<String> clickRequirements;
    private final Map<String, Integer> enchantments;
    private final ConditionalAction leftClickConditional;
    private final ConditionalAction rightClickConditional;
    private final ConditionalAction middleClickConditional;
    private final ConditionalAction shiftLeftClickConditional;

    public MenuItem(String materialString, Material material, int amount, String displayName, List<String> lore,
                    int customModelData, boolean glowing, String skullOwner, String leatherColor, int durability,
                    List<String> leftClickActions, List<String> rightClickActions,
                    List<String> middleClickActions, List<String> shiftLeftClickActions,
                    List<String> clickRequirements, Map<String, Integer> enchantments,
                    ConditionalAction leftClickConditional, ConditionalAction rightClickConditional,
                    ConditionalAction middleClickConditional, ConditionalAction shiftLeftClickConditional) {
        this.materialString = materialString;
        this.material = material;
        this.amount = amount;
        this.displayName = displayName;
        this.lore = lore;
        this.customModelData = customModelData;
        this.glowing = glowing;
        this.skullOwner = skullOwner;
        this.leatherColor = leatherColor;
        this.durability = durability;
        this.leftClickActions = leftClickActions;
        this.rightClickActions = rightClickActions;
        this.middleClickActions = middleClickActions;
        this.shiftLeftClickActions = shiftLeftClickActions;
        this.clickRequirements = clickRequirements;
        this.enchantments = enchantments;
        this.leftClickConditional = leftClickConditional;
        this.rightClickConditional = rightClickConditional;
        this.middleClickConditional = middleClickConditional;
        this.shiftLeftClickConditional = shiftLeftClickConditional;
    }

    @SuppressWarnings("deprecation")
    public ItemStack createItemStack(Player player) {
        ItemStack item = createBaseItem();
        if (item == null) {
            item = new ItemStack(Material.STONE, amount);
        }
        
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            if (displayName != null && !displayName.isEmpty()) {
                Component nameComponent = MenuUtils.parseComponent(displayName, player);
                meta.displayName(nameComponent);
            }

            if (lore != null && !lore.isEmpty()) {
                List<Component> loreComponents = new ArrayList<>();
                for (String line : lore) {
                    loreComponents.add(MenuUtils.parseComponent(line, player));
                }
                meta.lore(loreComponents);
            }

            if (customModelData > 0) {
                meta.setCustomModelData(customModelData);
            }

            if (glowing) {
                meta.addEnchant(Enchantment.UNBREAKING, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }

            if (enchantments != null && !enchantments.isEmpty()) {
                for (Map.Entry<String, Integer> entry : enchantments.entrySet()) {
                    try {
                        Enchantment enchantment = org.bukkit.Registry.ENCHANTMENT.get(org.bukkit.NamespacedKey.minecraft(entry.getKey().toLowerCase()));
                        if (enchantment != null) {
                            meta.addEnchant(enchantment, entry.getValue(), true);
                        }
                    } catch (Exception e) {
                    }
                }
            }

            if (meta instanceof SkullMeta && skullOwner != null && !skullOwner.isEmpty()) {
                SkullMeta skullMeta = (SkullMeta) meta;
                String processedOwner = MenuUtils.replacePlaceholders(skullOwner, player);
                org.bukkit.OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(processedOwner);
                skullMeta.setOwningPlayer(offlinePlayer);
            }
            
            if (meta instanceof org.bukkit.inventory.meta.LeatherArmorMeta && leatherColor != null && !leatherColor.isEmpty()) {
                org.bukkit.inventory.meta.LeatherArmorMeta leatherMeta = (org.bukkit.inventory.meta.LeatherArmorMeta) meta;
                org.bukkit.Color color = parseColor(leatherColor);
                if (color != null) {
                    leatherMeta.setColor(color);
                }
            }

            item.setItemMeta(meta);
        }
        
        if (durability > 0 && item.getType().getMaxDurability() > 0) {
            org.bukkit.inventory.meta.Damageable damageable = (org.bukkit.inventory.meta.Damageable) item.getItemMeta();
            if (damageable != null) {
                int maxDurability = item.getType().getMaxDurability();
                int damage = maxDurability - durability;
                if (damage >= 0 && damage < maxDurability) {
                    damageable.setDamage(damage);
                    item.setItemMeta((ItemMeta) damageable);
                }
            }
        }

        return item;
    }
    
    private org.bukkit.Color parseColor(String colorStr) {
        try {
            colorStr = colorStr.trim();

            if (colorStr.startsWith("#")) {
                String hex = colorStr.substring(1);
                int rgb = Integer.parseInt(hex, 16);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                return org.bukkit.Color.fromRGB(r, g, b);
            }
            
            if (colorStr.contains(",")) {
                String[] parts = colorStr.split(",");
                if (parts.length == 3) {
                    int r = Integer.parseInt(parts[0].trim());
                    int g = Integer.parseInt(parts[1].trim());
                    int b = Integer.parseInt(parts[2].trim());
                    return org.bukkit.Color.fromRGB(r, g, b);
                }
            }
            
            switch (colorStr.toUpperCase()) {
                case "RED": return org.bukkit.Color.RED;
                case "BLUE": return org.bukkit.Color.BLUE;
                case "GREEN": return org.bukkit.Color.GREEN;
                case "YELLOW": return org.bukkit.Color.YELLOW;
                case "ORANGE": return org.bukkit.Color.ORANGE;
                case "PURPLE": return org.bukkit.Color.PURPLE;
                case "WHITE": return org.bukkit.Color.WHITE;
                case "BLACK": return org.bukkit.Color.BLACK;
                case "GRAY": return org.bukkit.Color.GRAY;
                case "AQUA": return org.bukkit.Color.AQUA;
                case "FUCHSIA": return org.bukkit.Color.FUCHSIA;
                case "LIME": return org.bukkit.Color.LIME;
                case "MAROON": return org.bukkit.Color.MAROON;
                case "NAVY": return org.bukkit.Color.NAVY;
                case "OLIVE": return org.bukkit.Color.OLIVE;
                case "SILVER": return org.bukkit.Color.SILVER;
                case "TEAL": return org.bukkit.Color.TEAL;
            }
        } catch (Exception e) {
            Bukkit.getLogger().warning("[LiseryMenu] Ошибка парсинга цвета: " + colorStr);
        }
        return null;
    }
    
    private ItemStack createBaseItem() {
        if (materialString == null) {
            return material != null ? new ItemStack(material, amount) : null;
        }

        if (materialString.startsWith("ia:")) {
            ItemStack item = loadItemsAdderItem(materialString.substring("ia:".length()));
            if (item != null) return item;
        }

        if (materialString.startsWith("oraxen-")) {
            ItemStack item = loadOraxenItem(materialString.substring("oraxen-".length()));
            if (item != null) return item;
        }

        if (materialString.startsWith("nexo-")) {
            ItemStack item = loadNexoItem(materialString.substring("nexo-".length()));
            if (item != null) return item;
        }

        if (material != null) {
            return new ItemStack(material, amount);
        }

        return null;
    }
    
    private ItemStack loadItemsAdderItem(String itemId) {
        itemId = itemId.replace("-", ":");

        try {
            if (Bukkit.getPluginManager().getPlugin("ItemsAdder") != null) {
                Class<?> customStackClass = Class.forName("dev.lone.itemsadder.api.CustomStack");
                
                Object customStackInstance = null;
                try {
                    customStackInstance = customStackClass.getMethod("getInstance", String.class).invoke(null, itemId);
                } catch (NoSuchMethodException e) {
                    try {
                        customStackInstance = customStackClass.getMethod("getByName", String.class).invoke(null, itemId);
                    } catch (Exception ignored) {}
                }
                
                if (customStackInstance != null) {
                    ItemStack customItem = (ItemStack) customStackClass.getMethod("getItemStack").invoke(customStackInstance);
                    if (customItem != null) {
                        customItem.setAmount(amount);
                        return customItem;
                    }
                }
            }
        } catch (Exception e) {
            Bukkit.getLogger().warning("[LiseryMenu] Ошибка загрузки ItemsAdder предмета: " + itemId);
        }
        return null;
    }
    
    private ItemStack loadOraxenItem(String itemId) {
        try {
            if (Bukkit.getPluginManager().getPlugin("Oraxen") != null) {
                Class<?> oraxenItemsClass = Class.forName("io.th0rgal.oraxen.api.OraxenItems");
                
                Object itemBuilder = oraxenItemsClass.getMethod("getItemById", String.class).invoke(null, itemId);
                
                if (itemBuilder != null) {
                    Class<?> itemBuilderClass = Class.forName("io.th0rgal.oraxen.items.ItemBuilder");
                    ItemStack customItem = (ItemStack) itemBuilderClass.getMethod("build").invoke(itemBuilder);
                    
                    if (customItem != null) {
                        customItem.setAmount(amount);
                        return customItem;
                    }
                }
            }
        } catch (Exception e) {
            Bukkit.getLogger().warning("[LiseryMenu] Ошибка загрузки Oraxen предмета: " + itemId);
        }
        return null;
    }
    
    private ItemStack loadNexoItem(String itemId) {
        try {
            if (Bukkit.getPluginManager().getPlugin("Nexo") != null) {
                Class<?> nexoItemsClass = Class.forName("com.nexomc.nexo.api.NexoItems");
                
                ItemStack customItem = (ItemStack) nexoItemsClass.getMethod("itemFromId", String.class).invoke(null, itemId);
                
                if (customItem != null) {
                    customItem.setAmount(amount);
                    return customItem;
                }
            }
        } catch (Exception e) {
            Bukkit.getLogger().warning("[LiseryMenu] Ошибка загрузки Nexo предмета: " + itemId);
        }
        return null;
    }

    public List<String> getLeftClickActions() {
        return leftClickActions;
    }

    public List<String> getRightClickActions() {
        return rightClickActions;
    }
    
    public List<String> getMiddleClickActions() {
        return middleClickActions;
    }
    
    public List<String> getShiftLeftClickActions() {
        return shiftLeftClickActions;
    }
    
    public ConditionalAction getLeftClickConditional() {
        return leftClickConditional;
    }
    
    public ConditionalAction getRightClickConditional() {
        return rightClickConditional;
    }
    
    public ConditionalAction getMiddleClickConditional() {
        return middleClickConditional;
    }
    
    public ConditionalAction getShiftLeftClickConditional() {
        return shiftLeftClickConditional;
    }
    public List<String> getClickRequirements() {
        return clickRequirements;
    }

    public boolean checkRequirements(Player player) {
        if (clickRequirements == null || clickRequirements.isEmpty()) {
            return true;
        }

        for (String requirement : clickRequirements) {
            if (!checkRequirement(player, requirement)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkRequirement(Player player, String requirement) {
        requirement = requirement.trim();

        if (requirement.startsWith("permission:")) {
            String permission = requirement.substring("permission:".length()).trim();
            return player.hasPermission(permission);
        }

        return true;
    }
}

