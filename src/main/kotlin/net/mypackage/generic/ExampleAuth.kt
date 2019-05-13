package net.mypackage.generic

import net.bjoernpetersen.musicbot.api.plugin.Base
import net.bjoernpetersen.musicbot.spi.plugin.GenericPlugin

/**
 * A plugin which provides authentication for some service.
 */
@Base
interface ExampleAuth : GenericPlugin {

    suspend fun getToken(): String
}
