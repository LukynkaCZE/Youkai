package cz.lukynka.youkai

import cz.lukynka.youkai.objects.Custom2dItem
import cz.lukynka.youkai.objects.Custom3dModel
import cz.lukynka.prettylog.LoggerSettings
import cz.lukynka.prettylog.LoggerStyle
import cz.lukynka.youkai.config.ConfigManager
import cz.lukynka.youkai.resourcepack.YoukaiPack
import java.io.File

fun main() {
    LoggerSettings.loggerStyle = LoggerStyle.BRACKET_PREFIX_WHITE_TEXT
    ConfigManager.load()

    val pack = YoukaiPack("ember-seeker-pack")
    pack.compiledPackFileName = "§b§lEmber Seeker"
    pack.packFormat.pack.description = "§7Resourcepack for the §bEmber Seeker§7 server. §e§oAuto-compiled"

    val dir2d = File("./test/2d/")
    dir2d.listFiles()?.forEach {
        pack.custom2dItems.add(Custom2dItem(it))
    }

    val dir3d = File("./test/3d/")
    dir3d.listFiles()?.filter { it.extension == "json" }?.forEach {
        pack.custom3dModels.add(Custom3dModel(File(it.path), File(it.path.replace(it.extension, "png"))))
    }
    pack.basePack = File("C:/Users/LukynkaCZE/AppData/Roaming/ModrinthApp/profiles/1.21/resourcepacks/EMBER_SEEKER_BASE")

    pack.compile("C:/Users/LukynkaCZE/AppData/Roaming/ModrinthApp/profiles/1.21/resourcepacks/")
}