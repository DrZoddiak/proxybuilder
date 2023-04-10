package components

import ArtCard
import FinalizedCard
import api.card.ArtCardAPI
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
    var deckList: List<FinalizedCard>
    var setArtList: StateSetter<List<ArtCard>>
    var setDeckList: StateSetter<List<FinalizedCard>>
    var setDialogIsOpen: StateSetter<Boolean>
}

val DeckListComponent = FC<DeckListProps> { props ->
    props.deckList.map { card ->
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
                        props.setDialogIsOpen(true)
                        ArtCardAPI.reloadArtCards(card.name)
                        props.setArtList(ArtCardAPI.getArtCardList())
                    }
                }
                onAuxClick = {
                    scope.launch {
                        FinalizedCardAPI.deleteFinalizedCard(card)
                        props.setDeckList(FinalizedCardAPI.getFinalizedCardList())
                    }
                }
                onContextMenu = {
                    it.preventDefault()
                }
            }
            ImageListItemBar {
                title = Fragment.create {
                    +card.name
                }
            }
        }
    }
}
