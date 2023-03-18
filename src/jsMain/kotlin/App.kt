
import Api.addArtCard
import Api.addFinalizedCard
import Api.cardLookup
import Api.deleteArtCards
import Api.deleteFinalizedCard
import Api.deleteFinalizedCards
import Api.getArtCardList
import Api.getFinalizedCardList
import Api.zipFiles
import components.InputComponent
import csstype.ClassName
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import react.*
import react.dom.html.ReactHTML.a
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.hr
import react.dom.html.ReactHTML.i
import react.dom.html.ReactHTML.img
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.ul

private val scope = MainScope()

val App = FC<Props> {
    var deckList by useState(emptyList<FinalizedCard>())
    var artList by useState(emptyList<ArtCard>())
    useEffectOnce {
        scope.launch {
            deckList = getFinalizedCardList()
            artList = getArtCardList()
        }
    }
    h1 {
        +"MTG Proxy Builder"
    }
    p { i { +"Powered by Ktor & Scryfall!" } }
    hr()
    button {
        +"Delete"
        onClick = {
            scope.launch {
                deleteFinalizedCards()
                deleteArtCards()
                artList = getArtCardList()
                deckList = getFinalizedCardList()
            }
        }
    }
    button {
        +"Download"
        onClick = {
            scope.launch {
                zipFiles(deckList)
            }
        }
    }
    p {
        +"cards: ${deckList.size}"
    }
    //Selected cards
    ul {
        onContextMenu = {
            it.preventDefault()
        }
        deckList.forEach { deck ->
            key = deck.toString()
            img {
                className = ClassName("cardimg")
                src = deck.imageUri
                onClick = {
                    scope.launch {
                        deleteArtCards()

                        cardLookup(deck.name).map { list ->
                            list.map {
                                ArtCard(it.id, it.name, it.imageUris!!.png)
                            }
                        }.forEach {
                            addArtCard(it)
                        }

                        artList = getArtCardList()
                    }
                }
                onAuxClick = {
                    scope.launch {
                        deleteFinalizedCard(deck)
                        deckList = getFinalizedCardList()
                    }
                }
            }
        }
    }
    hr()
    //Art Selection
    ul {
        artList.forEach { artCard ->
            key = artCard.toString()
            img {
                className = ClassName("cardimg")
                src = artCard.imageUri
                onClick = {
                    scope.launch {
                        val oldCard = getFinalizedCardList().find { it.name == artCard.name }
                        oldCard?.let { card -> deleteFinalizedCard(card) }
                        addFinalizedCard(artCard.asFinalizedCard())
                        deleteArtCards()
                        artList = getArtCardList()
                        deckList = getFinalizedCardList()
                    }
                }
            }
        }

    }
    hr()
    //Input Form
    InputComponent {
        onSubmit = { input ->
            scope.launch {
                cardLookup(input)
                    .map { it[0] }
                    .map { FinalizedCard(it.id, it.name, it.imageUris!!.png) }
                    .forEach {
                        addFinalizedCard(it)
                    }
                deckList = getFinalizedCardList()
            }
        }
    }
    a {
        href = "/"
        +"Back to the main page"
    }
}

