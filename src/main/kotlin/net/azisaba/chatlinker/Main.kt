package net.azisaba.net.azisaba.chatlinker

import net.azisaba.chatlinker.bot.ChatLinkerBot

fun main() {
    // headless mode
    System.setProperty("java.awt.headless", "true")

    ChatLinkerBot().main()
}
