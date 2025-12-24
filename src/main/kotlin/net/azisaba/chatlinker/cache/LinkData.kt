package net.azisaba.chatlinker.cache

data class LinkData(
    val fromGuildId: String,
    val fromChannelId: String,
    val toGuildId: String,
    val toChannelId: String,
    val toChannelWebhook: String,
)
