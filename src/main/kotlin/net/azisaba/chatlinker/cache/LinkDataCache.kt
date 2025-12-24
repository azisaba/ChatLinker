package net.azisaba.chatlinker.cache

import net.azisaba.chatlinker.database.CLDatabase

object LinkDataCache {
    private val linkMap: MutableMap<String, LinkData> = mutableMapOf()

    fun init() {
        CLDatabase.Links.getAll().forEach { data ->
            linkMap[data.channelFrom] =
                LinkData(
                    data.guildFrom,
                    data.channelFrom,
                    data.guildTo,
                    data.channelTo,
                    data.webhook,
                )
        }
    }

    fun get(from: String): LinkData? = linkMap[from]

    fun getAll(): List<LinkData> = linkMap.values.toList()

    fun add(
        fromGuild: String,
        from: String,
        toGuild: String,
        to: String,
        webhook: String,
    ) {
        CLDatabase.Links.add(fromGuild, from, toGuild, to, webhook)
        linkMap[from] =
            LinkData(
                fromGuild,
                from,
                toGuild,
                to,
                webhook,
            )
    }

    fun remove(from: String) {
        val data = CLDatabase.Links.getByFrom(from) ?: return
        CLDatabase.Links.remove(data.id.value)
        linkMap.remove(from)
    }

    fun removeByTo(to: String) {
        val data = CLDatabase.Links.getByTo(to) ?: return
        CLDatabase.Links.remove(data.id.value)
        linkMap.remove(data.channelFrom)
    }
}
