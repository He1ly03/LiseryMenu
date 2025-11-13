package He1ly03.commands;

import He1ly03.LiseryMenu;
import He1ly03.menu.Menu;
import He1ly03.menu.MenuManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MenuCommandHandler implements CommandExecutor {
    private final MenuManager menuManager;
    private final String menuName;

    public MenuCommandHandler(LiseryMenu plugin, String menuName) {
        this.menuManager = plugin.getMenuManager();
        this.menuName = menuName;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cЭта команда доступна только игрокам!");
            return true;
        }

        Menu menu = menuManager.getMenu(menuName);
        if (menu == null) {
            player.sendMessage("§cМеню не найдено!");
            return true;
        }

        menuManager.openMenu(player, menuName);
        return true;
    }
}

