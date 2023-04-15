package api.card

import ArtCard
import FinalizedCard
import api.Api.scryfallApi
import api.card.CardLookupAPI.standardCard

object FinalizedCardAPI {
    private val finalizedCards = mutableListOf<FinalizedCard>()

    fun addCard(card: FinalizedCard): FinalizedCard {
        finalizedCards.add(card)
        return card
    }

    fun removeCard(card: FinalizedCard): Boolean {
        return finalizedCards.remove(card)
    }

    fun removeCards() = finalizedCards.clear()

    fun replaceCard(card: FinalizedCard, updated: ArtCard) {
        val index = finalizedCards.indexOf(card)
        finalizedCards.removeAt(index)
        finalizedCards[index] = updated.asFinalizedCard()
    }

    fun getFinalCards(): MutableList<FinalizedCard> {
        return finalizedCards
    }

    suspend fun finalizedCards(input: String) {
        standardCard(input).forEach {
            val lookup = scryfallApi.namedCardLookup(input) ?: error("We can't process this image")
            val normal = lookup.imageUris?.normal ?: error("We can't process this image")

            FinalizedCard(
                lookup.id,
                lookup.name,
                normal
            )
        }
    }

}