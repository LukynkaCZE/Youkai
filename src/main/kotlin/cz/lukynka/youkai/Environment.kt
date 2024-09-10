package cz.lukynka.cz.lukynka.youkai

import io.github.cdimascio.dotenv.Dotenv



object Environment {

    private var dotenv: Dotenv = Dotenv.load()

    val TOKEN: String = dotenv.get("TOKEN")!!
}