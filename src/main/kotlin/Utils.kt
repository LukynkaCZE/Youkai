package cz.lukynka

fun <K, V> Map<K, V>.shuffled(): Map<K, V> {
    return this.toList().shuffled().associate { it }
}