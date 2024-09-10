package cz.lukynka.youkai.resourcepack

import cz.lukynka.youkai.compilers.BasePackCompiler
import cz.lukynka.youkai.objects.Custom2dItem
import cz.lukynka.youkai.objects.Custom3dModel
import java.io.File

class YoukaiPack(var name: String) {

    var compiledPackFileName = name
    var packFormat = PackMcMeta(PackFormat(PackVersion.MC_1_21, "Youkai Compiled Resourcepack"))

    var custom2dItems: MutableList<Custom2dItem> = mutableListOf()
    var custom3dModels: MutableList<Custom3dModel> = mutableListOf()
    var basePack: File? = null

    fun compile(path: String) {
        val compiler = BasePackCompiler(this, path)
        compiler.compile()
    }
}