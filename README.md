# Lab 03 — Kotlin ADT (OkHttp + jsoup)

Laborator de programare în Kotlin cu accent pe tipuri algebrice de date și procesarea datelor web.

## Structura proiectului

```
lab03/
├── src/
│   ├── main/kotlin/ro/tuiasi/pp/lab03/
│   │   ├── rss/          # Tema 1: Parser RSS
│   │   ├── ebook/        # Tema 2: Procesor ebook
│   │   └── crawler/      # Tema 3: Web crawler
│   └── test/kotlin/ro/tuiasi/pp/lab03/
│       ├── RssParserTest.kt
│       ├── EbookProcessorTest.kt
│       └── TreeSerializerTest.kt
├── .github/workflows/classroom.yml
├── build.gradle.kts
├── settings.gradle.kts
├── ASSIGNMENT.md
└── README.md
```

## Tehnologii folosite

- **Kotlin** 2.0.21
- **OkHttp** 4.12.0 — cereri HTTP
- **jsoup** 1.18.1 — parsare HTML/XML
- **JUnit 5** — testare

## Cum rulezi

```bash
# Compilare și teste
gradle test

# Compilare fără teste
gradle classes
```

## Cerințe sistem

- JDK 21 (Temurin recomandat)
- Gradle 8.11+ (sau IntelliJ cu suport Gradle)

## Citește mai mult

Vezi [ASSIGNMENT.md](ASSIGNMENT.md) pentru cerințele complete ale temelor.
