import kotlinx.serialization.Serializable

@Serializable
data class FinalizedCard(
    override val cardId: String,
    override val name: String,
    override var imageUri: String,
) : Card {
    val id: Int = cardId.hashCode()

    companion object {
        const val path = "/finalizedCard"
    }

    val staticPath: String
        get() = path
}

@Serializable
data class ArtCard(
    override val cardId: String,
    override val name: String,
    override var imageUri: String,
) : Card {

    fun asFinalizedCard(): FinalizedCard {
        return FinalizedCard(cardId, name, imageUri)
    }
}

interface Card {
    val cardId: String
    val name: String
    var imageUri: String
}
