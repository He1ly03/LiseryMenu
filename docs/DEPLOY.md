# 🚀 Публикация сайта LiseryMenu

Инструкция по размещению сайта документации на различных платформах.

---

## 📋 Содержание

1. [GitHub Pages](#github-pages) (Бесплатно, рекомендуется)
2. [Netlify](#netlify) (Бесплатно)
3. [Vercel](#vercel) (Бесплатно)
4. [Обычный хостинг](#обычный-хостинг)

---

## 1. GitHub Pages

### Преимущества
✅ Бесплатно  
✅ HTTPS из коробки  
✅ Автоматические обновления  
✅ Простая настройка

### Шаги

1. **Создайте репозиторий на GitHub**
   ```
   Имя: liserymenu-docs
   ```

2. **Загрузите файлы**
   ```bash
   git init
   git add docs/*
   git commit -m "Initial commit"
   git branch -M main
   git remote add origin https://github.com/ваш-username/liserymenu-docs.git
   git push -u origin main
   ```

3. **Настройте GitHub Pages**
   - Зайдите в **Settings** → **Pages**
   - Source: `main` branch
   - Folder: `/docs`
   - Нажмите **Save**

4. **Готово!**
   ```
   Сайт доступен по адресу:
   https://ваш-username.github.io/liserymenu-docs/
   ```

### Кастомный домен (опционально)

1. Купите домен (например, `liserymenu.com`)
2. В настройках GitHub Pages добавьте домен
3. Настройте DNS записи у регистратора:
   ```
   Type: CNAME
   Name: www
   Value: ваш-username.github.io
   ```

---

## 2. Netlify

### Преимущества
✅ Бесплатно  
✅ Автоматический деплой  
✅ CDN из коробки  
✅ Формы и функции

### Шаги

1. **Зарегистрируйтесь на Netlify**
   - Перейдите на [netlify.com](https://netlify.com)
   - Войдите через GitHub

2. **Создайте новый сайт**
   - Нажмите **New site from Git**
   - Выберите ваш репозиторий
   - Build settings:
     ```
     Build command: (оставьте пустым)
     Publish directory: docs
     ```

3. **Деплой**
   - Нажмите **Deploy site**
   - Дождитесь завершения

4. **Готово!**
   ```
   Сайт доступен по адресу:
   https://случайное-имя.netlify.app
   ```

### Кастомный домен

1. В настройках сайта → **Domain settings**
2. **Add custom domain**
3. Следуйте инструкциям Netlify

---

## 3. Vercel

### Преимущества
✅ Бесплатно  
✅ Очень быстрый  
✅ Edge Network  
✅ Аналитика

### Шаги

1. **Зарегистрируйтесь на Vercel**
   - Перейдите на [vercel.com](https://vercel.com)
   - Войдите через GitHub

2. **Импортируйте проект**
   - Нажмите **New Project**
   - Выберите репозиторий
   - Framework: **Other**
   - Root Directory: `docs`

3. **Деплой**
   - Нажмите **Deploy**
   - Дождитесь завершения

4. **Готово!**
   ```
   Сайт доступен по адресу:
   https://ваш-проект.vercel.app
   ```

---

## 4. Обычный хостинг

### Для cPanel / DirectAdmin

1. **Загрузите файлы**
   - Зайдите в File Manager
   - Откройте папку `public_html`
   - Загрузите файлы из папки `docs`

2. **Настройте права**
   ```
   Файлы: 644
   Папки: 755
   ```

3. **Готово!**
   ```
   Сайт доступен по адресу:
   https://ваш-домен.com
   ```

### Для VPS / VDS (Nginx)

1. **Установите Nginx**
   ```bash
   sudo apt update
   sudo apt install nginx
   ```

2. **Создайте конфиг**
   ```bash
   sudo nano /etc/nginx/sites-available/liserymenu
   ```

3. **Добавьте конфигурацию**
   ```nginx
   server {
       listen 80;
       server_name ваш-домен.com;
       root /var/www/liserymenu/docs;
       index index.html;

       location / {
           try_files $uri $uri/ =404;
       }
   }
   ```

4. **Активируйте сайт**
   ```bash
   sudo ln -s /etc/nginx/sites-available/liserymenu /etc/nginx/sites-enabled/
   sudo nginx -t
   sudo systemctl reload nginx
   ```

5. **Загрузите файлы**
   ```bash
   sudo mkdir -p /var/www/liserymenu
   # Загрузите файлы из docs/
   ```

6. **Настройте SSL (Let's Encrypt)**
   ```bash
   sudo apt install certbot python3-certbot-nginx
   sudo certbot --nginx -d ваш-домен.com
   ```

---

## 📊 Сравнение платформ

| Платформа | Цена | Скорость | Сложность | SSL | Рекомендация |
|-----------|------|----------|-----------|-----|--------------|
| GitHub Pages | 🆓 | ⭐⭐⭐ | ⭐ | ✅ | ⭐⭐⭐⭐⭐ |
| Netlify | 🆓 | ⭐⭐⭐⭐⭐ | ⭐ | ✅ | ⭐⭐⭐⭐⭐ |
| Vercel | 🆓 | ⭐⭐⭐⭐⭐ | ⭐ | ✅ | ⭐⭐⭐⭐ |
| Обычный хостинг | 💰 | ⭐⭐⭐ | ⭐⭐⭐ | ❓ | ⭐⭐⭐ |

---

## 🔧 Настройка после публикации

### 1. Обновите ссылки
В `index.html` замените:
```html
<a href="../build/libs/LiseryMenu-1.0.jar">
```
На:
```html
<a href="https://github.com/ваш-username/LiseryMenu/releases/latest">
```

### 2. Добавьте аналитику (опционально)

**Google Analytics:**
```html
<!-- В <head> -->
<script async src="https://www.googletagmanager.com/gtag/js?id=G-XXXXXXXXXX"></script>
<script>
  window.dataLayer = window.dataLayer || [];
  function gtag(){dataLayer.push(arguments);}
  gtag('js', new Date());
  gtag('config', 'G-XXXXXXXXXX');
</script>
```

### 3. Добавьте мета-теги для SEO

```html
<meta name="description" content="LiseryMenu - Легковесная система создания меню для Minecraft 1.21+">
<meta name="keywords" content="minecraft, plugin, menu, liserymenu, deluxmenus">
<meta property="og:title" content="LiseryMenu - Документация">
<meta property="og:description" content="Легковесная система создания меню для Minecraft">
<meta property="og:image" content="https://ваш-сайт.com/preview.png">
```

---

## 📱 Проверка адаптивности

После публикации проверьте сайт на разных устройствах:

- ✅ Десктоп (1920x1080)
- ✅ Ноутбук (1366x768)
- ✅ Планшет (768x1024)
- ✅ Телефон (375x667)

**Инструменты:**
- Chrome DevTools (F12)
- [ResponsiveDesignChecker.com](https://responsivedesignchecker.com)
- [BrowserStack](https://www.browserstack.com)

---

## 🎯 Рекомендуемый вариант

**GitHub Pages** - идеально для документации:
- ✅ Бесплатно
- ✅ Просто
- ✅ Интегрируется с GitHub
- ✅ Автообновление при push

**Netlify** - если нужны дополнительные функции:
- ✅ Формы
- ✅ Функции (serverless)
- ✅ Сплит-тестинг

---

## 💡 Советы

1. **Используйте CDN** для статики (если хостите сами)
2. **Включите gzip** сжатие
3. **Оптимизируйте изображения** (если добавите)
4. **Настройте кеширование** в headers
5. **Добавьте robots.txt** и **sitemap.xml**

---

## 🆘 Проблемы?

### Сайт не открывается
- Проверьте DNS записи
- Подождите 24 часа (DNS propagation)
- Очистите кеш браузера

### Стили не применяются
- Проверьте пути к файлам
- Убедитесь что CSS загружается (DevTools → Network)

### 404 ошибка на GitHub Pages
- Убедитесь что файл называется `index.html`
- Проверьте настройки Pages (папка `/docs`)

---

**Готово! Ваш сайт опубликован! 🎉**

