package cz.lukynka

import LightweightWebServer
import responses.Response

class WebServer(port: Int) {

    private fun isAuthenticated(response: Response): Boolean {
        if(Config.AUTH_ENABLED) return true
        return response.requestHeaders["Authentication"] == Environment.TOKEN
    }

    init {
        val server = LightweightWebServer(port)
        server.get("/", ::isAuthenticated) {
            it.respond("Welcomne!")
        }
    }

}