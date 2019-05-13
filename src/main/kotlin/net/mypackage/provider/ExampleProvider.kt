package net.mypackage.provider

import net.bjoernpetersen.musicbot.api.player.Song
import net.bjoernpetersen.musicbot.api.plugin.IdBase
import net.bjoernpetersen.musicbot.spi.plugin.Provider

@IdBase("ExampleService")
interface ExampleProvider : Provider {

    /*
     * You could expose an expensive-to-create API instance for dependent plugins.
     */
    // val someApi: Any

    /**
     * If you don't want to expose a complete API object, a method like this is also nice.
     */
    suspend fun getPlaylist(id: String): List<Song>
}
