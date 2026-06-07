package ro.tuiasi.pp.lab03.ebook

class EbookProcessor {

    /**
     * Înlocuiește orice secvență de 2 sau mai multe spații cu un singur spațiu.
     *
     * Exemplu: "ana  are   mere" -> "ana are mere"
     */
    fun removeMultipleSpaces(text: String): String =
        text.replace(Regex(" {2,}"), " ")

    /**
     * Înlocuiește orice secvență de 3 sau mai multe newline-uri consecutive
     * (= 2+ linii goale) cu exact 2 newline-uri (= o singură linie goală).
     *
     * Normalizează și \r\n la \n înainte de procesare.
     */
    fun removeMultipleNewlines(text: String): String =
        text.replace("\r\n", "\n")
            .replace(Regex("\n{3,}"), "\n\n")

    /**
     * Elimină numerele de pagină apărute singure pe o linie
     * (linie cu doar cifre, opțional înconjurate de spații).
     *
     * Exemplu: "\n  42  \n" -> "\n\n"
     * Foloseşte modul MULTILINE (^/$  = început/sfârşit de linie, nu de string).
     */
    fun removePageNumbers(text: String): String =
        text.replace(Regex("(?m)^\\s*\\d+\\s*$"), "")

    /**
     * (Opțional) Corectează caractere românești encodate cu cedilă (greșit)
     * în caractere cu virgulă (corect conform standardului Unicode).
     *
     * ş (U+015F) → ș (U+0219)   s cu cedilă → s cu virgulă
     * ţ (U+0163) → ț (U+021B)   t cu cedilă → t cu virgulă
     */
    fun fixRomanianChars(text: String): String =
        text
            .replace('\u015F', '\u0219')  // ş → ș
            .replace('\u015E', '\u0218')  // Ş → Ș
            .replace('\u0163', '\u021B')  // ţ → ț
            .replace('\u0162', '\u021A')  // Ţ → Ț
}
