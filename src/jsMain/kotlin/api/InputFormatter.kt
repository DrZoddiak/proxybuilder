package api

object InputFormatter {
    private fun removeNumbers(input: String): String {
        return input.filterNot { it.isDigit() }.trim()
    }

    private val invalidEntries = listOf(
        "sideboard"
    )

    fun formatSearch(input: String): List<String> {
        return input.split("\n")
            .filterNot { it.isEmpty() }
            .map { removeNumbers(it) }
            .filterNot { invalidEntries.contains(it.lowercase()) }
            .distinct()
    }
}