internal object ServerSettings {
    private const val defaultPort = 9090

    internal val mongoUri = "MONGODB_URI".getEnv()
    internal val port = "PORT".getEnv()?.toInt() ?: defaultPort

    private fun String.getEnv(): String? = System.getenv(this)
}