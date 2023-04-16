package api.card

import CardData
import api.Api
import api.InputFormatter

internal object CardLookupAPI {
    suspend fun standardCard(input: String): List<CardData> {
        return InputFormatter.formatSearch(input).mapNotNull { Api.scryfallApi.namedCardLookup(it) }
    }

    internal suspend fun artCards(input: String): List<CardData>? {
        return Api.scryfallApi.namedCardLookup(input)?.printsSearchUri?.let { Api.scryfallApi.artLookup(it) }?.data
    }
}