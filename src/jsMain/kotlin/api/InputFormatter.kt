package api

object InputFormatter {
    fun format(input: String): String {
        return removeNumbers(input)
    }

    private fun removeNumbers(input: String): String {
        return input.filterNot { it.isDigit() }.trim()
    }

}