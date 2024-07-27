package cz.lukynka

import cz.lukynka.objects.Custom2dItem
import cz.lukynka.objects.Custom3dModel
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
    pack.compiledPackFileName = "§6Youkai Compiled"
    pack.packFormat.pack.description = "§eso silly !!"
    val dir2d = File("./test/2d/")
    dir2d.listFiles()?.forEach {
        pack.custom2dItems.add(Custom2dItem(it))
    }
    val dir3d = File("./test/3d/")
    dir3d.listFiles()?.filter { it.extension == "json" }?.forEach {
        pack.custom3dModels.add(Custom3dModel(File(it.path), File(it.path.replace(it.extension, "png"))))
    }


    pack.compile("C:/Users/LukynkaCZE/AppData/Roaming/com.modrinth.theseus/profiles/1.21/resourcepacks/${pack.name}")
}
