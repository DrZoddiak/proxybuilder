
import api.card.ArtCardAPI.deleteArtCards
import api.card.ArtCardAPI.getArtCardList
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
import csstype.ClassName
import csstype.px
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mui.material.*
import mui.system.sx
import react.VFC
import react.create
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.hr
import react.dom.html.ReactHTML.p
import react.useEffectOnce
import react.useState

val scope = MainScope()

val App = VFC {
    val (deckList, setDeckList) = useState(emptyList<FinalizedCard>())
    val (artList, setArtList) = useState(emptyList<ArtCard>())
    val (modalIsOpen, setIsOpen) = useState(false)
    var isLoading by useState(true)
    useEffectOnce {
        scope.launch {
            setDeckList(getFinalizedCardList())
            setArtList(getArtCardList())
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
                    setArtList(getArtCardList())
                    setDeckList(getFinalizedCardList())
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
        className = ClassName(if (!isLoading) " sneaky" else "")
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
        children = DeckListComponent.create {
            this.deckList = deckList
            this.setArtList = setArtList
            this.setDeckList = setDeckList
            this.setDialogIsOpen = setIsOpen
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
    //Art card Modal
    ArtDialogComponent {
        this.modalIsOpen = modalIsOpen
        this.setIsOpen = setIsOpen
        this.artList = artList
        this.setArtList = setArtList
        this.setDeckList = setDeckList
        this.setIsOpen = setIsOpen
    }
}

