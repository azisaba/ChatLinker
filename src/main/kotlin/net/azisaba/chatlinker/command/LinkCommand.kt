package net.azisaba.net.azisaba.chatlinker.command

import net.azisaba.net.azisaba.chatlinker.cache.LinkDataCache
import net.azisaba.net.azisaba.chatlinker.extension.boolean
import net.azisaba.net.azisaba.chatlinker.extension.channel
import net.azisaba.net.azisaba.chatlinker.extension.optionBoolean
import net.azisaba.net.azisaba.chatlinker.extension.optionChannel
import net.azisaba.net.azisaba.chatlinker.extension.optionString
import net.azisaba.net.azisaba.chatlinker.extension.respond
import net.azisaba.net.azisaba.chatlinker.extension.string
import net.azisaba.net.azisaba.chatlinker.extension.subCommand
import net.azisaba.net.azisaba.chatlinker.extension.urlToTextChannel
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.CommandData

class LinkCommand : Command() {
    override val commandData: CommandData
        get() =
            slashCommand("link", "チャンネル連携") {
                subCommand("add", "チャンネル連携を追加") {
                    string("from", "送信元", true)
                    string("to", "送信先", true)
                    boolean("non-twoway", "双方向の送信を無効にするか")
                }
                subCommand("remove", "チャンネル連携を削除") {
                    channel("from", "送信元", true)
                    boolean("remove-to", "反対方向の連携も削除するか")
                }
                subCommand("get", "チャンネル連携を確認") {
                    channel("from", "送信元", true)
                }
            }

    override fun onCommand(event: SlashCommandInteractionEvent) {
        val guild = event.guild ?: return
        val member = event.member ?: return
        when (event.subcommandName) {
            "add" -> add(guild, member, event)
            "remove" -> remove(guild, member, event)
            "get" -> get(guild, member, event)
        }
    }

    fun add(
        guild: Guild,
        member: Member,
        event: SlashCommandInteractionEvent,
    ) {
        val response = event.deferReply(true)
        val channelFrom = event.optionString("from")?.urlToTextChannel(event.jda) ?: return
        val channelTo = event.optionString("to")?.urlToTextChannel(event.jda) ?: return
        val nonTwoWay = event.optionBoolean("non-twoway") ?: false
        if (LinkDataCache.get(channelFrom.id) != null) {
            response.setContent("${channelFrom.asMention} はすでに連携されています。").queue()
            return
        }

        val webhook = channelTo.createWebhook("chatlinker").complete().url
        LinkDataCache.add(channelFrom.id, channelTo.id, webhook)

        if (!nonTwoWay) {
            if (LinkDataCache.get(channelTo.id) != null) {
                response.setContent("${channelTo.asMention} はすでに連携されています。").queue()
                return
            }

            val reversedWebhook = channelFrom.createWebhook("chatlinker").complete().url
            LinkDataCache.add(channelTo.id, channelFrom.id, reversedWebhook)
        }
        response.respond("連携作業が完了しました。")
    }

    fun remove(
        guild: Guild,
        member: Member,
        event: SlashCommandInteractionEvent,
    ) {
        val response = event.deferReply(true)
        val channelFrom = event.optionChannel("from")?.asTextChannel() ?: return
        val isRemoveTo = event.optionBoolean("remove-to") ?: false

        LinkDataCache.remove(channelFrom.id)
        if (isRemoveTo) {
            LinkDataCache.removeByTo(channelFrom.id)
        }
        response.respond("連携を削除しました。")
    }

    fun get(
        guild: Guild,
        member: Member,
        event: SlashCommandInteractionEvent,
    ) {
        val response = event.deferReply(true)
        val channelFrom = event.optionChannel("from")?.asTextChannel() ?: return
        val data = LinkDataCache.get(channelFrom.id)
        response.respond(
            if (data == null) {
                "見つかりませんでした"
            } else {
                "Connected to ${data.toChannelId}"
            },
        )
    }
}
