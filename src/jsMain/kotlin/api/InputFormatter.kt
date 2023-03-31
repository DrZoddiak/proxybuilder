package api

object InputFormatter {
    fun format(input: String): String {
        return input.removeNumbers(input)
    }

    private fun String.removeNumbers(input: String): String {
        return input.filterNot { it.isDigit() }.trim()
    }

}