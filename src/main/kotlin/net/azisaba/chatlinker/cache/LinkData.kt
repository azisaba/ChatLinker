package net.azisaba.net.azisaba.chatlinker.cache

data class LinkData(
    val fromChannelId: String,
    val toChannelId: String,
    val toChannelWebhook: String,
)
