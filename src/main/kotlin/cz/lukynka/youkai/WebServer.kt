package cz.lukynka.youkai

import cz.lukynka.cz.lukynka.youkai.Environment
import cz.lukynka.lkws.LightweightWebServer
import cz.lukynka.lkws.responses.Response

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