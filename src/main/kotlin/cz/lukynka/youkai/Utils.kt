package cz.lukynka.cz.lukynka.youkai

fun <K, V> Map<K, V>.shuffled(): Map<K, V> {
    return this.toList().shuffled().associate { it }
}