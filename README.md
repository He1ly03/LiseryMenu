# LiseryMenu

**LiseryMenu** - легкая и мощная система создания меню для Minecraft серверов (версия 1.21.8).
Аналог плагина DeluxMenus с простым и понятным синтаксисом конфигурации.

## Возможности

- 🎨 **Простая система создания меню** через YAML файлы
- ⚡ **Команда `/lm create`** для быстрого создания новых меню
- 🔒 **Функция `hide_items`** - скрытие инвентаря игрока при открытии меню
- 🎯 **Множество действий**: команды, открытие других меню, звуки, сообщения и др.
- 🎭 **PlaceHolderAPI интеграция** для расширенных плейсхолдеров
- 🔐 **Система требований** (permissions и др.)
- ✨ **Условные действия (if/else)** для динамического поведения
- 🎁 **ItemsAdder поддержка** для кастомных предметов

## Команды

| Команда | Описание | Права |
|---------|----------|-------|
| `/lm create <название>` | Создать новое меню | `liserymenu.create` |
| `/lm open <название>` | Открыть меню | `liserymenu.open` |
| `/lm reload` | Перезагрузить все меню | `liserymenu.reload` |
| `/lm list` | Список всех меню | `liserymenu.list` |
| `/lm help` | Справка по командам | - |

## Структура меню

Все меню хранятся в папке `plugins/LiseryMenu/menus/` в формате YAML.

### Базовая структура

```yaml
title: "<#ff7a1b>⭐ <white>Донат Статус</white>"
size: 9
open_command: "stats"
hide_items: false

items:
  vip_status:
    display_condition:
      if: "permission: donate.vip"
      then:
        slot: "4"
        material: GOLD_BLOCK
        display_name: "<#ffd27f>VIP Статус"
        lore:
          - ""
          - "<#ffd27f>Вы обладатель VIP статуса!"
          - "<gray>Спасибо за поддержку сервера."
          - ""
          - "<gray>Бонусы:"
          - "<white>• Дополнительные команды"
          - "<white>• Приоритет в очереди"
          - "<white>• Ежедневные бонусы"
          - ""
          - "<yellow>ПКМ › Узнать больше"
        glowing: true
        right_click_actions:
          - "[message] <#ffd27f>VIP Бонусы:"
          - "[message] <gray>• Дополнительные команды"
          - "[message] <gray>• Приоритет в очереди"
          - "[message] <gray>• Ежедневные бонусы"
      else:
        slot: "40"
        material: GOLD_BLOCK
        display_name: "<yellow>Получите VIP"
        lore:
          - ""
          - "<gray>Поддержите сервер и получите"
          - "<gray>VIP статус с бонусами!"
          - ""
          - "<yellow>ПКМ › Узнать больше"
        right_click_actions:
          - "[message] <#ffd27f>Получите VIP статус, поддержав сервер!"
          - "[message] <gray>Используйте <yellow>/donate <gray>для покупки"
```

## PlaceHolders

### Встроенные плейсхолдеры

| PlaceHolder | Описание |
|-------------|----------|
| `%player%` | Имя игрока |
| `%player_name%` | Имя игрока |
| `%player_uuid%` | UUID игрока |
| `%player_level%` | Уровень игрока |
| `%player_health%` | Здоровье игрока |
| `%player_food%` | Уровень голода |
| `%player_world%` | Мир игрока |
| `%player_gamemode%` | Режим игры |
| `%player_exp%` | Опыт игрока |

### PlaceholderAPI

LiseryMenu поддерживает **PlaceholderAPI**! Установите плагин для доступа к тысячам дополнительных плейсхолдеров:

```yaml
%vault_eco_balance%              # Баланс игрока
%player_health_rounded%          # Здоровье
%statistic_mob_kills%            # Убито мобов
%statistic_deaths%               # Смертей
# И многое другое!
```

## Действия (Actions)

Действия выполняются при клике на предмет в меню.

### Типы действий

| Действие | Описание | Пример |
|----------|----------|--------|
| `[close]` | Закрыть меню | `[close]` |
| `[open_menu] <menu>` | Открыть другое меню | `[open_menu] shop` |
| `[player] <command>` | Выполнить команду от игрока | `[player] spawn` |
| `[console] <command>` | Выполнить команду от консоли | `[console] give %player% diamond 1` |
| `[message] <text>` | Отправить сообщение игроку | `[message] &aПривет, %player%!` |
| `[broadcast] <text>` | Отправить сообщение всем | `[broadcast] &6Объявление!` |
| `[sound] <sound>,<volume>,<pitch>` | Воспроизвести звук | `[sound] ENTITY_PLAYER_LEVELUP,1.0,1.0` |
| `[refresh]` | Обновить текущее меню | `[refresh]` |

### Примеры использования

```yaml
# Закрыть меню
left_click_actions:
  - "[close]"

# Выполнить несколько действий
left_click_actions:
  - "[player] me Я купил предмет!"
  - "[console] give %player% diamond 1"
  - "[message] &aВы купили алмаз!"
  - "[sound] ENTITY_EXPERIENCE_ORB_PICKUP,1.0,1.0"
  - "[close]"

# Открыть другое меню
left_click_actions:
  - "[open_menu] shop"

# Условные действия (IF/ELSE)
left_click_actions:
  if: "%vault_eco_balance% >= 1000"
  then:
    - "[console] eco take %player_name% 1000"
    - "[console] give %player% diamond 1"
    - "[message] &aВы купили алмаз!"
  else:
    - "[message] &cНедостаточно денег!"

# Несколько условий
left_click_actions:
  if:
    - "%vault_eco_balance% >= 5000"
    - "permission: server.vip"
  then:
    - "[console] eco take %player_name% 5000"
    - "[message] &aПокупка успешна!"
  else:
    - "[message] &cНужно 5000$ и VIP статус!"
```

## ItemsAdder поддержка

LiseryMenu поддерживает кастомные предметы из **ItemsAdder**!

### Синтаксис

```yaml
material: "ia:namespace-item_id"
```

Формат: `ia:` + namespace + `-` + item_id

### Примеры

```yaml
# Простой предмет
ruby_item:
  slot: "13"
  material: "ia:iasurvival-ruby"
  display_name: "&cРубин"
  lore:
    - "&7Из ItemsAdder"

# Оружие
sword_item:
  slot: "14"
  material: "ia:weapon-super_sword"
  display_name: "&e&lСупер меч"

# С условиями покупки
custom_item:
  slot: "15"
  material: "ia:realcraft-iron_nugget"
  display_name: "&7Железная крупица"
  left_click_actions:
    if: "%vault_eco_balance% >= 5000"
    then:
      - "[console] eco take %player_name% 5000"
      - "[console] ia give %player% realcraft:iron_nugget 1"
      - "[message] &aКуплено!"
    else:
      - "[message] &cНужно 5000$!"
```

## Условные действия (IF/ELSE)

Выполняйте разные действия в зависимости от условий!

### Базовый синтаксис

```yaml
left_click_actions:
  if: "условие"
  then:
    - "[действие1]"
    - "[действие2]"
  else:
    - "[действие3]"
```

### Типы условий

- **Математические**: `>=`, `<=`, `>`, `<`, `==`, `!=`
- **Права доступа**: `permission: право.доступа`
- **С PlaceholderAPI**: любые плейсхолдеры

### Примеры

```yaml
# Проверка баланса
left_click_actions:
  if: "%vault_eco_balance% >= 1000"
  then:
    - "[message] &aУ вас достаточно денег!"
  else:
    - "[message] &cНедостаточно денег!"

# Несколько условий (все должны выполниться)
left_click_actions:
  if:
    - "%vault_eco_balance% >= 5000"
    - "permission: server.vip"
    - "%player_level% >= 20"
  then:
    - "[message] &aВсе условия выполнены!"
  else:
    - "[message] &cНе все условия выполнены!"
```

Подробное руководство: **CONDITIONAL_ACTIONS_GUIDE.md**

## Функция hide_items

При установке `hide_items: true` в меню:
- ✅ Инвентарь игрока **временно скрывается**
- ✅ Игрок **не может взаимодействовать** с предметами в своем инвентаре
- ✅ Игрок **не может выбрасывать** предметы
- ✅ Игрок **не может менять** предметы в руках (F)
- ✅ При **закрытии меню** инвентарь **возвращается**

Идеально для меню подтверждения, анкет и других интерактивных меню.

```yaml
title: "&4&lПодтверждение"
size: 27
hide_items: true  # Включить скрытие инвентаря
```

## Шаблонные меню

При первом запуске плагин создает три шаблонных меню:

### 1. example.yml
Базовое меню с примерами всех основных функций:
- Граница из стекла
- Информация о игроке с головой
- Примеры команд
- Переход в другое меню
- Команда: `/example`

### 2. player_info.yml
Меню с подробной информацией о игроке:
- Статистика игрока
- Здоровье, голод, уровень
- Красивое оформление
- Команда: `/playerinfo`

### 3. confirmation.yml
Меню подтверждения с `hide_items: true`:
- Кнопки подтверждения и отмены
- Демонстрация hide_items
- Команда: `/confirm`

## Примеры меню

### Простое меню

```yaml
title: "&6Главное меню"
size: 27
open_command: "menu"

items:
  border:
    slot: "0-8,9,17,18-26"
    material: GRAY_STAINED_GLASS_PANE
    display_name: " "
  
  close:
    slot: "22"
    material: BARRIER
    display_name: "&cЗакрыть"
    left_click_actions:
      - "[close]"
```

### Магазин

```yaml
title: "&2&lМагазин"
size: 54
open_command: "shop"

items:
  apple:
    slot: "10"
    material: APPLE
    display_name: "&aЯблоко"
    lore:
      - "&7Цена: &e100$"
      - ""
      - "&eКликните для покупки"
    left_click_actions:
      - "[console] eco take %player% 100"
      - "[console] give %player% apple 1"
      - "[message] &aВы купили яблоко!"
      - "[sound] ENTITY_EXPERIENCE_ORB_PICKUP,1.0,1.0"
```

### Меню телепортации

```yaml
title: "&d&lТелепортация"
size: 27
open_command: "warps"
open_requirements:
  - "permission: server.warps"

items:
  spawn:
    slot: "11"
    material: RED_BED
    display_name: "&aСпавн"
    lore:
      - "&7Телепортация на спавн"
    left_click_actions:
      - "[player] spawn"
      - "[message] &aТелепортация на спавн!"
      - "[close]"
  
  shop:
    slot: "13"
    material: EMERALD
    display_name: "&2Магазин"
    lore:
      - "&7Телепортация в магазин"
    left_click_actions:
      - "[player] warp shop"
      - "[message] &aТелепортация в магазин!"
      - "[close]"
```

## Цветовые коды

Используйте символ `&` для цветов:

| Код | Цвет | Код | Формат |
|-----|------|-----|--------|
| `&0` | Черный | `&l` | Жирный |
| `&1` | Темно-синий | `&m` | Зачеркнутый |
| `&2` | Темно-зеленый | `&n` | Подчеркнутый |
| `&3` | Темно-голубой | `&o` | Курсив |
| `&4` | Темно-красный | `&r` | Сброс |
| `&5` | Фиолетовый | | |
| `&6` | Золотой | | |
| `&7` | Серый | | |
| `&8` | Темно-серый | | |
| `&9` | Синий | | |
| `&a` | Зеленый | | |
| `&b` | Голубой | | |
| `&c` | Красный | | |
| `&d` | Розовый | | |
| `&e` | Желтый | | |
| `&f` | Белый | | |

## Права доступа

| Право | Описание | По умолчанию |
|-------|----------|--------------|
| `liserymenu.*` | Все права | op |
| `liserymenu.create` | Создание меню | op |
| `liserymenu.open` | Открытие меню | true |
| `liserymenu.open.*` | Открытие всех меню | true |
| `liserymenu.open.<menu>` | Открытие конкретного меню | - |
| `liserymenu.reload` | Перезагрузка | op |
| `liserymenu.list` | Список меню | true |

## Установка

1. Скачайте последнюю версию плагина
2. Поместите `.jar` файл в папку `plugins/`
3. Перезапустите сервер
4. Настройте меню в папке `plugins/LiseryMenu/menus/`
5. Используйте `/lm reload` для применения изменений

## Поддержка

- **Minecraft версия**: 1.21.8
- **Платформа**: Paper / Spigot
- **Java версия**: 21

## Автор

Плагин создан **He1ly03**

---

**Наслаждайтесь использованием LiseryMenu! 🎉**


