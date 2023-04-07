package api

import ArtCard
import CardData
import FinalizedCard
import api.WebClient.addCard
import api.WebClient.addCards
import api.WebClient.cardList
import api.WebClient.deleteCards
import api.WebClient.jsonClient
import io.ktor.client.fetch.*
import kotlinx.coroutines.await
import libraries.FileSaver
import libraries.JSZip
import libraries.generatorOptions
import libraries.invoke

object Api {
    val scryfallApi by lazy {
        ScryfallClient(jsonClient)
    }

    var uniqueSetting: Boolean = false

    suspend fun getFinalizedCardList(): List<FinalizedCard> {
        return cardList(FinalizedCard.path)
    }

    suspend fun getArtCardList(): List<ArtCard> {
        return cardList(ArtCard.path)
    }

    private suspend fun addArtCard(artCard: ArtCard) {
        addCard(artCard)
    }

    suspend fun addFinalizedCard(finalizedCard: FinalizedCard) {
        addCard(finalizedCard)
    }

    suspend fun addFinalizedCard(finalizedCard: List<FinalizedCard>) {
        addCards(finalizedCard)
    }

    suspend fun deleteArtCards() {
        deleteCards(ArtCard.path)
    }

    suspend fun deleteFinalizedCards() {
        deleteCards(FinalizedCard.path)
    }

    suspend fun deleteFinalizedCard(finalizedCard: FinalizedCard) {
        deleteCards(FinalizedCard.path + "/${finalizedCard.id}")
    }

    suspend fun cardLookup(input: String): List<CardData> {
        return input.split("\n")
            .filterNot { it.isEmpty() }
            .map { InputFormatter.format(it) }
            .mapNotNull { scryfallApi.namedCardLookup(it) }
    }

    private suspend fun singleCardLookup(input: String): List<CardData>? {
        val card = scryfallApi.namedCardLookup(input)
        return card?.printsSearchUri?.let { scryfallApi.artLookup(it) }?.data
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

    suspend fun zipFiles(list: List<FinalizedCard>) {
        val zip = JSZip()
        val img = zip.folder("images")
        list.forEach { card ->
            fetch(card.imageUri).then {
                img?.file("${card.name}.png", it.blob())
            }.await()
        }
        zip.generateAsync(generatorOptions("blob")).then { blob ->
            FileSaver(blob, "proxy.zip")
        }.await()
    }

}


