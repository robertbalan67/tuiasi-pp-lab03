package ro.tuiasi.pp.lab03.crawler

import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import java.net.URI
import java.util.concurrent.TimeUnit

// ─── ADT ──────────────────────────────────────────────────────────────────────

data class LinkNode(
    val url: String,
    val children: MutableList<LinkNode> = mutableListOf()
)

// ─── Web Crawler ───────────────────────────────────────────────────────────────

class WebCrawler {

    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    /**
     * Crawlează startUrl la adâncimea 2:
     *   root (startUrl)
     *     └─ level-1 links (din startUrl)
     *          └─ level-2 links (din fiecare link de nivel 1)
     *
     * Reguli:
     *  - Păstrează doar link-uri din același domeniu ca startUrl
     *  - Nu vizitează același URL de două ori
     */
    fun crawl(startUrl: String): LinkNode {
        val root = LinkNode(startUrl)
        val visited = mutableSetOf(startUrl)
        val domain = extractDomain(startUrl)

        // Nivel 1
        for (url1 in fetchLinks(startUrl, domain)) {
            if (url1 in visited) continue
            visited.add(url1)
            val node1 = LinkNode(url1)
            root.children.add(node1)

            // Nivel 2
            for (url2 in fetchLinks(url1, domain)) {
                if (url2 in visited) continue
                visited.add(url2)
                node1.children.add(LinkNode(url2))
            }
        }

        return root
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private fun fetchLinks(url: String, domain: String): List<String> = try {
        val request = Request.Builder().url(url).build()
        client.newCall(request).execute().use { response ->
            val html = response.body?.string() ?: return emptyList()
            Jsoup.parse(html, url)
                .select("a[href]")
                .map { it.absUrl("href") }
                .filter { it.isNotBlank() && extractDomain(it) == domain }
                .distinct()
        }
    } catch (_: Exception) {
        emptyList()
    }

    private fun extractDomain(url: String): String = try {
        URI(url).host?.lowercase() ?: ""
    } catch (_: Exception) {
        ""
    }
}

// ─── Tree Serializer ──────────────────────────────────────────────────────────

/**
 * Serializare/deserializare arbore de link-uri.
 *
 * Format ales: "adancime:url" pe fiecare linie
 *
 *   0:https://example.com
 *   1:https://example.com/page1
 *   2:https://example.com/page1/sub1
 *   2:https://example.com/page1/sub2
 *   1:https://example.com/page2
 *
 * Primul ":" este separatorul de adâncime — restul liniei este URL-ul complet
 * (inclusiv "https://..." care conține ":" în el).
 * Roundtrip garantat: deserialize(serialize(root)) == root
 */
class TreeSerializer {

    fun serialize(root: LinkNode): String {
        val sb = StringBuilder()
        serializeNode(root, depth = 0, sb)
        return sb.toString().trimEnd('\n')
    }

    private fun serializeNode(node: LinkNode, depth: Int, sb: StringBuilder) {
        sb.append("$depth:${node.url}\n")
        for (child in node.children) {
            serializeNode(child, depth + 1, sb)
        }
    }

    fun deserialize(input: String): LinkNode {
        val lines = input.trim().lines().filter { it.isNotBlank() }
        require(lines.isNotEmpty()) { "Input gol — nu se poate deserializa" }

        // Parsăm prima linie ca rădăcină
        val (rootDepth, rootUrl) = parseLine(lines[0])
        require(rootDepth == 0) { "Prima linie trebuie să aibă adâncimea 0" }
        val root = LinkNode(rootUrl)

        // Stivă de (adâncime, nod) — menţinem calea curentă de la rădăcină
        val stack = ArrayDeque<Pair<Int, LinkNode>>()
        stack.addLast(0 to root)

        for (line in lines.drop(1)) {
            val (depth, url) = parseLine(line)
            val node = LinkNode(url)

            // Scoatem din stivă până găsim părintele (adâncime = depth - 1)
            while (stack.isNotEmpty() && stack.last().first >= depth) {
                stack.removeLast()
            }

            stack.last().second.children.add(node)
            stack.addLast(depth to node)
        }

        return root
    }

    private fun parseLine(line: String): Pair<Int, String> {
        val colonIdx = line.indexOf(':')
        require(colonIdx > 0) { "Linie invalidă (fără ':' prefix): $line" }
        val depth = line.substring(0, colonIdx).toInt()
        val url   = line.substring(colonIdx + 1)
        return depth to url
    }
}
