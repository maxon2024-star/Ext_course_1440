# 🛰️ Система управления спутниковой группировкой

Java-приложение для управления спутниковыми группировками с использованием **Spring Boot**, **Dependency Injection** и полного покрытия тестами.

---

## 📋 Содержание

- [О проекте](#о-проекте)
- [Технологии](#технологии)
- [Структура проекта](#структура-проекта)
- [Установка и запуск](#установка-и-запуск)
- [Тестирование](#тестирование)
- [Отчёты](#отчёты)
- [Принципы SOLID](#принципы-solid)
- [Пример вывода](#пример-вывода)

---

## 📖 О проекте

Проект демонстрирует реализацию системы управления спутниковыми группировками с применением:

| Компонент | Описание |
|-----------|----------|
| **Spring Boot** | Контейнер для управления зависимостями (DI) |
| **Lombok** | Генерация шаблонного кода (геттеры, конструкторы, toString) |
| **JUnit 5** | Фреймворк для написания тестов |
| **Mockito** | Библиотека для создания моков |
| **JaCoCo** | Генерация отчётов о покрытии кода тестами |

### Основные возможности

- ✅ Создание спутников разных типов (связь, дистанционное зондирование)
- ✅ Объединение спутников в группировки
- ✅ Активация спутников и выполнение миссий
- ✅ CRUD-операции с группировками через репозиторий
- ✅ Три вида тестов: Unit, Mock, Integration
- ✅ Отчёт о покрытии кода (цель: 60%+)

---

## 🛠 Технологии

| Технология | Версия | Назначение |
|------------|--------|------------|
| Java | 21 | Основной язык разработки |
| Spring Boot | 3.4.2 | Dependency Injection, контейнер бинов |
| Gradle | 8.x | Система сборки |
| JUnit 5 | 5.x | Фреймворк для тестирования |
| Mockito | 5.x | Библиотека для моков |
| Lombok | 1.18.x | Генерация кода из аннотаций |
| JaCoCo | 0.8.12 | Покрытие кода тестами |

---

## 📁 Структура проекта

```
Ext_course_1440/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── seminars/
│   │           ├── Main.java                          # Точка входа (@SpringBootApplication)
│   │           ├── satellite/
│   │           │   ├── Satellite.java                 # Базовый класс спутника
│   │           │   ├── CommunicationSatellite.java    # Спутник связи
│   │           │   ├── ImagingSatellite.java          # Спутник съёмки
│   │           │   ├── SatelliteState.java            # Состояние спутника
│   │           │   └── EnergySystem.java              # Система энергии
│   │           ├── constellation/
│   │           │   └── SatelliteConstellation.java    # Спутниковая группировка
│   │           ├── repository/
│   │           │   └── ConstellationRepository.java   # Хранилище группировок (@Component)
│   │           └── service/
│   │               └── SpaceOperationCenterService.java # Сервис операций (@Service)
│   └── test/
│       └── java/
│           └── seminars/
│               ├── repository/
│               │   ├── ConstellationRepositoryUnitTest.java      # Юнит-тесты
│               │   ├── ConstellationRepositoryMockTest.java      # Мок-тесты
│               │   └── ConstellationRepositoryIntegrationTest.java # Интеграционные тесты
│               └── service/
│                   ├── SpaceOperationCenterServiceMockTest.java
│                   └── SpaceOperationCenterServiceIntegrationTest.java
├── build.gradle.kts          # Конфигурация сборки
└── README.md                 # Этот файл
```

---

## 🚀 Установка и запуск

### Требования

- Java 21 или выше
- Gradle 8.x (или использовать Gradle Wrapper)

### Шаг 1: Клонирование/подготовка проекта

Убедитесь, что все файлы находятся в правильных директориях согласно структуре выше.

### Шаг 2: Сборка проекта

```bash
./gradlew clean build
```

### Шаг 3: Запуск приложения

```bash
./gradlew bootRun
```

Или через IDE: запустите класс `Main.java` как Spring Boot приложение.

### Шаг 4: Проверка результата

В консоли вы увидите лог запуска Spring Boot и результаты операций:

```
ЗАПУСК СИСТЕМЫ УПРАВЛЕНИЯ СПУТНИКОВОЙ ГРУППИРОВКОЙ
============================================================
  ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
\\/  ___)| |_)| | | | | || (_| |  ) ) ) )
 '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.4.2)

СОЗДАНИЕ СПЕЦИАЛИЗИРОВАННЫХ СПУТНИКОВ:
---------------------------------------------
Создан спутник: Связь-1 (0.85)
...
```

---

## 🧪 Тестирование

### Запуск всех тестов

```bash
./gradlew test
```

### Запуск с генерацией отчёта JaCoCo

```bash
./gradlew clean test jacocoTestReport
```

### Типы тестов

| Тип | Класс | Описание |
|-----|-------|----------|
| **Unit** | `ConstellationRepositoryUnitTest` | Тестирование без внешних зависимостей |
| **Mock** | `ConstellationRepositoryMockTest` | Тестирование с изоляцией через Mockito |
| **Integration** | `ConstellationRepositoryIntegrationTest` | Тестирование в контексте Spring Boot |

### Аннотации в тестах

```java
// Unit-тесты
@Test
@DisplayName("Описание теста")
@BeforeEach

// Mock-тесты
@ExtendWith(MockitoExtension.class)
@Mock
@InjectMocks
@MockitoSettings(strictness = Strictness.LENIENT)

// Integration-тесты
@SpringBootTest
@Autowired
```

---

## 📊 Отчёты

### Отчёт о результатах тестов

После запуска `./gradlew test` откройте:

```
build/reports/tests/test/index.html
```

### Отчёт о покрытии кода (JaCoCo)

После запуска `./gradlew test jacocoTestReport` откройте:

```
build/reports/jacoco/test/html/index.html
```

### Требования к покрытию

| Классы | Минимальное покрытие |
|--------|---------------------|
| Недоменные классы | 60%+ |
| Доменные классы | по возможности |

---

## 🏗 Принципы SOLID

В проекте реализованы следующие принципы:

| Принцип | Реализация |
|---------|------------|
| **S** - Single Responsibility | Каждый класс отвечает за одну задачу |
| **O** - Open/Closed | Классы открыты для расширения, закрыты для изменений |
| **L** - Liskov Substitution | Подклассы спутников заменяют базовый класс |
| **I** - Interface Segregation | Чёткое разделение интерфейсов |
| **D** - Dependency Inversion | Внедрение зависимостей через Spring DI |

### Dependency Inversion в действии

```java
@Service
public class SpaceOperationCenterService {
    private final ConstellationRepository constellationRepository;
    
    // Внедрение через конструктор
    public SpaceOperationCenterService(ConstellationRepository constellationRepository) {
        this.constellationRepository = constellationRepository;
    }
}
```

---

## 📝 Пример вывода

```
=== АКТИВАЦИЯ СПУТНИКОВ В ГРУППИРОВКЕ: Орбита-1 ===
✅ Связь-1: Активация успешна
✅ ДЗЗ-1: Активация успешна
✅ ДЗЗ-2: Активация успешна

=== ВЫПОЛНЕНИЕ МИССИЙ ДЛЯ ГРУППИРОВКИ: Орбита-1 ===
ВЫПОЛНЕНИЕ МИССИЙ ГРУППИРОВКИ ОРБИТА-1
==================================================
Связь-1: Передача данных со скоростью 500.0 Мбит/с
ДЗЗ-1: Снимок #1 сделан!
ДЗЗ-2: Снимок #1 сделан!

=== СТАТУС ГРУППИРОВКИ: Орбита-1 ===
Количество спутников: 3
SatelliteState{isActive=true, statusMessage='Активен'}
...
```

---

## 🔧 Полезные команды Gradle

| Команда | Описание |
|---------|----------|
| `./gradlew clean` | Очистка build-директории |
| `./gradlew build` | Сборка проекта |
| `./gradlew bootRun` | Запуск Spring Boot приложения |
| `./gradlew test` | Запуск тестов |
| `./gradlew test jacocoTestReport` | Тесты + отчёт о покрытии |
| `./gradlew dependencies` | Просмотр зависимостей |

---

## 📚 Дополнительные материалы

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://site.mockito.org/)
- [JaCoCo Documentation](https://www.jacoco.org/jacoco/)
- [Project Lombok](https://projectlombok.org/)

---

## 👨‍💻 Автор

Проект создан в рамках курса по Spring Boot и принципам SOLID.

---

## 📄 Лицензия

Учебный проект для демонстрации принципов разработки.

---

> 💡 **Совет**: Если тесты не проходят, проверьте, что аннотация `@DisplayName` используется только на классах и методах, но не на полях!
