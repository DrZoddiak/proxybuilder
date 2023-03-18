
import Api.addFinalizedCard
import Api.cardLookup
import Api.deleteArtCards
import Api.deleteFinalizedCard
import Api.deleteFinalizedCards
import Api.getArtCardList
import Api.getFinalizedCardList
import Api.printsLookup
import Api.reloadArtCards
import Api.zipFiles
import components.InputComponent
import components.SliderComponent
import csstype.ClassName
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import react.*
import react.dom.html.ReactHTML.a
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
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
        deckList.forEach { deck ->
            key = deck.toString()
            div {
                className = ClassName("card")
                div {
                    className = ClassName(if (deck.totalCards > 1) "caption" else "captionOff")
                    i {
                        className = ClassName("fa-solid fa-square-plus")
                    }
                }
                img {
                    className = ClassName("cardimg")
                    src = deck.imageUri
                    onClick = {
                        scope.launch {
                            reloadArtCards(deck.name)
                            artList = getArtCardList()
                        }
                    }
                    onAuxClick = {
                        scope.launch {
                            deleteFinalizedCard(deck)
                            deckList = getFinalizedCardList()
                        }
                    }
                    onContextMenu = {
                        it.preventDefault()
                    }
                }
            }
        }
    }
    hr()
    //Art Selection
    SliderComponent {
        handleToggle = {
            scope.launch {
                Api.uniqueSetting = it
                if (artList.isNotEmpty()) {
                    reloadArtCards(artList[0].name)
                    artList = getArtCardList()
                }
            }
        }
    }
    ul {
        artList.forEach { artCard ->
            key = artCard.toString()
            div {
                className = ClassName("card")
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

    }
    hr()
//Input Form
    InputComponent {
        onSubmit = { input ->
            scope.launch {
                cardLookup(input)
                    .map { it.data }
                    .map { it[0] }
                    .map {
                        val cat = "https://excitedcats.com/wp-content/uploads/2020/06/brown-tabby_shutterstock_-gillmar-scaled.jpg"
                        FinalizedCard(it.id, it.name, it.imageUris?.png ?: cat, printsLookup(it.printsSearchUri))
                    }
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

