package api

import Card
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.js.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object WebClient {
    internal val jsonClient = HttpClient(Js) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
        install(Logging)
    }

    internal suspend fun deleteCards(path: String) {
        jsonClient.delete(path)
    }

    internal suspend inline fun <reified T> cardList(path: String): List<T> {
        return jsonClient.get(path).body()
    }

    internal suspend inline fun <reified T : Card> addCards(cards: List<T>) {
        cards.forEach {
            addCard(it)
        }
    }

    internal suspend inline fun <reified T : Card> addCard(card: T) {
        jsonClient.post(card.staticPath) {
            contentType(ContentType.Application.Json)
            setBody(card)
        }
    }

}