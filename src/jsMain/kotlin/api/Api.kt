package api

import api.WebClient.jsonClient

object Api {
    val scryfallApi by lazy {
        ScryfallClient(jsonClient)
    }
}


