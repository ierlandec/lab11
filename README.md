# 🔔 Умные напоминания о перерывах (Lab 11)

## 📌 Описание проекта
Приложение для Android помогает соблюдать режим работы и отдыха и через заданные промежутки времени отправляет локальные уведомления без интернета и FCM. Логику напоминаний обеспечивает `AlarmManager` с точными будильниками и обработкой событий в `BroadcastReceiver`, а пользовательские действия реализованы через кнопки в уведомлении с последующим повторным планированием или закрытием.

## 🛠 Технический стек
Проект написан на Kotlin, интерфейс построен на Jetpack Compose и Material 3, уведомления создаются через `NotificationManager` и `NotificationCompat`, а фоновые события и разрешения учитывают Android 12+ для точных будильников и Android 13+ для постинга уведомлений.

## 📂 Документация по ключевым файлам
Файл `app/src/main/java/com/example/lab11/MainActivity.kt` — это точка входа UI, где создан Compose-экран `BreakReminderScreen`. Здесь используется стек Jetpack Compose и Material 3, а также система разрешений Android 13+ через `ActivityResultContracts.RequestPermission`. Внутри экрана реализованы две пользовательские операции: запуск рабочего цикла и тестовое уведомление. Для первого сценария вызывается `hasPermission(context)`, затем проверяется возможность точных будильников через `AlarmManager.canScheduleExactAlarms()` на Android 12+, после чего создается `NotificationHelper` и вызывается `scheduleReminder(50)`. Для теста вызывается `showBreakReminder()`. Методы `onCreate`, `BreakReminderScreen`, `hasPermission` и `GreetingPreview` показывают, где настраивается тема, как проверяются разрешения и как инициируется бизнес-логика уведомлений.

Файл `app/src/main/java/com/example/lab11/NotificationHelper.kt` инкапсулирует всю работу с уведомлениями и планированием. Здесь используется стек `NotificationManager`, `NotificationCompat` и `AlarmManager` с точными будильниками `setExactAndAllowWhileIdle`. Метод `showBreakReminder()` строит уведомление, формирует `PendingIntent` для открытия приложения и действий "Пропустить" и "Отложить", задает иконки и тексты, затем показывает уведомление через `NotificationManager.notify`. Метод `scheduleReminder(delayMinutes)` вычисляет время срабатывания и регистрирует `PendingIntent` на `ReminderReceiver`. В `companion object` находятся идентификаторы канала и действий, которые используются в разных компонентах приложения.

Файл `app/src/main/java/com/example/lab11/ReminderReceiver.kt` — приемник `BroadcastReceiver`, который срабатывает по сигналу `AlarmManager` и инициирует показ уведомления. Здесь используется минимальный стек Android компонентов: `BroadcastReceiver`, `Context` и `Intent`. Основной метод `onReceive` создает `NotificationHelper` и вызывает `showBreakReminder()`, поэтому именно этот класс связывает будильник и показ уведомления.

Файл `app/src/main/java/com/example/lab11/NotificationReceiver.kt` обрабатывает действия пользователя в уведомлении. В `onReceive` читается `intent.action` и при `ACTION_SKIP` уведомление закрывается через `NotificationManager.cancel`, а при `ACTION_SNOOZE` дополнительно вызывается `NotificationHelper(context).scheduleReminder(5)`. Используются `BroadcastReceiver`, `NotificationManager`, `Toast` и константы действий из `NotificationHelper`, что делает этот класс связующим звеном между UI-уведомлением и повторным планированием.

Файл `app/src/main/java/com/example/lab11/MainApplication.kt` отвечает за подготовку инфраструктуры уведомлений на уровне приложения. В `onCreate` вызывается `createNotificationChannel()`, где создается `NotificationChannel` с важностью `IMPORTANCE_HIGH`, описанием и настройкой подсветки, а затем регистрируется в `NotificationManager`. Используется стек `Application`, `NotificationChannel`, `NotificationManager` и проверка версии Android, потому что канал обязателен на Android 8.0+.

Файлы `app/src/main/java/com/example/lab11/ui/theme/Theme.kt`, `Type.kt` и `Color.kt` — это слой темы Compose, который определяет цветовые схемы и типографику Material 3. В `Theme.kt` используется `MaterialTheme`, `darkColorScheme`, `lightColorScheme` и динамические цвета на Android 12+, а `Type.kt` и `Color.kt` задают базовые параметры шрифтов и цвета, которые применяются ко всему UI.

## ⚠️ Важные примечания
Уведомления планируются через `setExactAndAllowWhileIdle`, поэтому приходят даже в режиме Doze, а на Android 13+ пользователь должен разрешить отправку уведомлений при первом запуске, иначе кнопки запуска будут недоступны.
