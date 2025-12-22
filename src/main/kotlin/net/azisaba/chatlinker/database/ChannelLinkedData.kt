package net.azisaba.net.azisaba.chatlinker.database

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

object ChannelLinkedDataTable : IntIdTable() {
    val channelFrom = text("channel_from")
    val channelTo = text("channel_to")
    val webhook = text("webhook")
}

class ChannelLinkedData(
    id: EntityID<Int>,
) : IntEntity(id) {
    companion object : IntEntityClass<ChannelLinkedData>(ChannelLinkedDataTable)

    var channelFrom by ChannelLinkedDataTable.channelFrom
    var channelTo by ChannelLinkedDataTable.channelTo
    var webhook by ChannelLinkedDataTable.webhook
}
