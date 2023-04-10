package api

import api.WebClient.jsonClient
import csstype.ClassName

object Api {
    val scryfallApi by lazy {
        ScryfallClient(jsonClient)
    }

    fun Boolean.sneaky(): ClassName {
        return ClassName(if (this) "" else "sneaky")
    }
}


