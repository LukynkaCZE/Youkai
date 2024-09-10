package cz.lukynka.youkai

import cz.lukynka.lkws.LightweightWebServer
import cz.lukynka.lkws.responses.Response
import cz.lukynka.youkai.config.ConfigManager

class WebServer(port: Int) {

    private fun isAuthenticated(response: Response): Boolean {
        if(!ConfigManager.currentConfig.general.auth) return true
        return response.requestHeaders["Authentication"] == ConfigManager.currentConfig.general.youkaiToken
    }

    init {
        val server = LightweightWebServer(port)
        server.get("/", ::isAuthenticated) {
            it.respond("Welcomne!")
        }
    }

}