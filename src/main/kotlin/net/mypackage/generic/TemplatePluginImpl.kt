package net.mypackage.generic

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import net.bjoernpetersen.musicbot.api.config.Config
import net.bjoernpetersen.musicbot.spi.plugin.management.InitStateWriter
import kotlin.coroutines.CoroutineContext

class TemplatePluginImpl : TemplatePlugin, CoroutineScope {
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    override val name: String = TODO("Choose a name")
    override val description = TODO("Write a description")

    override fun createConfigEntries(config: Config): List<Config.Entry<*>> {
        TODO("not implemented")
    }

    override fun createSecretEntries(secrets: Config): List<Config.Entry<*>> {
        TODO("not implemented")
    }

    override fun createStateEntries(state: Config) {
        TODO("not implemented")
    }

    override suspend fun initialize(initStateWriter: InitStateWriter) {
        withContext(coroutineContext) {
            // TODO initialize
        }
    }

    override suspend fun close() {
        // Don't accept new children
        job.complete()
        try {
            // wait for job to complete
            withTimeout(500) { job.join() }
        } catch (e: TimeoutCancellationException) {
            job.cancel()
        }

        // TODO: Release all resources
    }
}
