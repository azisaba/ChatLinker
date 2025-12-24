package net.azisaba.chatlinker.extension

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel

fun String.urlToTextChannel(jda: JDA): TextChannel? {
    val splitUrl = removePrefix("https://discord.com/channels/").split("/")
    if (splitUrl.size < 2) {
        return null
    }
    return jda.getGuildById(splitUrl[0])?.getChannelById(TextChannel::class.java, splitUrl[1])
}
