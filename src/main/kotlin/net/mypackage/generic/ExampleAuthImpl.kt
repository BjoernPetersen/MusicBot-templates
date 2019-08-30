package net.mypackage.generic

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import net.bjoernpetersen.musicbot.api.config.ActionButton
import net.bjoernpetersen.musicbot.api.config.Config
import net.bjoernpetersen.musicbot.api.plugin.PluginScope
import net.bjoernpetersen.musicbot.spi.plugin.InitializationException
import net.bjoernpetersen.musicbot.spi.plugin.management.InitStateWriter

class ExampleAuthImpl : ExampleAuth, CoroutineScope by PluginScope() {
    override val name = "ExampleAuth"
    override val description = "Provides an authentication token for ExampleService"

    private lateinit var token: Config.StringEntry

    override fun createConfigEntries(config: Config): List<Config.Entry<*>> = emptyList()
    override fun createSecretEntries(secrets: Config): List<Config.Entry<*>> {
        token = secrets.StringEntry(
            key = "token",
            description = "",
            configChecker = { null },
            uiNode = ActionButton("Refresh", { it }, {
                try {
                    refreshToken()
                    true
                } catch (e: Exception) {
                    false
                }
            }),
            default = null
        )
        return listOf(token)
    }

    /**
     * Retrieves a new token, updates [token].
     *
     * @return the new token
     */
    private suspend fun refreshToken(): String {
        // do some expensive token refresh
        delay(1000)
        return "someNewToken".also {
            token.set(it)
        }
    }

    override suspend fun initialize(initStateWriter: InitStateWriter) {
        withContext(coroutineContext) {
            initStateWriter.state("Retrieving token")
            try {
                refreshToken()
            } catch (e: Exception) {
                throw InitializationException(e)
            }
        }
    }

    override suspend fun getToken(): String {
        return withContext(coroutineContext) {
            token.get() ?: refreshToken()
        }
    }

    override fun createStateEntries(state: Config) {}

    override suspend fun close() {
        run {
            // Cancel the plugin coroutine scope
            cancel()
        }

        // do whatever blocking action here to close any resources
    }
}
