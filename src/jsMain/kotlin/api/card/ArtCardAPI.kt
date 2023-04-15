package api.card

import ArtCard
import FinalizedCard

object ArtCardAPI {
    private val artCards = mutableMapOf<FinalizedCard, List<ArtCard>>()

    fun clear() {
        artCards.clear()
    }

    suspend fun loadCards(card: FinalizedCard): List<ArtCard> {
        return artCards.getOrPut(card) { this.artCards(card.name) ?: error("Error loading image") }
    }

    private suspend fun artCards(card: String): List<ArtCard>? {
        val artCards = CardLookupAPI.artCards(card)?.map {
            //TODO: Process images with special faces
            //Image may not process because it has multiple faces
            val image = it.imageUris?.normal ?: error("We can't process this image")
            ArtCard(
                it.id, it.name, image
            )
        }

        return artCards
    }
}