import api.Api.addFinalizedCard
import api.Api.cardLookup
import api.Api.deleteArtCards
import api.Api.deleteFinalizedCard
import api.Api.deleteFinalizedCards
import api.Api.getArtCardList
import api.Api.getFinalizedCardList
import api.Api.reloadArtCards
import api.Api.scryfallApi
import api.Api.zipFiles
import components.InputComponent
import components.modalComponent
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

val scope = MainScope()

val App = FC<Props> {
    var deckList by useState(emptyList<FinalizedCard>())
    var artList by useState(emptyList<ArtCard>())
    val (modalIsOpen, setIsOpen) = useState(false)
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
    div {
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
                    alt = deck.name
                    onClick = {
                        scope.launch {
                            setIsOpen(true)
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
    InputComponent {
        onSubmit = { input ->
            scope.launch {
                cardLookup(input)
                    .map {
                        val cat =
                            "https://excitedcats.com/wp-content/uploads/2020/06/brown-tabby_shutterstock_-gillmar-scaled.jpg"
                        FinalizedCard(
                            it.id,
                            it.name,
                            it.imageUris?.normal ?: cat,
                            scryfallApi.artLookup(it.printsSearchUri).totalCards
                        )
                    }
                    .forEach {
                        addFinalizedCard(it)
                    }
                deckList = getFinalizedCardList()
            }
        }
    }
    modalComponent {
        isOpen = modalIsOpen
        content = VFC {
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
                                    setIsOpen(false)
                                }
                            }
                        }
                    }
                }
            }
        }
        onRequestClose = {
            setIsOpen(false)
        }
        onClose = {
            scope.launch {
                deleteArtCards()
                artList = getArtCardList()
                deckList = getFinalizedCardList()
            }
        }
    }
    a {
        href = "/"
        +"Back to the main page"
    }
}

