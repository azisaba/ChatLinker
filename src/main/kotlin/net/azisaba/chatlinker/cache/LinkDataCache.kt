package net.azisaba.net.azisaba.chatlinker.cache

import net.azisaba.net.azisaba.chatlinker.database.CLDatabase

object LinkDataCache {
    private val linkMap: MutableMap<String, LinkData> = mutableMapOf()

    fun init() {
        CLDatabase.Links.getAll().forEach { data ->
            linkMap[data.channelFrom] = LinkData(data.channelFrom, data.channelTo, data.webhook)
        }
    }

    fun get(from: String): LinkData? = linkMap[from]

    fun add(
        from: String,
        to: String,
        webhook: String,
    ) {
        CLDatabase.Links.add(from, to, webhook)
        linkMap[from] = LinkData(from, to, webhook)
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
