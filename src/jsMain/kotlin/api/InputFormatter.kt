package api

object InputFormatter {
    fun format(input: String): String {
        return removeNumbers(input)
    }

    private fun removeNumbers(input: String): String {
        return input.filterNot { it.isDigit() }.trim()
    }

    fun formatSearch(input: String): List<String> {
        return input.split("\n")
            .filterNot { it.isEmpty() }
            .map { removeNumbers(it) }
    }

}