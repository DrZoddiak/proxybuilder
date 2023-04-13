object ServerSettings {
    private const val defaultPort = 9090

    val mongoUri = "MONGODB_URI".getEnv()
    val port = "PORT".getEnv()?.toInt() ?: defaultPort

    private fun String.getEnv(): String? = System.getenv(this)
}