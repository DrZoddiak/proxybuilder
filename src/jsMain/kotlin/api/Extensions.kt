package api

internal fun <T> MutableList<T>.addAllOf(vararg elements: Collection<T>): List<T> {
    elements.forEach {
        this.addAll(it)
    }
    return this.toList()
}
