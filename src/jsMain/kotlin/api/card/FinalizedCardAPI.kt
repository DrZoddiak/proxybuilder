package api.card

import ArtCard
import FinalizedCard
import api.Api
import api.WebClient.addCard
import api.WebClient.cardList
import api.WebClient.deleteCards
import api.WebClient.replaceCard
import api.card.CardLookupAPI.cardLookup

object FinalizedCardAPI {
    suspend fun getFinalizedCardList(): List<FinalizedCard> {
        return cardList(FinalizedCard.path)
    }

    suspend fun addFinalizedCard(finalizedCard: FinalizedCard) {
        addCard(finalizedCard)
    }

    suspend fun replaceFinalizedCard(card: FinalizedCard, artCard: ArtCard) {
        replaceCard(card, artCard)
    }

    suspend fun deleteFinalizedCards() {
        deleteCards(FinalizedCard.path)
    }

    suspend fun deleteFinalizedCard(finalizedCard: FinalizedCard) {
        deleteCards(FinalizedCard.path + "/${finalizedCard.id}")
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