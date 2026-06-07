package ro.tuiasi.pp.lab03.rss

import org.jsoup.Jsoup
import org.jsoup.parser.Parser

// ─── ADT-uri (tipuri algebrice de date) ───────────────────────────────────────

data class RssItem(
    val title: String,
    val link: String,
    val description: String
)

data class RssChannel(
    val title: String,
    val link: String,
    val description: String,
    val items: List<RssItem>
)

// ─── Parser ───────────────────────────────────────────────────────────────────

class RssParser {

    /**
     * Parsează un șir XML conform structurii RSS 2.0 și returnează un RssChannel
     * populat cu toate elementele <item> găsite.
     *
     * Exemplu XML de intrare:
     * <rss version="2.0">
     *   <channel>
     *     <title>Știri Tehnice</title>
     *     <link>https://example.com</link>
     *     <description>Cele mai noi știri</description>
     *     <item>
     *       <title>Articol 1</title>
     *       <link>https://example.com/1</link>
     *       <description>Descriere articol 1</description>
     *     </item>
     *   </channel>
     * </rss>
     */
    fun parse(xmlString: String): RssChannel {
        // Parser.xmlParser() tratează documentul ca XML pur, nu HTML
        // — esențial pentru <link> care altfel ar fi tratat ca self-closing în HTML
        val doc = Jsoup.parse(xmlString, "", Parser.xmlParser())

        // Selectori "channel > tag" = copil DIRECT al lui <channel>,
        // ca să nu capturăm <title>/<link> din interiorul <item>-urilor
        val title       = doc.selectFirst("channel > title")?.text()       ?: ""
        val link        = doc.selectFirst("channel > link")?.text()         ?: ""
        val description = doc.selectFirst("channel > description")?.text()  ?: ""

        val items = doc.select("item").map { item ->
            RssItem(
                title       = item.selectFirst("title")?.text()       ?: "",
                link        = item.selectFirst("link")?.text()         ?: "",
                description = item.selectFirst("description")?.text()  ?: ""
            )
        }

        return RssChannel(title, link, description, items)
    }
}
