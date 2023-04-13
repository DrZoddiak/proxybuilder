import ServerSettings.mongoUri
import com.mongodb.ConnectionString
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

object DatabaseConnection {
    private const val dbName: String = "deck"

    private val connectionString = mongoUri?.let {
        ConnectionString("$it?retryWrites=false")
    }

    private val client =
        if (connectionString != null) KMongo.createClient(connectionString).coroutine else KMongo.createClient().coroutine

    private val database by lazy {
        client.getDatabase(connectionString?.database ?: dbName)
    }

    val collection = database.getCollection<FinalizedCard>()
}