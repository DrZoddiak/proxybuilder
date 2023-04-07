package api.card

import ArtCard
import FinalizedCard
import api.WebClient

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

}