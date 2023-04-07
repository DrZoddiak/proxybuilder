package api.card

import ArtCard
import api.WebClient
import api.WebClient.addCard
import api.WebClient.cardList
import api.card.CardLookupAPI.singleCardLookup

object ArtCardAPI {
    suspend fun getArtCardList(): List<ArtCard> {
        return cardList(ArtCard.path)
    }

    private suspend fun addArtCard(artCard: ArtCard) {
        addCard(artCard)
    }

    suspend fun deleteArtCards() {
        WebClient.deleteCards(ArtCard.path)
    }

    suspend fun reloadArtCards(card: String) {
        deleteArtCards()

        artCards(card)?.forEach {
            addArtCard(it)
        }
    }

    private suspend fun artCards(card: String): List<ArtCard>? {
        //produce Art Card
        val cat =
            "https://excitedcats.com/wp-content/uploads/2020/06/brown-tabby_shutterstock_-gillmar-scaled.jpg"
        val artCards = singleCardLookup(card)?.map {
            val imageUris = it.imageUris
            val normal = imageUris?.normal ?: cat
            ArtCard(
                it.id, it.name, normal, 2
            )
        }

        return artCards
    }
}