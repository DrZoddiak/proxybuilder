@file:Suppress("unused")

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.js.*
import io.ktor.client.fetch.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.await
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import libraries.FileSaver
import libraries.JSZip
import libraries.JSZipGeneratorOptions
import libraries.invoke

object Api {
    private val jsonClient = HttpClient(Js) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
        install(Logging)
    }

    private suspend fun deleteCards(path: String) {
        jsonClient.delete(path)
    }

    private suspend inline fun <reified T> cardList(path: String): List<T> {
        return jsonClient.get(path).body()
    }

    private suspend inline fun <reified T> addCards(cards: List<T>, path: String) {
        cards.forEach {
            jsonClient.post(path) {
                contentType(ContentType.Application.Json)
                setBody(it)
            }
        }
    }

    private fun removeNumbers(entry: String): String {
        return entry.filterNot { it.isDigit() }.trim()
    }

    private val ScrypicsApi by lazy {
        Scrypics(jsonClient)
    }

    private class Scrypics(private val client: HttpClient, private val url: String = "https://api.scryfall.com/cards") {
        suspend fun fetch(input: String): List<CardData> {
            delay(50)
            return client.get("$url/search") {
                url {
                    parameters.append("unique", "art")
                    parameters.append("q", """ "$input" """.trim())
                }
            }.body<BaseResponse>().data
        }
    }

    suspend fun getFinalizedCardList(): List<FinalizedCard> {
        return cardList(FinalizedCard.path)
    }

    private fun generatorOptions(type: String? = ""): JSZipGeneratorOptions {
        val o = js("({})")
        o["type"] = type
        return o
    }

    suspend fun getArtCardList(): List<ArtCard> {
        return cardList(ArtCard.path)
    }

    suspend fun addArtCard(artCard: List<ArtCard>) {
        addCards(artCard, ArtCard.path)
    }

    suspend fun addFinalizedCard(finalizedCard: FinalizedCard) {
        jsonClient.post(FinalizedCard.path) {
            contentType(ContentType.Application.Json)
            setBody(finalizedCard)
        }
    }

    suspend fun addFinalizedCard(finalizedCard: List<FinalizedCard>) {
        addCards(finalizedCard, FinalizedCard.path)
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

    suspend fun cardLookup(input: String): List<List<CardData>> {
        return input.split("\n")
            .filterNot { it.isEmpty() }
            .map { removeNumbers(it) }
            .map { ScrypicsApi.fetch(it) }
    }

    suspend fun zipFiles(list : List<FinalizedCard>) {
        val zip = JSZip()
        val img = zip.folder("images")
        list.forEach {card ->
            fetch(card.imageUri).then {
                img?.file("${card.name}.png",it.blob())
            }.await()
        }
        zip.generateAsync(generatorOptions("blob")).then { blob ->
            FileSaver(blob, "proxy.zip")
        }.await()
    }
}


