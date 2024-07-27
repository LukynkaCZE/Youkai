package cz.lukynka

import cz.lukynka.objects.Custom2dItem
import cz.lukynka.prettylog.LoggerSettings
import cz.lukynka.prettylog.LoggerStyle
import cz.lukynka.resourcepack.YoukaiPack
import java.io.File

object Youkai {
    lateinit var webServer: WebServer
}

fun main() {
    LoggerSettings.loggerStyle = LoggerStyle.BRACKET_PREFIX_WHITE_TEXT
    Youkai.webServer = WebServer(Config.PORT)

    val pack = YoukaiPack("test")
    pack.compiledPackFileName = "§c§lTESTING TESTING"
    pack.packFormat.pack.description = "§e:3"
    val dir = File("./test/2d/")
    dir.listFiles()?.forEach {
        pack.custom2dItems.add(Custom2dItem(it))
    }

    pack.compile("C:/Users/LukynkaCZE/AppData/Roaming/com.modrinth.theseus/profiles/1.21/resourcepacks/${pack.name}")
}
