package components

import ArtCard
import FinalizedCard
import api.Api
import api.card.ArtCardAPI.loadCards
import api.card.FinalizedCardAPI
import api.card.FinalizedCardAPI.addCard
import components.dialog.ArtDialogComponent
import csstype.ClassName
import csstype.px
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import libraries.ProgressiveImage
import mui.material.ImageListItem
import mui.material.ImageListItemBar
import mui.material.Skeleton
import mui.material.SkeletonVariant
import react.*
import react.dom.html.ImgLoading
import react.dom.html.ReactHTML.img
import scope

external interface DeckListProps : Props {
    var cardName: String
    var searchList: List<String>
    var setSearchList: StateSetter<List<String>>
}

val DeckListComponent = FC<DeckListProps> { props ->
    var artCards by useState(emptyList<ArtCard>())
    val (card, setCard) = useState<FinalizedCard>()
    val (isOpen, setIsOpen) = useState(false)

    useEffectOnce {
        scope.launch {
            val search = async {
                val cardData = Api.scryfallApi.namedCardLookup(props.cardName)
                    ?: error("Cannot find card by name - ${props.cardName}")
                cardData.imageUris?.normal?.let {
                    val finalCard = FinalizedCard(cardData.id, cardData.name, it)
                    artCards = loadCards(finalCard)
                    addCard(finalCard)
                    finalCard
                }
            }
            setCard(search.await())
        }
    }
    val skeleton = Skeleton.create {
        variant = SkeletonVariant.rectangular
        width = 182.px
        height = 261.px
    }
    if (card == null) skeleton.asElementOrNull()
    else {
        ImageListItem {
            key = card.name

            ProgressiveImage {
                src = "${card.imageUri}?w=182&h=261&auto=format"
                placeholder = ""
                children = { image, load, _ ->
                    if (!load) {
                        img.create {
                            className = ClassName("cardimg")
                            src = "$image?w=182&h=261&auto=format"
                            alt = card.name
                            loading = ImgLoading.eager
                            onClick = {
                                setIsOpen(true)
                            }
                            onAuxClick = {
                                scope.launch {
                                    FinalizedCardAPI.removeCard(card)
                                    val search = props.searchList.filterNot { it == card.name }
                                    props.setSearchList(search)
                                }
                            }
                        }
                    } else {
                        skeleton
                    }
                }
            }
            ImageListItemBar {
                title = Fragment.create {
                    +props.cardName
                }
            }
        }
        ArtDialogComponent {
            this.setCard = setCard
            this.artList = artCards
            this.modalIsOpen = isOpen
            this.setIsOpen = setIsOpen
        }
    }
}
