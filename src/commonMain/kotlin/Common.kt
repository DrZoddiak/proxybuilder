import kotlinx.serialization.Serializable

@Serializable
data class FinalizedCard(
    override val cardId: String,
    override val name: String,
    override var imageUri: String,
    override val totalCards: Int
) : Card {
    val id: Int = cardId.hashCode()

    companion object {
        const val path = "/finalizedCard"
    }

    override val staticPath: String
        get() = path
}

@Serializable
data class ArtCard(
    override val cardId: String,
    override val name: String,
    override var imageUri: String,
    override val totalCards: Int
) : Card {
    companion object {
        const val path = "/artcard"
    }

    override val staticPath: String
        get() = path

    fun asFinalizedCard(): FinalizedCard {
        return FinalizedCard(cardId, name, imageUri, totalCards)
    }
}

interface Card {
    val cardId: String
    val name: String
    var imageUri: String
    val totalCards: Int
    val staticPath: String
}
