package net.azisaba.chatlinker.database

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

object ChannelLinkedDataTable : IntIdTable() {
    val guildFrom = text("guild_from")
    val channelFrom = text("channel_from")
    val guildTo = text("guild_to")
    val channelTo = text("channel_to")
    val webhook = text("webhook")
}

class ChannelLinkedData(
    id: EntityID<Int>,
) : IntEntity(id) {
    companion object : IntEntityClass<ChannelLinkedData>(ChannelLinkedDataTable)

    var guildFrom by ChannelLinkedDataTable.guildFrom
    var channelFrom by ChannelLinkedDataTable.channelFrom
    var guildTo by ChannelLinkedDataTable.guildTo
    var channelTo by ChannelLinkedDataTable.channelTo
    var webhook by ChannelLinkedDataTable.webhook
}
