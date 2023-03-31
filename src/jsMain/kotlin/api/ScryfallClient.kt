package api

import BaseResponse
import CardData
import api.Api.uniqueSetting
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.delay

class ScryfallClient(private val client: HttpClient, private val url: String = "https://api.scryfall.com/cards") {
    private val delayTime = 50L //Suggested delay by api
    private val uniqueParam: String
        get() = if (uniqueSetting) "art" else "prints"

    private suspend fun fetch(urlString: String, block: HttpRequestBuilder.() -> Unit): HttpResponse {
        delay(delayTime)
        return client.get(urlString, block)
    }

    private suspend fun fetchBase(input: String): HttpResponse {
        return fetch("$url/search") {
            url {
                parameters.append("unique", uniqueParam)
                parameters.append("q", """ "$input" """.trim())
            }
        }
    }
    
    private suspend fun fetchNamed(input: String): HttpResponse {
        return fetch("$url/named") {
            url {
                parameters.append("exact", input)
            }
        }
    }

    private suspend inline fun <reified T> HttpResponse.bodyOrNull(): T? =
        if (status == HttpStatusCode.NotFound) null else body()

    suspend fun namedCardLookup(input: String): CardData? {
        val response = fetchNamed(input)
        return response.bodyOrNull()
    }

    suspend fun artLookup(input: String): BaseResponse {
        return client.get(input).body()
    }

}
