package api.card

import ArtCard
import FinalizedCard
import api.Api
import api.WebClient
import api.card.CardLookupAPI.cardLookup

object FinalizedCardAPI {
    suspend fun getFinalizedCardList(): List<FinalizedCard> {
        return WebClient.cardList(FinalizedCard.path)
    }

    suspend fun addFinalizedCard(finalizedCard: FinalizedCard) {
        WebClient.addCard(finalizedCard)
    }

    suspend fun replaceFinalizedCard(card: FinalizedCard, artCard: ArtCard) {
        WebClient.replaceCard(card, artCard)
    }

    suspend fun deleteFinalizedCards() {
        WebClient.deleteCards(FinalizedCard.path)
    }

    suspend fun deleteFinalizedCard(finalizedCard: FinalizedCard) {
        WebClient.deleteCards(FinalizedCard.path + "/${finalizedCard.id}")
    }

    suspend fun finalizedCards(input: String): List<FinalizedCard> {
        return cardLookup(input).map {
            val cat =
                "https://excitedcats.com/wp-content/uploads/2020/06/brown-tabby_shutterstock_-gillmar-scaled.jpg"
            val normal = it.imageUris?.normal ?: cat
            FinalizedCard(
                it.id,
                it.name,
                normal,
                Api.scryfallApi.artLookup(it.printsSearchUri).totalCards
            )
        }
    }

}