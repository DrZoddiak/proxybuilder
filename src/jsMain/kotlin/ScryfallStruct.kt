
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class BaseResponse(
    @SerialName("total_cards")
    val totalCards : Int,
    val data: List<CardData>
)

@Serializable
internal data class ImageTypes(
    val png: String,
    @SerialName("border_crop")
    val borderCrop : String,
    @SerialName("art_crop")
    val artCrop : String,
    val large : String,
    val normal : String,
    val small : String
) {
    override fun toString(): String = png
}

@Serializable
internal data class CardData(
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