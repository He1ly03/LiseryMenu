package He1ly03;

import He1ly03.commands.LiseryMenuCommand;
import He1ly03.commands.MenuCommandHandler;
import He1ly03.listeners.MenuListener;
import He1ly03.menu.Menu;
import He1ly03.menu.MenuManager;
import He1ly03.menu.MenuUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public final class LiseryMenu extends JavaPlugin {

    private MenuManager menuManager;
    private final Map<String, org.bukkit.command.Command> registeredMenuCommands = new HashMap<>();

    @Override
    public void onEnable() {
        MenuUtils.initialize();
        
        if (Bukkit.getPluginManager().getPlugin("ItemsAdder") != null) {
            getLogger().info("ItemsAdder найден! Поддержка кастомных предметов включена.");
        }
        
        menuManager = new MenuManager(this);
        
        menuManager.loadMenus();
        
        getServer().getPluginManager().registerEvents(new MenuListener(this), this);
        
        LiseryMenuCommand mainCommand = new LiseryMenuCommand(this);
        getCommand("lm").setExecutor(mainCommand);
        getCommand("lm").setTabCompleter(mainCommand);
        
        registerMenuCommands();
        
        getLogger().info("LiseryMenu успешно загружен!");
        getLogger().info("Загружено меню: " + menuManager.getMenus().size());
    }

    @Override
    public void onDisable() {
        getLogger().info("LiseryMenu выключен!");
    }

    public MenuManager getMenuManager() {
        return menuManager;
    }

    private void registerMenuCommands() {
        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());

            for (Menu menu : menuManager.getMenus().values()) {
                String openCommand = menu.getOpenCommand();
                if (openCommand != null && !openCommand.isEmpty()) {
                    registerCommand(commandMap, menu.getName(), openCommand);
                }
            }
        } catch (Exception e) {
            getLogger().severe("Ошибка при регистрации команд меню: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void registerSingleMenuCommand(String menuName) {
        try {
            Menu menu = menuManager.getMenu(menuName);
            if (menu == null) {
                return;
            }

            String openCommand = menu.getOpenCommand();
            if (openCommand == null || openCommand.isEmpty()) {
                return;
            }

            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());

            registerCommand(commandMap, menu.getName(), openCommand);
            
            getLogger().info("Зарегистрирована команда: /" + openCommand + " для меню " + menu.getName());
        } catch (Exception e) {
            getLogger().severe("Ошибка при регистрации команды меню: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void registerCommand(CommandMap commandMap, String menuName, String commandName) {
        org.bukkit.command.defaults.BukkitCommand command = new org.bukkit.command.defaults.BukkitCommand(commandName) {
            @Override
            public boolean execute(org.bukkit.command.CommandSender sender, String label, String[] args) {
                MenuCommandHandler handler = new MenuCommandHandler(LiseryMenu.this, menuName);
                return handler.onCommand(sender, this, label, args);
            }
        };
        
        command.setDescription("Открыть меню " + menuName);
        command.setPermission("liserymenu.open." + menuName);
        commandMap.register("liserymenu", command);
        
        registeredMenuCommands.put(commandName, command);
    }

    public void reloadMenuCommands() {
        getLogger().info("Перерегистрация команд меню...");
        
        unregisterMenuCommands();
        
        registerMenuCommands();
        
        getLogger().info("Команды меню перерегистрированы! Всего: " + registeredMenuCommands.size());
    }
    
    private void unregisterMenuCommands() {
        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
            
            if (commandMap instanceof SimpleCommandMap) {
                Field knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
                knownCommandsField.setAccessible(true);
                @SuppressWarnings("unchecked")
                Map<String, org.bukkit.command.Command> knownCommands = 
                    (Map<String, org.bukkit.command.Command>) knownCommandsField.get(commandMap);
                
                for (String commandName : registeredMenuCommands.keySet()) {
                    knownCommands.remove(commandName);
                    knownCommands.remove("liserymenu:" + commandName);
                    getLogger().info("Удалена команда: /" + commandName);
                }
                
                registeredMenuCommands.clear();
            }
        } catch (Exception e) {
            getLogger().warning("Ошибка при удалении старых команд: " + e.getMessage());
        }
    }
}
