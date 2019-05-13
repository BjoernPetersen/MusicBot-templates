package net.mypackage.provider

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import net.bjoernpetersen.musicbot.api.config.Config
import net.bjoernpetersen.musicbot.api.loader.FileResource
import net.bjoernpetersen.musicbot.api.player.Song
import net.bjoernpetersen.musicbot.spi.loader.Resource
import net.bjoernpetersen.musicbot.spi.plugin.NoSuchSongException
import net.bjoernpetersen.musicbot.spi.plugin.Playback
import net.bjoernpetersen.musicbot.spi.plugin.management.InitStateWriter
import net.bjoernpetersen.musicbot.spi.plugin.predefined.Mp3PlaybackFactory
import net.mypackage.generic.ExampleAuth
import java.io.File
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ExampleProviderImpl : ExampleProvider, CoroutineScope {

    override val name = "ExampleServer default"
    override val description = "This is a plugin description."
    // The name of the service this is getting its songs from
    override val subject = "Example service"

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    @Inject
    private lateinit var auth: ExampleAuth
    @Inject
    private lateinit var playbackFactory: Mp3PlaybackFactory

    private lateinit var api: ExampleApi

    override fun createConfigEntries(config: Config): List<Config.Entry<*>> = emptyList()

    override fun createSecretEntries(secrets: Config): List<Config.Entry<*>> = emptyList()

    override fun createStateEntries(state: Config) {}

    override suspend fun initialize(initStateWriter: InitStateWriter) {
        withContext(coroutineContext) {
            initStateWriter.state("Retrieving token")
            val token = auth.getToken()
            initStateWriter.state("Initializing API")
            api = ExampleApi.create(token)
        }
    }

    private fun ApiSong.toSong(): Song {
        return Song(
            id = id,
            provider = this@ExampleProviderImpl,
            title = title,
            description = description,
            duration = duration
        )
    }

    override suspend fun search(query: String, offset: Int): List<Song> {
        return withContext(coroutineContext) {
            val ids: List<ApiSong> = api.search(query)
            ids.map { it.toSong() }
        }
    }

    override suspend fun lookup(id: String): Song {
        return withContext(coroutineContext) {
            api.getSong(id)?.toSong() ?: throw NoSuchSongException(id)
        }
    }

    private fun pathForSong(id: String): File {
        return File("songs", id)
    }

    override suspend fun loadSong(song: Song): Resource {
        return withContext(coroutineContext) {
            val file = pathForSong(song.id)
            api.download(song.id, file)
            FileResource(file)
        }
    }

    override suspend fun supplyPlayback(song: Song, resource: Resource): Playback {
        // You may differentiate between song types here.
        return playbackFactory.createPlayback(pathForSong(song.id))
    }

    override suspend fun getPlaylist(id: String): List<Song> {
        return withContext(coroutineContext) {
            api.getPlaylist(id).map { it.toSong() }
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
        // Release all resources
        api.close()
    }
}

/**
 * Some song/track object returned by our fictional API.
 */
private data class ApiSong(
    val id: String,
    val title: String,
    val description: String,
    val duration: Int
)

@Suppress("unused", "UNUSED_PARAMETER")
private class ExampleApi private constructor(private val token: String) {
    fun getPlaylist(id: String): List<ApiSong> = TODO("Not a real API")
    fun search(query: String): List<ApiSong> = TODO("Not a real API")
    fun getSong(id: String): ApiSong? = TODO("Not a real API")
    suspend fun download(songId: String, file: File) {
        // Simulate download
        delay(200)
    }

    suspend fun close() {
        // Suspend expensive cleanup
        delay(2000)
    }

    companion object {
        suspend fun create(token: String): ExampleApi {
            return ExampleApi(token).also {
                // Simulate expensive setup
                delay(2000)
            }
        }
    }
}
