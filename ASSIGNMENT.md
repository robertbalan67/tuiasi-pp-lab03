# Lab 03 — Kotlin ADT (OkHttp + jsoup)

## Obiective

- Familiarizarea cu parsarea datelor structurate (XML/HTML) în Kotlin
- Utilizarea bibliotecilor OkHttp și jsoup
- Definirea tipurilor algebrice de date (ADT) în Kotlin
- Procesarea textului cu expresii regulate
- Construirea și serializarea structurilor de tip arbore

---

## Tema 1 — Parser RSS feed

### Cerință

Implementați un parser pentru feed-uri RSS în clasa `RssParser` din pachetul `ro.tuiasi.pp.lab03.rss`.

### Structura datelor

```
RssItem(title: String, link: String, description: String)
RssChannel(title: String, link: String, description: String, items: List<RssItem>)
```

### Funcție de implementat

```kotlin
fun parse(xmlString: String): RssChannel
```

Funcția primește un șir XML conform structurii RSS standard și returnează un obiect `RssChannel` populat cu toate elementele `<item>` găsite.

### Exemplu XML de intrare

```xml
<rss version="2.0">
  <channel>
    <title>Știri Tehnice</title>
    <link>https://example.com</link>
    <description>Cele mai noi știri</description>
    <item>
      <title>Articol 1</title>
      <link>https://example.com/1</link>
      <description>Descriere articol 1</description>
    </item>
  </channel>
</rss>
```

---

## Tema 2 — Procesor text ebook

### Cerință

Implementați clasa `EbookProcessor` din pachetul `ro.tuiasi.pp.lab03.ebook` cu următoarele funcții de procesare text folosind expresii regulate.

### Funcții de implementat

| Funcție | Comportament |
|---------|-------------|
| `removeMultipleSpaces(text: String): String` | Înlocuiește orice secvență de 2+ spații cu un singur spațiu |
| `removeMultipleNewlines(text: String): String` | Înlocuiește orice secvență de 2+ linii goale cu o singură linie goală |
| `removePageNumbers(text: String): String` | Elimină numere de pagină apărute singure pe o linie (ex: `\n  42  \n`) |
| `fixRomanianChars(text: String): String` | *(opțional)* Corectează caractere românești encodate greșit (ş→ș, ţ→ț etc.) |

---

## Tema 3 — Web crawler cu arbore

### Cerință

Implementați un crawler web care construiește un arbore de link-uri cu adâncimea 2 (URL inițial → link-urile sale → link-urile lor), păstrând doar același domeniu.

### Clase de implementat

**`LinkNode`** — nod în arborele de link-uri:
```kotlin
data class LinkNode(val url: String, val children: MutableList<LinkNode> = mutableListOf())
```

**`WebCrawler`** — efectuează crawling-ul:
```kotlin
fun crawl(startUrl: String): LinkNode
```

**`TreeSerializer`** — serializare/deserializare arbore:
```kotlin
fun serialize(root: LinkNode): String
fun deserialize(input: String): LinkNode
```

### Observații

- Folosiți OkHttp pentru cereri HTTP și jsoup pentru parsarea HTML
- Păstrați doar link-urile din același domeniu ca URL-ul de start
- Evitați ciclurile (un URL vizitat nu se mai vizitează a doua oară)
- Formatul de serializare este la alegere, dar trebuie să fie reversibil (roundtrip fără pierderi)

---

## Criterii de evaluare

| Criteriu | Puncte |
|---------|--------|
| Tema 1 — RssParser funcțional | 30p |
| Tema 2 — EbookProcessor complet | 30p |
| Tema 3 — WebCrawler + serializare | 40p |

**Total: 100 puncte**

---

## Rulare teste

```bash
gradle test
```

Toate testele trebuie să treacă înainte de predare.
