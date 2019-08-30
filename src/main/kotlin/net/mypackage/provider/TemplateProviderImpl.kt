package net.mypackage.provider

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.withContext
import net.bjoernpetersen.musicbot.api.config.Config
import net.bjoernpetersen.musicbot.api.player.Song
import net.bjoernpetersen.musicbot.api.plugin.PluginScope
import net.bjoernpetersen.musicbot.spi.loader.Resource
import net.bjoernpetersen.musicbot.spi.plugin.Playback
import net.bjoernpetersen.musicbot.spi.plugin.PlaybackFactory
import net.bjoernpetersen.musicbot.spi.plugin.management.InitStateWriter
import javax.inject.Inject

class TemplateProviderImpl : TemplateProvider, CoroutineScope by PluginScope() {
    override val name: String = TODO("Choose a name")
    override val description: String = TODO("Write a description")
    override val subject: String
        get() = TODO("Choose a subject, may be dynamic")

    // TODO choose an actual PlaybackFactory subtype
    @Inject
    private lateinit var playback: PlaybackFactory

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

    override suspend fun search(query: String, offset: Int): List<Song> {
        withContext(coroutineContext) {
            TODO("not implemented")
        }
    }

    override suspend fun lookup(id: String): Song {
        withContext(coroutineContext) {
            TODO("not implemented")
        }
    }

    override suspend fun loadSong(song: Song): Resource {
        withContext(coroutineContext) {
            TODO("Download/load song, may not be necessary")
        }
    }

    override suspend fun supplyPlayback(song: Song, resource: Resource): Playback {
        withContext(coroutineContext) {
            TODO("Delegate to playback")
        }
    }

    override suspend fun close() {
        run {
            // Cancel the plugin coroutine scope
            cancel()
        }

        // TODO: Release all resources
    }
}
