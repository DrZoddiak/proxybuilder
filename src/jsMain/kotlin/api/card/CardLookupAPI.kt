package api.card

import CardData
import api.Api
import api.InputFormatter

object CardLookupAPI {
    suspend fun cardLookup(input: String): List<CardData> {
        return input.split("\n")
            .filterNot { it.isEmpty() }
            .map { InputFormatter.format(it) }
            .mapNotNull { Api.scryfallApi.namedCardLookup(it) }
    }

    suspend fun singleCardLookup(input: String): List<CardData>? {
        return Api.scryfallApi.namedCardLookup(input)?.printsSearchUri?.let { Api.scryfallApi.artLookup(it) }?.data
    }
}