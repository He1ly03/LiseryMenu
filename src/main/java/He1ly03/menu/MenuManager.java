package He1ly03.menu;

import He1ly03.LiseryMenu;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;

public class MenuManager {
    private final LiseryMenu plugin;
    private final Map<String, Menu> menus;
    private final Map<UUID, ItemStack[]> hiddenInventories;
    private final Map<UUID, String> openMenus;
    private final Map<UUID, Map<Integer, MenuItem>> activeMenuItems;

    public MenuManager(LiseryMenu plugin) {
        this.plugin = plugin;
        this.menus = new HashMap<>();
        this.hiddenInventories = new HashMap<>();
        this.openMenus = new HashMap<>();
        this.activeMenuItems = new HashMap<>();
    }

    public void loadMenus() {
        menus.clear();
        activeMenuItems.clear();
        File menusFolder = new File(plugin.getDataFolder(), "menus");
        
        if (!menusFolder.exists()) {
            menusFolder.mkdirs();
        }

        File[] files = menusFolder.listFiles((dir, name) -> name.endsWith(".yml"));
        if (files != null) {
            for (File file : files) {
                loadMenu(file);
            }
        }

        plugin.getLogger().info("Загружено " + menus.size() + " меню");
    }

    private void loadMenu(File file) {
        try {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            String menuName = file.getName().replace(".yml", "");

            String title = config.getString("title", "Menu");
            int size = config.getInt("size", 27);
            boolean hideItems = config.getBoolean("hide_items", false);
            String openCommand = config.getString("open_command", null);
            List<String> openRequirements = config.getStringList("open_requirements");
            List<String> openCommands = config.getStringList("open_commands");

            List<MenuItemDefinition> itemDefinitions = new ArrayList<>();
            ConfigurationSection itemsSection = config.getConfigurationSection("items");

            if (itemsSection != null) {
                for (String key : itemsSection.getKeys(false)) {
                    ConfigurationSection itemSection = itemsSection.getConfigurationSection(key);
                    if (itemSection == null) {
                        continue;
                    }

                    Map<String, Object> baseValues = extractBaseValues(itemSection);
                    DisplayCondition displayCondition = parseDisplayCondition(itemSection.getConfigurationSection("display_condition"));

                    itemDefinitions.add(new MenuItemDefinition(key, baseValues, displayCondition));
                }
            }

            Menu menu = new Menu(menuName, title, size, hideItems, openCommand, itemDefinitions, openRequirements, openCommands);
            menus.put(menuName.toLowerCase(), menu);

            if (openCommand != null && !openCommand.isEmpty()) {
                plugin.getLogger().info("Меню '" + menuName + "' будет открываться командой: /" + openCommand);
            }

        } catch (Exception e) {
            plugin.getLogger().severe("Ошибка при загрузке меню из файла " + file.getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private MenuItem loadMenuItem(ConfigurationSection section) {
        String materialStr = section.getString("material", "STONE");
        Material material = null;
        
        if (!materialStr.startsWith("ia:") && !materialStr.startsWith("oraxen-") && !materialStr.startsWith("nexo-")) {
            try {
                material = Material.valueOf(materialStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                material = Material.STONE;
                plugin.getLogger().warning("Неизвестный материал: " + materialStr + ", использован STONE");
            }
        }

        int amount = section.getInt("amount", 1);
        String displayName = section.getString("display_name", "");
        List<String> lore = section.getStringList("lore");
        int customModelData = section.getInt("custom_model_data", 0);
        boolean glowing = section.getBoolean("glowing", false);
        String skullOwner = section.getString("skull_owner", null);
        String leatherColor = section.getString("leather_color", null);
        int durability = section.getInt("durability", 0);
        List<String> clickRequirements = section.getStringList("click_requirements");

        Map<String, Integer> enchantments = new HashMap<>();
        ConfigurationSection enchSection = section.getConfigurationSection("enchantments");
        if (enchSection != null) {
            for (String enchKey : enchSection.getKeys(false)) {
                enchantments.put(enchKey, enchSection.getInt(enchKey));
            }
        }

        ConditionalAction leftClickConditional = parseConditionalActions(section, "left_click_actions");
        List<String> leftClickActions = leftClickConditional == null ? 
            section.getStringList("left_click_actions") : new ArrayList<>();

        ConditionalAction rightClickConditional = parseConditionalActions(section, "right_click_actions");
        List<String> rightClickActions = rightClickConditional == null ?
            section.getStringList("right_click_actions") : new ArrayList<>();

        ConditionalAction middleClickConditional = parseConditionalActions(section, "middle_click_actions");
        List<String> middleClickActions = middleClickConditional == null ?
            section.getStringList("middle_click_actions") : new ArrayList<>();

        ConditionalAction shiftLeftClickConditional = parseConditionalActions(section, "shift_left_click_actions");
        List<String> shiftLeftClickActions = shiftLeftClickConditional == null ?
            section.getStringList("shift_left_click_actions") : new ArrayList<>();

        return new MenuItem(materialStr, material, amount, displayName, lore, customModelData, glowing,
                skullOwner, leatherColor, durability,
                leftClickActions, rightClickActions, middleClickActions,
                shiftLeftClickActions, clickRequirements, enchantments,
                leftClickConditional, rightClickConditional, middleClickConditional,
                shiftLeftClickConditional);
    }

    private Map<String, Object> extractBaseValues(ConfigurationSection section) {
        Map<String, Object> values = new LinkedHashMap<>();
        if (section == null) {
            return values;
        }

        for (String key : section.getKeys(false)) {
            if ("display_condition".equalsIgnoreCase(key)) {
                continue;
            }
            Object value = section.get(key);
            if (value instanceof ConfigurationSection childSection) {
                extractSectionValues(childSection, key, values);
            } else {
                values.put(key, value);
            }
        }
        return values;
    }

    private void extractSectionValues(ConfigurationSection section, String prefix, Map<String, Object> values) {
        if (section == null) {
            return;
        }

        for (String key : section.getKeys(false)) {
            Object value = section.get(key);
            String path = (prefix == null || prefix.isEmpty()) ? key : prefix + "." + key;

            if (value instanceof ConfigurationSection childSection) {
                extractSectionValues(childSection, path, values);
            } else {
                values.put(path, value);
            }
        }
    }

    private Map<String, Object> extractValuesFromSection(ConfigurationSection section) {
        Map<String, Object> values = new LinkedHashMap<>();
        if (section == null) {
            return values;
        }
        extractSectionValues(section, "", values);
        return values;
    }

    private Map<String, Object> extractValuesFromObject(Object obj) {
        if (obj instanceof ConfigurationSection configurationSection) {
            return extractValuesFromSection(configurationSection);
        }
        if (obj instanceof Map<?, ?> rawMap) {
            YamlConfiguration temp = new YamlConfiguration();
            for (Map.Entry<?, ?> entry : rawMap.entrySet()) {
                String key = entry.getKey().toString();
                temp.set(key, entry.getValue());
            }
            return extractValuesFromSection(temp);
        }
        return Collections.emptyMap();
    }

    private DisplayCondition parseDisplayCondition(ConfigurationSection section) {
        if (section == null) {
            return null;
        }

        List<DisplayConditionBlock> blocks = new ArrayList<>();
        Map<String, Object> elseValues = null;

        Object ifSource = extractConditionSource(section);
        Map<String, Object> thenValues = extractValuesFromObject(section.get("then"));
        List<String> ifConditions = parseConditionsList(ifSource);
        if (!ifConditions.isEmpty() && !thenValues.isEmpty()) {
            blocks.add(new DisplayConditionBlock(ifConditions, thenValues));
        }

        Object elifValue = section.get("elif");
        if (elifValue instanceof List<?> elifList) {
            for (Object entry : elifList) {
                if (entry instanceof Map<?, ?> elifMap) {
                    Object elifSource = extractConditionSource(elifMap);
                    List<String> elifConditions = parseConditionsList(elifSource);
                    Map<String, Object> elifThenValues = extractValuesFromObject(elifMap.get("then"));
                    if (!elifConditions.isEmpty() && !elifThenValues.isEmpty()) {
                        blocks.add(new DisplayConditionBlock(elifConditions, elifThenValues));
                    }
                }
            }
        } else if (elifValue instanceof ConfigurationSection elifSection) {
            Object elifSource = extractConditionSource(elifSection);
            List<String> elifConditions = parseConditionsList(elifSource);
            Map<String, Object> elifThenValues = extractValuesFromSection(elifSection.getConfigurationSection("then"));
            if (!elifConditions.isEmpty() && !elifThenValues.isEmpty()) {
                blocks.add(new DisplayConditionBlock(elifConditions, elifThenValues));
            }
        }

        int elifIndex = 1;
        while (section.contains("elif" + elifIndex)) {
            ConfigurationSection elifSection = section.getConfigurationSection("elif" + elifIndex);
            if (elifSection != null) {
                Object elifSource = extractConditionSource(elifSection);
                List<String> elifConditions = parseConditionsList(elifSource);
                Map<String, Object> elifThenValues = extractValuesFromSection(elifSection.getConfigurationSection("then"));
                if (!elifConditions.isEmpty() && !elifThenValues.isEmpty()) {
                    blocks.add(new DisplayConditionBlock(elifConditions, elifThenValues));
                }
            }
            elifIndex++;
        }

        Object elseObj = section.get("else");
        if (elseObj != null) {
            elseValues = extractValuesFromObject(elseObj);
        }

        if (blocks.isEmpty() && (elseValues == null || elseValues.isEmpty())) {
            return null;
        }

        return new DisplayCondition(blocks, elseValues);
    }

    private MenuItem createMenuItem(Map<String, Object> values) {
        YamlConfiguration temp = new YamlConfiguration();
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            if ("slot".equalsIgnoreCase(entry.getKey()) || "slots".equalsIgnoreCase(entry.getKey())) {
                continue;
            }
            temp.set(entry.getKey(), entry.getValue());
        }
        return loadMenuItem(temp);
    }

    private Object extractConditionSource(Map<?, ?> map) {
        if (map == null) {
            return null;
        }
        if (map.containsKey("if")) {
            return map.get("if");
        }
        if (map.containsKey("conditions")) {
            return map.get("conditions");
        }
        if (map.containsKey("condition")) {
            return map.get("condition");
        }
        if (map.containsKey("when")) {
            return map.get("when");
        }
        return null;
    }

    private Object extractConditionSource(ConfigurationSection section) {
        if (section == null) {
            return null;
        }
        if (section.contains("if")) {
            return section.get("if");
        }
        if (section.contains("conditions")) {
            return section.get("conditions");
        }
        if (section.contains("condition")) {
            return section.get("condition");
        }
        if (section.contains("when")) {
            return section.get("when");
        }
        return null;
    }

    private List<String> parseActionsList(Object actionsObj) {
        List<String> actions = new ArrayList<>();
        if (actionsObj == null) {
            return actions;
        }
        if (actionsObj instanceof List<?>) {
            for (Object action : (List<?>) actionsObj) {
                actions.add(action.toString());
            }
        } else if (actionsObj instanceof String) {
            actions.add(actionsObj.toString());
        } else if (actionsObj instanceof Map<?, ?> map) {
            Object nested = map.containsKey("actions") ? map.get("actions") : map.get("then");
            actions.addAll(parseActionsList(nested));
        }
        return actions;
    }

    public static class MenuItemDefinition {
        private final String key;
        private final Map<String, Object> baseValues;
        private final DisplayCondition displayCondition;

        public MenuItemDefinition(String key, Map<String, Object> baseValues, DisplayCondition displayCondition) {
            this.key = key;
            this.baseValues = baseValues != null ? new LinkedHashMap<>(baseValues) : new LinkedHashMap<>();
            this.displayCondition = displayCondition;
        }

        public ResolvedItem resolve(Player player, MenuManager manager) {
            Map<String, Object> combinedValues = new LinkedHashMap<>(baseValues);

            if (displayCondition != null) {
                DisplayCondition.DisplayConditionResult result =
                        displayCondition.resolve(player, !combinedValues.isEmpty());
                if (result.isHide()) {
                    return null;
                }
                combinedValues.putAll(result.values());
            }

            Object slotObj = combinedValues.containsKey("slot")
                    ? combinedValues.get("slot")
                    : combinedValues.get("slots");
            if (slotObj == null) {
                if (manager.plugin != null) {
                    manager.plugin.getLogger().warning("[LiseryMenu] Предмет '" + key + "' не имеет слота и будет пропущен.");
                }
                return null;
            }

            String slotString = normalizeSlotValue(slotObj);
            if (slotString == null || slotString.isEmpty()) {
                return null;
            }

            List<Integer> slots = manager.parseSlots(slotString);
            if (slots.isEmpty()) {
                return null;
            }

            Map<String, Object> itemValues = new LinkedHashMap<>(combinedValues);
            itemValues.remove("slot");
            itemValues.remove("slots");

            MenuItem menuItem = manager.createMenuItem(itemValues);
            if (menuItem == null) {
                return null;
            }

            return new ResolvedItem(slots, menuItem);
        }

        private String normalizeSlotValue(Object slotObj) {
            if (slotObj instanceof Number number) {
                return Integer.toString(number.intValue());
            }
            if (slotObj instanceof List<?> list) {
                StringBuilder builder = new StringBuilder();
                for (Object obj : list) {
                    if (builder.length() > 0) {
                        builder.append(",");
                    }
                    builder.append(obj.toString());
                }
                return builder.toString();
            }
            return slotObj.toString();
        }
    }

    public static class ResolvedItem {
        private final List<Integer> slots;
        private final MenuItem menuItem;

        public ResolvedItem(List<Integer> slots, MenuItem menuItem) {
            this.slots = slots;
            this.menuItem = menuItem;
        }

        public List<Integer> slots() {
            return slots;
        }

        public MenuItem menuItem() {
            return menuItem;
        }
    }

    public static class DisplayCondition {
        private final List<DisplayConditionBlock> blocks;
        private final Map<String, Object> elseValues;

        public DisplayCondition(List<DisplayConditionBlock> blocks, Map<String, Object> elseValues) {
            this.blocks = blocks != null ? blocks : new ArrayList<>();
            this.elseValues = elseValues != null ? elseValues : Collections.emptyMap();
        }

        public DisplayConditionResult resolve(Player player, boolean hasBaseValues) {
            for (DisplayConditionBlock block : blocks) {
                if (block.matches(player)) {
                    return DisplayConditionResult.show(block.values());
                }
            }

            if (!elseValues.isEmpty()) {
                return DisplayConditionResult.show(elseValues);
            }

            if (hasBaseValues) {
                return DisplayConditionResult.show(Collections.emptyMap());
            }

            return DisplayConditionResult.hide();
        }

        public static class DisplayConditionResult {
            private static final DisplayConditionResult HIDE =
                    new DisplayConditionResult(true, Collections.emptyMap());
            private final boolean hide;
            private final Map<String, Object> values;

            private DisplayConditionResult(boolean hide, Map<String, Object> values) {
                this.hide = hide;
                this.values = values != null ? values : Collections.emptyMap();
            }

            public static DisplayConditionResult hide() {
                return HIDE;
            }

            public static DisplayConditionResult show(Map<String, Object> values) {
                return new DisplayConditionResult(false, values);
            }

            public boolean isHide() {
                return hide;
            }

            public Map<String, Object> values() {
                return values;
            }
        }
    }

    public static class DisplayConditionBlock {
        private final List<String> conditions;
        private final Map<String, Object> values;

        public DisplayConditionBlock(List<String> conditions, Map<String, Object> values) {
            this.conditions = conditions != null ? conditions : new ArrayList<>();
            this.values = values != null ? new LinkedHashMap<>(values) : new LinkedHashMap<>();
        }

        public boolean matches(Player player) {
            if (conditions.isEmpty()) {
                return true;
            }
            for (String condition : conditions) {
                if (!MenuUtils.evaluateCondition(condition, player)) {
                    return false;
                }
            }
            return true;
        }

        public Map<String, Object> values() {
            return values;
        }
    }

    private ConditionalAction parseConditionalActions(ConfigurationSection section, String key) {
        Object value = section.get(key);
        
        if (!(value instanceof ConfigurationSection)) {
            return null;
        }

        ConfigurationSection actionSection = (ConfigurationSection) value;

        if (!actionSection.contains("if")
                && !actionSection.contains("conditions")
                && !actionSection.contains("condition")
                && !actionSection.contains("when")) {
            return null;
        }

        List<ConditionalAction.ConditionalBlock> blocks = new ArrayList<>();
        List<String> elseActions = new ArrayList<>();

        Object ifSource = extractConditionSource(actionSection);
        List<String> ifConditions = parseConditionsList(ifSource);
        List<String> thenActions = actionSection.getStringList("then");
        if (!ifConditions.isEmpty() && !thenActions.isEmpty()) {
            blocks.add(new ConditionalAction.ConditionalBlock(ifConditions, thenActions));
        }

        Object elifValue = actionSection.get("elif");
        if (elifValue != null) {
            if (elifValue instanceof List) {
                List<?> elifList = (List<?>) elifValue;
                for (Object elifObj : elifList) {
                    if (elifObj instanceof Map) {
                        Map<?, ?> elifMap = (Map<?, ?>) elifObj;
                        Object conditionSource = extractConditionSource(elifMap);
                        List<String> elifConditions = parseConditionsList(conditionSource);
                        List<String> elifActions = parseActionsList(elifMap.get("then"));
                        if (!elifConditions.isEmpty() && !elifActions.isEmpty()) {
                            blocks.add(new ConditionalAction.ConditionalBlock(elifConditions, elifActions));
                        }
                    }
                }
            } else if (elifValue instanceof ConfigurationSection) {
                ConfigurationSection elifSection = (ConfigurationSection) elifValue;
                Object conditionSource = extractConditionSource(elifSection);
                List<String> elifConditions = parseConditionsList(conditionSource);
                List<String> elifActions = elifSection.getStringList("then");
                if (!elifConditions.isEmpty() && !elifActions.isEmpty()) {
                    blocks.add(new ConditionalAction.ConditionalBlock(elifConditions, elifActions));
                }
            }
        }

        int elifIndex = 1;
        while (actionSection.contains("elif" + elifIndex)) {
            ConfigurationSection elifSection = actionSection.getConfigurationSection("elif" + elifIndex);
            if (elifSection != null) {
                Object conditionSource = extractConditionSource(elifSection);
                List<String> elifConditions = parseConditionsList(conditionSource);
                List<String> elifActions = elifSection.getStringList("then");
                if (!elifConditions.isEmpty() && !elifActions.isEmpty()) {
                    blocks.add(new ConditionalAction.ConditionalBlock(elifConditions, elifActions));
                }
            }
            elifIndex++;
        }

        if (actionSection.contains("else")) {
            elseActions = actionSection.getStringList("else");
        }

        if (blocks.isEmpty()) {
            return null;
        }

        return new ConditionalAction(blocks, elseActions);
    }
    
    private List<String> parseConditionsList(Object conditionsObj) {
        List<String> conditions = new ArrayList<>();
        
        if (conditionsObj == null) {
            return conditions;
        }
        
        if (conditionsObj instanceof String) {
            conditions.add((String) conditionsObj);
        } else if (conditionsObj instanceof List) {
            for (Object cond : (List<?>) conditionsObj) {
                conditions.add(cond.toString());
            }
        }
        
        return conditions;
    }

    private List<Integer> parseSlots(String slotsStr) {
        List<Integer> slots = new ArrayList<>();
        
        if (slotsStr.contains("-")) {
            String[] parts = slotsStr.split("-");
            try {
                int start = Integer.parseInt(parts[0].trim());
                int end = Integer.parseInt(parts[1].trim());
                for (int i = start; i <= end; i++) {
                    slots.add(i);
                }
            } catch (NumberFormatException e) {
                slots.add(0);
            }
        } else if (slotsStr.contains(",")) {
            String[] parts = slotsStr.split(",");
            for (String part : parts) {
                try {
                    slots.add(Integer.parseInt(part.trim()));
                } catch (NumberFormatException e) {
                }
            }
        } else {
            try {
                slots.add(Integer.parseInt(slotsStr.trim()));
            } catch (NumberFormatException e) {
                slots.add(0);
            }
        }
        
        return slots;
    }


    public void openMenu(Player player, String menuName) {
        Menu menu = menus.get(menuName.toLowerCase());
        if (menu == null) {
            player.sendMessage("§cМеню '" + menuName + "' не найдено!");
            return;
        }

        if (!menu.checkRequirements(player)) {
            player.sendMessage("§cУ вас нет доступа к этому меню!");
            return;
        }

        if (menu.isHideItems()) {
            hidePlayerInventory(player);
        }

        menu.executeOpenCommands(player);

        Map<Integer, MenuItem> resolvedItems = menu.resolveItems(player, this);
        Inventory inventory = menu.createInventory(player, resolvedItems);

        UUID uuid = player.getUniqueId();
        activeMenuItems.put(uuid, resolvedItems);
        openMenus.put(uuid, menuName.toLowerCase());

        player.openInventory(inventory);
    }

    public void closeMenu(Player player) {
        UUID uuid = player.getUniqueId();
        
        if (hiddenInventories.containsKey(uuid)) {
            restorePlayerInventory(player);
        }
        
        openMenus.remove(uuid);
        activeMenuItems.remove(uuid);
        player.closeInventory();
    }

    private void hidePlayerInventory(Player player) {
        UUID uuid = player.getUniqueId();
        ItemStack[] contents = player.getInventory().getContents();
        hiddenInventories.put(uuid, contents);
        player.getInventory().clear();
    }

    private void restorePlayerInventory(Player player) {
        UUID uuid = player.getUniqueId();
        ItemStack[] contents = hiddenInventories.remove(uuid);
        if (contents != null) {
            player.getInventory().setContents(contents);
        }
    }

    public Menu getMenu(String name) {
        return menus.get(name.toLowerCase());
    }

    public Map<String, Menu> getMenus() {
        return menus;
    }

    public MenuItem getActiveMenuItem(Player player, int slot) {
        Map<Integer, MenuItem> items = activeMenuItems.get(player.getUniqueId());
        if (items == null) {
            return null;
        }
        return items.get(slot);
    }

    public String getOpenMenu(Player player) {
        return openMenus.get(player.getUniqueId());
    }

    public boolean isInMenu(Player player) {
        return openMenus.containsKey(player.getUniqueId());
    }

    public void handleInventoryClose(Player player) {
        UUID uuid = player.getUniqueId();
        if (hiddenInventories.containsKey(uuid)) {
            restorePlayerInventory(player);
        }
        openMenus.remove(uuid);
        activeMenuItems.remove(uuid);
    }

    public boolean createMenu(String menuName) {
        File menusFolder = new File(plugin.getDataFolder(), "menus");
        if (!menusFolder.exists()) {
            menusFolder.mkdirs();
        }

        File menuFile = new File(menusFolder, menuName + ".yml");
        if (menuFile.exists()) {
            return false;
        }

        YamlConfiguration config = new YamlConfiguration();
        config.set("title", "&6" + menuName);
        config.set("size", 27);
        config.set("hide_items", false);
        config.set("open_command", menuName.toLowerCase());

        config.set("items.example.slot", "13");
        config.set("items.example.material", "DIAMOND");
        config.set("items.example.display_name", "&b&lПример предмета");
        config.set("items.example.lore", Arrays.asList(
                "&7Это пример предмета",
                "&7Измените его в конфигурации!"
        ));
        config.set("items.example.left_click_actions", Collections.singletonList("[close]"));

        try {
            config.save(menuFile);
            loadMenu(menuFile);
            plugin.registerSingleMenuCommand(menuName.toLowerCase());
            return true;
        } catch (Exception e) {
            plugin.getLogger().severe("Ошибка при создании меню: " + e.getMessage());
            return false;
        }
    }
}

