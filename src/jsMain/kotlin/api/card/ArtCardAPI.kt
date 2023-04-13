package api.card

import ArtCard
import FinalizedCard
import api.card.CardLookupAPI.singleCardLookup

object ArtCardAPI {
    private val artCards = mutableMapOf<FinalizedCard, List<ArtCard>>()

    fun deleteArtCards() {
        artCards.clear()
    }

    suspend fun loadArtCards(card: FinalizedCard): List<ArtCard> {
        return artCards.getOrPut(card) { this.artCards(card.name) ?: error("Whoops") }
    }

    private suspend fun artCards(card: String): List<ArtCard>? {
        //produce Art Card
        val artCards = singleCardLookup(card)?.map {
            val imageUris = it.imageUris
            val normal = imageUris?.normal ?: error("We can't process this image")
            ArtCard(
                it.id, it.name, normal
            )
        }

        return artCards
    }
}