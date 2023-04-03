import api.Api
import api.Api.addFinalizedCard
import api.Api.cardLookup
import api.Api.deleteArtCards
import api.Api.deleteFinalizedCard
import api.Api.deleteFinalizedCards
import api.Api.getArtCardList
import api.Api.getFinalizedCardList
import api.Api.scryfallApi
import api.Api.zipFiles
import components.ImageComponent
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
    var isLoading by useState(true)
    useEffectOnce {
        scope.launch {
            deckList = getFinalizedCardList()
            artList = getArtCardList()
            isLoading = false
        }
    }
    h1 {
        +"MTG Proxy Builder"
    }
    p { i { +"Powered by Ktor & Scryfall!" } }
    hr()
    //Buttons & Card Counter
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
    //Loader
    div {
        className = ClassName("loadingio-spinner-magnify-x8bpipc3dcs" + if (!isLoading) " sneaky" else "")
        div {
            className = ClassName("ldio-wxp2nv6ydn")

            div {
                div {
                    div {

                    }
                    div {

                    }
                }
            }
        }
    }
    //Selected Cards
    ul {
        deckList.map { deck ->
            key = deck.toString()
            div {
                //TODO Replace with ProgressiveImage
                ImageComponent {
                    src = deck.imageUri
                    placeholder = deck.altImageUri
                    alt = deck.name
                    totalCards = deck.totalCards
                    onClick = {
                        scope.launch {
                            setIsOpen(true)
                            Api.reloadArtCards(deck.name)
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
    }
    hr()
    //Input Box
    InputComponent {
        onSubmit = { input ->
            scope.launch {
                isLoading = true
                cardLookup(input)
                    .map {
                        val cat =
                            "https://excitedcats.com/wp-content/uploads/2020/06/brown-tabby_shutterstock_-gillmar-scaled.jpg"
                        val normal = it.imageUris?.normal ?: cat
                        val minified = it.imageUris?.small ?: cat
                        FinalizedCard(
                            it.id,
                            it.name,
                            normal,
                            minified,
                            scryfallApi.artLookup(it.printsSearchUri).totalCards
                        )
                    }
                    .forEach {
                        addFinalizedCard(it)
                    }
                deckList = getFinalizedCardList()
            }.invokeOnCompletion {
                isLoading = false
            }
        }
    }
    //Art card Modal
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
    //Return
    a {
        href = "/"
        +"Back to the main page"
    }
}

