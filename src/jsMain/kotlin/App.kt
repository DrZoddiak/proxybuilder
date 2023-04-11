
import api.Api.sneaky
import api.card.FinalizedCardAPI.addFinalizedCard
import api.card.FinalizedCardAPI.finalizedCards
import api.card.FinalizedCardAPI.getFinalizedCardList
import components.*
import components.dialog.ArtDialogComponent
import csstype.Auto
import csstype.px
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mui.material.CircularProgress
import mui.material.CircularProgressColor
import mui.material.ImageList
import mui.system.sx
import react.*
import react.dom.html.ReactHTML.hr

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
    CardButtons {
        this.deckList = deckList
        this.setDeckList = setDeckList
        this.setArtList = setArtList
    }
    CardCounter {
        this.cardCount = deckList.size
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

