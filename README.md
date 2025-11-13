# LiseryMenu

> Легковесная и мощная система создания меню для Minecraft 1.21–1.21.10, с современным синтаксисом YAML и динамическим поведением.

![Minecraft](https://img.shields.io/badge/Minecraft-1.21%E2%80%931.21.10-47A248?logo=minecraft&logoColor=white)
![Java](https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white)
![Platform](https://img.shields.io/badge/Platform-Paper%20%7C%20Spigot-blueviolet)
![Size](https://img.shields.io/badge/JAR-~40%20KB-lightgrey)

---

## 🧭 Обзор

LiseryMenu задуман как современный аналог DeluxMenus: он проще в настройке, не требует перезапуска сервера и поддерживает динамическое поведение предметов. Плагин подойдёт для создания меню магазина, телепортации, статистики, подтверждений действий и любых других GUI для вашего сервера.

## 🚀 Быстрые ссылки

- 🔽 Скачать последнюю версию: [GitHub Releases](https://github.com/He1ly03/LiseryMenu/releases)
- 📦 Готовые примеры: `docs/example_stats_menu.yml`
- 🌐 Полная офлайн-документация: `docs/index.html`
- 🧾 Инструкция по деплою: `docs/DEPLOY.md`
- 🐞 Сообщить об ошибке: [Issue Tracker](https://github.com/He1ly03/LiseryMenu/issues)

---

## 📑 Содержание

- [🧭 Обзор](#-обзор)
- [🚀 Быстрые ссылки](#-быстрые-ссылки)
- [✨ Основные возможности](#-основные-возможности)
- [📥 Установка](#-установка)
- [🚀 Быстрый старт](#-быстрый-старт)
- [📋 Команды и права](#-команды-и-права)
- [🧱 Структура меню](#-структура-меню)
- [🎨 Форматирование текста](#-форматирование-текста)
- [📝 Плейсхолдеры](#-плейсхолдеры)
- [🎯 Действия (Actions)](#-действия-actions)
- [🔀 Условные действия (if/elif/else)](#-условные-действия-ifelifelse)
- [🔄 Display Condition](#-display-condition)
- [🎁 Поддержка кастомных предметов](#-поддержка-кастомных-предметов)
- [🧩 Расширенные настройки предметов](#-расширенные-настройки-предметов)
- [🔒 Функция hide_items](#-функция-hide_items)
- [📚 Примеры меню](#-примеры-меню)
- [❓ FAQ](#-faq)
- [🔧 Troubleshooting](#-troubleshooting)
- [⚙️ Технические детали](#-технические-детали)
- [🗺️ Дорожная карта](#-дорожная-карта)
- [🤝 Как помочь](#-как-помочь)
- [💬 Поддержка и связь](#-поддержка-и-связь)
- [🧾 Лицензия](#-лицензия)
- [🌐 English Summary](#-english-summary)
- [📖 Дополнительные материалы](#-дополнительные-материалы)

---

## ✨ Основные возможности

- 🎨 **Простая система создания меню** через YAML файлы
- ⚡ **Команда `/lm create`** для быстрого создания новых меню
- 🔒 **Функция `hide_items`** — скрытие инвентаря игрока при открытии меню
- 🎯 **Множество действий**: команды, открытие других меню, звуки, сообщения и др.
- 🔄 **Горячая перезагрузка** всех меню без перезапуска сервера
- 🎭 **PlaceholderAPI интеграция** для расширенных плейсхолдеров
- 🔐 **Система требований** (permissions и др.)
- ✨ **Условные действия (if/elif/else)** для динамического поведения
- 🎁 **Поддержка кастомных предметов**: ItemsAdder, Oraxen, Nexo
- 🎨 **MiniMessage и HEX цвета** для красивого форматирования текста
- 🔄 **Display Condition** — динамическое изменение предметов в меню
- 🎨 **Настройка цвета кожаной брони** (RGB, HEX, named colors)
- ⚙️ **Изменение прочности предметов**
- 🖱️ **Поддержка всех типов кликов**: left, right, middle, shift+left

---

## 📥 Установка

### Требования

- **Minecraft версия**: 1.21 — 1.21.10
- **Платформа**: Paper (рекомендуется) / Spigot
- **Java версия**: 21

### Шаги установки

1. Скачайте последнюю версию плагина `LiseryMenu-1.0.jar`
2. Поместите `.jar` файл в папку `plugins/`
3. Перезапустите сервер
4. Папка `plugins/LiseryMenu/` будет создана автоматически
5. Создайте меню в папке `plugins/LiseryMenu/menus/`
6. Используйте `/lm reload` для применения изменений

### Опциональные зависимости

Плагин работает без них, но добавляет дополнительные функции:

- **PlaceholderAPI** — расширенные плейсхолдеры
- **ItemsAdder** — поддержка кастомных предметов (`ia:namespace-item`)
- **Oraxen** — поддержка кастомных предметов (`oraxen-item`)
- **Nexo** — поддержка кастомных предметов (`nexo-item`)

---

## 🚀 Быстрый старт

### Создание первого меню

1. **Создайте меню через команду:**
   ```
   /lm create vip
   ```

2. **Откройте файл** `plugins/LiseryMenu/menus/vip.yml`

3. **Отредактируйте меню:**
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
   ```

4. **Перезагрузите меню:**
   ```
   /lm reload
   ```

5. **Откройте меню:**
   ```
   /vip
   ```

---

## 📋 Команды и права

| Команда | Описание | Права | Алиасы |
|---------|----------|-------|--------|
| `/lm create <название>` | Создать новое меню | `liserymenu.create` | - |
| `/lm open <название>` | Открыть меню | `liserymenu.open` | - |
| `/lm reload` | Перезагрузить все меню | `liserymenu.reload` | - |
| `/lm list` | Список всех меню | `liserymenu.list` | - |
| `/lm help` | Справка по командам | - | - |

### Алиасы команды `/lm`

- `/liserymenu` — полное название
- `/lisery` — короткий алиас

### Права доступа

| Право | Описание | По умолчанию |
|-------|----------|--------------|
| `liserymenu.*` | Все права | op |
| `liserymenu.create` | Создание меню | op |
| `liserymenu.open` | Открытие меню | true |
| `liserymenu.open.*` | Открытие всех меню | true |
| `liserymenu.open.<menu>` | Открытие конкретного меню | - |
| `liserymenu.reload` | Перезагрузка | op |
| `liserymenu.list` | Список меню | true |

---

## 🧱 Структура меню

Все меню хранятся в папке `plugins/LiseryMenu/menus/` в формате YAML.

### Основные параметры меню

| Параметр | Тип | Описание | Обязательный |
|----------|-----|----------|--------------|
| `title` | String | Название меню (отображается в инвентаре) | Да |
| `size` | Integer | Размер меню (9, 18, 27, 36, 45, 54) | Да |
| `hide_items` | Boolean | Скрывать инвентарь игрока | Нет (по умолчанию: false) |
| `open_command` | String | Команда для открытия меню | Нет |
| `open_requirements` | List | Требования для открытия меню | Нет |
| `open_commands` | List | Команды, выполняемые при открытии | Нет |
| `items` | Section | Предметы в меню | Нет |

### Базовая структура

```yaml
title: "<#ff7a1b>📊 <white>Мое меню"
size: 27
hide_items: false
open_command: "mymenu"

open_requirements:
  - "permission: example.permission"

open_commands:
  - "[console] say Игрок %player% открыл меню!"

items:
  example_item:
    slot: "13"
    material: DIAMOND
    display_name: "<#9cff6b>Пример предмета"
    lore:
      - "<gray>Это первая строка"
      - "<gray>Это вторая строка"
    left_click_actions:
      - "[message] <green>Клик выполнен!"
      - "[close]"
```

### Размеры меню

Размер меню определяет количество слотов:

- `9` — 1 ряд (9 слотов)
- `18` — 2 ряда (18 слотов)
- `27` — 3 ряда (27 слотов)
- `36` — 4 ряда (36 слотов)
- `45` — 5 рядов (45 слотов)
- `54` — 6 рядов (54 слота)

### Слоты предметов

Слоты можно указывать несколькими способами:

```yaml
# Один слот
slot: "13"

# Несколько слотов через запятую
slots: "0,1,2,3"

# Диапазон слотов
slots: "0-8"

# Комбинация
slots: "0-8,18-26,45-53"
```

**Важно:** Используйте либо `slot`, либо `slots`, но не оба одновременно.

---

## 🎨 Форматирование текста

### MiniMessage

LiseryMenu поддерживает **MiniMessage** для современного форматирования текста:

#### Основные теги MiniMessage

| Тег | Описание | Пример |
|-----|----------|--------|
| `<#hex>` | HEX цвет | `<#ff7a1b>Текст` |
| `<color>` | Named color | `<red>Текст` |
| `<bold>` | Жирный текст | `<bold>Текст` |
| `<italic>` | Курсив | `<italic>Текст` |
| `<underlined>` | Подчеркнутый | `<underlined>Текст` |
| `<strikethrough>` | Зачеркнутый | `<strikethrough>Текст` |
| `<obfuscated>` | Случайные символы | `<obfuscated>Текст` |
| `<reset>` | Сброс форматирования | `<reset>Текст` |

#### Примеры MiniMessage

```yaml
display_name: "<#ff7a1b>Название <bold>жирным</bold>"
lore:
  - "<gray>Серый текст"
  - "<green>Зеленый текст"
  - "<#ffffff>HEX цвет"
  - "<bold><red>Жирный красный"
  - "<italic><blue>Курсив синий"
```

### Legacy цвета

Также поддерживаются классические цветовые коды с `&`:

#### Цветовые коды

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

#### HEX цвета в Legacy формате

```yaml
display_name: "&ffffffHEX цвет"
lore:
  - "&ff7a1bОранжевый HEX"
```

#### Примеры Legacy

```yaml
display_name: "&6&lЗолотое название"
lore:
  - "&7Серый текст"
  - "&aЗеленый текст"
  - "&ffffffHEX цвет"
  - "&c&l&nКрасный жирный подчеркнутый"
```

---

## 📝 Плейсхолдеры

### Встроенные плейсхолдеры

| PlaceHolder | Описание | Пример значения |
|-------------|----------|-----------------|
| `%player%` | Имя игрока | `Player123` |
| `%player_name%` | Имя игрока | `Player123` |
| `%player_uuid%` | UUID игрока | `550e8400-e29b-41d4-a716-446655440000` |
| `%player_level%` | Уровень игрока | `25` |
| `%player_health%` | Здоровье игрока | `20.0` |
| `%player_food%` | Уровень голода | `20` |
| `%player_world%` | Мир игрока | `world` |
| `%player_gamemode%` | Режим игры | `SURVIVAL` |
| `%player_exp%` | Опыт игрока | `0.5` |

### PlaceholderAPI

При установке **PlaceholderAPI** доступны тысячи дополнительных плейсхолдеров:

#### Популярные плейсхолдеры

```yaml
%vault_eco_balance%              # Баланс игрока (Vault)
%player_health_rounded%         # Здоровье (округленное)
%statistic_mob_kills%           # Убито мобов
%statistic_deaths%              # Смертей
%statistic_time_played%         # Время игры
%player_first_play%             # Первый вход
%player_last_played%            # Последний вход
%player_x%                      # Координата X
%player_y%                      # Координата Y
%player_z%                      # Координата Z
%player_biome%                  # Биом игрока
%world_time_12%                 # Время в мире (12-часовой формат)
```

#### Использование в меню

```yaml
display_name: "<green>%player_name%"
lore:
  - "<gray>Баланс: <yellow>%vault_eco_balance%$"
  - "<gray>Уровень: <white>%player_level%"
  - "<gray>Здоровье: <red>%player_health% / %player_max_health%"
```

---

## 🎯 Действия (Actions)

Действия выполняются при клике на предмет в меню.

### Типы действий

| Действие | Описание | Пример |
|----------|----------|--------|
| `[close]` | Закрыть меню | `[close]` |
| `[open_menu] <menu>` | Открыть другое меню | `[open_menu] shop` |
| `[player] <command>` | Выполнить команду от игрока | `[player] spawn` |
| `[console] <command>` | Выполнить команду от консоли | `[console] give %player% diamond 1` |
| `[message] <text>` | Отправить сообщение игроку | `[message] <green>Привет!` |
| `[broadcast] <text>` | Отправить сообщение всем | `[broadcast] <yellow>Объявление!` |
| `[sound] <sound>,<volume>,<pitch>` | Воспроизвести звук | `[sound] ENTITY_PLAYER_LEVELUP,1.0,1.0` |
| `[refresh]` | Обновить текущее меню | `[refresh]` |

### Детальное описание действий

#### `[close]`

Закрывает текущее меню.

```yaml
left_click_actions:
  - "[close]"
```

#### `[open_menu] <menu>`

Открывает другое меню по его имени (без расширения `.yml`).

```yaml
left_click_actions:
  - "[open_menu] shop"
  - "[open_menu] stats"
```

#### `[player] <command>`

Выполняет команду от имени игрока. Игрок должен иметь права на выполнение команды.

```yaml
left_click_actions:
  - "[player] spawn"
  - "[player] warp shop"
  - "[player] home"
```

#### `[console] <command>`

Выполняет команду от имени консоли. Плейсхолдеры заменяются автоматически.

```yaml
left_click_actions:
  - "[console] give %player% diamond 1"
  - "[console] eco give %player_name% 1000"
  - "[console] teleport %player% 0 64 0"
```

#### `[message] <text>`

Отправляет сообщение игроку. Поддерживает MiniMessage и Legacy цвета.

```yaml
left_click_actions:
  - "[message] <green>Покупка выполнена!"
  - "[message] &aВы получили предмет!"
```

#### `[broadcast] <text>`

Отправляет сообщение всем игрокам на сервере.

```yaml
left_click_actions:
  - "[broadcast] <yellow>Игрок %player% купил предмет!"
```

#### `[sound] <sound>,<volume>,<pitch>`

Воспроизводит звук игроку.

- `<sound>` — название звука (например, `ENTITY_PLAYER_LEVELUP`)
- `<volume>` — громкость (0.0 — 1.0)
- `<pitch>` — высота тона (0.0 — 2.0)

```yaml
left_click_actions:
  - "[sound] ENTITY_PLAYER_LEVELUP,1.0,1.0"
  - "[sound] ENTITY_EXPERIENCE_ORB_PICKUP,0.5,1.5"
  - "[sound] BLOCK_NOTE_BLOCK_PLING,1.0,0.8"
```

**Популярные звуки:**
- `ENTITY_PLAYER_LEVELUP` — звук повышения уровня
- `ENTITY_EXPERIENCE_ORB_PICKUP` — звук подбора опыта
- `BLOCK_NOTE_BLOCK_PLING` — звук нотного блока
- `ENTITY_VILLAGER_YES` — звук согласия жителя
- `ENTITY_VILLAGER_NO` — звук отказа жителя

#### `[refresh]`

Обновляет текущее меню, перерисовывая все предметы.

```yaml
left_click_actions:
  - "[message] <green>Меню обновлено!"
  - "[refresh]"
```

### Типы кликов

Поддерживаются следующие типы кликов:

- `left_click_actions` — Левый клик (ЛКМ)
- `right_click_actions` — Правый клик (ПКМ)
- `middle_click_actions` — Средний клик (колесико мыши)
- `shift_left_click_actions` — Shift + Левый клик

#### Пример использования разных кликов

```yaml
items:
  multi_click_item:
    slot: "13"
    material: DIAMOND
    display_name: "<green>Многофункциональный предмет"
    lore:
      - "<gray>ЛКМ › Купить"
      - "<gray>ПКМ › Информация"
      - "<gray>Средний клик › Обновить"
      - "<gray>Shift+ЛКМ › Быстрая покупка"
    left_click_actions:
      - "[message] <green>Левый клик!"
    right_click_actions:
      - "[message] <yellow>Правый клик!"
    middle_click_actions:
      - "[message] <blue>Средний клик!"
    shift_left_click_actions:
      - "[message] <red>Shift + Левый клик!"
```

### Множественные действия

Можно указать несколько действий, они выполнятся последовательно:

```yaml
left_click_actions:
  - "[player] me Я купил предмет!"
  - "[console] give %player% diamond 1"
  - "[message] <green>Вы купили алмаз!"
  - "[sound] ENTITY_EXPERIENCE_ORB_PICKUP,1.0,1.0"
  - "[close]"
```

---

## 🔀 Условные действия (if/elif/else)

Условные действия позволяют выполнять разные действия в зависимости от условий.

### Базовый синтаксис

```yaml
left_click_actions:
  if:
    - "условие1"
    - "условие2"
  then:
    - "[действие1]"
    - "[действие2]"
  elif:
    - "условие3"
  then:
    - "[действие3]"
  else:
    - "[действие4]"
```

### Типы условий

#### Математические операторы

- `>=` — больше или равно
- `<=` — меньше или равно
- `>` — больше
- `<` — меньше
- `==` — равно
- `!=` — не равно

```yaml
if: "%vault_eco_balance% >= 1000"
if: "%player_level% > 20"
if: "%player_health% <= 10"
```

#### Права доступа

```yaml
if: "permission: server.vip"
if: "permission: admin.use"
```

#### Проверка предметов

```yaml
if: "has_item: DIAMOND 5"
if: "has_item: ia:iasurvival-ruby 1"
if: "has_item: oraxen-super_sword 1"
```

#### С PlaceholderAPI

Любые плейсхолдеры из PlaceholderAPI можно использовать в условиях:

```yaml
if: "%statistic_mob_kills% >= 100"
if: "%player_first_play% == 2024-01-01"
```

### Примеры условных действий

#### Простая проверка баланса

```yaml
left_click_actions:
  if: "%vault_eco_balance% >= 1000"
  then:
    - "[console] eco take %player_name% 1000"
    - "[console] give %player% diamond 1"
    - "[message] <green>Покупка выполнена!"
  else:
    - "[message] <red>Недостаточно средств!"
```

#### Несколько условий

Все условия в списке `if` должны выполниться одновременно:

```yaml
left_click_actions:
  if:
    - "%vault_eco_balance% >= 5000"
    - "permission: server.vip"
    - "%player_level% >= 20"
  then:
    - "[console] eco take %player_name% 5000"
    - "[message] <green>Все условия выполнены!"
  else:
    - "[message] <red>Нужно 5000$, VIP статус и 20+ уровень!"
```

#### Использование ELIF

```yaml
left_click_actions:
  if:
    - "%vault_eco_balance% >= 1000"
    - "permission: server.vip"
  then:
    - "[console] eco take %player_name% 1000"
    - "[console] give %player% diamond 1"
    - "[message] <green>Покупка выполнена!"
  elif:
    - "permission: server.vip"
  then:
    - "[message] <yellow>Накопите ещё немного средств."
  else:
    - "[message] <red>Недостаточно средств или прав!"
```

#### Проверка предметов

```yaml
left_click_actions:
  if: "has_item: DIAMOND 5"
  then:
    - "[console] take %player% diamond 5"
    - "[console] give %player% emerald 1"
    - "[message] <green>Обмен выполнен!"
  else:
    - "[message] <red>Нужно 5 алмазов!"
```

---

## 🔄 Display Condition

Display Condition позволяет динамически изменять предметы в меню в зависимости от условий.

### Синтаксис

```yaml
items:
  item_name:
    display_condition:
      if: "условие"
      then:
        slot: "10"
        material: DIAMOND
        display_name: "Название"
        lore:
          - "Описание"
      elif:
        - "условие2"
      then:
        slot: "10"
        material: EMERALD
        display_name: "Другое название"
      else:
        slot: "10"
        material: STONE
        display_name: "По умолчанию"
```

### Примеры

#### VIP статус

```yaml
items:
  vip_status:
    display_condition:
      if: "permission: donate.vip"
      then:
        slot: "10"
        material: GOLD_BLOCK
        display_name: "<#ffd27f>⭐ VIP Статус"
        lore:
          - "<gray>Вы обладатель VIP статуса!"
          - "<gray>Спасибо за поддержку сервера."
        glowing: true
        right_click_actions:
          - "[message] <#ffd27f>VIP Бонусы:"
          - "[message] <gray>• Дополнительные команды"
      else:
        slot: "10"
        material: GOLD_BLOCK
        display_name: "<yellow>⭐ Получите VIP"
        lore:
          - "<gray>Поддержите сервер и получите"
          - "<gray>VIP статус с бонусами!"
        right_click_actions:
          - "[message] <#ffd27f>Получите VIP статус!"
```

#### Разные предметы для разных уровней

```yaml
items:
  level_reward:
    display_condition:
      if: "%player_level% >= 50"
      then:
        slot: "22"
        material: DIAMOND_BLOCK
        display_name: "<green>Награда за 50 уровень"
        lore:
          - "<gray>Поздравляем!"
      elif:
        - "%player_level% >= 25"
      then:
        slot: "22"
        material: GOLD_BLOCK
        display_name: "<yellow>Награда за 25 уровень"
        lore:
          - "<gray>Продолжайте в том же духе!"
      else:
        slot: "22"
        material: IRON_BLOCK
        display_name: "<gray>Достигните 25 уровня"
        lore:
          - "<gray>Для получения награды"
```

---

## 🎁 Поддержка кастомных предметов

### ItemsAdder

#### Синтаксис

```yaml
material: "ia:namespace-item_id"
```

#### Примеры

```yaml
# Простой предмет
ruby_item:
  slot: "13"
  material: "ia:iasurvival-ruby"
  display_name: "<red>Рубин"
  lore:
    - "<gray>Из ItemsAdder"

# Оружие
sword_item:
  slot: "14"
  material: "ia:weapon-super_sword"
  display_name: "<yellow>Супер меч"
```

### Oraxen

#### Синтаксис

```yaml
material: "oraxen-item_id"
```

#### Примеры

```yaml
sword_item:
  slot: "15"
  material: "oraxen-super_sword"
  display_name: "<green>Супер меч"
  lore:
    - "<gray>Из Oraxen"
```

### Nexo

#### Синтаксис

```yaml
material: "nexo-item_id"
```

#### Примеры

```yaml
staff_item:
  slot: "16"
  material: "nexo-magic_staff"
  display_name: "<purple>Магический посох"
  lore:
    - "<gray>Из Nexo"
```

### Использование в условиях

Кастомные предметы можно проверять в условиях `has_item`:

```yaml
left_click_actions:
  if: "has_item: ia:iasurvival-ruby 1"
  then:
    - "[message] <green>У вас есть рубин!"
  else:
    - "[message] <red>Нужен рубин!"
```

---

## 🧩 Расширенные настройки предметов

### Цвет кожаной брони

Можно изменять цвет кожаной брони тремя способами:

#### HEX формат

```yaml
material: LEATHER_CHESTPLATE
leather_color: "#ff7a1b"
```

#### RGB формат

```yaml
material: LEATHER_CHESTPLATE
leather_color: "255,122,27"
```

#### Named color

```yaml
material: LEATHER_CHESTPLATE
leather_color: "orange"
```

**Доступные named colors:**
- `red`, `green`, `blue`, `yellow`, `orange`, `purple`, `pink`, `cyan`, `magenta`, `lime`, `aqua`, `white`, `black`, `gray`, `silver`

### Прочность предмета

```yaml
material: DIAMOND_SWORD
durability: 100  # Прочность (0 — максимальная для предмета)
```

**Важно:** Прочность указывается как оставшаяся прочность, а не изношенность.  
Для алмазного меча максимальная прочность = 1561, поэтому `durability: 100` означает, что осталось 100 прочности.

### Другие настройки предметов

#### Полный пример

```yaml
items:
  custom_item:
    slot: "13"
    material: DIAMOND
    amount: 1                    # Количество
    custom_model_data: 1001      # Custom Model Data
    glowing: true                # Эффект свечения
    skull_owner: "%player_name%" # Владелец головы (для PLAYER_HEAD)
    leather_color: "#ff7a1b"     # Цвет кожаной брони
    durability: 100              # Прочность
    display_name: "<green>Кастомный предмет"
    lore:
      - "<gray>Описание"
      - "<gray>Ещё описание"
    enchantments:
      sharpness: 5               # Зачарование
      unbreaking: 3
    left_click_actions:
      - "[message] <green>Клик!"
```

---

## 🔒 Функция hide_items

При установке `hide_items: true`:

- ✅ Инвентарь игрока **временно скрывается**
- ✅ Игрок **не может взаимодействовать** с предметами в своем инвентаре
- ✅ Игрок **не может выбрасывать** предметы
- ✅ Игрок **не может менять** предметы в руках (F)
- ✅ При **закрытии меню** инвентарь **возвращается**

### Использование

```yaml
title: "<red>Подтверждение"
size: 27
hide_items: true

items:
  confirm:
    slot: "11"
    material: GREEN_CONCRETE
    display_name: "<green>Подтвердить"
    left_click_actions:
      - "[message] <green>Подтверждено!"
      - "[close]"
  
  cancel:
    slot: "15"
    material: RED_CONCRETE
    display_name: "<red>Отменить"
    left_click_actions:
      - "[message] <red>Отменено!"
      - "[close]"
```

**Идеально для:**
- Меню подтверждения
- Анкет
- Интерактивных меню
- Меню выбора

---

## 📚 Примеры меню

### Простое меню с рамкой

```yaml
title: "<#ff7a1b>Главное меню"
size: 27
open_command: "menu"

items:
  border:
    slots: "0-8,18-26"
    material: GRAY_STAINED_GLASS_PANE
    display_name: " "
  
  close:
    slot: "22"
    material: BARRIER
    display_name: "<red>✖ Закрыть"
    left_click_actions:
      - "[close]"
```

### Магазин с условиями

```yaml
title: "<green>Магазин"
size: 54
open_command: "shop"

items:
  border:
    slots: "0-8,45-53"
    material: GRAY_STAINED_GLASS_PANE
    display_name: " "
  
  diamond:
    slot: "10"
    material: DIAMOND
    display_name: "<#9cff6b>Алмаз"
    lore:
      - "<gray>Цена: <yellow>1000$"
      - ""
      - "<yellow>ЛКМ › Купить"
    left_click_actions:
      if: "%vault_eco_balance% >= 1000"
      then:
        - "[console] eco take %player_name% 1000"
        - "[console] give %player% diamond 1"
        - "[message] <green>Покупка выполнена!"
        - "[sound] ENTITY_EXPERIENCE_ORB_PICKUP,1.0,1.0"
      else:
        - "[message] <red>Недостаточно средств!"
  
  emerald:
    slot: "12"
    material: EMERALD
    display_name: "<green>Изумруд"
    lore:
      - "<gray>Цена: <yellow>500$"
    left_click_actions:
      if: "%vault_eco_balance% >= 500"
      then:
        - "[console] eco take %player_name% 500"
        - "[console] give %player% emerald 1"
        - "[message] <green>Покупка выполнена!"
      else:
        - "[message] <red>Недостаточно средств!"
```

### Меню телепортации

```yaml
title: "<blue>Телепортация"
size: 27
open_command: "warps"
open_requirements:
  - "permission: server.warps"

items:
  spawn:
    slot: "11"
    material: RED_BED
    display_name: "<green>Спавн"
    lore:
      - "<gray>Телепортация на спавн"
    left_click_actions:
      - "[player] spawn"
      - "[message] <green>Телепортация на спавн!"
      - "[close]"
  
  shop:
    slot: "13"
    material: EMERALD
    display_name: "<green>Магазин"
    lore:
      - "<gray>Телепортация в магазин"
    left_click_actions:
      - "[player] warp shop"
      - "[message] <green>Телепортация в магазин!"
      - "[close]"
  
  pvp:
    slot: "15"
    material: DIAMOND_SWORD
    display_name: "<red>PvP Арена"
    lore:
      - "<gray>Телепортация на PvP арену"
    left_click_actions:
      - "[player] warp pvp"
      - "[message] <green>Телепортация на PvP арену!"
      - "[close]"
```

### Меню статистики

См. полный пример в файле `docs/example_stats_menu.yml`

---

## ❓ FAQ

### Нужно ли PlaceholderAPI / ItemsAdder?

**Нет.** Они подключаются как softdepend: плагин работает без них, но включает дополнительные функции при наличии.

### Как скрыть инвентарь игрока?

Установите `hide_items: true` в конфигурации меню. Предметы игрока временно уберутся и вернутся при закрытии меню.

### Shift-клики

Начиная с версии v1.3 поддерживается `shift_left_click_actions`. Для дополнительных сценариев используйте `display_condition` или отдельные предметы.

### Как обновить меню без перезапуска сервера?

Используйте команду `/lm reload`. Все меню будут перезагружены, а команды из `open_command` обновятся автоматически.

### Можно ли использовать кастомные предметы в условиях?

Да! Используйте `has_item: ia:namespace-item 1` или аналогично для Oraxen/Nexo.

### Как изменить цвет кожаной брони?

Используйте параметр `leather_color` с форматом HEX (`#ff7a1b`), RGB (`255,122,27`) или named color (`orange`).

### Поддерживаются ли версии ниже 1.21?

Нет, плагин поддерживает только версии 1.21–1.21.10.

### Как создать меню с несколькими страницами?

Создайте несколько меню и используйте `[open_menu]` для перехода между ними.

---

## 🔧 Troubleshooting

### Меню не открывается

1. Проверьте, что файл меню находится в `plugins/LiseryMenu/menus/`
2. Проверьте синтаксис YAML (используйте онлайн валидатор)
3. Выполните `/lm reload`
4. Проверьте права доступа (`liserymenu.open.<menu>`)
5. Проверьте логи сервера на наличие ошибок

### Плейсхолдеры не работают

1. Убедитесь, что PlaceholderAPI установлен и работает
2. Проверьте правильность написания плейсхолдера
3. Используйте `/papi parse me <placeholder>` для проверки

### Кастомные предметы не отображаются

1. Убедитесь, что соответствующий плагин (ItemsAdder/Oraxen/Nexo) установлен
2. Проверьте правильность формата:
   - ItemsAdder: `ia:namespace-item`
   - Oraxen: `oraxen-item`
   - Nexo: `nexo-item`
3. Проверьте, что предмет существует в плагине

### Команда меню не регистрируется

1. Выполните `/lm reload`
2. Проверьте, что `open_command` указан в конфигурации
3. Перезапустите сервер (команды регистрируются при старте)

### Ошибки при загрузке меню

1. Проверьте логи сервера — там будут указаны файлы с ошибками
2. Проверьте синтаксис YAML
3. Убедитесь, что все обязательные параметры указаны (`title`, `size`)

---

## ⚙️ Технические детали

### Производительность

- Плагин весит всего **~40 КБ**
- Зависимости подключаются как `compileOnly` — не увеличивают размер
- Меню загружаются асинхронно при старте сервера
- `/lm reload` перезагружает меню мгновенно

### Архитектура

- **Reflection** используется для работы с опциональными зависимостями
- **Soft dependencies** — плагин работает без PlaceholderAPI/ItemsAdder/Oraxen/Nexo
- **Dynamic command registration** — команды меню регистрируются динамически

### Версии

- **Текущая версия**: 1.0
- **Поддерживаемые версии Minecraft**: 1.21 — 1.21.10
- **API версия**: 1.21

### Логирование

Плагин логирует только важную информацию:
- Количество загруженных меню
- Время загрузки
- Ошибки при загрузке меню

---

## 🗺️ Дорожная карта

> Идеи для будущих релизов. Вы можете предложить свои через Issue Tracker.

- Поддержка шаблонов и предустановок для быстрых стартов
- Экспортер/импортёр конфигураций DeluxMenus
- Расширенный API для разработчиков других плагинов
- Автоматическое обновление меню при изменении файлов (watcher)

---

## 🤝 Как помочь

- Сообщайте об ошибках и предложениях через [Issues](https://github.com/He1ly03/LiseryMenu/issues)
- Делитесь собственными примерами меню через Pull Request или Discussions
- Помогайте с переводом документации (например, английская версия этого README)
- Проверяйте совместимость с новыми версиями Paper/Spigot и Java

Перед отправкой Pull Request:

1. Создайте отдельную ветку от `main`
2. Соберите проект командой `./gradlew build`
3. Описывайте изменения в `docs/CHANGELOG.md` (если файл добавлен)
4. Убедитесь, что новые YAML-примеры корректно валидируются

---

## 💬 Поддержка и связь

- GitHub Issues: [github.com/He1ly03/LiseryMenu/issues](https://github.com/He1ly03/LiseryMenu/issues)
- Discord: *будет объявлено позже* (смотрите обновления README)
- Email: `he1ly03.dev@gmail.com` (по общим вопросам и партнёрствам)

Если нашли критическую проблему, приложите логи сервера и конфигурацию меню — так решение появится быстрее.

---

## 🧾 Лицензия

Лицензия проекта будет опубликована в отдельном файле `LICENSE`.  
До появления файла использование плагина регулируется договорённостью с автором. При коммерческом использовании свяжитесь с `He1ly03` для подтверждения.

---

## 🌐 English Summary

LiseryMenu is a lightweight menu plugin for Minecraft 1.21–1.21.10 (Paper/Spigot). It offers YAML-driven configuration, hot reloads, conditional actions, PlaceholderAPI support, and integrations with custom item plugins (ItemsAdder, Oraxen, Nexo). Check `docs/index.html` for the interactive documentation and `docs/example_stats_menu.yml` for a comprehensive example. Feel free to open issues or pull requests if you need assistance or want to contribute.

---

## 📖 Дополнительные материалы

- `docs/index.html` — интерактивная документация
- `docs/example_stats_menu.yml` — готовый пример меню со статистикой
- `docs/DEPLOY.md` — рекомендации по деплою и CI
- `docs/SITE_SUMMARY.md` — краткий обзор разделов документации

---

## 👤 Автор

Плагин создан **He1ly03**

---

**Наслаждайтесь использованием LiseryMenu! 🎉**


