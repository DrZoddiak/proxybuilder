import api.Api.sneaky
import api.card.ArtCardAPI.deleteArtCards
import api.card.FinalizedCardAPI.addFinalizedCard
import api.card.FinalizedCardAPI.deleteFinalizedCards
import api.card.FinalizedCardAPI.finalizedCards
import api.card.FinalizedCardAPI.getFinalizedCardList
import api.library.DownloadAPI.zipFiles
import components.ArtDialogComponent
import components.DeckListComponent
import components.InputComponent
import components.TitleComponent
import csstype.Auto
import csstype.px
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mui.material.*
import mui.system.sx
import react.*
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.hr
import react.dom.html.ReactHTML.p

val scope = MainScope()

val App = VFC {
    val (deckList, setDeckList) = useState(emptyList<FinalizedCard>())
    val (artList, setArtList) = useState(emptyList<ArtCard>())
    val (modalIsOpen, setIsOpen) = useState(false)
    var isLoading by useState(true)
    useEffectOnce {
        scope.launch {
            setDeckList(getFinalizedCardList())
            isLoading = false
        }
    }
    TitleComponent()
    hr()
    //Buttons & Card Counter
    div {
        Button {
            +"Delete"
            variant = ButtonVariant.contained
            color = ButtonColor.error
            onClick = {
                scope.launch {
                    deleteFinalizedCards()
                    deleteArtCards()
                    setArtList(emptyList())
                    setDeckList(emptyList())
                }
            }
        }
        Button {
            +"Download"
            variant = ButtonVariant.contained
            color = ButtonColor.primary
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
        className = isLoading.sneaky()
    }

    //Selected Cards
    ImageList {
        sx {
            width = 1334.px
            height = 803.px
            padding = 30.px
            gap = 10.px
            margin = Auto.auto
        }
        rowHeight = 261
        cols = 7
        children = Fragment.create {
            deckList.map { card ->
                DeckListComponent {
                    this.finalizedCard = card
                    this.setArtList = setArtList
                    this.setDeckList = setDeckList
                    this.setDialogIsOpen = setIsOpen
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
                finalizedCards(input).forEach {
                    addFinalizedCard(it)
                }
                setDeckList(getFinalizedCardList())
            }.invokeOnCompletion {
                isLoading = false
            }
        }
    }
    ArtDialogComponent {
        this.artList = artList
        this.modalIsOpen = modalIsOpen
        this.setIsOpen = setIsOpen
        this.setDeckList = setDeckList
    }
}

