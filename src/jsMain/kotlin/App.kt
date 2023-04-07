import api.Api
import api.Api.addFinalizedCard
import api.Api.cardLookup
import api.Api.deleteArtCards
import api.Api.deleteFinalizedCard
import api.Api.deleteFinalizedCards
import api.Api.getArtCardList
import api.Api.getFinalizedCardList
import api.Api.replaceFinalizedCard
import api.Api.scryfallApi
import api.Api.zipFiles
import components.InputComponent
import csstype.Auto
import csstype.ClassName
import csstype.px
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mui.material.*
import mui.system.sx
import react.*
import react.dom.html.ImgLoading
import react.dom.html.ReactHTML.a
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.hr
import react.dom.html.ReactHTML.i
import react.dom.html.ReactHTML.img
import react.dom.html.ReactHTML.p

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
    CircularProgress {
        color = CircularProgressColor.secondary
        className = ClassName(if (!isLoading) " sneaky" else "")
    }
    //Selected Cards
    ImageList {
        sx {
            width = 1334.px
            height = 803.px
            gap = 10.px
            margin = Auto.auto
        }
        rowHeight = 261
        cols = 7
        children = Fragment.create {
            deckList.map { card ->
                ImageListItem {
                    key = card.toString()
                    img {
                        className = ClassName("cardimg")
                        src = "${card.imageUri}?w=182&h=261&auto=format"
                        srcSet = "${card.imageUri}?w=182&h=261&auto=format&dpr=2 2x"
                        alt = card.name
                        loading = ImgLoading.lazy
                        onClick = {
                            scope.launch {
                                setIsOpen(true)
                                Api.reloadArtCards(card.name)
                                artList = getArtCardList()
                            }
                        }
                        onAuxClick = {
                            scope.launch {
                                deleteFinalizedCard(card)
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
                        FinalizedCard(
                            it.id,
                            it.name,
                            normal,
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
    Dialog {
        maxWidth = "md"
        open = modalIsOpen
        DialogTitle {
            +"Select an art card!"
        }
        onClose = { _, _ ->
            setIsOpen(false)
        }
        ImageList {
            sx {
                width = 546.px
                margin = 10.px
            }
            cols = 3
            rowHeight = 261
            children = Fragment.create {
                artList.map { card ->
                    ImageListItem {
                        key = card.imageUri
                        img {
                            className = ClassName("cardimg")
                            src = "${card.imageUri}?&auto=format"
                            srcSet = "${card.imageUri}?&auto=format&dpr=2 2x"
                            alt = card.name
                            loading = ImgLoading.lazy
                            onClick = {
                                scope.launch {
                                    val oldCard = getFinalizedCardList().find { it.name == card.name }
                                    oldCard?.let { old -> replaceFinalizedCard(old, card) }
                                    setIsOpen(false)
                                    deckList = getFinalizedCardList()
                                    deleteArtCards()
                                    artList = getArtCardList()

                                }
                            }
                        }
                    }
                }
            }
        }
    }
    //Return
    a {
        href = "/"
        +"Back to the main page"
    }
}

