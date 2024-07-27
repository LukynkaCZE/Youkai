package cz.lukynka.resourcepack

import cz.lukynka.compilers.BasePackCompiler
import cz.lukynka.objects.*

class YoukaiPack(var name: String) {

    var compiledPackFileName = name
    var packFormat = PackMcMeta(PackFormat(PackVersion.MC_1_21, "Youkai Compiled Resourcepack"))

    var custom2dItems: MutableList<Custom2dItem> = mutableListOf()
    var custom3dModels: MutableList<Custom3dModel> = mutableListOf()

    fun compile(path: String) {
        val compiler = BasePackCompiler(this, path)
        compiler.compile()
    }
}