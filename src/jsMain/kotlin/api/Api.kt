package api

import api.WebClient.jsonClient

internal object Api {
    val scryfallApi by lazy {
        ScryfallClient(jsonClient)
    }
}


