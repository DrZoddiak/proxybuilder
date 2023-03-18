
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse(
    @SerialName("total_cards")
    val totalCards : Int,
    val data: List<CardData>
)

@Serializable
data class ImageTypes(
    val png: String,
) {
    override fun toString(): String = png
}

@Serializable
data class CardData(
    val id: String,
    val name: String,
    @SerialName("image_uris")
    val imageUris: ImageTypes? = null,
    @SerialName("prints_search_uri")
    val printsSearchUri: String,
) {
    override fun toString(): String {
        return "$name - $imageUris"
    }
}