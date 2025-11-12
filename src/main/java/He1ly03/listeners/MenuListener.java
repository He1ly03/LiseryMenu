package He1ly03.listeners;

import He1ly03.LiseryMenu;
import He1ly03.menu.ConditionalAction;
import He1ly03.menu.Menu;
import He1ly03.menu.MenuItem;
import He1ly03.menu.MenuManager;
import He1ly03.menu.MenuUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import java.util.List;

public class MenuListener implements Listener {
    private final LiseryMenu plugin;
    private final MenuManager menuManager;

    public MenuListener(LiseryMenu plugin) {
        this.plugin = plugin;
        this.menuManager = plugin.getMenuManager();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        String menuName = menuManager.getOpenMenu(player);
        if (menuName == null) {
            return;
        }

        Menu menu = menuManager.getMenu(menuName);
        if (menu == null) {
            return;
        }

        event.setCancelled(true);

        int slot = event.getRawSlot();
        if (slot < 0 || slot >= menu.getSize()) {
            return;
        }

        MenuItem menuItem = menuManager.getActiveMenuItem(player, slot);
        if (menuItem == null) {
            return;
        }

        if (!menuItem.checkRequirements(player)) {
            player.sendMessage("§cУ вас нет доступа к этому предмету!");
            return;
        }

        org.bukkit.event.inventory.ClickType clickType = event.getClick();
        ConditionalAction conditional = null;
        List<String> actions = null;

        if (event.isLeftClick()) {
            if (event.isShiftClick()) {
                conditional = menuItem.getShiftLeftClickConditional();
                actions = menuItem.getShiftLeftClickActions();
            } else {
                conditional = menuItem.getLeftClickConditional();
                actions = menuItem.getLeftClickActions();
            }
        } else if (event.isRightClick()) {
            conditional = menuItem.getRightClickConditional();
            actions = menuItem.getRightClickActions();
        } else if (clickType == org.bukkit.event.inventory.ClickType.MIDDLE) {
            conditional = menuItem.getMiddleClickConditional();
            actions = menuItem.getMiddleClickActions();
        }

        executeClick(player, conditional, actions);
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        if (menuManager.isInMenu(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) {
            return;
        }

        menuManager.handleInventoryClose(player);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        String menuName = menuManager.getOpenMenu(player);
        
        if (menuName != null) {
            Menu menu = menuManager.getMenu(menuName);
            if (menu != null && menu.isHideItems()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        String menuName = menuManager.getOpenMenu(player);
        
        if (menuName != null) {
            Menu menu = menuManager.getMenu(menuName);
            if (menu != null && menu.isHideItems()) {
                event.setCancelled(true);
            }
        }
    }

    private void executeActions(Player player, List<String> actions) {
        for (String action : actions) {
            executeAction(player, action);
        }
    }

    private void executeClick(Player player, ConditionalAction conditional, List<String> fallbackActions) {
        if (conditional != null) {
            List<String> actionsToExecute = conditional.getActionsToExecute(player);
            if (actionsToExecute != null && !actionsToExecute.isEmpty()) {
                executeActions(player, actionsToExecute);
                return;
            }
        }

        if (fallbackActions != null && !fallbackActions.isEmpty()) {
            executeActions(player, fallbackActions);
        }
    }

    private void executeAction(Player player, String action) {
        action = MenuUtils.replacePlaceholders(action, player);
        action = action.trim();

        if (action.equalsIgnoreCase("[close]")) {
            menuManager.closeMenu(player);
        } else if (action.startsWith("[open_menu]")) {
            String menuName = action.substring("[open_menu]".length()).trim();
            Bukkit.getScheduler().runTask(plugin, () -> menuManager.openMenu(player, menuName));
        } else if (action.startsWith("[player]")) {
            String command = action.substring("[player]".length()).trim();
            Bukkit.getScheduler().runTask(plugin, () -> player.performCommand(command));
        } else if (action.startsWith("[console]")) {
            String command = action.substring("[console]".length()).trim();
            Bukkit.getScheduler().runTask(plugin, () -> 
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
        } else if (action.startsWith("[message]")) {
            String message = action.substring("[message]".length()).trim();
            player.sendMessage(MenuUtils.parseComponent(message, player));
        } else if (action.startsWith("[broadcast]")) {
            String message = action.substring("[broadcast]".length()).trim();
            Component messageComponent = MenuUtils.parseComponent(message);
            Bukkit.broadcast(messageComponent);
        } else if (action.startsWith("[sound]")) {
            String soundStr = action.substring("[sound]".length()).trim();
            playSound(player, soundStr);
        } else if (action.equalsIgnoreCase("[refresh]")) {
            String menuName = menuManager.getOpenMenu(player);
            if (menuName != null) {
                Bukkit.getScheduler().runTask(plugin, () -> menuManager.openMenu(player, menuName));
            }
        }
    }

    private void playSound(Player player, String soundStr) {
        try {
            String[] parts = soundStr.split(",");
            String soundName = parts[0].trim();
            float volume = parts.length > 1 ? Float.parseFloat(parts[1].trim()) : 1.0f;
            float pitch = parts.length > 2 ? Float.parseFloat(parts[2].trim()) : 1.0f;
            
            player.playSound(player.getLocation(), soundName, volume, pitch);
        } catch (Exception e) {
            plugin.getLogger().warning("Не удалось воспроизвести звук: " + soundStr);
        }
    }
}

