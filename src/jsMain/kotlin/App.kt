import api.InputFormatter
import components.*
import csstype.Auto
import csstype.px
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mui.material.ImageList
import mui.system.sx
import react.*
import react.dom.html.ReactHTML.hr

val scope = MainScope()

val App = VFC {
    val (searchList, setSearchList) = useState(emptyList<String>())

    TitleComponent()
    hr()
    CardButtons {
        this.searchSetter = setSearchList
    }
    CardCounter {
        this.cardCount = searchList.size
    }
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
            searchList.map { card ->
                DeckListComponent {
                    key = card
                    this.cardName = card
                    this.searchList = searchList
                    this.setSearchList = setSearchList
                }
            }
        }
        onContextMenu = {
            it.preventDefault()
        }
    }
    hr()
    InputComponent {
        onSubmit = { input ->
            scope.launch {
                setSearchList(InputFormatter.formatSearch(input))
            }
        }
    }
}

