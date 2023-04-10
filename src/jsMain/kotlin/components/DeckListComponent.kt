package components

import ArtCard
import FinalizedCard
import api.card.ArtCardAPI.loadArtCards
import api.card.FinalizedCardAPI
import csstype.ClassName
import kotlinx.coroutines.launch
import mui.material.ImageListItem
import mui.material.ImageListItemBar
import react.*
import react.dom.html.ImgLoading
import react.dom.html.ReactHTML.img
import scope

external interface DeckListProps : Props {
    var finalizedCard: FinalizedCard
    var setArtList: StateSetter<List<ArtCard>>
    var setDeckList: StateSetter<List<FinalizedCard>>
    var setDialogIsOpen: StateSetter<Boolean>
}

val DeckListComponent = FC<DeckListProps> { props ->
    var artCards by useState(emptyList<ArtCard>())
    useEffectOnce {
        scope.launch {
            artCards = loadArtCards(props.finalizedCard)
        }
    }

    ImageListItem {
        key = props.finalizedCard.toString()
        img {
            className = ClassName("cardimg")
            src = "${props.finalizedCard.imageUri}?w=182&h=261&auto=format"
            srcSet = "${props.finalizedCard.imageUri}?w=182&h=261&auto=format&dpr=2 2x"
            alt = props.finalizedCard.name
            loading = ImgLoading.lazy
            onClick = {
                props.setDialogIsOpen(true)
                props.setArtList(artCards)
            }
            onAuxClick = {
                scope.launch {
                    FinalizedCardAPI.deleteFinalizedCard(props.finalizedCard)
                    props.setDeckList(FinalizedCardAPI.getFinalizedCardList())
                }
            }
            onContextMenu = {
                it.preventDefault()
            }
        }
        ImageListItemBar {
            title = Fragment.create {
                +props.finalizedCard.name
            }
        }
    }
}
