package He1ly03.commands;

import He1ly03.LiseryMenu;
import He1ly03.menu.Menu;
import He1ly03.menu.MenuManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LiseryMenuCommand implements CommandExecutor, TabCompleter {
    private final LiseryMenu plugin;
    private final MenuManager menuManager;

    public LiseryMenuCommand(LiseryMenu plugin) {
        this.plugin = plugin;
        this.menuManager = plugin.getMenuManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "create":
                return handleCreate(sender, args);
            case "open":
                return handleOpen(sender, args);
            case "reload":
                return handleReload(sender);
            case "list":
                return handleList(sender);
            case "help":
                sendHelp(sender);
                return true;
            default:
                sender.sendMessage("§cНеизвестная команда! Используйте /lm help");
                return true;
        }
    }

    private boolean handleCreate(CommandSender sender, String[] args) {
        if (!sender.hasPermission("liserymenu.create")) {
            sender.sendMessage("§cУ вас нет прав для использования этой команды!");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage("§cИспользование: /lm create <название_меню>");
            return true;
        }

        String menuName = args[1].toLowerCase();

        if (menuManager.getMenu(menuName) != null) {
            sender.sendMessage("§cМеню с таким названием уже существует!");
            return true;
        }

        if (menuManager.createMenu(menuName)) {
            sender.sendMessage("§aМеню §e" + menuName + " §aуспешно создано!");
            sender.sendMessage("§7Файл находится в папке: §fplug ins/LiseryMenu/menus/" + menuName + ".yml");
        } else {
            sender.sendMessage("§cОшибка при создании меню!");
        }

        return true;
    }

    private boolean handleOpen(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cЭта команда доступна только игрокам!");
            return true;
        }

        if (!sender.hasPermission("liserymenu.open")) {
            sender.sendMessage("§cУ вас нет прав для использования этой команды!");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage("§cИспользование: /lm open <название_меню>");
            return true;
        }

        String menuName = args[1].toLowerCase();
        menuManager.openMenu(player, menuName);

        return true;
    }

    private boolean handleReload(CommandSender sender) {
        if (!sender.hasPermission("liserymenu.reload")) {
            sender.sendMessage("§cУ вас нет прав для использования этой команды!");
            return true;
        }

        long startTime = System.currentTimeMillis();
        
        menuManager.loadMenus();
        
        plugin.reloadMenuCommands();
        
        long endTime = System.currentTimeMillis();

        sender.sendMessage("§aПлагин LiseryMenu успешно перезагружен!");
        sender.sendMessage("§7Загружено меню: §e" + menuManager.getMenus().size());
        sender.sendMessage("§7Команды меню перерегистрированы!");
        sender.sendMessage("§7Время загрузки: §e" + (endTime - startTime) + "ms");

        return true;
    }

    private boolean handleList(CommandSender sender) {
        if (!sender.hasPermission("liserymenu.list")) {
            sender.sendMessage("§cУ вас нет прав для использования этой команды!");
            return true;
        }

        if (menuManager.getMenus().isEmpty()) {
            sender.sendMessage("§cНет загруженных меню!");
            return true;
        }

        sender.sendMessage("§7§m-----§r §6§lСписок меню §7§m-----");
        for (Menu menu : menuManager.getMenus().values()) {
            String openCmd = menu.getOpenCommand() != null ? " §7(/" + menu.getOpenCommand() + ")" : "";
            sender.sendMessage("§e- §f" + menu.getName() + openCmd);
        }
        sender.sendMessage("§7Всего меню: §e" + menuManager.getMenus().size());

        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("§7§m-----§r §6§lLiseryMenu §7§m-----");
        sender.sendMessage("§e/lm create <название> §7- Создать новое меню");
        sender.sendMessage("§e/lm open <название> §7- Открыть меню");
        sender.sendMessage("§e/lm reload §7- Перезагрузить все меню");
        sender.sendMessage("§e/lm list §7- Список всех меню");
        sender.sendMessage("§e/lm help §7- Показать эту справку");
        sender.sendMessage("§7Версия: §f" + plugin.getPluginMeta().getVersion());
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            List<String> subCommands = Arrays.asList("create", "open", "reload", "list", "help");
            return subCommands.stream()
                    .filter(cmd -> cmd.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("open")) {
                return menuManager.getMenus().keySet().stream()
                        .filter(name -> name.startsWith(args[1].toLowerCase()))
                        .collect(Collectors.toList());
            }
        }

        return completions;
    }
}

