package He1ly03.menu;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class Menu {
    private final String name;
    private final String title;
    private final int size;
    private final boolean hideItems;
    private final String openCommand;
    private final List<MenuManager.MenuItemDefinition> itemDefinitions;
    private final List<String> openRequirements;
    private final List<String> openCommands;

    public Menu(String name, String title, int size, boolean hideItems, String openCommand,
                List<MenuManager.MenuItemDefinition> itemDefinitions, List<String> openRequirements, List<String> openCommands) {
        this.name = name;
        this.title = title;
        this.size = size;
        this.hideItems = hideItems;
        this.openCommand = openCommand;
        this.itemDefinitions = itemDefinitions;
        this.openRequirements = openRequirements;
        this.openCommands = openCommands;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public int getSize() {
        return size;
    }

    public boolean isHideItems() {
        return hideItems;
    }

    public String getOpenCommand() {
        return openCommand;
    }

    public List<MenuManager.MenuItemDefinition> getItemDefinitions() {
        return itemDefinitions;
    }

    public List<String> getOpenRequirements() {
        return openRequirements;
    }

    public List<String> getOpenCommands() {
        return openCommands;
    }

    public Map<Integer, MenuItem> resolveItems(Player player, MenuManager manager) {
        Map<Integer, MenuItem> resolved = new java.util.HashMap<>();
        if (itemDefinitions == null || itemDefinitions.isEmpty()) {
            return resolved;
        }

        for (MenuManager.MenuItemDefinition definition : itemDefinitions) {
            MenuManager.ResolvedItem resolvedItem = definition.resolve(player, manager);
            if (resolvedItem == null) {
                continue;
            }

            List<Integer> slots = resolvedItem.slots();
            MenuItem menuItem = resolvedItem.menuItem();
            if (slots == null || menuItem == null) {
                continue;
            }

            for (int slot : slots) {
                resolved.put(slot, menuItem);
            }
        }

        return resolved;
    }

    public Inventory createInventory(Player player, Map<Integer, MenuItem> resolvedItems) {
        Component titleComponent = MenuUtils.parseComponent(title, player);
        Inventory inventory = Bukkit.createInventory(null, size, titleComponent);

        for (Map.Entry<Integer, MenuItem> entry : resolvedItems.entrySet()) {
            int slot = entry.getKey();
            MenuItem menuItem = entry.getValue();
            
            if (slot >= 0 && slot < size) {
                ItemStack itemStack = menuItem.createItemStack(player);
                inventory.setItem(slot, itemStack);
            }
        }

        return inventory;
    }

    public void executeOpenCommands(Player player) {
        if (openCommands != null && !openCommands.isEmpty()) {
            for (String command : openCommands) {
                String processedCommand = MenuUtils.replacePlaceholders(command, player);
                executeCommand(player, processedCommand);
            }
        }
    }

    private void executeCommand(Player player, String command) {
        if (command.startsWith("[player]")) {
            String cmd = command.substring("[player]".length()).trim();
            player.performCommand(cmd);
        } else if (command.startsWith("[console]")) {
            String cmd = command.substring("[console]".length()).trim();
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
        } else {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        }
    }

    public boolean checkRequirements(Player player) {
        if (openRequirements == null || openRequirements.isEmpty()) {
            return true;
        }

        for (String requirement : openRequirements) {
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

